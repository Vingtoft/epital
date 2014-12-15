package com.example.epital.tablettestapplication.database;

import android.widget.Button;

import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.DailyMeasurementDataObject;
import com.example.epital.tablettestapplication.dashboard.DashboardFragmentContainerActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by oscarandersen on 14/10/14.
 */

public class DailyMeasurementDatabaseHandler {
    String database_name = "dailymeasurements13.realm";
    DashboardFragmentContainerActivity activity;
    Realm realm;

    public DailyMeasurementDatabaseHandler(DashboardFragmentContainerActivity activity) {
        this.activity = activity;
        this.realm = Realm.getInstance(activity, database_name);
    }

    public int generateID() {
        RealmQuery<RealmDailyMeasurementDataObject> query = realm.where(RealmDailyMeasurementDataObject.class);
        return (int) query.maximumInt("client_id") + 1;
    }

    public void updateDailyMeasurement(int client_id, int server_id, String server_timestamp) {
        RealmQuery<RealmDailyMeasurementDataObject> query = realm.where(RealmDailyMeasurementDataObject.class);
        query.equalTo("client_id", client_id);
        RealmResults<RealmDailyMeasurementDataObject> result = query.findAll();
        realm.beginTransaction();
        result.get(0).setServer_id(server_id);
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(server_timestamp);
            result.get(0).setDate_synced(date);
        } catch (Exception e) {
            System.out.println("Kan ikke formarere dato i updateDailyMeasurement " + e);
        }
        realm.commitTransaction();
    }

    public int saveDailyMeasurement(DailyMeasurementDataObject dailyMeasurementDataObject) {
        int id = generateID();
        realm.beginTransaction();
        RealmDailyMeasurementDataObject realmObject = realm.createObject(RealmDailyMeasurementDataObject.class);
        realmObject.setPulse(dailyMeasurementDataObject.getPulse());
        realmObject.setOxygen(dailyMeasurementDataObject.getOxygen());
        realmObject.setFev1(dailyMeasurementDataObject.getFev1());
        realmObject.setTemperature(dailyMeasurementDataObject.getTemperature());
        realmObject.setQuestion1(dailyMeasurementDataObject.getQuestion1());
        realmObject.setQuestion2(dailyMeasurementDataObject.getQuestion2());
        realmObject.setQuestion3(dailyMeasurementDataObject.getQuestion3());
        realmObject.setDate_created(dailyMeasurementDataObject.getTimestamp());
        realmObject.setClient_id(id);
        realm.commitTransaction();
        return id;
    }

    public RealmResults<RealmDailyMeasurementDataObject> getAllMeasurements() {
        System.out.println("getAllMeasurements");
        return realm.where(RealmDailyMeasurementDataObject.class).findAll();
    }

    public RealmQuery<RealmDailyMeasurementDataObject> getRealmQuery() {
        System.out.println("getRealmQuery");
        return realm.where(RealmDailyMeasurementDataObject.class);
    }

    public int getTotalMeasurementsFromDates(Date date1, Date date2) {
        System.out.println("getTotalMeasurementsFromDates");
        RealmQuery<RealmDailyMeasurementDataObject> query = realm.where(RealmDailyMeasurementDataObject.class);
        query.between("time_stamp", date1, date2);
        RealmResults<RealmDailyMeasurementDataObject> result = query.findAll();
        return result.size();
    }

    public ArrayList<Double> getTemperatureFromDates(Date date1, Date date2) {
        System.out.println("getTemperatureFromDates");
        RealmQuery<RealmDailyMeasurementDataObject> query = realm.where(RealmDailyMeasurementDataObject.class);
        query.between("time_stamp", date1, date2);
        RealmResults<RealmDailyMeasurementDataObject> results = query.findAll();
        ArrayList<Double> resultList = new ArrayList<Double>();
        for (RealmDailyMeasurementDataObject result : results) {
            resultList.add(result.getTemperature());
        }
        return resultList;
    }

    public void generateTestData() {
        /**
         * Generates xxx test measurements. The data must make sense
         * */
        int iterations = 100;
        for (int a = 0; a < iterations; a++) {
            //Generate date (one measurement for the last 1000 days!)
            Calendar cal = Calendar.getInstance();
            cal.setTime(cal.getTime()); //set reference time to TODAY
            cal.add(Calendar.DATE, -(iterations - a));
            Date date = cal.getTime();
            //generate a random pulse between 60 and 100
            int pulse = (int) (Math.random() * 40 + 60);
            //generate a random oxygen level betweem 85 and 100
            int oxygen = (int) (Math.random() * 15 + 85);
            //generate a random FEV1 between 0.4 and 3.5 with one decimal
            double fev1 = (double) Math.round((Math.random() * 3.5 + 0.4) * 10) / 10;
            //generate a random temperature between 36 and 42 with one decimal
            double temperature = (double) Math.round((Math.random() * 6 + 36) * 10) / 10;
            //generate random answers to the three questions
            boolean question1 = Math.random() < 0.5;
            boolean question2 = Math.random() < 0.5;
            boolean question3 = Math.random() < 0.5;
            a = (Math.random() < 0.9) ? a : a - 1; //Simulate the chance og making two measurements on one day
            //start the actual transaction
            Realm realm = Realm.getInstance(activity, database_name);
            realm.beginTransaction();
            RealmDailyMeasurementDataObject realmObject = realm.createObject(RealmDailyMeasurementDataObject.class);
            realmObject.setDate_created(date);
            realmObject.setDate_synced(null);
            realmObject.setPulse(pulse);
            realmObject.setOxygen(oxygen);
            realmObject.setFev1(fev1);
            realmObject.setTemperature(temperature);
            realmObject.setQuestion1(question1);
            realmObject.setQuestion2(question2);
            realmObject.setQuestion3(question3);
            realm.commitTransaction();
        }
    }

    public void printAllMeasurements() {
        RealmQuery<RealmDailyMeasurementDataObject> query = realm.where(RealmDailyMeasurementDataObject.class);
        RealmResults<RealmDailyMeasurementDataObject> results = query.findAll();
        for (RealmDailyMeasurementDataObject result : results) {
            System.out.println("Result: " + result.toString());
        }
    }

    public RealmResults<RealmDailyMeasurementDataObject> getUnsyncedData() {
        RealmQuery<RealmDailyMeasurementDataObject> query = realm.where(RealmDailyMeasurementDataObject.class);
        query.equalTo("server_id", 0);
        return query.findAll();
    }

}
