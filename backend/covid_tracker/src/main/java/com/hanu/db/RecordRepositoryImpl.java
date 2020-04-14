package com.hanu.db;

<<<<<<< HEAD
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;


import com.hanu.base.RepositoryImpl;
import com.hanu.domain.model.Record;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.exception.InvalidQueryTypeException;
import com.hanu.util.db.RecordToDbConverter;

public class RecordRepositoryImpl extends RepositoryImpl<Record, String> implements RecordRepository{
	

	public RecordRepositoryImpl() {
		super();
    }
	
	@Override
	public List<Record> getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Record getById(String id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(Record item) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(Iterable<Record> items) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(Record item) {
		String sql = RecordToDbConverter.removeRecordDb(item); 
		try {
			int rs = this.getConnector().connect().executeDelete(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidQueryTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

	@Override
	public int remove(Iterable<Record> items) {
		Iterator<Record> removeRecords= items.iterator();
	    while (removeRecords.hasNext()) {
	    	remove(removeRecords.next());	    	
		}
		return 1;
	}

	
	@Override
	public Record update(Record item) {
		String sql = RecordToDbConverter.updateRecordDb(item);
			try {
				int rs = this.getConnector().connect().executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidQueryTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		return item;
	}



	@Override
	public Iterable<Record> update(Iterable<Record> items) {
	    Iterator<Record> updateRecords= items.iterator();
	    while (updateRecords.hasNext()) {
	    	update(updateRecords.next());
			}		
		return items;
	}

	@Override
	public long count() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean exists(String id) {
		// TODO Auto-generated method stub
		return false;
	}

}
=======
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class RecordRepositoryImpl extends RepositoryImpl<Record, Integer> implements RecordRepository {
    private static final String AGGREGATE_TEMPLATE = Configuration.get("db.aggregated.template");
    private static final String WORLD_TEMPLATE = Configuration.get("db.aggregated.world");
    private static final String FIELD_SUM_TEMPLATE = Configuration.get("db.aggregated.sum.fields");
    private static final String LATEST_LIMIT = Configuration.get("db.aggregated.latest");

    @Override
    public List<Record> getAll() {
        
        return null;
    }

    @Override
    public Record getById(Integer id) {
        
        return null;
    }

    @Override
    public void add(Record item) {
        

    }

    @Override
    public void add(Iterable<Record> items) {
        

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
>>>>>>> aeed3567fefd5f1569d612d71a4ba47c258c6a4c
