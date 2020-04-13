package com.hanu.domain.repository;

import java.sql.SQLException;
import java.util.List;

import com.hanu.base.Repository;
import com.hanu.db.util.AggregationType;
import com.hanu.domain.model.Record;
import com.hanu.exception.InvalidQueryTypeException;
import com.hanu.util.di.Dependency;

@Dependency
public interface RecordRepository extends Repository<Record, Integer> {
    List<Record> getByPoiID(int input) throws SQLException,InvalidQueryTypeException;
	List<Record> getAggregatedRecords(AggregationType type) throws SQLException, InvalidQueryTypeException;
}