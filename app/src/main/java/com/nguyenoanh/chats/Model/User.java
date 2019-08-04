package com.nguyenoanh.chats.Model;

public class User {
    private String id;
    private String userName;
    private String inmageURL;

    public User(String id, String userName, String inmageURL) {
        this.id = id;
        this.userName = userName;
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
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInmageURL() {
        return inmageURL;
    }

    public void setInmageURL(String inmageURL) {
        this.inmageURL = inmageURL;
    }
}
