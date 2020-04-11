package com.hanu.db;

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
