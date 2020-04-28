package com.hanu.domain.usecase;

import java.util.List;

import com.hanu.base.RequestHandler;
import com.hanu.domain.repository.PointOfInterestRepository;
import com.hanu.util.di.Inject;

public class RemovePointOfInterestUseCase implements RequestHandler<List<Integer>, Integer> {
	
	@Inject
	private PointOfInterestRepository repository;
	
	@Override
	public Integer handle(List<Integer> input) {
		try {
			return repository.remove(input);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
