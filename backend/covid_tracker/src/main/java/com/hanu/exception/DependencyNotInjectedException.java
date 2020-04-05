package com.hanu.exception;

public class DependencyNotInjectedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DependencyNotInjectedException() {
        super("Dependency is not injected!");
    }
}
