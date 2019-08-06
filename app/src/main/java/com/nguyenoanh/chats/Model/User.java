package com.nguyenoanh.chats.Model;

public class User {
    private String id;
    private String username;
    private String inmageURL;

    public User(String id, String username, String inmageURL) {
        this.id = id;
        this.username = username;
        this.inmageURL = inmageURL;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getInmageURL() {
        return inmageURL;
    }

    public void setInmageURL(String inmageURL) {
        this.inmageURL = inmageURL;
    }
}
