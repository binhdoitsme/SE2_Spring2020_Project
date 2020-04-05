package com.hanu;

import lombok.SneakyThrows;
import com.hanu.util.di.DependencyContainer;
import com.hanu.util.di.DependencyLoader;

public class Startup {

    @SuppressWarnings("unused")
    private DependencyContainer container;

    @SneakyThrows
    public void configureDependencies() {
        // load dependency injection instances here
        container = DependencyContainer.getInstance();
        DependencyLoader.loadDependencies();
    }

}
