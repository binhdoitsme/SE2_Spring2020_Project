package com.hanu.domain.usecase;

import java.util.List;

import com.hanu.base.RequestHandler;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.util.db.NonQueryResult;
import com.hanu.util.di.Inject;

public class RemoveManyRecordUseCase implements RequestHandler<List<Integer>, Object>{
	@Inject
	private RecordRepository repository;
	
	public RemoveManyRecordUseCase() { }
	
	public Object handle(List<Integer> ids) {
		int rowsAffected = repository.remove(ids);
		return new NonQueryResult(rowsAffected);
	}
}
