package com.hanu.domain.usecase;

import java.util.ArrayList;
import java.util.List;

import com.hanu.base.RequestHandler;
import com.hanu.domain.model.Record;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.util.di.Inject;

public class GetAllRecordUseCase implements RequestHandler<Record, List<Record>> {
	@Inject
    private RecordRepository repository;
	
	public GetAllRecordUseCase() {
	}
	
	public List<Record> handle() {
		try {
			return repository.getAll();
		} catch (Exception e){
			return new ArrayList<Record>();
		}
	}

	@Override
	public List<Record> handle(Record input) {
		// TODO Auto-generated method stub
		return null;
	}
}
