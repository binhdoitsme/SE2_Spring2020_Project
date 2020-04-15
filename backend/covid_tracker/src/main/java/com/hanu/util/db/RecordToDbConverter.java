package com.hanu.util.db;

import java.util.function.Function;

import com.hanu.base.Converter;
import com.hanu.domain.model.Record;

public class RecordToDbConverter extends Converter<Record, String> {

	public RecordToDbConverter(Function<Record, String> forwardConverter) {
		super(forwardConverter, null);
		// TODO Auto-generated constructor stub
	}
	
	public static String updateRecordDb(Record updateRecord) {
		String updateSql = "Update Record Set infected = '" + updateRecord.getInfected()
													+ "' , death = '" + updateRecord.getDeath()
													+ "' , recovered = '" + updateRecord.getRecovered()
													+ "' where id = '" + updateRecord.getId() + "' ;";
		return updateSql;
	}
	
	public static String removeRecordDb(String id) {
		String removeSql = "Delete from Record where id = '" + id + "' ;";
		return removeSql;
	}
}
