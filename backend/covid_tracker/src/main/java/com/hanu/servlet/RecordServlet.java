package com.hanu.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanu.controller.RecordController;
import com.hanu.domain.dto.RecordDto;
import com.hanu.domain.model.Record;
import com.hanu.exception.ServerFailedException;
import com.hanu.exception.UnauthorizedException;
import com.hanu.util.authentication.Authenticator;
import com.hanu.util.configuration.Configuration;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

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
            List<Record> allRecords = controller.getRecords();
            writeAsJsonToResponse(allRecords, resp);
            return;
        } else {
            List<RecordDto> result = new LinkedList<>();
            String groupBy = req.getParameter(GROUP_BY);
            String timeframe = req.getParameter(TIMEFRAME);
            String continent = req.getParameter(CONTINENT);
            String latest = req.getParameter(LATEST);

            if (groupBy == null) {
                // GET by continent here
                // get filtered records
                List<Record> filteredRecords = controller.getFilteredRecords(continent, timeframe, latest);
                writeAsJsonToResponse(filteredRecords, resp);
                return;
            } else {
                List<RecordDto> aggregateResult = controller.getAggregatedRecords(groupBy, timeframe, latest,
                    continent);
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
        resp.setCharacterEncoding("UTF-8");
        String json = new String();
        try {
            if (o instanceof JSONObject) {
                json = o.toString();
            } else if (o instanceof String) {
                json = (String)o;
            }
            else {
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
        while ((line = reader.readLine()) != null) {
            body.append(line).append("\n");
        }
        return body.toString();
    }

    private String getTextDataFromUrl(String url) throws IOException, MalformedURLException {
        /* Start of Fix */
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
            public void checkClientTrusted(X509Certificate[] certs, String authType) { }
            public void checkServerTrusted(X509Certificate[] certs, String authType) { }

        } };

        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = (hostname, session) -> true;
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        /* End of the fix*/

        URL link = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection)link.openConnection();
        String charset = "UTF-8";

        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.122 Safari/537.36");

        connection.setRequestProperty("Accept-Charset", charset);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

        BufferedReader reader;
        if ("gzip".equals(connection.getContentEncoding())) {
            reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream()), "UTF-8"));
        }
        else {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        }
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        reader.close();
        connection.disconnect();
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
            String body = getRequestBody(req);

            if (body.equals("")) {
//                String remoteDataUrl = Configuration.get("io.remote.countries");
//                String remoteData = getTextDataFromUrl(remoteDataUrl);
//
//                // delgate to controller
//                Object addResult = controller.addBatch(remoteData);
//                writeAsJsonToResponse(addResult, resp);
//                return;
                try {
                    Object result = updateFromChosenUrls();
                    writeAsJsonToResponse(result, resp);
                } catch (Exception e) {
                    e.printStackTrace();
                    writeAsJsonToResponse(new ServerFailedException(), resp);
                }
            } else {
                updateManually(body, req, resp);
            }
        }
    }

    // parse data from chosen urls io.remote.update.duration
    private Object updateFromChosenUrls() throws IOException {
        final long durationBetweenUpdates = Long.parseLong(Configuration.get("io.remote.update.duration"));
        long latestTime = controller.getLatestTime().getTime();
        long now = new Date().getTime();
        if (now - latestTime < durationBetweenUpdates) {
            return "{\"message\" : \"Not yet time for update data, try again in " + ((latestTime - now  + durationBetweenUpdates)/1000) + " second(s)!\"}";
        }

        final String worldUrl = Configuration.get("io.remote.countries.latest");
        final String worldSelector = Configuration.get("io.remote.countries.latest.selector");
        final String vietnamUrl = Configuration.get("io.remote.vietnam.latest");
        final String vietnamSelector = Configuration.get("io.remote.vietnam.latest.selector");

        String vietnamHtml = getTextDataFromUrl(vietnamUrl);
        String worldHtml = getTextDataFromUrl(worldUrl);

        Document worldDoc = Jsoup.parse(worldHtml);
        Document vietnamDoc = Jsoup.parse(vietnamHtml);

        Elements worldRows = worldDoc.select(worldSelector);
        Elements vietnamRows = vietnamDoc.select(vietnamSelector);

        List<Record> worldRecordsToAdd = new LinkedList<>();
        worldRecordsToAdd.addAll(worldRows.stream()
                                .map(row -> createWorldRecordFromRow(row, now))
                                .distinct()
                                .filter(row -> row != null)
                                .collect(Collectors.toList()));

        List<Record> vietnameseRecordsToAdd = new LinkedList<>();
        vietnameseRecordsToAdd.addAll(vietnamRows.stream()
                                .map(row -> createVietnamRecordFromRow(row, now))
                                .distinct()
                                .filter(row -> row != null)
                                .collect(Collectors.toList()));

        Object result1 = controller.addBatch(worldRecordsToAdd, "unknown");
        Object result2 = controller.addBatch(vietnameseRecordsToAdd, "Vietnam");
        if (result1 instanceof ServerFailedException || result2 instanceof ServerFailedException) {
            return new ServerFailedException();
        } else {
            return "{\"message\": \"Database updated!\"}";
        }
//        return "{\"message\": \"Database updated!\"}";

    }

    private Record createWorldRecordFromRow(Element row, long now) {
        System.out.println(row);
        int infected = Integer.parseInt(row.child(2).text().replace(",", ""));
        String deathStr = row.child(4).text().replace(",", "");
        int death = Integer.parseInt(deathStr.equals("") || deathStr.equals("N/A") ? "0": deathStr);
        String recoveredStr = row.child(6).text().replace(",", "");
        int recovered = Integer.parseInt(recoveredStr.equals("") || recoveredStr.equals("N/A") ? "0": recoveredStr);
        String country = row.child(1).child(0).text()
            .replace("USA", "US")
            .replace("UK", "United Kingdom")
            .replace("S. Korea", "South Korea")
            .replace("UAE", "United Arab Emirates");
        if (country.startsWith("Total")) return null;
        return new Record(0, new Timestamp(now), 0, infected, death, recovered).poiName(country);
    }

    private Record createVietnamRecordFromRow(Element row, long now) {
        int infected = Integer.parseInt(row.child(1).text().replace(",", ""));
        String deathStr = row.child(4).text().replace(",", "");
        int death = Integer.parseInt(deathStr.equals("") || deathStr.equals("N/A") ? "0": deathStr);
        String recoveredStr = row.child(3).text().replace(",", "");
        int recovered = Integer.parseInt(recoveredStr.equals("") || recoveredStr.equals("N/A") ? "0": recoveredStr);
        String country = row.child(0).text();
        if (country.startsWith("Total")) return null;
        return new Record(0, new Timestamp(now), 0, infected, death, recovered).poiName(country);
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authToken = req.getParameter("authToken");
        boolean authenticated = new Authenticator().validateJwt(authToken);
        if (authenticated) {
            // get json from request and log it out
            String body = getRequestBody(req);
            controller.addRecordFromBody(body);
        } else {
            writeAsJsonToResponse(new UnauthorizedException(), resp);
        }
    }
}
