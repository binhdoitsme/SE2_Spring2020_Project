package com.hanu.domain.usecase;

import java.util.List;

import com.hanu.base.RequestHandler;
import com.hanu.domain.model.PointOfInterest;
import com.hanu.domain.repository.PointOfInterestRepository;
import com.hanu.util.di.Inject;

public class GetAllPointUseCase implements RequestHandler<PointOfInterest, List<PointOfInterest>>{
	
	@Inject
    private PointOfInterestRepository repository;

	@Override
	public List<PointOfInterest> handle(PointOfInterest input) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<PointOfInterest> handle() {
		return repository.getAll();
	}
}
