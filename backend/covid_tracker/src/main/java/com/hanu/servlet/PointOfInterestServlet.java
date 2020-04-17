package com.hanu.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanu.controller.PointOfInterestController;
import com.hanu.domain.model.PointOfInterest;
import com.hanu.domain.model.Record;
import com.hanu.exception.ServerFailedException;
import com.hanu.exception.UnauthorizedException;
import com.hanu.util.authentication.Authenticator;
import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

@WebServlet(name = "POI", urlPatterns = "/points" )
public class PointOfInterestServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PointOfInterestController controller;
	public PointOfInterestServlet() {
		// TODO Auto-generated constructor stub
		controller = new PointOfInterestController();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		List<String> queryParamNames = Collections.list(req.getParameterNames());
		if(queryParamNames.isEmpty()) {
			List<PointOfInterest> list = controller.getAll();
			writeAsJsonToResponse(list, resp);
		} else if (req.getParameter("id") != null) {
			int id = Integer.parseInt(req.getParameter("id"));
			PointOfInterest p = controller.getById(id);
			writeAsJsonToResponse(p, resp);
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
    		JSONObject jsonObject = HTTP.toJSONObject(getRequestBody(req));
    		JSONArray jsonArray = (JSONArray) jsonObject.get("PointOfInterest");
    		List<PointOfInterest> pointOfInterests = new ArrayList<PointOfInterest>();
    		for( int i = 0; i < jsonArray.length(); i++) {
    			JSONObject js = (JSONObject) jsonArray.get(i);
    			pointOfInterests.add(new PointOfInterest(js.getInt("id"), js.getString("name"), js.getString("code"), js.getString("continent")));
    		}
    		controller.add(pointOfInterests);
    	} else {
    		writeAsJsonToResponse(new UnauthorizedException(), resp);
    	}
    }
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String authToken = req.getParameter("authToken");
    	boolean authenticated = new Authenticator().validateJwt(authToken);
    	if(authenticated) { 
    		JSONObject jsonObject = HTTP.toJSONObject(getRequestBody(req));
    		JSONArray jsonArray = (JSONArray) jsonObject.get("PointOfInterest");
    		List<PointOfInterest> pointOfInterests = new ArrayList<PointOfInterest>();
    		for( int i = 0; i < jsonArray.length(); i++) {
    			JSONObject js = (JSONObject) jsonArray.get(i);
    			pointOfInterests.add(new PointOfInterest(js.getInt("id"), js.getString("name"), js.getString("code"), js.getString("continent")));
    		}
    		controller.update(pointOfInterests);
    	} else {
    		writeAsJsonToResponse(new UnauthorizedException(), resp);
    	}
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	// TODO Auto-generated method stub
    	super.doDelete(req, resp);
    }
}
