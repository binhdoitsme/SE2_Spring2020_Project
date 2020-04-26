package com.hanu.domain.usecase;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.hanu.base.RequestHandler;
import com.hanu.domain.model.PointOfInterest;
import com.hanu.domain.model.Record;
import com.hanu.domain.repository.PointOfInterestRepository;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.exception.InvalidQueryTypeException;
import com.hanu.util.di.Inject;

public class AddManyRecordsUseCase implements RequestHandler<Map<String, List<Record>>, Integer> {

    @Inject
    private RecordRepository recordRepository;
    @Inject
    private PointOfInterestRepository poiRepository;

    public AddManyRecordsUseCase() { }

    @Override
    public Integer handle(Map<String, List<Record>> input) throws SQLException, InvalidQueryTypeException {
        Integer rowsAffected = 0;
        Date latestDate = recordRepository.getLatestDate();
        Timestamp latestTimestamp = Timestamp.valueOf(latestDate.toString().concat(" 23:00:00")); // fixed the bug of putting redundant records into db

        for (String poiName : input.keySet()) {
            Integer poiId = recordRepository.getPoiIdByName(poiName);
            if (poiId == -1) {
                poiRepository.add(new PointOfInterest(poiName, generateRandomCode(), "unknown"));
                poiId = recordRepository.getPoiIdByName(poiName);
            }
            final int finalPoiId = poiId;
            List<Record> values = input.get(poiName)
                                        .stream()
                                        .map(r -> r.poiId(finalPoiId))
                                        .filter(r -> r.getTimestamp().after(latestTimestamp))
                                        .distinct()
                                        .collect(Collectors.toList());
            if (values.isEmpty()) continue;
            rowsAffected += recordRepository.add(values);
        }
        return rowsAffected;
    }

    static String generateRandomCode() {
        Random rd = new Random();
        return "" + (char)rd.nextInt(255) + (char)rd.nextInt(255);
    }
}