package com.hanu.domain.usecase;

import java.util.List;

import com.hanu.base.RequestHandler;
import com.hanu.db.RecordRepositoryImpl;
import com.hanu.domain.model.Record;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.util.di.Inject;

public class AddRecordUseCase implements RequestHandler<List<Record>, Integer> {

	@Inject
    private RecordRepository repositoty ;	

	public Integer handle(List<Record> input) {
		// TODO Auto-generated method stub
		try {
			repositoty.add(input);
			return 1;
		} catch (Exception e){
			e.printStackTrace();
			return -1;
		}
	}

}
