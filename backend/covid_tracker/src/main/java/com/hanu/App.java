package com.hanu;

import java.util.List;

import javax.servlet.http.HttpServlet;

import com.hanu.util.server.ServletLoader;
import com.hanu.util.server.TomcatBuilder;
import com.hanu.util.server.TomcatBuilderImpl;

import lombok.SneakyThrows;

public final class App implements Runnable {

    private static final String SERVLET_PACKAGE_NAME = "com.hanu.servlet";

    private App() { }

    @SneakyThrows
    public void run() {
        List<Class<HttpServlet>> servlets = ServletLoader.getServletClasses(SERVLET_PACKAGE_NAME);
        TomcatBuilder appBuilder = new TomcatBuilderImpl();
        appBuilder.defaultConfigure()
                .registerServlets(servlets)
                .startServer();
    }

    private <T extends Startup> App useStartup(Class<T> startupClass)
            throws IllegalAccessException, InstantiationException {
        T startupInstance = startupClass.newInstance();
        startupInstance.configureDependencies();
        return this;
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        new App().useStartup(Startup.class).run();
    }
}
