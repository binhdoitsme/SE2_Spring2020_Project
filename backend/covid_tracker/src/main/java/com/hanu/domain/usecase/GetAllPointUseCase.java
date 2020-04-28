package com.hanu.domain.usecase;

import java.util.ArrayList;
import java.util.List;

import com.hanu.base.RequestHandler;
import com.hanu.domain.model.PointOfInterest;
import com.hanu.domain.repository.PointOfInterestRepository;
import com.hanu.util.di.Inject;

public class GetAllPointUseCase implements RequestHandler<Void, List<PointOfInterest>>{
	
	@Inject
    private PointOfInterestRepository repository;

	@Override
	public List<PointOfInterest> handle(Void input) {
		try {
			return repository.getAll();
		} catch (Exception e){
			return new ArrayList<PointOfInterest>();
		}
	}
}
