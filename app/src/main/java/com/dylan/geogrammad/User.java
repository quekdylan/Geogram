package com.dylan.geogrammad;

import java.util.List;

public class User {
    public String username;
    public String password;
    public List<ImageLocation> imageLocation;
    public List<String> friends;

    public String getUsername() {
        return username;
    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
