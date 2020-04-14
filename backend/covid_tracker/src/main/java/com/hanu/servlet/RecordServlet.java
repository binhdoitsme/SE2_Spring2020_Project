package com.hanu.servlet;

<<<<<<< HEAD
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
=======
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
>>>>>>> aeed3567fefd5f1569d612d71a4ba47c258c6a4c

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

<<<<<<< HEAD
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
=======
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanu.controller.RecordController;
import com.hanu.domain.dto.RecordDto;
import com.hanu.exception.ServerFailedException;

import org.json.JSONObject;

/**
 * The servlet for overall statistics
 * @author BinhDH
 */
@WebServlet(name="record", urlPatterns="/stats")
public class RecordServlet extends HttpServlet {
    // private static final Logger logger = LoggerFactory.getLogger(RecordServlet.class);

    private static final long serialVersionUID = 9213308487519536574L;
    private static final String GROUP_BY = "groupby";
    private static final String TIMEFRAME = "timeframe";
    private static final String CONTINENT = "continent";
    private static final String LATEST = "latest";
    
    private RecordController controller;

    public RecordServlet() {
        controller = new RecordController();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // get all request params
        List<String> queryParamNames = Collections.list(req.getParameterNames());

        if (queryParamNames.isEmpty()) {
            // GET /stats 
            // will be done by others
            return;
        } else {
            List<RecordDto> result = new LinkedList<>();
            String groupBy = req.getParameter(GROUP_BY);
            String timeframe = req.getParameter(TIMEFRAME);
            String continent = req.getParameter(CONTINENT);
            String latest = req.getParameter(LATEST);

            if (groupBy == null && timeframe == null && latest == null) {
                // GET by continent here
            } else {
                List<RecordDto> aggregateResult = controller.getAggregatedRecords(groupBy, timeframe, latest, continent);
                result.addAll(aggregateResult);
            }

            writeAsJsonToResponse(result, resp);
            return;
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
>>>>>>> aeed3567fefd5f1569d612d71a4ba47c258c6a4c
