package com.example.epital.tablettestapplication;

import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.DailyMeasurementDataObject;
import com.google.gson.*;

/**
 * Created by oscarandersen on 04/11/14.
 */
public class SaveDailyMeasurementToServer implements Runnable {

    DailyMeasurementDataObject dataObject;

    public SaveDailyMeasurementToServer(DailyMeasurementDataObject dataObject) {
        this.dataObject = dataObject;
    }

    @Override
    public void run() {
        sendData();
    }

    private void sendData() {
        Gson gson = new GsonBuilder().craete();
    }
}
