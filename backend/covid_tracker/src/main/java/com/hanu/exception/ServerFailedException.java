package com.hanu.exception;

public class ServerFailedException extends UserFriendlyException {
    public ServerFailedException() {
        super("A server error occured, please check the server log for details.");
    }
}