package com.hanu.util.db;

import java.util.function.Function;
import com.hanu.base.Converter;
import com.hanu.domain.model.Record;

public class RecordToDbConverter extends Converter<Record, String> {
	
	public RecordToDbConverter(Function<Record, String> forwardConverter) {
		super(forwardConverter, null);
		// TODO Auto-generated constructor stub
	}
	public static String forwardConverter(Record record) {
		return new String("($timestamp,$poi_id,$infected, $death, $recovered)")
								.replace("$poi_id", "\'" + record.getPoiId() + "\'")
								.replace("$timestamp", "\'" + record.getTimestamp() + "\'")
								.replace("$infected", "\'" + record.getInfected() + "\'")
								.replace("$death", "\'" + record.getDeath() + "\'")
								.replace("$recovered", "\'" + record.getRecovered() + "\'");
	}
}
