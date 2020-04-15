package com.hanu.domain.usecase;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hanu.base.RequestHandler;
import com.hanu.db.RecordRepositoryImpl;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.util.di.Inject;

public class RemoveRecordUsecase implements RequestHandler<String, Integer>{
	private static final Logger logger = LoggerFactory.getLogger(RemoveRecordUsecase.class);
	
	@Inject
	private RecordRepository repository = new RecordRepositoryImpl();
	
	public RemoveRecordUsecase() {
		
		// TODO Auto-generated constructor stub
	}
	
	public Integer handle(String id) {
		try {		
			repository.remove(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
