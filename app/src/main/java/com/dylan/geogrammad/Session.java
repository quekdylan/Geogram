package com.dylan.geogrammad;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences prefs;

    public Session(Context context) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setuser(String username, String password) {
        prefs.edit().putString("username", username).apply();
        prefs.edit().putString("password", password).apply();
        prefs.edit().putBoolean("isLoggedIn", true).apply();
    }

    public String getusername() {
        return(prefs.getString("username",null));
    }

    public String getpassword() {
        return(prefs.getString("password",null));
    }

    public boolean checkLogin() {
        return (prefs.getBoolean("isLoggedIn", false));
    }

    public void logout (){
        prefs.edit().clear().commit();
    }
}
