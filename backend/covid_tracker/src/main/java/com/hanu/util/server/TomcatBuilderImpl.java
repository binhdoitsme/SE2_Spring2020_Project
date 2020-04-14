package com.hanu.util.server;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.List;

public class TomcatBuilderImpl implements TomcatBuilder {
    // constants
    private static final int DEFAULT_PORT = 9808;
    private static final String DEFAULT_HOSTNAME = "localhost";
    private static final String DEFAULT_APP_BASE = ".";
    private static final String TMP_DIR = System.getProperty("java.io.tmpdir");
    private static final String CONTEXT_PATH = "";
    private static final Logger logger = LoggerFactory.getLogger(TomcatBuilderImpl.class);

    // fields
    private Tomcat instance;
    private Context context;

    public TomcatBuilderImpl() {
        instance = new Tomcat();
    }

    @Override
    public TomcatBuilder defaultConfigure() {
        instance.setHostname(DEFAULT_HOSTNAME);
        instance.setPort(DEFAULT_PORT);
        instance.getConnector();
        instance.getHost().setAppBase(DEFAULT_APP_BASE);
        context = instance.addContext(CONTEXT_PATH, TMP_DIR);
        return this;
    }

    @Override
    public TomcatBuilder registerServlets(List<Class<HttpServlet>> servlets) {
        try {
            for (Class<HttpServlet> servlet : servlets) {
                WebServlet servletAnnotation = servlet.getAnnotation(WebServlet.class);
                String servletName = servletAnnotation.name();
                HttpServlet servletInstance = servlet.newInstance();
                String[] urlPatterns = servletAnnotation.urlPatterns();
                instance.addServlet(CONTEXT_PATH, servletName, servletInstance);
                for (String urlPattern : urlPatterns) {
                    context.addServletMappingDecoded(urlPattern, servletName);
                }
            }
        } catch (Exception e) {
            logger.error(e.getClass().getName() + ": " + e.getMessage());
        }

        return this;
    }

    @Override
    public void startServer() {
        try {
            instance.start();
            instance.getServer().await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
