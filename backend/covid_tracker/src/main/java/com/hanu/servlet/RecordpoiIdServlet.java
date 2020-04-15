package com.hanu.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanu.controller.RecordController;
import com.hanu.domain.model.Record;
import com.hanu.exception.ServerFailedException;

@WebServlet(name = "poiId", urlPatterns = { "/stats/*" })
public class RecordpoiIdServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RecordController controller;

	public RecordpoiIdServlet() {
	        controller = new RecordController();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String getInfor = req.getPathInfo().substring(1);
		String[] list = getInfor.split("/");
		if(list.length == 1) {
			List<Record> records = controller.getByPoiID(Integer.parseInt(list[0]));
			writeAsJsonToResponse(records, resp);	
		} else {
			//to get by ID in poiID
		}
	}

	private void writeAsJsonToResponse(Object o, HttpServletResponse resp) throws IOException {
        resp.setHeader("Content-Type", "application/json");
        String json = new String();
        try {
            if (o instanceof JSONObject) {
                json = o.toString();
            } else {
                json = new ObjectMapper().writeValueAsString(o);
            }
            resp.setStatus(200);
        } catch (Exception e) {
            json = new ServerFailedException().toString();
            resp.setStatus(500);
        }
        resp.getWriter().write(json);
    }
}
