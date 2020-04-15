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

    // extra fields for data transfer
    private String poiName;
    private String continent;
    
    public Record(int id, Timestamp timestamp, int poiId, long infected, long death, long recovered) {
        this.id = id;
        this.timestamp = timestamp;
        this.poiId = poiId;
        this.infected = infected;
        this.death = death;
        this.recovered = recovered;
    }
    
    public Record(Timestamp timestamp, int poiId, long infected, long death, long recovered) {
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

    public Record poiName(String name) {
        this.poiName = name;
        return this;
    }

    public Record continent(String continent) {
        this.continent = continent;
        return this;
    }
}