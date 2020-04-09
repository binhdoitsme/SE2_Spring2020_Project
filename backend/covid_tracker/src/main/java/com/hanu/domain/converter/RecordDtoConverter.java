package com.hanu.domain.converter;

import java.sql.Timestamp;

import com.hanu.base.Converter;
import com.hanu.domain.dto.RecordDto;
import com.hanu.domain.model.Record;

public class RecordDtoConverter extends Converter<Record, RecordDto> {

    public RecordDtoConverter() {
        super(RecordDtoConverter::toDto, RecordDtoConverter::fromDto);
    }

    public static RecordDto toDto(Record r) {
        return new RecordDto(r.getTimestamp().toString(), 
                            r.getInfected(), r.getDeath(), 
                            r.getRecovered(), r.getPoiName(), r.getContinent());
    }

    public static Record fromDto(RecordDto r) {
        return new Record(0, Timestamp.valueOf(r.getTimestamp()), 0,
                        r.getInfected(), r.getDeath(), r.getRecovered());
    }
}