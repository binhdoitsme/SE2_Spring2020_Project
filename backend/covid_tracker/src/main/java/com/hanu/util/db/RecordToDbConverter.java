package com.hanu.util.db;

import java.util.function.Function;
import com.hanu.base.Converter;
import com.hanu.domain.model.Record;

public class RecordToDbConverter extends Converter<Record, String> {
	
	public RecordToDbConverter(Function<Record, String> forwardConverter) {
		super(forwardConverter, null);
		// TODO Auto-generated constructor stub
	}
	
	//todo
	//$id

	public static String forwardConverter(Record record) {
		return new String("($id,$timestamp,$poiID,$infected, $death, $recovered)")
								.replace("$id", "" + "\'" + record.getId() + "\'")
								.replace("$timestamp", "\'" + record.getTimestamp() + "\'")
								.replace("$infected", "" + record.getInfected())
								.replace("$death", "\'" + record.getDeath() + "\'")
								.replace("$recovered", "\'" + record.getRecovered() + "\'");
	}
}
