package com.example.epital.tablettestapplication;

import android.app.Application;

/**
 * Created by oscarandersen on 05/11/14.
 */
public class ApplicationObject extends Application{
    boolean logged_in = false;

    String auth_username, auth_password;


    public boolean isLogged_in() {
        return logged_in;
    }

    public String getAuth_username() {
        return auth_username;
    }

    public String getAuth_password() {
        return auth_password;
    }

    public void setLogged_in(boolean logged_in) {
        this.logged_in = logged_in;
    }

    public void setAuth_username(String auth_username) {
        this.auth_username = auth_username;
    }

    public void setAuth_password(String auth_password) {
        this.auth_password = auth_password;
    }
}
