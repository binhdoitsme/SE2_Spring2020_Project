package com.hanu.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.hanu.base.Converter;
import com.hanu.db.util.AggregationType;
import com.hanu.db.util.GroupByType;
import com.hanu.db.util.TimeframeType;
import com.hanu.domain.converter.RecordDtoConverter;
import com.hanu.domain.dto.RecordDto;
import com.hanu.domain.model.Record;
import com.hanu.domain.usecase.GetAggregatedRecordsUseCase;
import com.hanu.domain.usecase.GetAllByPoiIdUseCase;
import com.hanu.domain.usecase.GetAllRecordUseCase;
import com.hanu.util.string.StringConvert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordController {
    public static final Logger logger = LoggerFactory.getLogger(RecordController.class);
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
    
    //getAllRecord
    public List<Record> getRecords() {
    	return new GetAllRecordUseCase().handle();
    }
    
    //getAllRecordByPoiID
    public List<Record> getByPoiID(int input){
    	return new GetAllByPoiIdUseCase().handle(input);
    }
}