package com.example.epital.tablettestapplication.connection;

/**
 * Created by oscarandersen on 24/11/14.
 */
public class LoginDataObject {
    String username, password;

    public LoginDataObject(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
