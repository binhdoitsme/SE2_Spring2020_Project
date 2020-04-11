package com.hanu.domain.usecase;

import com.hanu.db.RecordRepositoryImpl;
import com.hanu.domain.model.Record;

public class UpdateRecordUsecase {

	public UpdateRecordUsecase() {
		// TODO Auto-generated constructor stub
	}


	public Record handle(Record updateRecord) {
		try {
			RecordRepositoryImpl update = new RecordRepositoryImpl();
			update.update(updateRecord);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return updateRecord;
	}
}
