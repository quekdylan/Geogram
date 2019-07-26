package com.dylan.geogrammad;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class Session {
    private SharedPreferences prefs;

    Session(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    void setUser(String username, String password) {
        prefs.edit().putString("username", username).apply();
        prefs.edit().putString("password", password).apply();
        prefs.edit().putBoolean("isLoggedIn", true).apply();
    }

    String getUsername() {
        return(prefs.getString("username",null));
    }

    boolean checkLogin() {
        return (prefs.getBoolean("isLoggedIn", false));
    }

    void logout (){
        prefs.edit().clear().apply();
    }
}
