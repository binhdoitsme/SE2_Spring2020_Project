package com.hanu.domain.repository;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import com.hanu.base.Repository;
import com.hanu.domain.model.Record;

@Dependency
public interface RecordRepository extends Repository<Record, Integer> {
    List<Record> getAggregatedRecords(AggregationType type) throws SQLException, InvalidQueryTypeException;
    int getPoiIdByName(String poiName) throws SQLException, InvalidQueryTypeException;
    Date getLatestDate() throws SQLException, InvalidQueryTypeException;
}
