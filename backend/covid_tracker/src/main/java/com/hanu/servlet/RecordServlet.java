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

import com.hanu.controller.RecordController;

@WebServlet(name = "record", urlPatterns = "/record")
public class RecordServlet extends HttpServlet {
	/**
	 * 
	 */
	public static final Logger logger = LoggerFactory.getLogger(RecordServlet.class);
	
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
		try {
			controller.updateRecords(reader);
			resp.setStatus(200);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp.setStatus(500);
		}		
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		BufferedReader reader = req.getReader();
		//jsonString Array -> list of record
		try {
			controller.removeRecords(reader);
			resp.setStatus(200);			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp.setStatus(500);
		}		
	}
	
	
}
