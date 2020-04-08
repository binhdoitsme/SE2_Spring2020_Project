package com.hanu.domain.model;

public class User {
    private String username;
    private String password; // password is encrypted
    private String salt;

    public User(String username, String password, String salt) {
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + ":(username=" + username
                + ", password=" + password
                + ", salt=" + salt + ")";
    }
}