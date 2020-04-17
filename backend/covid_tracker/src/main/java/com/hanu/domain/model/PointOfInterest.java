package com.hanu.domain.model;

public class PointOfInterest {
	private int id;
	private String name;
	private String code;
	private String continent;
	
	public PointOfInterest(int id, String name, String code, String continent) {
		this.id = id;
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
}
