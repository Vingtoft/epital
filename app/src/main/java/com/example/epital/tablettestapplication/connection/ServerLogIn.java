package com.example.epital.tablettestapplication.connection;

import android.os.Handler;

import com.example.epital.tablettestapplication.ApplicationObject;
import com.example.epital.tablettestapplication.dashboard.DashboardFragmentContainerActivity;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by oscarandersen on 24/11/14.
 */

public class ServerLogIn {

    String basic_url = "http://www.grower.dk/";

    Handler mhandler;

    Callback okHttpCallback;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public ServerLogIn(){

    }

    public ServerLogIn(Callback okHttpCallback) {
        this.okHttpCallback = okHttpCallback;
    }

    public void getSecurityToken(String username, String password) {
        Gson gson = new Gson();
        LoginDataObject loginDataObject = new LoginDataObject(username, password);
        String jsonObject = gson.toJson(loginDataObject);
        String url = basic_url + "api-token-auth/";
        try {
            post(url, jsonObject);
        } catch (IOException e) {
            System.out.println("Fejl under hentning af token: " + e);
        }
    }

    public void changeLoggedInStatus(String status, String token) {
        //status 1 : log in, 2: set not available, 3: log out
        String url = basic_url + "citizen/change_status/" + status+ "/";
        String json = "";
        try {
            postWithLocalCallBack(url, json, token);
        } catch (IOException e) {
            System.out.println("Fejl under ændring af citizen status");
        }
    }

    private void postWithLocalCallBack(String url, String json, String token) throws IOException{
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Token " + token)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("Fail!");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                System.out.println("Success on change status: " + response);
            }
        });
    }

    private void post(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(okHttpCallback);
    }
}