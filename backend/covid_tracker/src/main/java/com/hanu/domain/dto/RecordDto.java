package com.hanu.domain.dto;

public class RecordDto {
    private String timestamp;
    private long infected;
    private long death;
    private long recovered;
    private String poiName;
    private String continent;

    public RecordDto(String timestamp, long infected, long death, long recovered,
                    String poiName, String continent) {
        this.timestamp = timestamp;
        this.infected = infected;
        this.death = death;
        this.recovered = recovered;
        this.poiName = poiName;
        this.continent = continent;
    }

    public String getContinent() {
        return continent;
    }

    public long getDeath() {
        return death;
    }

    public long getInfected() {
        return infected;
    }

    public String getPoiName() {
        return poiName;
    }

    public long getRecovered() {
        return recovered;
    }

    public String getTimestamp() {
        return timestamp;
    }
}