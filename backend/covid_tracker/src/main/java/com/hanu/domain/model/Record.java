package com.hanu.domain.model;

import java.sql.Timestamp;

/**
 * Represent a CoViD-19 record that can be saved in database
 */
public class Record {
    private int id;
    private Timestamp timestamp;
    private int poiId;
    private long infected;
    private long death;
	private long recovered;
	
	// joined fields
	private String continent;
	private String poiName;
		
	public Record() {
		super();
    }
    
    // added: for update case
    public Record(int id, long infected, long death, long recovered) {
        this.id = id;
        this.infected = infected;
        this.death = death;
        this.recovered = recovered;
    }
       
	public Record(int id, Timestamp timestamp, int poiId, long infected, long death, long recovered) {
        this.id = id;
        this.timestamp = timestamp;
        this.poiId = poiId;
        this.infected = infected;
        this.death = death;
        this.recovered = recovered;
    }

    public int getId() {
        return id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getPoiId() {
        return poiId;
    }

    public long getInfected() {
        return infected;
    }

    public long getDeath() {
        return death;
    }

    public long getRecovered() {
        return recovered;
    }

    public String getContinent() {
        return continent;
    }

    public String getPoiName() {
        return poiName;
    }

    public Record poiId(int poiId) {
        this.poiId = poiId;
        return this;
    }

    public Record poiName(String name) {
        this.poiName = name;
        return this;
    }

    public Record continent(String continent) {
        this.continent = continent;
        return this;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public void setPoiId(int poiId) {
		this.poiId = poiId;
	}

	public void setInfected(long infected) {
		this.infected = infected;
	}

	public void setDeath(long death) {
		this.death = death;
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