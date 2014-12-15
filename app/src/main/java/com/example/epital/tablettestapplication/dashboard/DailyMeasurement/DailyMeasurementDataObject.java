package com.example.epital.tablettestapplication.dashboard.DailyMeasurement;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by oscarandersen on 13/10/14.
 */

public class DailyMeasurementDataObject {
    int pulse, oxygen, id, client_id;
    double temperature, fev1;
    boolean question1, question2, question3;
    String date_created_on_client, date_saved_on_server;

    //TODO: Get timestamp, weather, location, device_id and more.

    public String getDate_saved_on_server() {
        return date_saved_on_server;
    }

    public void setDate_saved_on_server(String date_saved_on_server) {
        this.date_saved_on_server = date_saved_on_server;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getDate_created_on_client() {
        return date_created_on_client;
    }

    public void setDate_created_on_client(String date_created_on_client) {
        this.date_created_on_client = date_created_on_client;
    }

    public Date getTimestamp() {
        return  Calendar.getInstance().getTime();
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public int getPulse() {
        return this.pulse;
    }

    public void setOxygen(int oxygen) {
        this.oxygen = oxygen;
    }

    public int getOxygen() {
        return this.oxygen;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTemperature() {
        return this.temperature;
    }

    public void setFev1(double fev1) {
        this.fev1 = fev1;
    }

    public double getFev1() {
        return this.fev1;
    }

    public void setQuestion1(boolean question1) {
        this.question1 = question1;
    }

    public boolean getQuestion1() {
        return this.question1;
    }

    public void setQuestion2(boolean question2) {
        this.question2 = question2;
    }

    public boolean getQuestion2() {
        return this.question2;
    }

    public void setQuestion3(boolean question3) {
        this.question3 = question3;
    }

    public boolean getQuestion3() {
        return this.question3;
    }

}
