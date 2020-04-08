package com.hanu.util.authentication;

public class AuthToken {
    private String authToken;

    public AuthToken(String token) {
        this.authToken = token;
    }

    public String getAuthToken() {
        return authToken;
    }
}