package com.hanu.domain.usecase;

import com.hanu.db.RecordRepositoryImpl;
import com.hanu.domain.model.Record;

public class RemoveRecordUsecase {

	public RemoveRecordUsecase() {
		// TODO Auto-generated constructor stub
	}
	
	public Record handle(Record removeRecord) {
		try {
			RecordRepositoryImpl remove = new RecordRepositoryImpl();
			remove.remove(removeRecord);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return removeRecord;
	}
}
