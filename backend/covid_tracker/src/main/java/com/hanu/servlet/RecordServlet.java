package com.hanu.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hanu.controller.RecordController;
import com.hanu.domain.model.Record;

@WebServlet(name = "record", urlPatterns = "/record")
public class RecordServlet extends HttpServlet {
	/**
	 * 
	 */
	public static final Logger logger = LoggerFactory.getLogger(RecordController.class);
	
	private static final long serialVersionUID = 3234304044417212560L;

	private RecordController controller;

	public RecordServlet() {
		controller = new RecordController();
	}

	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		BufferedReader reader = req.getReader();
		//jsonString Array -> list of record
		Iterable<Record> recordArray = new Gson().fromJson(reader, new TypeToken<ArrayList<Record>>(){}.getType());
		for (Record record : recordArray) {
	        try {	        	
	        	record.setInfected(Long.parseLong(req.getParameter("infected")));
	        	record.setDeath(Long.parseLong(req.getParameter("death")));
	        	record.setRecovered(Long.parseLong(req.getParameter("recovered")));
	        } catch (Exception e) {
	        	logger.error(e.getMessage(), e);
	        }
		}
		controller.updateRecords(recordArray);
		resp.getWriter().println("Updated!!!");
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		BufferedReader reader = req.getReader();
		//jsonString Array -> list of record
		Iterable<Record> recordArray = new Gson().fromJson(reader, new TypeToken<ArrayList<Record>>(){}.getType());
		controller.removeRecords(recordArray);
		resp.getWriter().println("Removed!!!");
	}
	
	
}
