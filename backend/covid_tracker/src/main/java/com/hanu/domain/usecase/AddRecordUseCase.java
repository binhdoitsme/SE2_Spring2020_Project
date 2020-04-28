package com.hanu.domain.usecase;

import java.util.List;

import com.hanu.base.RequestHandler;
import com.hanu.domain.model.Record;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.util.di.Inject;

public class AddRecordUseCase implements RequestHandler<List<Record>, Integer> {

	@Inject
	private RecordRepository repository;

	public Integer handle(List<Record> input) {
		try {
			return repository.add(input);
		} catch (Exception e){
			e.printStackTrace();
			return -1;
		}
	}

}
