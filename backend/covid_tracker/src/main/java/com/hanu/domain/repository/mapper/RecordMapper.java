package com.hanu.domain.repository.mapper;

import java.sql.ResultSet;
<<<<<<< HEAD
import java.sql.SQLException;
import java.util.Date;
=======
>>>>>>> aeed3567fefd5f1569d612d71a4ba47c258c6a4c

import com.hanu.base.Mapper;
import com.hanu.domain.model.Record;

<<<<<<< HEAD
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
=======
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordMapper extends Mapper<Record> {
    private static final Logger logger = LoggerFactory.getLogger(RecordMapper.class);

    public RecordMapper() {
        super(RecordMapper::resultToRecord);
    }
    
    public static Record resultToRecord(ResultSet source) {
        Record r = null;
        try {
            r = new Record(source.getInt("id"), source.getTimestamp("timestamp"), source.getInt("poi_id"),
                            source.getInt("infected"), source.getInt("death"), source.getInt("recovered"));
            String continent = source.getString("continent");
            String poiName = source.getString("poi_name");
            r = r.continent(continent).poiName(poiName);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return r;
>>>>>>> aeed3567fefd5f1569d612d71a4ba47c258c6a4c
    }
}