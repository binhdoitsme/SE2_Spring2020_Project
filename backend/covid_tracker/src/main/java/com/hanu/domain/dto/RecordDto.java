package com.hanu.domain.dto;

<<<<<<< HEAD
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
=======
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
>>>>>>> aeed3567fefd5f1569d612d71a4ba47c258c6a4c
