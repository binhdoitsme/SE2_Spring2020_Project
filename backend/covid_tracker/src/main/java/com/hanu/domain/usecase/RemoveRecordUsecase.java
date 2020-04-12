package com.hanu.domain.usecase;

import java.util.List;

import com.hanu.db.RecordRepositoryImpl;
import com.hanu.domain.model.Record;

public class RemoveRecordUsecase {

	public RemoveRecordUsecase() {
		// TODO Auto-generated constructor stub
	}
	
	public void handle(List<Record> removeRecords) {
		try {
			RecordRepositoryImpl remove = new RecordRepositoryImpl();
			remove.remove(removeRecords);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
