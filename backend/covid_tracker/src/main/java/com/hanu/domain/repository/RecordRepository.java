package com.hanu.domain.repository;

<<<<<<< HEAD
import com.hanu.base.Repository;
import com.hanu.domain.model.Record;

public interface RecordRepository extends Repository<Record, String>{

}
=======
import java.sql.SQLException;
import java.util.List;

import com.hanu.base.Repository;
import com.hanu.db.util.AggregationType;
import com.hanu.domain.model.Record;
import com.hanu.exception.InvalidQueryTypeException;
import com.hanu.util.di.Dependency;

@Dependency
public interface RecordRepository extends Repository<Record, Integer> {
    List<Record> getAggregatedRecords(AggregationType type) throws SQLException, InvalidQueryTypeException;
}
>>>>>>> aeed3567fefd5f1569d612d71a4ba47c258c6a4c
