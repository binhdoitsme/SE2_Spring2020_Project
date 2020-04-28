package com.hanu.util.db;

import com.hanu.base.Converter;
import com.hanu.domain.model.Record;
import com.hanu.util.configuration.Configuration;

public class RecordToUpdateStringConverter extends Converter<Record, String> {

	private static final String UPDATE_RECORD_TEMPLATE = Configuration.get("db.record.updatesingle");

	public RecordToUpdateStringConverter() {
		super(RecordToUpdateStringConverter::updateRecordDb, null);
	}
	
	private static String updateRecordDb(Record updateRecord) {
		String updateSql = UPDATE_RECORD_TEMPLATE.replace("$infected", String.valueOf(updateRecord.getInfected()))
												.replace("$death", String.valueOf(updateRecord.getDeath()))
												.replace("$recovered", String.valueOf(updateRecord.getRecovered()))
												.replace("$id", String.valueOf(updateRecord.getId()));
		return updateSql;
	}
}
