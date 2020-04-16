package com.hanu.db;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.hanu.base.Mapper;
import com.hanu.base.RepositoryImpl;
import com.hanu.db.util.AggregationType;
import com.hanu.db.util.GroupByType;
import com.hanu.db.util.TimeframeType;
import com.hanu.domain.model.Record;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.domain.repository.mapper.RecordMapper;
import com.hanu.exception.InvalidQueryTypeException;
import com.hanu.util.configuration.Configuration;
import com.hanu.util.db.RecordToDeleteStringConverter;
import com.hanu.util.db.RecordToUpdateStringConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordRepositoryImpl extends RepositoryImpl<Record, Integer> implements RecordRepository {
    private static final String AGGREGATE_TEMPLATE = Configuration.get("db.aggregated.template");
    private static final String WORLD_TEMPLATE = Configuration.get("db.aggregated.world");
    private static final String FIELD_SUM_TEMPLATE = Configuration.get("db.aggregated.sum.fields");
    private static final String LATEST_LIMIT = Configuration.get("db.aggregated.latest");
    private static final String CONTINENT_TEMPLATE = Configuration.get("db.nonaggregate.continent");
    private static final String LATEST_DATE = Configuration.get("db.scalar.latestdate");
    private static final String POI_ID_FROM_NAME = Configuration.get("db.scalar.poi_id");

    private static final Logger logger = LoggerFactory.getLogger(RecordRepositoryImpl.class);

    @Override
    public List<Record> getAll() {
        
        return null;
    }

    @Override
    public Record getById(Integer id) {
        
        return null;
    }

    @Override
    public int add(Record item) {
        return 0;
    }

    @Override
    public int add(List<Record> items) {
        return 0;
    }

    @Override
	public int remove(Integer id) {
		try {
            List<Integer> ids = new ArrayList<>();
            ids.add(id);
            String sql = new RecordToDeleteStringConverter().forwardConvert(ids);
            return getConnector().connect().executeDelete(sql);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return 0;
        }
	}

	@Override
	public int remove(List<Integer> ids) {
        try {
            String sql = new RecordToDeleteStringConverter().forwardConvert(ids);
            return getConnector().connect().executeDelete(sql);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return 0;
        }
	}
	
	@Override
	public int update(Record item) {
        try {
            String sql = new RecordToUpdateStringConverter().forwardConvert(item);
            return getConnector().connect().executeUpdate(sql);
        } catch (Exception e) {
            logger.info("Error in item: $item".replace("$item", item.toString()));
            logger.info("Exception type is $type".replace("$type", e.getClass().getName()));
            return 0;
        }
	}

	@Override
	public int update(List<Record> items) {
        int rowsUpdated = 0;
        for (Record item : items) {
            rowsUpdated += update(item);
        }
		return rowsUpdated;
	}

    @Override
    public long count() {
        return 0;
    }

    @Override
    public boolean exists(Integer id) {
        return true;
    }

    @Override
    public List<Record> getAggregatedRecords(AggregationType type) throws SQLException, InvalidQueryTypeException {
        String sql = buildAggregateQueryString(type);       

        ResultSet rs = super.getConnector().connect().executeSelect(sql);
        List<Record> resultList = new LinkedList<>();
        Mapper<Record> recordMapper = new RecordMapper();
        while (rs.next()) {
            Record r = recordMapper.forwardConvert(rs);
            if (r != null) resultList.add(r);
        }
        return resultList;
    }

    private String buildAggregateQueryString(AggregationType type) {
        GroupByType groupByType = type.groupBy();
        String continent = type.continent();
        TimeframeType timeframeType = type.timeframe();
        String sql = groupByType == null ? AGGREGATE_TEMPLATE.replace("$group_by", "") :
                    groupByType.equals(GroupByType.WORLD) ? 
                        WORLD_TEMPLATE 
                        : AGGREGATE_TEMPLATE.replace("$group_by", groupByType.toString().concat(","));
        
        String limitStr = groupByType == null ? LATEST_LIMIT :
                            groupByType.equals(GroupByType.WORLD) ? "LIMIT 2" : LATEST_LIMIT;
        sql = sql.replace("$limit", type.isLatest() ? limitStr : "")
                .replace("$timeframe", timeframeType == null ? "DAY" : timeframeType.toString())
                .replace("$continent_clause", continent.isEmpty() ? 
                        "AND continent in ('asia', 'america', 'europe', 'africa')"
                        : "AND continent = '$continent'".replace("$continent", continent))
                .replace("$fields", groupByType == GroupByType.CONTINENT ? FIELD_SUM_TEMPLATE : "*");
        return sql;
    }

	@Override
	public List<Record> getRecordByContinent(String continent) throws SQLException, InvalidQueryTypeException {
		List<Record> records = new ArrayList<>();
		String query = CONTINENT_TEMPLATE.replace("$continent", continent);

        ResultSet rs = this.getConnector().connect().executeSelect(query);
        while (rs.next()) {
            records.add(RecordMapper.resultToRecord(rs));
        }
		return records;
	}

    @Override
    public int getPoiIdByName(String name) throws SQLException, InvalidQueryTypeException {
        return getConnector().connect()
                .executeScalar(POI_ID_FROM_NAME.replace("$name", name));
    }

    @Override
    public Date getLatestDate() throws SQLException, InvalidQueryTypeException {
        return getConnector().connect()
                    .executeScalar(LATEST_DATE);
    }
}
