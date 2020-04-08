package com.hanu.exception;

public class EmptyBodyException extends UserFriendlyException {
    public EmptyBodyException() {
        super("No message was detected!");
    }
}