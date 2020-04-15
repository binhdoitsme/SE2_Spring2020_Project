package com.hanu.domain.usecase;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hanu.base.RequestHandler;
import com.hanu.db.RecordRepositoryImpl;
import com.hanu.domain.model.Record;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.util.di.Inject;

public class UpdateManyRecordUsecase implements RequestHandler<List<Record>, Integer>{
	private static final Logger logger = LoggerFactory.getLogger(UpdateManyRecordUsecase.class);
	
	@Inject
	private RecordRepository repository = new RecordRepositoryImpl();
	
	public UpdateManyRecordUsecase() {
		// TODO Auto-generated constructor stub
	}


	public Integer handle(List<Record> recordArray){
		try {			
			repository.update(recordArray);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
