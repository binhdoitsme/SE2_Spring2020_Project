package com.hanu.domain.usecase;

import com.hanu.base.RequestHandler;
import com.hanu.db.util.FilterType;
import com.hanu.domain.model.Record;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.util.di.Inject;

import java.util.ArrayList;
import java.util.List;

public class GetFilteredRecordsUseCase implements RequestHandler<FilterType, List<Record>> {

    @Inject
    private RecordRepository repository;

    @Override
    public List<Record> handle(FilterType input) {
        try {
            return repository.getFilteredRecords(input);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
