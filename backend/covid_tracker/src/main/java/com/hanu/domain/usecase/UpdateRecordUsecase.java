package com.hanu.domain.usecase;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hanu.base.RequestHandler;
import com.hanu.db.RecordRepositoryImpl;
import com.hanu.domain.model.Record;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.util.di.Inject;

public class UpdateRecordUsecase implements RequestHandler<Record, Integer>{
	private static final Logger logger = LoggerFactory.getLogger(UpdateRecordUsecase.class);
	
	@Inject
	private RecordRepository repository = new RecordRepositoryImpl();
	
	public UpdateRecordUsecase() {
		// TODO Auto-generated constructor stub
	}


	public Integer handle(Record record){
		try {			
			repository.update(record);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
