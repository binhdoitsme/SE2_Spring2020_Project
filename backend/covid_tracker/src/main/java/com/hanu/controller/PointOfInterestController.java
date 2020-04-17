package com.hanu.controller;

import java.util.List;
import com.hanu.domain.model.PointOfInterest;
import com.hanu.domain.model.Record;
import com.hanu.domain.usecase.AddPointOfInterestUseCase;
import com.hanu.domain.usecase.GetAllPointUseCase;
import com.hanu.domain.usecase.GetPointOfInterestByIdUseCase;
import com.hanu.domain.usecase.RemovePointOfInterestUseCase;
import com.hanu.domain.usecase.UpdatePointOfInterestUseCase;

public class PointOfInterestController {
	
	public List<PointOfInterest> getAll() {
		return new GetAllPointUseCase().handle(null);
	}
	
	public PointOfInterest getById(int id) {
		List<Record> records = new RecordController().getByPoiID(id);
		System.out.println(records.size());
		return new GetPointOfInterestByIdUseCase().handle(id).setRecords(records);
	}
	
	public int add(List<PointOfInterest> input) {
		return new AddPointOfInterestUseCase().handle(input);
	}
	
	public int update(List<PointOfInterest> input) {
		return new UpdatePointOfInterestUseCase().handle(input);
	}
	
	public int remove(List<Integer> input) {
		return new RemovePointOfInterestUseCase().handle(input);
	}
}
