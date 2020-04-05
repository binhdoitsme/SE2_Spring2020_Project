package com.hanu.util.server;

import javax.servlet.http.HttpServlet;
import java.util.List;

public interface TomcatBuilder {
    TomcatBuilder defaultConfigure();
    TomcatBuilder registerServlets(List<Class<HttpServlet>> servlets);
    void startServer();
}
