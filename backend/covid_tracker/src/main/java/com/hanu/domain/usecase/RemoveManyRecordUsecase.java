package com.hanu.domain.usecase;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hanu.base.RequestHandler;
import com.hanu.db.RecordRepositoryImpl;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.util.db.NonQueryResult;
import com.hanu.util.di.Inject;

public class RemoveManyRecordUseCase implements RequestHandler<List<Integer>, Object>{
	@Inject
	private RecordRepository repository = new RecordRepositoryImpl();
	
	public RemoveManyRecordUseCase() { }
	
	public Object handle(List<Integer> ids) {
		int rowsAffected = repository.remove(ids);
		return new NonQueryResult(rowsAffected);
	}
}
