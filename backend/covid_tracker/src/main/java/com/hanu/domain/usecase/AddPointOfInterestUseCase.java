package com.hanu.domain.usecase;

import java.util.List;

import com.hanu.base.RequestHandler;
import com.hanu.domain.model.PointOfInterest;
import com.hanu.domain.repository.PointOfInterestRepository;
import com.hanu.util.di.Inject;

public class AddPointOfInterestUseCase implements RequestHandler<List<PointOfInterest>, Integer> {

	@Inject
	private PointOfInterestRepository repository;
	
	@Override
	public Integer handle(List<PointOfInterest> input) {
		try {
			return repository.add(input);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

}
