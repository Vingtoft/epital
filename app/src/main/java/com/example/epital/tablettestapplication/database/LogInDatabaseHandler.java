package com.example.epital.tablettestapplication.database;

import android.app.Activity;
import android.util.Log;

import com.example.epital.tablettestapplication.login.LoginFragmentContainerActivity;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by oscarandersen on 24/11/14.
 */

public class LogInDatabaseHandler {

    String database_name = "logondatabase2.realm";
    Realm realm;
    LoginFragmentContainerActivity activity;

    public LogInDatabaseHandler(Activity activity) {
        this.activity = (LoginFragmentContainerActivity) activity;
        this.realm = Realm.getInstance(activity, database_name);
    }

    public void deletePasswordAndToken() {
        realm.beginTransaction();
        RealmQuery<RealmLogInDataObject> query = realm.where(RealmLogInDataObject.class);
        RealmResults<RealmLogInDataObject> results = query.findAll();
        results.removeAll(results);
        realm.commitTransaction();
    }

    /**
     * Checks if the user has been created (there must be a short-password and a security-token,
     * otherwise the user is considered to be invalid
     */
    public boolean proceedToCreateUser() {
        /*realm.beginTransaction();
        RealmLogInDataObject dataObject = realm.createObject(RealmLogInDataObject.class);
        dataObject.setShort_password("8520");
        dataObject.setToken("hejsa");
        realm.commitTransaction();*/

        //realm.beginTransaction();
        RealmQuery<RealmLogInDataObject> query = realm.where(RealmLogInDataObject.class);
        RealmResults<RealmLogInDataObject> results = query.findAll();
        //results.removeAll(results);
        //realm.commitTransaction();
        System.out.println("result size: " + results);

        if (results.size() > 0) {
            if (results.get(0).getShort_password() == null ||
                    results.get(0).getToken() == null) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public String getToken(){
        RealmQuery<RealmLogInDataObject> query = realm.where(RealmLogInDataObject.class);
        RealmResults<RealmLogInDataObject> results = query.findAll();
        return results.get(0).getToken();
    }

    public boolean matchPasswords(String password) {
        RealmQuery<RealmLogInDataObject> realmQuery = realm.where(RealmLogInDataObject.class);
        RealmResults<RealmLogInDataObject> results = realmQuery.findAll();
        return results.get(0).getShort_password().equals(password);
    }

    public void saveToken(String token) {
        realm.beginTransaction();
        RealmQuery<RealmLogInDataObject> realmQuery = realm.where(RealmLogInDataObject.class);
        RealmResults<RealmLogInDataObject> results = realmQuery.findAll();
        if (results.size() == 0) {
            RealmLogInDataObject logInDataObject = realm.createObject(RealmLogInDataObject.class);
            logInDataObject.setToken(token);
        } else {
            results.get(0).setToken(token);
        }
        realm.commitTransaction();
    }

    public void saveShortPassword(String shortPassword) {
        realm.beginTransaction();
        RealmQuery<RealmLogInDataObject> realmQuery = realm.where(RealmLogInDataObject.class);
        RealmResults<RealmLogInDataObject> results = realmQuery.findAll();
        if (results.size() == 0) {
            RealmLogInDataObject logInDataObject = realm.createObject(RealmLogInDataObject.class);
            logInDataObject.setShort_password(shortPassword);
        } else {
            results.get(0).setShort_password(shortPassword);
        }
        realm.commitTransaction();
    }
}
