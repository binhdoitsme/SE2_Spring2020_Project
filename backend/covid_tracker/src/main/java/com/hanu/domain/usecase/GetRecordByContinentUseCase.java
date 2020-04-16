package com.hanu.domain.usecase;

import java.util.List;
import java.sql.SQLException;
import java.util.ArrayList;

import com.hanu.base.Repository;
import com.hanu.base.RequestHandler;
import com.hanu.domain.model.Record;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.exception.InvalidQueryTypeException;
import com.hanu.util.di.Inject;

public class GetRecordByContinentUseCase implements RequestHandler<String, List<Record>> {

	@Inject
	private RecordRepository repo; 
	@Override
	public List<Record> handle(String continent)  {
		// TODO Auto-generated method stub
		try {
			return repo.getRecordByContinent(continent);
		} catch (SQLException | InvalidQueryTypeException e) {
			// TODO Auto-generated catch block
			return new ArrayList<Record>();
		}
	}

}
