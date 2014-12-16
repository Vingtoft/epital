package com.example.epital.tablettestapplication.connection;

import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.DailyMeasurementDataObject;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;


import java.io.IOException;

import com.google.gson.Gson;

/**
 * Created by oscarandersen on 04/11/14.
 */

public class SaveDailyMeasurementToServer{

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    Callback okHttpCallback;

    public SaveDailyMeasurementToServer(){

    }

    public SaveDailyMeasurementToServer(Callback okHttpCallback){
        this.okHttpCallback = okHttpCallback;
    }



    public void save(DailyMeasurementDataObject dataObject, String token){
        Gson gson = new Gson();
        String dataObjectJSON = gson.toJson(dataObject);
        String url = "http://www.grower.dk/daily_measurements/";
        try{
            post(url, dataObjectJSON, token);
        } catch (IOException e){
            System.out.println("Error in SaveDailyMeasurements: " + e);
        }
    }

    private void post(String url, String json, String token) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Token " + token)
                .build();
        client.newCall(request).enqueue(okHttpCallback);
    }
}
