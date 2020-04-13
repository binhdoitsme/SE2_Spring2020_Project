package com.hanu.domain.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import com.hanu.base.Mapper;
import com.hanu.domain.model.Record;

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
    }
    
    public static Record forwardConvertOnce(ResultSet source) {
    	Record r = null;
        try {
            r = new Record(source.getInt("id"), source.getTimestamp("timestamp"), source.getInt("poi_id"),
                            source.getInt("infected"), source.getInt("death"), source.getInt("recovered"));
            try {
            	String continent = source.getString("continent");
            	String poiName = source.getString("name");
            	r = r.continent(continent).poiName(poiName);
            } catch (Exception e) {
            	//todo
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return r;
    }
}