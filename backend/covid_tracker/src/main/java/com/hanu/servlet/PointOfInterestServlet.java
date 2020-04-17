package com.hanu.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanu.controller.PointOfInterestController;
import com.hanu.domain.model.PointOfInterest;
import com.hanu.exception.ServerFailedException;
import com.hanu.exception.UnauthorizedException;
import com.hanu.util.authentication.Authenticator;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(name = "POI", urlPatterns = "/pointOfInterest/*")
public class PointOfInterestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private PointOfInterestController controller;

	public PointOfInterestServlet() {
		controller = new PointOfInterestController();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		pathInfo = pathInfo == null ? "" : pathInfo;
		
		switch (pathInfo) {
			case "":
			case "/":
				List<PointOfInterest> list = controller.getAll();
				writeAsJsonToResponse(list, resp);
				break;
			default:
			try {
				int id = Integer.parseInt(pathInfo.substring(1));
				PointOfInterest p = controller.getById(id);
				writeAsJsonToResponse(p, resp);
				return;
			} catch (NumberFormatException e) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			} catch (Exception e) {
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}

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
		if (o == null) {
			resp.setStatus(404);
			return;
		}
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
		if (authenticated) {
			String body = getRequestBody(req);
			JSONArray array = new JSONArray(body);
			List<PointOfInterest> pointOfInterests = new ArrayList<PointOfInterest>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject js = (JSONObject) array.get(i);
				pointOfInterests.add(
						new PointOfInterest(js.getString("name"), js.getString("code"), js.getString("continent")));
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
		if (authenticated) {
			String body = getRequestBody(req);
			JSONArray array = new JSONArray(body);
			List<PointOfInterest> pointOfInterests = new ArrayList<PointOfInterest>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject js = (JSONObject) array.get(i);
				pointOfInterests.add(new PointOfInterest(js.getInt("id"), js.getString("name"), js.getString("code"),
						js.getString("continent")));
			}
			controller.update(pointOfInterests);
		} else {
			writeAsJsonToResponse(new UnauthorizedException(), resp);
		}
	}

	// ONLY DELETING BY ID IS ENOUGH!!!
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String authToken = req.getParameter("authToken");
		boolean authenticated = new Authenticator().validateJwt(authToken);
		if (authenticated) {
			String[] ids = getRequestBody(req) // type of [ids]
							.replace("[", "").replace("]", "")
							.trim().split(",");
			
			List<Integer> pointOfInterestIds = new ArrayList<>();

			for (String id : ids) {
				pointOfInterestIds.add(new Integer(id.trim()));
			}

			controller.remove(pointOfInterestIds);
		} else {
			writeAsJsonToResponse(new UnauthorizedException(), resp);
		}
	}
}
