package com.hanu.servlet;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hanu.exception.EmptyBodyException;
import com.hanu.exception.UnauthorizedException;
import com.hanu.exception.UserFriendlyException;
import com.hanu.util.authentication.AuthToken;
import com.hanu.util.authentication.Authenticator;

import org.json.JSONObject;

@WebServlet(name = "session", urlPatterns = "/session")
public class SessionServlet extends HttpServlet {

    private static final long serialVersionUID = -5046823476115632313L;

    /**
     * Handle session validation
     * @see {@link #doGet(HttpServletRequest, HttpServletResponse)}
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // get authToken from user
        String jwt = req.getParameter("authToken");
        Authenticator auth = new Authenticator();
        if (auth.validateJwt(jwt)) {
            writeOk(null, resp);
        } else {
            Authenticator.invalidateJwt(jwt);
            writeClientError(new UnauthorizedException(), resp);
        }
    }

    /**
     * Handle session creation
     * @return authToken in {@link #resp}
     * @see {@link #doPost(HttpServletRequest, HttpServletResponse)}
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // get body as json
        String jsonString = req.getReader().lines().collect(Collectors.joining("\n"));
        if (jsonString == null || jsonString.length() == 0) {
            writeClientError(new EmptyBodyException(), resp);
            return;
        }

        JSONObject body = new JSONObject(jsonString);
        
        // get username and password
        String username = body.getString("username");
        String password = body.getString("password");

        // auth result
        String authToken = new Authenticator().authenticate(username, password);
        switch (authToken) {
            case "":
                writeClientError(new UnauthorizedException(), resp);
                return;
            default:
                writeOk(new AuthToken(authToken), resp);
                return;
        }
    }

    private void writeClientError(UserFriendlyException exception, HttpServletResponse resp) throws IOException {
        // set response mode
        resp.setHeader("Content-Type", "application/json");
        resp.setStatus(400);
        String jsonResponse = new JSONObject(exception).toString();
        resp.getWriter().write(jsonResponse);
    }

    private void writeOk(Object body, HttpServletResponse resp) throws IOException {
        // set response mode
        resp.setHeader("Content-Type", "application/json");
        resp.setStatus(200);
        String jsonResponse = body == null ? "" : new JSONObject(body).toString();
        resp.getWriter().write(jsonResponse);
    }

    /**
     * Handle session termination
     * @see {@link #doDelete(HttpServletRequest, HttpServletResponse)}
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // get authToken from user
        String jwt = req.getParameter("authToken");
        Authenticator auth = new Authenticator();
        if (auth.validateJwt(jwt)) {
            Authenticator.invalidateJwt(jwt);            
            writeOk(null, resp);
        } else {
            Authenticator.invalidateJwt(jwt);
            writeClientError(new UnauthorizedException(), resp);
        }
    }
}