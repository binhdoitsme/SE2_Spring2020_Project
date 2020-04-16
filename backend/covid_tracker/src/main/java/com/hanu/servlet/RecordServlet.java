package com.hanu.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
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
import com.hanu.util.configuration.Configuration;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "record", urlPatterns = "/stats")
public class RecordServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(RecordServlet.class);

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
                try {
                    List<Record> recordByContinent = controller.getRecordByContinent(continent);
                    writeAsJsonToResponse(recordByContinent, resp);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    writeAsJsonToResponse(new ServerFailedException(), resp);
                }
            } else {
                List<RecordDto> aggregateResult = controller.getAggregatedRecords(groupBy, timeframe, latest, continent);
                result.addAll(aggregateResult);
            }

            writeAsJsonToResponse(result, resp);
            return;
        }
    }

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // request parameter: authToken
        String authToken = req.getParameter("authToken");
        boolean authenticated = new Authenticator().validateJwt(authToken);
        if (!authenticated) {
            writeAsJsonToResponse(new UnauthorizedException(), resp);
            return;
        }

		String body = getRequestBody(req);
        
        Object result = controller.removeRecords(body);
        writeAsJsonToResponse(result, resp);
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
            if (o instanceof ServerFailedException) {
                resp.setStatus(500);
            } else if (o instanceof UnauthorizedException) {
                resp.setStatus(400);
            } else {
                resp.setStatus(200);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            json = new ServerFailedException().toString();
            resp.setStatus(500);
        }
        resp.getWriter().write(json);
    }

    private String getRequestBody(HttpServletRequest req) throws IOException {
        BufferedReader reader = req.getReader();
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine())!= null) {
            body.append(line).append("\n");
        }
        return body.toString();
    }

    private String getTextDataFromUrl(String url) throws IOException, MalformedURLException {
        URL link = new URL(url);
        BufferedReader reader = new BufferedReader(new InputStreamReader(link.openConnection().getInputStream()));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine())!= null) {
            content.append(line).append("\n");
        }
        return content.toString();
    }

    /**
     * PUT /stats: update data by using pre-defined source
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // request parameter: authToken
        String authToken = req.getParameter("authToken");
        boolean authenticated = new Authenticator().validateJwt(authToken);
        if (!authenticated) {
            writeAsJsonToResponse(new UnauthorizedException(), resp);
            return;
        } else {
            // get body
            BufferedReader reader = req.getReader();
            StringBuilder bodyStringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                bodyStringBuilder.append(line).append("\n");
            }
            String body = bodyStringBuilder.toString();

            if (body == null) {
                String remoteDataUrl = Configuration.get("io.remotedatasource");
                String remoteData = getTextDataFromUrl(remoteDataUrl);
                
                // delgate to controller
                Object adđResult = controller.addBatch(remoteData);
                writeAsJsonToResponse(adđResult, resp);
                return;
            } else {
                updateManually(body, req, resp);
            }
        }
    }

    private void updateManually(String body, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
            // jsonString Array -> list of record
            String updateRecords = body.toString();
            Object result = controller.updateRecords(updateRecords);
            resp.setStatus(200);
            writeAsJsonToResponse(result, resp);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resp.setStatus(500);
            writeAsJsonToResponse(new ServerFailedException(), resp);
        }
    }
}
