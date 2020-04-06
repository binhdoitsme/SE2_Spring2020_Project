package com.hanu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class App implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    // startup instance
    private Startup startupInstance;

    private App() { }

    public void run() {
        try {    
            startupInstance.getConfigurations()
                            .configureDependencies()
                            .configureServer();
        } catch (Exception e) {
            logger.error(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private <T extends Startup> App useStartup(Class<T> startupClass)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        startupInstance = startupClass.newInstance();
        return this;
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        new App().useStartup(Startup.class).run();
    }
}
