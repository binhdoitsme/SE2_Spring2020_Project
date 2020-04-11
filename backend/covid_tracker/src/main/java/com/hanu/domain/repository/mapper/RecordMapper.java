package com.hanu.domain.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.hanu.base.Mapper;
import com.hanu.domain.model.Record;

public class RecordMapper extends Mapper<Record> {
    public RecordMapper() {
        super(RecordMapper::fromResultSet);
    }

    private static Record fromResultSet(ResultSet rs) {
        try {
            return new Record(rs.getInt("id"),
            		rs.getDate("timestamp"),
            		rs.getInt("poiId"), 
            		rs.getLong("infected"),
            		rs.getLong("death"),
            		rs.getLong("recovered"));
        } catch (SQLException e) {
            return null;
        }
    }
}