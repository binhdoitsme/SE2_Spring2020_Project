package com.hanu.domain.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class PointOfInterest {
	private int id;
	private String name;
	private String code;
	private String continent;

	@JsonInclude(value = Include.NON_NULL)
	private List<Record> records;
	
	public PointOfInterest(int id, String name, String code, String continent) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.continent = continent;
	}
	
	public PointOfInterest( String name, String code, String continent) {
		this.name = name;
		this.code = code;
		this.continent = continent;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public String getContinent() {
		return continent;
	}

	public List<Record> getRecords() {
		return records;
	}

	public PointOfInterest setRecords(List<Record> records) {
		this.records = records;
		return this;
	}
}
