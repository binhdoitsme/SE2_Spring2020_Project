package com.hanu;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;

import com.hanu.db.PointOfInterestRepositoryImpl;
import com.hanu.db.RecordRepositoryImpl;
import com.hanu.db.UserRepositoryImpl;
import com.hanu.domain.repository.PointOfInterestRepository;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.domain.repository.UserRepository;
import com.hanu.util.configuration.Configuration;
import com.hanu.util.db.DbConnector;
import com.hanu.util.db.DbConnectorImpl;
import com.hanu.util.di.DependencyContainer;
import com.hanu.util.di.DependencyLoader;
import com.hanu.util.server.ServletLoader;
import com.hanu.util.server.TomcatBuilder;
import com.hanu.util.server.TomcatBuilderImpl;

import org.apache.commons.configuration2.ex.ConfigurationException;

import javassist.CannotCompileException;
import javassist.NotFoundException;

public class Startup {
    // private static final Logger logger = LoggerFactory.getLogger(App.class);

    private DependencyContainer container;

    public Startup configureDependencies()
            throws ClassNotFoundException, IOException, CannotCompileException, NotFoundException {
        // load dependency injection instances here
        container = DependencyContainer.getInstance();
        DependencyLoader.loadDependencies();

        // add dependencies here
        container.addDependency(DbConnector.class, new DbConnectorImpl());
        container.addDependency(UserRepository.class, new UserRepositoryImpl());
        container.addDependency(RecordRepository.class, new RecordRepositoryImpl());
        container.addDependency(PointOfInterestRepository.class, new PointOfInterestRepositoryImpl());
        return this;
    }

    public void configureServer() throws ClassNotFoundException, IOException {
        String servletPackageName = Configuration.get("servlet.package");
        List<Class<HttpServlet>> servlets = ServletLoader.getServletClasses(servletPackageName);
        TomcatBuilder appBuilder = new TomcatBuilderImpl();
        appBuilder.defaultConfigure().registerServlets(servlets).startServer();
    }

    public Startup getConfigurations() throws ConfigurationException {
        Configuration.getInstance().loadConfigurations();
        return this;
    }
}
