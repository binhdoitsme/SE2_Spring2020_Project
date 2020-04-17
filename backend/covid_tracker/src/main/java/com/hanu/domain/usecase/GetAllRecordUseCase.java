package com.hanu.domain.usecase;

import java.util.ArrayList;
import java.util.List;

import com.hanu.base.RequestHandler;
import com.hanu.domain.model.Record;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.util.di.Inject;

public class GetAllRecordUseCase implements RequestHandler<Void, List<Record>> {
	
	@Inject
    private RecordRepository repository;
	
	public GetAllRecordUseCase() { }

	@Override
	public List<Record> handle(Void input) {
		try {
			return repository.getAll();
		} catch (Exception e){
			return new ArrayList<Record>();
		}
	}
}
