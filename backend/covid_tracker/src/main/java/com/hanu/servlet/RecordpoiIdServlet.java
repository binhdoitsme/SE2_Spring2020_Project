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
		List<Record> records = null;
		String getInfor = req.getPathInfo().substring(1);
		String[] list = getInfor.split("/");
		records = controller.getByPoiID(Integer.parseInt(list[0]));
		if(list.length == 1) {
			writeAsJsonToResponse(records, resp);	
		} else if(list.length == 2) {
			try {
				List<Record> getbyID = new ArrayList<Record>();
				for(Record record : records) {
					if(record.getId() == Integer.parseInt(list[1])) {
						getbyID.add(record);
					}
				}
				writeAsJsonToResponse(getbyID, resp);
			} catch (Exception e) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		} else {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
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
