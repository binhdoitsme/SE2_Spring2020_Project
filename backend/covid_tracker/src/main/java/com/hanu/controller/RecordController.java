package com.hanu.controller;

import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD

import com.hanu.domain.model.Record;
import com.hanu.domain.usecase.RemoveRecordUsecase;
import com.hanu.domain.usecase.UpdateRecordUsecase;

=======
import java.util.stream.Collectors;

import com.hanu.base.Converter;
import com.hanu.db.util.AggregationType;
import com.hanu.db.util.GroupByType;
import com.hanu.db.util.TimeframeType;
import com.hanu.domain.converter.RecordDtoConverter;
import com.hanu.domain.dto.RecordDto;
import com.hanu.domain.model.Record;
import com.hanu.domain.usecase.GetAggregatedRecordsUseCase;
import com.hanu.util.string.StringConvert;
>>>>>>> aeed3567fefd5f1569d612d71a4ba47c258c6a4c

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordController {
    public static final Logger logger = LoggerFactory.getLogger(RecordController.class);
<<<<<<< HEAD
    
    private UpdateRecordUsecase UpdateRecordUsecase;
    private RemoveRecordUsecase RemoveRecordUsecase;
        
    public RecordController() {
		super();
		UpdateRecordUsecase = new UpdateRecordUsecase();
		RemoveRecordUsecase = new RemoveRecordUsecase();
	}


    
    private Record getRecord(Record input) {
    	Record record = new Record();
    	try {        	
        	record.setId(input.getId());
        	record.setTimestamp(input.getTimestamp());
        	record.setPoiId(input.getPoiId());
        	record.setInfected(input.getInfected());
        	record.setDeath(input.getDeath());
        	record.setRecovered(input.getRecovered()); 
        	
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
		return record;
    }

    
	public void updateRecords(Iterable<Record> recordArray) {
		List<Record> updateRecords = new ArrayList<Record>();
		for (Record record : recordArray) {
	        try {
	        	Record r = getRecord(record);
	        	updateRecords.add(r);	            
	        } catch (Exception e) {
	        	logger.error(e.getMessage(), e);
	        }
	        UpdateRecordUsecase.handle(updateRecords);
		}   	
    }
	
    
    public void removeRecords(Iterable<Record> recordArray) {
    	List<Record> removeRecords = new ArrayList<Record>();
		for (Record record : recordArray) {
	        try {        	
	        	removeRecords.add(getRecord(record));	            
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	        }
	        RemoveRecordUsecase.handle(removeRecords);
		}   	
    }



=======
    // other methods written by others...

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
            return new GetAggregatedRecordsUseCase().handle(type).stream()
                        .map(r -> converter.forwardConvert(r))
                        .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }
>>>>>>> aeed3567fefd5f1569d612d71a4ba47c258c6a4c
}