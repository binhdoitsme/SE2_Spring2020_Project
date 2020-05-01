package com.hanu.domain.usecase;

import com.hanu.base.RequestHandler;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.util.di.Inject;

import java.sql.Timestamp;

public class GetLatestTimeUseCase implements RequestHandler<Void, Timestamp> {
    @Inject
    private RecordRepository repository;

    public GetLatestTimeUseCase() { }

    @Override
    public Timestamp handle(Void input) throws Exception {
        return repository.getLatestTime();
    }
}
