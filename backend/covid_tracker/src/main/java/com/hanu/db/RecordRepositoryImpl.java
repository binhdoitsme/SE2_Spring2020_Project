package com.hanu.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import com.hanu.util.db.RecordToDbConverter;

public class RecordRepositoryImpl extends RepositoryImpl<Record, Integer> implements RecordRepository {
    private static final String AGGREGATE_TEMPLATE = Configuration.get("db.aggregated.template");
    private static final String WORLD_TEMPLATE = Configuration.get("db.aggregated.world");
    private static final String FIELD_SUM_TEMPLATE = Configuration.get("db.aggregated.sum.fields");
    private static final String LATEST_LIMIT = Configuration.get("db.aggregated.latest");

    @Override
	public List<Record> getAll() {
		List<Record> records = new ArrayList<>();
		String query = "SELECT * FROM record";
		try {
			ResultSet rs = this.getConnector().connect().connect().executeSelect(query);
			while(rs.next()) {
				records.add(RecordMapper.forwardConvertOnce(rs));
			}
		} catch (SQLException | InvalidQueryTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return records;
	}

    @Override
	public Record getById(Integer id) {
		Record record = null;
		String query = "SELECT * FROM record INNER JOIN point_of_interest ON record.poi_id = point_of_interest.id"
				+  " WHERE id = " + "\'" +id + "\'";
		try {
			ResultSet rs = this.getConnector().connect().executeSelect(query);
			record = RecordMapper.forwardConvertOnce(rs);
		} catch (SQLException | InvalidQueryTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return record;
	}
    
    @Override
	public List<Record> getByPoiID(int input) throws SQLException, InvalidQueryTypeException {
		List<Record> records = new ArrayList<>();
		String query = "SELECT * FROM record INNER JOIN point_of_interest ON record.poi_id = point_of_interest.id"
				+  " WHERE poi_id = " + "\'" +input+ "\'";
		try {
			ResultSet rs = this.getConnector().connect().executeSelect(query);
			while(rs.next()) {
				records.add(RecordMapper.forwardConvertOnce(rs));
			}
		} catch (SQLException | InvalidQueryTypeException e) {				
			e.printStackTrace();
		}			
		return records;
	}
    
    @Override
	public void add(Record item) {
		String query = new String("INSERT INTO record VALUES $values")
								.replace("$values",RecordToDbConverter.forwardConverter(item));
		try {
			this.getConnector().connect().executeInsert(query);
		} catch (SQLException | InvalidQueryTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @Override
	public void add(Iterable<Record> items) {
		ArrayList<String> insertValueStrings = new ArrayList<String>();
		for (Record record : items) {
			insertValueStrings.add(RecordToDbConverter.forwardConverter(record));
		}
		String query = new String("INSERT INTO record VALUES $values")
								.replace("$values", String.join(",",insertValueStrings));
		try {
			this.getConnector().connect().executeInsert(query);
		} catch (SQLException | InvalidQueryTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    

    @Override
    public void remove(Record item) {
        

    }

    @Override
    public void remove(Iterable<Record> items) {
        

    }

    @Override
    public Record update(Record item) {
        
        return null;
    }

    @Override
    public Iterable<Record> update(Iterable<Record> items) {
        
        return null;
    }

    @Override
    public long count() {
        
        return 0;
    }

    @Override
    public boolean exists(Integer id) {
        
        return false;
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
        
        String limitStr = groupByType == null ? "" :
                            groupByType.equals(GroupByType.WORLD) ? "LIMIT 2" : LATEST_LIMIT;
        sql = sql.replace("$limit", type.isLatest() ? limitStr : "")
                .replace("$timeframe", timeframeType == null ? "DAY" : timeframeType.toString())
                .replace("$continent_clause", continent.isEmpty() ? 
                        "AND continent in ('asia', 'america', 'europe', 'africa')"
                        : "AND continent = '$continent'".replace("$continent", continent))
                .replace("$fields", groupByType == GroupByType.CONTINENT ? FIELD_SUM_TEMPLATE : "*");
        return sql;
    }
}