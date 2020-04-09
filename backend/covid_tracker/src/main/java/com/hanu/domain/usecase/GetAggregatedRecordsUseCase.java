package com.hanu.domain.usecase;

import java.util.ArrayList;
import java.util.List;

import com.hanu.base.RequestHandler;
import com.hanu.db.util.AggregationType;
import com.hanu.domain.model.Record;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.util.di.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetAggregatedRecordsUseCase implements RequestHandler<AggregationType, List<Record>> {
    private static final Logger logger = LoggerFactory.getLogger(GetAggregatedRecordsUseCase.class);

    @Inject
    private RecordRepository repository;

    public GetAggregatedRecordsUseCase() { }

    @Override
    public List<Record> handle(AggregationType type) {
        try {
            return repository.getAggregatedRecords(type);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}