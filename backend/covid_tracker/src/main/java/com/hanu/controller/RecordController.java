package com.hanu.controller;

import java.util.ArrayList;
import java.util.List;

import com.hanu.domain.model.Record;
import com.hanu.domain.usecase.RemoveRecordUsecase;
import com.hanu.domain.usecase.UpdateRecordUsecase;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordController {
    public static final Logger logger = LoggerFactory.getLogger(RecordController.class);
    
    private UpdateRecordUsecase UpdateRecordUsecase;
    private RemoveRecordUsecase RemoveRecordUsecase;
        
    public RecordController() {
		super();
		UpdateRecordUsecase = new UpdateRecordUsecase();
		RemoveRecordUsecase = new RemoveRecordUsecase();
	}


    
    private Record getRecord(Record input) {
    	Record record = new Record();
    	try {        	
        	record.setId(input.getId());
        	record.setTimestamp(input.getTimestamp());
        	record.setPoiId(input.getPoiId());
        	record.setInfected(input.getInfected());
        	record.setDeath(input.getDeath());
        	record.setRecovered(input.getRecovered()); 
        	
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
		return record;
    }

    
	public void updateRecords(Iterable<Record> recordArray) {
		List<Record> updateRecords = new ArrayList<Record>();
		for (Record record : recordArray) {
	        try {
	        	Record r = getRecord(record);
	        	updateRecords.add(r);	            
	        } catch (Exception e) {
	        	logger.error(e.getMessage(), e);
	        }
	        UpdateRecordUsecase.handle(updateRecords);
		}   	
    }
	
    
    public void removeRecords(Iterable<Record> recordArray) {
    	List<Record> removeRecords = new ArrayList<Record>();
		for (Record record : recordArray) {
	        try {        	
	        	removeRecords.add(getRecord(record));	            
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	        }
	        RemoveRecordUsecase.handle(removeRecords);
		}   	
    }



}