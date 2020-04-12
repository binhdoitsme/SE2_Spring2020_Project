package com.hanu.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hanu.controller.RecordController;
import com.hanu.db.RecordRepositoryImpl;
import com.hanu.domain.model.Record;
import com.hanu.exception.EmptyBodyException;

@WebServlet(name = "record", urlPatterns = "/record")
public class RecordServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3234304044417212560L;

	private RecordController controller;

	public RecordServlet() {
		controller = new RecordController();
	}


	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		BufferedReader reader = req.getReader();
		Iterable<Record> recordArray = new Gson().fromJson(reader, new TypeToken<ArrayList<Record>>(){}.getType());
		controller.updateRecords(recordArray);
		// RecordRepositoryImpl update = new RecordRepositoryImpl();
		// update.update(updateRecord);
		resp.getWriter().println("Updated!!!");
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		BufferedReader reader = req.getReader();
		Iterable<Record> recordArray = new Gson().fromJson(reader, new TypeToken<ArrayList<Record>>(){}.getType());
		controller.removeRecords(recordArray);
		resp.getWriter().println("Removed!!!");
	}
}
