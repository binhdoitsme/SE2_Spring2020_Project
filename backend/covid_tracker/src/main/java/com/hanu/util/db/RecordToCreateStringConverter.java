package com.hanu.util.db;

import com.hanu.base.Converter;
import com.hanu.domain.model.Record;

// renamed this
public class RecordToCreateStringConverter extends Converter<Record, String> {
	
	public RecordToCreateStringConverter() {
		super(RecordToCreateStringConverter::forwardConverter, null);
	}

	private static String forwardConverter(Record record) {
		return new String("($timestamp,$poi_id,$infected, $death, $recovered)")
								.replace("$poi_id", "\'" + record.getPoiId() + "\'")
								.replace("$timestamp", "\'" + record.getTimestamp() + "\'")
								.replace("$infected", "\'" + record.getInfected() + "\'")
								.replace("$death", "\'" + record.getDeath() + "\'")
								.replace("$recovered", "\'" + record.getRecovered() + "\'");
	}
}
