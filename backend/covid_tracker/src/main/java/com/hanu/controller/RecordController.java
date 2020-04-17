package com.hanu.controller;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hanu.base.Converter;
import com.hanu.db.util.AggregationType;
import com.hanu.db.util.GroupByType;
import com.hanu.db.util.TimeframeType;
import com.hanu.domain.converter.RecordDtoConverter;
import com.hanu.domain.dto.RecordDto;
import com.hanu.domain.model.Record;
import com.hanu.domain.usecase.AddManyRecordsUseCase;
import com.hanu.domain.usecase.AddRecordUseCase;
import com.hanu.domain.usecase.GetAggregatedRecordsUseCase;
import com.hanu.domain.usecase.GetAllByPoiIdUseCase;
import com.hanu.domain.usecase.GetAllRecordUseCase;
import com.hanu.domain.usecase.GetRecordByContinentUseCase;
import com.hanu.domain.usecase.RemoveManyRecordUseCase;
import com.hanu.domain.usecase.UpdateManyRecordUseCase;
import com.hanu.exception.InvalidQueryTypeException;
import com.hanu.exception.ServerFailedException;
import com.hanu.util.db.NonQueryResult;
import com.hanu.util.string.StringConvert;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordController {
	public static final Logger logger = LoggerFactory.getLogger(RecordController.class);

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

    // split method
    private Map<String, List<Record>> getRecordsPoiNameMap(String csv) {
        // map csv to list of Records
        String[] lines = csv.trim().replace("\"Korea, South\"", "South Korea").split("\n");
        logger.info("Line cnt: " + lines.length);

        Map<String, List<Record>> recordsByPoiName = new HashMap<>();

        // get position of label
        List<String> labels = Arrays.asList(lines[0].trim().split(","));
        final int length = labels.size();
        final int infectedPos = labels.indexOf("Confirmed");
        final int deathPos = labels.indexOf("Deaths");
        final int recoveredPos = labels.indexOf("Recovered");
        final int datePos = labels.indexOf("Date");
        final int countryPos = labels.indexOf("Country");
        // final int provincePos = labels.indexOf("Province/State");

        // parse
        for (int i = 1; i < lines.length; i++) {
            Record r = recordFromLineString(lines[i], length, infectedPos, deathPos, 
                                        recoveredPos, countryPos, datePos);
            if (r == null) continue;
            recordsByPoiName.putIfAbsent(r.getPoiName(), new ArrayList<>());
            recordsByPoiName.get(r.getPoiName()).add(r);
        }
        return recordsByPoiName;
    }

    public Object addBatch(String csv) {
        Object result = null;
        try {
            Map<String, List<Record>> recordsByPoiName = getRecordsPoiNameMap(csv);

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
            int countryPos, int datePos) {
        String[] elements = line.trim().split(",");
        if (elements.length != length) {
            logger.info(Arrays.toString(elements));
            return null;
        }
        int infected = elements[infectedPos].isEmpty() ? 0 : new Integer(elements[infectedPos]);
        int death = elements[deathPos].isEmpty() ? 0 : new Integer(elements[deathPos]);
        int recovered = elements[recoveredPos].isEmpty() ? 0 : new Integer(elements[recoveredPos]);
        String country = elements[countryPos];
        // String province = elements[provincePos];
        // String poiName = province.isEmpty() ? country : province;
        LocalDateTime date = LocalDate.parse(elements[datePos]).atTime(23, 00);
        Timestamp timestamp = Timestamp.valueOf(date);
        return new Record(0, timestamp, 0, infected, death, recovered).poiName(country);
    }

    // changed: many+single
    // removed dependency on Gson, replaced by org.json
	public Object updateRecords(String input) throws SQLException, InvalidQueryTypeException {
        UpdateManyRecordUseCase usecase = new UpdateManyRecordUseCase();

        List<Record> records = parseRecordsFromString(input);
		int rowsAffected = usecase.handle(records);
        return new NonQueryResult(rowsAffected);
    }
    
    // added
    private List<Record> parseRecordsFromString(String input) {
        // parsing
        List<Record> updateRecords = new ArrayList<Record>();
        if (input.startsWith("[")) {
            // array case
            JSONArray array = new JSONArray(input);
            for (Object item : array) {
                JSONObject obj = (JSONObject) item;
                updateRecords.add(new Record(
                    obj.getInt("id"),
                    obj.getInt("infected"),
                    obj.getInt("death"),
                    obj.getInt("recovered")
                ));
            }
        } else {
            JSONObject obj = new JSONObject(input);
            updateRecords.add(new Record(
                obj.getInt("id"),
                obj.getInt("infected"),
                obj.getInt("death"),
                obj.getInt("recovered")
            ));
        }
        return updateRecords;
    }

    // changed: Collapse many+single => many
    // remove dependency on gson
	public Object removeRecords(String input) {
        RemoveManyRecordUseCase usecase = new RemoveManyRecordUseCase();
        
        // input => list<int>
        List<Integer> ids = new ArrayList<>();
        String[] idStrings = input.trim().replace("[", "").replace("]", "").split(",");
        for (String id : idStrings) {
            ids.add(Integer.parseInt(id.trim()));
        }
        return usecase.handle(ids);
	}

	public List<Record> getRecordByContinent(String continent) throws Exception {
		return new GetRecordByContinentUseCase().handle(continent);
	}
    
    // getAllRecord
    public List<Record> getRecords() {
    	return new GetAllRecordUseCase().handle(null);
    }
    
    // getAllRecordByPoiID
    public List<Record> getByPoiID(int input){
    	return new GetAllByPoiIdUseCase().handle(input);
    }
    
    // addRecord
    public int addRecordFromBody(String body) {
        JSONArray array = new JSONArray(body);
        List<Record> records = new LinkedList<Record>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject js = (JSONObject) array.get(i);
            records.add(new Record(new Timestamp(js.getLong("timestamp")), js.getInt("poiId"), js.getLong("infected"),
                    js.getLong("death"), js.getLong("recovered")));
        }
        return new AddRecordUseCase().handle(records);
    }
}
