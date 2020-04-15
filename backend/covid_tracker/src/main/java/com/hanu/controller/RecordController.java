package com.hanu.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hanu.domain.model.Record;
import com.hanu.domain.usecase.AddManyRecordsUseCase;
import com.hanu.domain.usecase.GetAggregatedRecordsUseCase;
import com.hanu.exception.ServerFailedException;
import com.hanu.util.db.NonQueryResult;
import com.hanu.util.string.StringConvert;
import com.hanu.domain.usecase.RemoveRecordUsecase;
import com.hanu.domain.usecase.UpdateRecordUsecase;
import com.hanu.domain.usecase.RemoveManyRecordUsecase;
import com.hanu.domain.usecase.UpdateManyRecordUsecase;
import com.hanu.exception.InvalidQueryTypeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordController {
	public static final Logger logger = LoggerFactory.getLogger(RecordController.class);
	private UpdateRecordUsecase UpdateRecordUsecase;
	private RemoveRecordUsecase RemoveRecordUsecase;
	private UpdateManyRecordUsecase UpdateManyRecordUsecase;
	private RemoveManyRecordUsecase RemoveManyRecordUsecase;

    /**
     * Delegate to GetAggregatedRecords usecase
     */
    public List<RecordDto> getAggregatedRecords(String groupBy, String timeframe, String latest, String continent) {
        try {
            String groupByStr = StringConvert.camelToSnakeCase(groupBy).toUpperCase();
            String timeframeStr = StringConvert.camelToSnakeCase(timeframe).toUpperCase();
            String latestStr = StringConvert.camelToSnakeCase(latest).toUpperCase();
            AggregationType type = new AggregationType()
                    .groupBy(groupByStr.isEmpty() ? null : GroupByType.valueOf(groupByStr))
                    .timeframe(timeframeStr.isEmpty() ? null : TimeframeType.valueOf(timeframeStr))
                    .withContinent(continent == null ? "" : continent)
                    .isLatest(latestStr.isEmpty() ? false : Boolean.parseBoolean(latest));
            Converter<Record, RecordDto> converter = new RecordDtoConverter();
            return new GetAggregatedRecordsUseCase().handle(type).stream().map(r -> converter.forwardConvert(r))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public Object addBatch(String csv) {
        Object result = null;
        try {
            // map csv to list of Records
            String[] lines = csv.trim().split("\n");

            Map<String, List<Record>> recordsByPoiName = new HashMap<>();

            // get position of label
            List<String> labels = Arrays.asList(lines[0].trim().split(","));
            final int length = labels.size();
            final int infectedPos = labels.indexOf("Confirmed");
            final int deathPos = labels.indexOf("Deaths");
            final int recoveredPos = labels.indexOf("Recovered");
            final int datePos = labels.indexOf("Date");
            final int countryPos = labels.indexOf("Country/Region");
            final int provincePos = labels.indexOf("Province/State");

            // parse
            for (int i = 1; i < lines.length; i++) {
                Record r = recordFromLineString(lines[i], length, infectedPos, deathPos, 
                                            recoveredPos, countryPos, provincePos, datePos);
                if (r == null) continue;
                recordsByPoiName.putIfAbsent(r.getPoiName(), new ArrayList<>());
                recordsByPoiName.get(r.getPoiName()).add(r);
            }

            // add
            int affectedRows = new AddManyRecordsUseCase().handle(recordsByPoiName);
            result = new NonQueryResult(affectedRows);

        } catch (Exception e) {
            e.printStackTrace();
            result = new ServerFailedException();
        }
        return result;
    }

    private Record recordFromLineString(String line, int length, int infectedPos, int deathPos, int recoveredPos,
            int countryPos, int provincePos, int datePos) {
        String[] elements = line.trim().split(",");
        if (elements.length != length)
            return null;
        int infected = elements[infectedPos].isEmpty() ? 0 : new Integer(elements[infectedPos]);
        int death = elements[deathPos].isEmpty() ? 0 : new Integer(elements[deathPos]);
        int recovered = elements[recoveredPos].isEmpty() ? 0 : new Integer(elements[recoveredPos]);
        String country = elements[countryPos];
        String province = elements[provincePos];
        String poiName = province.isEmpty() ? country : province;
        long date = LocalDate.parse(elements[datePos]).atTime(23, 00).toEpochSecond(ZoneOffset.ofHours(0));
        Timestamp timestamp = new Timestamp(date);
        return new Record(0, timestamp, 0, infected, death, recovered).poiName(poiName);
    }

	public RecordController() {
		super();
		UpdateRecordUsecase = new UpdateRecordUsecase();
		RemoveRecordUsecase = new RemoveRecordUsecase();
		UpdateManyRecordUsecase = new UpdateManyRecordUsecase();
		RemoveManyRecordUsecase = new RemoveManyRecordUsecase();
	}

	public void updateRecords(String input) throws SQLException, InvalidQueryTypeException {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd' 'HH:mm:ss").create();
		List<Record> updateRecords = new ArrayList<Record>();
		try {
			Iterable<Record> recordArray = gson.fromJson(input, new TypeToken<ArrayList<Record>>() {
			}.getType());
			for (Record record : recordArray) {
				try {
					updateRecords.add(record);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			UpdateManyRecordUsecase.handle(updateRecords);
		} catch (Exception e) {
			Record record = gson.fromJson(input, Record.class);
			UpdateRecordUsecase.handle(record);
		}
	}

	public void removeRecords(String input) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd' 'HH:mm:ss").create();
		List<String> recordIDs = new ArrayList<>();
		try {
			Iterable<Record> recordArray = gson.fromJson(input, new TypeToken<ArrayList<Record>>() {
			}.getType());
			for (Record record : recordArray) {
				try {
					recordIDs.add(String.valueOf(record.getId()));
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			RemoveManyRecordUsecase.handle(recordIDs);
		} catch (Exception e) {
			Record record = gson.fromJson(input, Record.class);
			RemoveRecordUsecase.handle(String.valueOf(record.getId()));
		}
	}
}
