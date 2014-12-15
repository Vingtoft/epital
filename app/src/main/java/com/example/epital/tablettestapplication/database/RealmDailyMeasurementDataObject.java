package com.example.epital.tablettestapplication.database;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by oscarandersen on 05/11/14.
 */
/*
* Database plan (små øvelsesopgaver) :
* Step 1: Gem data i den nye tabel
* Step 2: Hent alt data
* Step 3: Hent specifikt data
* Step 4: Slet specifikt data
* Step 5: Slet alt data
* */

@RealmClass
public class RealmDailyMeasurementDataObject extends RealmObject{
    private Date date_created, date_synced;
    private int pulse, oxygen, client_id, server_id;
    private double fev1, temperature;
    private boolean question1, question2, question3;

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Date getDate_synced() {
        return date_synced;
    }

    public void setDate_synced(Date date_synced) {
        this.date_synced = date_synced;
    }

    public int getPulse() {
        return pulse;
    }

    public int getOxygen() {
        return oxygen;
    }

    public double getFev1() {
        return fev1;
    }

    public double getTemperature() {
        return temperature;
    }

    public boolean isQuestion1() {
        return question1;
    }

    public boolean isQuestion2() {
        return question2;
    }

    public boolean isQuestion3() {
        return question3;
    }

    public void setQuestion1(boolean question1) {
        this.question1 = question1;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public void setOxygen(int oxygen) {
        this.oxygen = oxygen;
    }

    public void setFev1(double fev1) {
        this.fev1 = fev1;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setQuestion2(boolean question2) {
        this.question2 = question2;
    }

    public void setQuestion3(boolean question3) {
        this.question3 = question3;
    }

}
