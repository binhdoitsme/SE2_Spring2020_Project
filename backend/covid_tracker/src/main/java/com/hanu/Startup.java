package com.hanu;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;

import com.hanu.util.configuration.Configuration;
import com.hanu.util.di.DependencyContainer;
import com.hanu.util.di.DependencyLoader;
import com.hanu.util.server.ServletLoader;
import com.hanu.util.server.TomcatBuilder;
import com.hanu.util.server.TomcatBuilderImpl;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javassist.CannotCompileException;
import javassist.NotFoundException;

public class Startup {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private static final String SERVLET_PACKAGE_NAME = "com.hanu.servlet";

    @SuppressWarnings("unused")
    private DependencyContainer container;

    public Startup configureDependencies()
            throws ClassNotFoundException, IOException, CannotCompileException, NotFoundException {
        // load dependency injection instances here
        container = DependencyContainer.getInstance();
        DependencyLoader.loadDependencies();
        return this;
    }

    public void configureServer() throws ClassNotFoundException {
        List<Class<HttpServlet>> servlets = ServletLoader.getServletClasses(SERVLET_PACKAGE_NAME);
        TomcatBuilder appBuilder = new TomcatBuilderImpl();
        logger.info("database.connectionstring=" + Configuration.getInstance().get("database.connectionstring"));
        appBuilder.defaultConfigure().registerServlets(servlets).startServer();
    }

    public Startup getConfigurations() throws ConfigurationException {
        Configuration.getInstance().loadConfigurations();
        return this;
    }
}
