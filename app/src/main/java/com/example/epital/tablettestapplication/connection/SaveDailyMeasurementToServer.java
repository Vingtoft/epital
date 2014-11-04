package com.example.epital.tablettestapplication.connection;

import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.DailyMeasurementDataObject;
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

public class SaveDailyMeasurementToServer {

    String dataObjectJSON;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    public SaveDailyMeasurementToServer(DailyMeasurementDataObject dataObject) {
        Gson gson = new Gson();
        this.dataObjectJSON = gson.toJson(dataObject);
        System.out.println(this.dataObjectJSON);
    }

    String post(String url, String json) throws IOException{
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
