package com.hanu.util.db;

import java.util.List;
import java.util.stream.Collectors;

import com.hanu.base.Converter;
import com.hanu.util.configuration.Configuration;

public class RecordToDeleteStringConverter extends Converter<List<Integer>, String> {

    private static final String DELETE_RECORD_TEMPLATE = Configuration.get("db.record.deletecondition");

    public RecordToDeleteStringConverter() {
        super(RecordToDeleteStringConverter::removeRecordDb, null);
    }

    private static String removeRecordDb(List<Integer> ids) {
        String removeSql = DELETE_RECORD_TEMPLATE
                            .replace("$where", 
                                String.join(" OR ", 
                                    ids.stream()
                                        .map(id -> "id = $id"
                                            .replace("$id", id.toString()))
                                        .collect(Collectors.toList())));
		return removeSql;
	}
}