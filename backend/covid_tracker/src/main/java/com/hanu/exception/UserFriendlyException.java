package com.hanu.exception;

/**
 * The error class that will be presented to api user
 */
public class UserFriendlyException {
    private String message;

    protected UserFriendlyException() { }

    public UserFriendlyException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "\"message\": \"$message\"".replace("$message", message);
    }
}