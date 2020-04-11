package com.hanu.domain.dto;

import java.util.Date;

public class RecordDto {
	private Date timestamp;
    private String poiCode;
    private long infected;
    private long death;
    private long recovered;
    
	public RecordDto(Date timestamp, String poiCode, long infected, long death, long recovered) {
		super();
		this.timestamp = timestamp;
		this.poiCode = poiCode;
		this.infected = infected;
		this.death = death;
		this.recovered = recovered;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	
	public String getPoiCode() {
		return poiCode;
	}


	public Long getInfected() {
		return infected;
	}


	public Long getDeath() {
		return death;
	}


	public Long getRecovered() {
		return recovered;
	}   
}
