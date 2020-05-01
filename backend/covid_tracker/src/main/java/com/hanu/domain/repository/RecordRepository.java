package com.hanu.domain.repository;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.hanu.base.Repository;
import com.hanu.db.util.AggregationType;
import com.hanu.db.util.FilterType;
import com.hanu.domain.model.Record;
import com.hanu.exception.InvalidQueryTypeException;
import com.hanu.util.di.Dependency;

@Dependency
public interface RecordRepository extends Repository<Record, Integer> {
    List<Record> getAggregatedRecords(AggregationType type) throws SQLException, InvalidQueryTypeException;
    int getPoiIdByName(String poiName) throws SQLException, InvalidQueryTypeException;
    Timestamp getLatestTime() throws SQLException, InvalidQueryTypeException;
    List<Record> getRecordByContinent(String continent) throws SQLException, InvalidQueryTypeException;
    List<Record> getByPoiID(int input) throws SQLException,InvalidQueryTypeException;
    List<Record> getFilteredRecords(FilterType filterType) throws SQLException, InvalidQueryTypeException;
}
