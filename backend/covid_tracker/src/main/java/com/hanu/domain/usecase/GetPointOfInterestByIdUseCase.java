package com.hanu.domain.usecase;

import com.hanu.base.RequestHandler;
import com.hanu.domain.model.PointOfInterest;
import com.hanu.domain.repository.PointOfInterestRepository;
import com.hanu.util.di.Inject;

public class GetPointOfInterestByIdUseCase implements RequestHandler<Integer, PointOfInterest>{
	@Inject
    private PointOfInterestRepository repository;
	
	@Override
	public PointOfInterest handle(Integer input) {
		return repository.getById(input);
	}
}
