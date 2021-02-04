package com.berk.zuulserver.model;

public class ReturnUserDetails {
    private int id;
    private String username;

    public ReturnUserDetails() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
