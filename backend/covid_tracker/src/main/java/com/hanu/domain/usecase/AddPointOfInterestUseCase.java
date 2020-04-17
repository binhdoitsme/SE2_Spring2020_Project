package com.hanu.domain.usecase;

import java.util.List;

import com.hanu.base.RequestHandler;
import com.hanu.domain.model.PointOfInterest;
import com.hanu.domain.repository.PointOfInterestRepository;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.util.di.Inject;

public class AddPointOfInterestUseCase implements RequestHandler<List<PointOfInterest>, Integer> {

	@Inject
    private PointOfInterestRepository repositoty ;
	
	@Override
	public Integer handle(List<PointOfInterest> input) {
		return repositoty.add(input);
	}

}
