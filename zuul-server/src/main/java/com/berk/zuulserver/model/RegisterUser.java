package com.berk.zuulserver.model;

import javax.validation.constraints.Size;

public class RegisterUser {
    @Size(min = 6, max = 30, message = "Username must be between 6 and 30 characters")
    private String username;

    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    public RegisterUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
