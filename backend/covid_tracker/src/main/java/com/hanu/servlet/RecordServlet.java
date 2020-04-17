package com.hanu.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanu.controller.RecordController;
import com.hanu.domain.dto.RecordDto;
import com.hanu.domain.model.Record;
import com.hanu.exception.ServerFailedException;
import com.hanu.exception.UnauthorizedException;
import com.hanu.util.authentication.Authenticator;
import com.mysql.cj.xdevapi.JsonArray;

import javassist.expr.NewArray;

import org.json.HTTP;
import org.json.JSONArray;
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
            List<Record> allRecords = controller.getRecords();
            writeAsJsonToResponse(allRecords, resp);
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
    
    private String getRequestBody(HttpServletRequest req) throws IOException {
        BufferedReader reader = req.getReader();
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line).append("\n");
        }
        return body.toString();
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
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String authToken = req.getParameter("authToken");
    	boolean authenticated = new Authenticator().validateJwt(authToken);
    	if(authenticated) {		
    		// get json from request and log it out
    		String body = getRequestBody(req);
    		JSONArray array = new JSONArray(body);
    		List<Record> records = new ArrayList<Record>();
    		for( int i = 0; i < array.length(); i++) {
    			JSONObject js = (JSONObject) array.get(i);
    			records.add(new Record(new Timestamp(js.getLong("timestamp")), 
    									js.getInt("poiId"), 
    									js.getLong("infected"), 
    									js.getLong("death"), 
    									js.getLong("recovered")));
    		}
    		controller.addRecord(records);
    	} else {
    		writeAsJsonToResponse(new UnauthorizedException(), resp);
    	}
    }
}