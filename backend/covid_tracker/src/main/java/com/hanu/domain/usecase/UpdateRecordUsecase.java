package com.hanu.domain.usecase;

import java.util.List;

import com.hanu.db.RecordRepositoryImpl;
import com.hanu.domain.model.Record;

public class UpdateRecordUsecase {

	public UpdateRecordUsecase() {
		// TODO Auto-generated constructor stub
	}


	public void handle(List<Record> updateRecords) {
		try {
			RecordRepositoryImpl update = new RecordRepositoryImpl();
			update.update(updateRecords);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
