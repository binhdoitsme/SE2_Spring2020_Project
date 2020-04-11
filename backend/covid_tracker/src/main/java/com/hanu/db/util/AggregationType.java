package com.hanu.db.util;

public class AggregationType {
    private GroupByType groupByType;
    private TimeframeType timeframeType;
    private String continent;
    private boolean latest;

    public AggregationType() { }

    public GroupByType groupBy() {
        return groupByType;
    }

    public TimeframeType timeframe() {
        return timeframeType;
    }

    public String continent() {
        return continent;
    }

    public AggregationType groupBy(GroupByType groupBy) {
        this.groupByType = groupBy;
        return this;
    }

    public AggregationType timeframe(TimeframeType timeframe) {
        this.timeframeType = timeframe;
        return this;
    }

    public AggregationType withContinent(String continent) {
        this.continent = continent;
        return this;
    }

    public AggregationType isLatest(boolean latest) {
        this.latest = latest;
        return this;
    }

    public boolean isLatest() {
        return this.latest;
    }
}