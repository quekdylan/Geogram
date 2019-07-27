package com.dylan.geogrammad;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class User {
    public String username;
    public String password;
    public List<ImageLocation> imageLocation;
    public List<String> friends;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User(String username) {
        this.username = username;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
