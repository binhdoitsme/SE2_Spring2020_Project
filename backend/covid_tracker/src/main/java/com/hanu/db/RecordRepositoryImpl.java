package com.hanu.db;

import com.hanu.base.Converter;
import com.hanu.base.Mapper;
import com.hanu.base.RepositoryImpl;
import com.hanu.db.util.AggregationType;
import com.hanu.db.util.FilterType;
import com.hanu.db.util.GroupByType;
import com.hanu.db.util.TimeframeType;
import com.hanu.domain.model.Record;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.domain.repository.mapper.RecordMapper;
import com.hanu.exception.InvalidQueryTypeException;
import com.hanu.util.configuration.Configuration;
import com.hanu.util.db.RecordToCreateStringConverter;
import com.hanu.util.db.RecordToDeleteStringConverter;
import com.hanu.util.db.RecordToUpdateStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RecordRepositoryImpl extends RepositoryImpl<Record, Integer> implements RecordRepository {
    private static final String AGGREGATE_TEMPLATE = Configuration.get("db.aggregated.template");
    private static final String WORLD_TEMPLATE = Configuration.get("db.aggregated.world");
    private static final String FIELD_SUM_TEMPLATE = Configuration.get("db.aggregated.sum.fields");
    private static final String LATEST_LIMIT = Configuration.get("db.aggregated.latest");
    private static final String CONTINENT_TEMPLATE = Configuration.get("db.nonaggregate.continent");
    private static final String LATEST_DATE = Configuration.get("db.scalar.latestdate");
    private static final String POI_ID_FROM_NAME = Configuration.get("db.scalar.poi_id");
    private static final String RECORD_ALL_TEMPLATE = Configuration.get("db.record.all");
    private static final String RECORD_FILTER_TEMPLATE = Configuration.get("db.record.filter");

    private static final Logger logger = LoggerFactory.getLogger(RecordRepositoryImpl.class);

    @Override
    public int add(Record item) {
        String values = new RecordToCreateStringConverter().forwardConvert(item);
        String query = new String("INSERT INTO record(timestamp,poi_id,infected, death, recovered) VALUES $values")
            .replace("$values", values);
        try {
            return this.getConnector().connect().executeInsert(query);
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int add(List<Record> items) {
        Converter<Record, String> recordToValuesConverter = new RecordToCreateStringConverter();
        ArrayList<String> insertValueStrings = new ArrayList<String>();
        for (Record record : items) {
            insertValueStrings.add(recordToValuesConverter.forwardConvert(record));
        }
        String query = new String("INSERT INTO record(timestamp,poi_id,infected, death, recovered) VALUES $values")
            .replace("$values", String.join(",", insertValueStrings));
        try {
            return this.getConnector().connect().executeInsert(query);
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int remove(Integer id) {
        try {
            List<Integer> ids = new ArrayList<>();
            ids.add(id);
            String sql = new RecordToDeleteStringConverter().forwardConvert(ids);
            return getConnector().connect().executeDelete(sql);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public int remove(List<Integer> ids) {
        try {
            String sql = new RecordToDeleteStringConverter().forwardConvert(ids);
            return getConnector().connect().executeDelete(sql);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public int update(Record item) {
        try {
            String sql = new RecordToUpdateStringConverter().forwardConvert(item);
            return getConnector().connect().executeUpdate(sql);
        } catch (Exception e) {
            logger.info("Error in item: $item".replace("$item", item.toString()));
            logger.info("Exception type is $type".replace("$type", e.getClass().getName()));
            return 0;
        }
    }

    @Override
    public int update(List<Record> items) {
        int rowsUpdated = 0;
        for (Record item : items) {
            rowsUpdated += update(item);
        }
        return rowsUpdated;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public boolean exists(Integer id) {
        return true;
    }

    public List<Record> getByPoiID(int input) throws SQLException, InvalidQueryTypeException {
        List<Record> records = new ArrayList<>();
        String query = "SELECT *, name poi_name FROM record INNER JOIN point_of_interest ON record.poi_id = point_of_interest.id"
            + " WHERE poi_id = '$input'".replace("$input", String.valueOf(input));
        Mapper<Record> mapper = new RecordMapper();
        try {
            ResultSet rs = this.getConnector().connect().executeSelect(query);
            while (rs.next()) {
                records.add(mapper.forwardConvert(rs));
            }
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
        }
        return records;
    }

    private String createSqlFromFilterType(FilterType filterType) {
        String continent = filterType.getContinent() == null ? "" : filterType.getContinent();
        TimeframeType timeframeType = filterType.getTimeframeType();
        boolean isFilteredByTimeframe = timeframeType != null;
        String timeframe = isFilteredByTimeframe ? timeframeType.toString() : "";
        boolean isLatest = filterType.isLatest();
        String sql = RECORD_FILTER_TEMPLATE
            .replace("$group_by", isFilteredByTimeframe ? ", " + timeframe + "(timestamp)" : "")
            .replace("$filter_condition", continent != "" ?
                "WHERE continent = '$continent'".replace("$continent", continent) : "$filter_condition");

        if (sql.contains("WHERE")) {
            sql = sql.replace("WHERE", isLatest ?
                "WHERE timestamp >= DATE(NOW()) - INTERVAL 2 $time AND"
                : "WHERE");
        } else {
            sql = sql.replace("$filter_condition", isLatest ?
                "WHERE timestamp >= DATE(NOW()) - INTERVAL 2 $time"
                : "");
        }

        sql = sql.replace("$timeframe", isFilteredByTimeframe ? timeframe : "DATE")
                .replace("$time", isFilteredByTimeframe ? timeframe : "DAY")
                .replace("INTERVAL 2 DATE", "INTERVAL 2 DAY");
        return sql;
    }

    @Override
    public List<Record> getFilteredRecords(FilterType filterType) throws SQLException, InvalidQueryTypeException {
        String sql = createSqlFromFilterType(filterType);

        ResultSet rs = getConnector().connect().executeSelect(sql);
        List<Record> records = new ArrayList<>();
        Mapper<Record> mapper = new RecordMapper();
        while (rs.next()) {
            records.add(mapper.forwardConvert(rs));
        }
        return records;
    }

    @Override
    public List<Record> getAll() {
        List<Record> records = new ArrayList<>();
        Mapper<Record> mapper = new RecordMapper();
        String query = RECORD_ALL_TEMPLATE;
        try {
            ResultSet rs = this.getConnector().connect().connect().executeSelect(query);
            while (rs.next()) {
                records.add(mapper.forwardConvert(rs));
            }
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
        }
        return records;
    }

    @Override
    public Record getById(Integer id) {
        Mapper<Record> mapper = new RecordMapper();
        Record record = null;
        String query = "SELECT * FROM record INNER JOIN point_of_interest ON record.poi_id = point_of_interest.id"
            + " WHERE id = " + "\'" + id + "\'";
        try {
            ResultSet rs = this.getConnector().connect().executeSelect(query);
            record = mapper.forwardConvert(rs);
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
        }
        return record;
    }

    @Override
    public List<Record> getAggregatedRecords(AggregationType type) throws SQLException, InvalidQueryTypeException {
        String sql = buildAggregateQueryString(type);

        ResultSet rs = super.getConnector().connect().executeSelect(sql);
        List<Record> resultList = new LinkedList<>();
        Mapper<Record> recordMapper = new RecordMapper();
        while (rs.next()) {
            Record r = recordMapper.forwardConvert(rs);
            if (r != null) resultList.add(r);
        }
        return resultList;
    }

    private String buildAggregateQueryString(AggregationType type) {
        GroupByType groupByType = type.groupBy();
        String continent = type.continent();
        TimeframeType timeframeType = type.timeframe();
        String sql = groupByType == null ? AGGREGATE_TEMPLATE.replace("$group_by", "") :
            groupByType.equals(GroupByType.WORLD) ?
                WORLD_TEMPLATE
                : AGGREGATE_TEMPLATE.replace("$group_by", groupByType.toString().concat(","));

        String limitStr = groupByType == null ? LATEST_LIMIT :
            groupByType.equals(GroupByType.WORLD) ? "LIMIT 2" : LATEST_LIMIT;
        sql = sql.replace("$limit", type.isLatest() ? limitStr : "")
            .replace("$timeframe", timeframeType == null ? "DATE" : timeframeType.toString())
            .replace("$continent_clause", continent.isEmpty() ?
                "" : "AND continent = '$continent'".replace("$continent", continent))
            .replace("$fields", groupByType == GroupByType.CONTINENT ? FIELD_SUM_TEMPLATE
                : "r.id id, timestamp, poi_id, infected, death, " +
                "recovered, code, continent, mts, name poi_name")
            .replace("INTERVAL 2 DATE", "INTERVAL 2 DAY");
        if (sql.contains("COUNTRY")) {
            sql = sql.replace("poi_name FROM", "poi_name, name COUNTRY FROM")
                .replace("HAVING", "HAVING continent IN ('Africa', 'Asia', 'North America', 'South America', 'Oceania', 'Europe', 'unknown') AND");
        }
        return sql;
    }

    @Override
    public List<Record> getRecordByContinent(String continent) throws SQLException, InvalidQueryTypeException {
        List<Record> records = new ArrayList<>();
        String query = CONTINENT_TEMPLATE.replace("$continent", continent);

        ResultSet rs = this.getConnector().connect().executeSelect(query);
        while (rs.next()) {
            records.add(RecordMapper.resultToRecord(rs));
        }
        return records;
    }

    @Override
    public int getPoiIdByName(String name) throws SQLException, InvalidQueryTypeException {
        Integer id = getConnector().connect()
            .executeScalar(POI_ID_FROM_NAME.replace("$name", name));
        if (id == null) {
            return -1;
        }
        return id;
    }

    @Override
    public Date getLatestDate() throws SQLException, InvalidQueryTypeException {
        Date date = getConnector().connect()
            .executeScalar(LATEST_DATE);
        date = date == null ? new Date(0) : date;
        return date;
    }
}
