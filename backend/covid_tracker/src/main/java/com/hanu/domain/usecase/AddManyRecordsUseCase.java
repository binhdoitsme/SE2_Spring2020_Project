package com.hanu.domain.usecase;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hanu.base.RequestHandler;
import com.hanu.domain.model.Record;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.exception.InvalidQueryTypeException;
import com.hanu.util.di.Inject;

public class AddManyRecordsUseCase implements RequestHandler<Map<String, List<Record>>, Integer> {

    @Inject
    private RecordRepository repository;

    public AddManyRecordsUseCase() {
    }

    @Override
    public Integer handle(Map<String, List<Record>> input) throws SQLException, InvalidQueryTypeException {
        Integer rowsAffected = 0;
        Date latestDate = repository.getLatestDate();
        for (String poiName : input.keySet()) {
            int poiId = repository.getPoiIdByName(poiName);
            Collection<Record> values = input.get(poiName)
                                            .stream()
                                            .map(r -> r.poiId(poiId))
                                            .filter(r -> r.getTimestamp().after(latestDate))
                                            .collect(Collectors.toSet());
            if (values.isEmpty()) continue;
            rowsAffected += repository.add(values);
        }
        return rowsAffected;
    }

}