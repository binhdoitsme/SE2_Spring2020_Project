package com.hanu.controller;

import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hanu.domain.model.Record;
import com.hanu.domain.usecase.RemoveRecordUsecase;
import com.hanu.domain.usecase.UpdateRecordUsecase;
import com.hanu.domain.usecase.RemoveManyRecordUsecase;
import com.hanu.domain.usecase.UpdateManyRecordUsecase;
import com.hanu.exception.InvalidQueryTypeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordController {
    public static final Logger logger = LoggerFactory.getLogger(RecordController.class);
    private UpdateRecordUsecase UpdateRecordUsecase;
    private RemoveRecordUsecase RemoveRecordUsecase;
    private UpdateManyRecordUsecase UpdateManyRecordUsecase;
    private RemoveManyRecordUsecase RemoveManyRecordUsecase;
    
        
    public RecordController() {
		super();
		UpdateRecordUsecase = new UpdateRecordUsecase();
		RemoveRecordUsecase = new RemoveRecordUsecase();
		UpdateManyRecordUsecase = new UpdateManyRecordUsecase();
		RemoveManyRecordUsecase = new RemoveManyRecordUsecase();
	}

    
    
	public void updateRecords(BufferedReader reader) throws SQLException, InvalidQueryTypeException {		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd' 'HH:mm:ss").create();
		List<Record> updateRecords = new ArrayList<Record>();
    	try {
    		Iterable<Record> recordArray = gson.fromJson(reader, new TypeToken<ArrayList<Record>>(){}.getType());    		
    		for (Record record : recordArray) {
    	        try {	        	
    	        	updateRecords.add(record);	            
    	        } catch (Exception e) {
    	        	logger.error(e.getMessage(), e);
    	        }    	        
    		} 
    		UpdateManyRecordUsecase.handle(updateRecords);  	
		} catch (Exception e) {
			Record record = gson.fromJson(reader, Record.class);		
			UpdateRecordUsecase.handle(record);			
		}	    	
    }
	
    
    public void removeRecords(BufferedReader reader) {
    	Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd' 'HH:mm:ss").create();
		List<String> recordIDs = new ArrayList<>();
		try {
    		Iterable<Record> recordArray = gson.fromJson(reader, new TypeToken<ArrayList<Record>>(){}.getType());    		
    		for (Record record : recordArray) {
    	        try {	        	
    	        	recordIDs.add(String.valueOf(record.getId()));            
    	        } catch (Exception e) {
    	        	logger.error(e.getMessage(), e);
    	        }   	        
    		}
    		RemoveManyRecordUsecase.handle(recordIDs);
		} catch (Exception e) {
			try {
				Record record = gson.fromJson(reader, Record.class);
				RemoveRecordUsecase.handle(String.valueOf(record.getId()));
			} catch (Exception e2) {
				logger.error(e.getMessage(), e);
			}		
		}		    
		}   	
    }

