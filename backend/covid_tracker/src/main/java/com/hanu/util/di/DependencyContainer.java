package com.hanu.util.di;

import com.hanu.exception.DependencyNotInjectedException;

import java.util.HashMap;
import java.util.Map;

public class DependencyContainer {

    private static DependencyContainer instance;

    private static Map<Class<?>, Object> dependencyContainer = new HashMap<>();

    DependencyContainer() { }

    public DependencyContainer addDependency(Class<?> dependencyType, Object implementation) {
        dependencyContainer.put(dependencyType, implementation);
        return this;
    }

    public static DependencyContainer getInstance() {
        if (instance == null) {
            instance = new DependencyContainer();
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    private <T> T locateDependencyFor(Class<T> dependencyClass) {
        T dependencyResult = (T) dependencyContainer.get(dependencyClass);
        if (dependencyResult == null) {
            throw new DependencyNotInjectedException();
        }
        return dependencyResult;
    }

    public static <T> T locateDependency(Class<T> dependencyClass) {
        return instance.locateDependencyFor(dependencyClass);
    }
}
