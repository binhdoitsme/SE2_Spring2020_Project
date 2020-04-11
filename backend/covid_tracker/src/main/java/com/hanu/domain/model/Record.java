package com.hanu.domain.model;

import java.util.Date;

public class Record {
	private int id;
	private Date timestamp;
    private int poiId;
    private long infected;
    private long death;
    private long recovered;
       
	public Record(int id, Date timestamp, int poiId, long infected, long death, long recovered) {
		super();
		this.id = id;
		this.timestamp = timestamp;
		this.poiId = poiId;
		this.infected = infected;
		this.death = death;
		this.recovered = recovered;
	}
	
	public Record() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getPoiId() {
		return poiId;
	}

	public void setPoiId(int poiId) {
		this.poiId = poiId;
	}

	public long getInfected() {
		return infected;
	}

	public void setInfected(long infected) {
		this.infected = infected;
	}

	public long getDeath() {
		return death;
	}

	public void setDeath(long death) {
		this.death = death;
	}

	public long getRecovered() {
		return recovered;
	}

	public void setRecovered(long recovered) {
		this.recovered = recovered;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()
				+ "Record (id=" + id 
				+ ", timestamp=" + timestamp 
				+ ", poiId=" + poiId 
				+ ", infected=" + infected
				+ ", death=" + death 
				+ ", recovered=" + recovered + ")";
	}

    
    
}
