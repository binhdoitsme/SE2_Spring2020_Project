package com.hanu.domain.usecase;

import java.util.List;

import com.hanu.base.RequestHandler;
import com.hanu.domain.model.Record;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.util.di.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateManyRecordUseCase implements RequestHandler<List<Record>, Integer>{
	private static final Logger logger = LoggerFactory.getLogger(UpdateManyRecordUseCase.class);
	
	@Inject
	private RecordRepository repository;
	
	public UpdateManyRecordUseCase() { }


	public Integer handle(List<Record> recordArray){
		try {			
			return repository.update(recordArray);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return 0;
		}
	}
}
