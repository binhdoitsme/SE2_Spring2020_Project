package com.hanu.exception;

public class InvalidQueryTypeException extends Exception {

    private static final long serialVersionUID = -6668338263140448550L;
    
    public InvalidQueryTypeException() {
        super("The supplied query does not match the method query type!");
    }
}