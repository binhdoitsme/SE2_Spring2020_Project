package com.hanu.db.util;

public class FilterType {
    private final String continent;
    private final TimeframeType timeframeType;
    private final boolean isLatest;

    private FilterType(String continent, TimeframeType timeframeType, boolean isLatest) {
        this.continent = continent;
        this.timeframeType = timeframeType;
        this.isLatest = isLatest;
    }

    public String getContinent() {
        return continent;
    }

    public TimeframeType getTimeframeType() {
        return timeframeType;
    }

    public boolean isLatest() {
        return isLatest;
    }

    public static FilterType from(String continent, TimeframeType timeframeType, boolean isLatest) {
        return new FilterType(continent, timeframeType, isLatest);
    }
}
