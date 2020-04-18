package com.hanu.domain.usecase;

import java.util.List;

import com.hanu.base.RequestHandler;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.util.di.Inject;

public class RemoveManyRecordUseCase implements RequestHandler<List<Integer>, Integer>{
	@Inject
	private RecordRepository repository;
	
	public RemoveManyRecordUseCase() { }
	
	public Integer handle(List<Integer> ids) {
		int rowsAffected = repository.remove(ids);
		return rowsAffected;
	}
}
