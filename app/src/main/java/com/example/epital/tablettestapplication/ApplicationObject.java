package com.example.epital.tablettestapplication;

import android.app.Application;

/**
 * Created by oscarandersen on 05/11/14.
 */
public class ApplicationObject extends Application{
    String auth_token;
    boolean logged_in;
    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public boolean isLoggedIn() {
        return logged_in;
    }

    public void setLogged_in(boolean logged_in) {
        this.logged_in = logged_in;
    }

}
