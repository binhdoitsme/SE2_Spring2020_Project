package com.hanu.exception;

/**
 * User exception mapping to 400 request
 */
public class UnauthorizedException extends UserFriendlyException {
    public UnauthorizedException() {
        super("Unauthorized user!");
    }
}