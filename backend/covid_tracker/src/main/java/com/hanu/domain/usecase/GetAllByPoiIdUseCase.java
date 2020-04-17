package com.hanu.domain.usecase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hanu.base.RequestHandler;
import com.hanu.domain.model.Record;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.exception.InvalidQueryTypeException;
import com.hanu.util.di.Inject;

public class GetAllByPoiIdUseCase implements RequestHandler<Integer, List<Record>> {

	@Inject
    private RecordRepository repository;
	
	@Override
	public List<Record> handle(Integer input) {
			try {
				return repository.getByPoiID(input);
			} catch (SQLException | InvalidQueryTypeException e) {
				return new ArrayList<Record>();
			}
	}
}
