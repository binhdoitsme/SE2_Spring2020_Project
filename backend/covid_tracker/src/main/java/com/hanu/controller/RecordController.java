package com.hanu.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.hanu.base.Converter;
import com.hanu.domain.model.Record;
import com.hanu.domain.usecase.RemoveRecordUsecase;
import com.hanu.domain.usecase.UpdateRecordUsecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordController {
    public static final Logger logger = LoggerFactory.getLogger(RecordController.class);
    // other methods written by others...

    /**
     * Delegate to GetAggregatedRecords usecase
     */
    private UpdateRecordUsecase UpdateRecordUsecase;
    private RemoveRecordUsecase RemoveRecordUsecase;
        
    public RecordController() {
		super();
		UpdateRecordUsecase = new UpdateRecordUsecase();
		RemoveRecordUsecase = new RemoveRecordUsecase();
	}

	public void updateRecords(Record record) {
    	Record updateRecord = new Record();
        try {        	
        	updateRecord.setId(record.getId());
        	updateRecord.setTimestamp(record.getTimestamp());
        	updateRecord.setPoiId(record.getPoiId());
        	updateRecord.setInfected(record.getInfected());
        	updateRecord.setDeath(record.getDeath());
        	updateRecord.setRecovered(record.getRecovered());
    	              
            UpdateRecordUsecase.handle(updateRecord);
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    public void removeRecords(Record record) {
    	Record removeRecord = new Record();
        try {        	
        	removeRecord.setId(record.getId());
        	removeRecord.setTimestamp(record.getTimestamp());
        	removeRecord.setPoiId(record.getPoiId());
        	removeRecord.setInfected(record.getInfected());
        	removeRecord.setDeath(record.getDeath());
        	removeRecord.setRecovered(record.getRecovered());
    	              
            RemoveRecordUsecase.handle(removeRecord);
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);          
        }
    }



}