package com.example.epital.tablettestapplication.database;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by oscarandersen on 24/11/14.
 */
@RealmClass
public class RealmLogInDataObject extends RealmObject {
    private String token, short_password;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getShort_password() {
        return short_password;
    }

    public void setShort_password(String short_password) {
        this.short_password = short_password;
    }
}
