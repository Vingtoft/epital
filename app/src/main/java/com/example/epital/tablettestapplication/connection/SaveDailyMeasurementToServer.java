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


    public static void save(DailyMeasurementDataObject dataObject){
        Gson gson = new Gson();
        String dataObjectJSON = gson.toJson(dataObject);
        String url = "http://93.167.89.66:8852/daily_measurements/";
        try{
            post(url, dataObjectJSON);
        } catch (IOException e){
            System.out.println("Error in SaveDailyMeasurements: " + e);
        }
    }

    private static String post(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("Failt! " + e);
            }
            @Override
            public void onResponse(Response response) throws IOException {
                System.out.println("Virker! Reponse: " + response);
            }
        });
        return "hejsa";
    }

}
