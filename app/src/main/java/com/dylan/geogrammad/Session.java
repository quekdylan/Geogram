package com.dylan.geogrammad;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class Session {
    private SharedPreferences prefs;

    Session(Context context) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    void setuser(String username, String password) {
        prefs.edit().putString("username", username).apply();
        prefs.edit().putString("password", password).apply();
        prefs.edit().putBoolean("isLoggedIn", true).apply();
    }

    String getusername() {
        return(prefs.getString("username",null));
    }

    String getpassword() {
        return(prefs.getString("password",null));
    }

    boolean checkLogin() {
        return (prefs.getBoolean("isLoggedIn", false));
    }

    void logout (){
        prefs.edit().clear().apply();
    }
}
