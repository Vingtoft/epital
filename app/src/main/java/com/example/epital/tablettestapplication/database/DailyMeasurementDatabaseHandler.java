package com.example.epital.tablettestapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by oscarandersen on 14/10/14.
 */
public class DailyMeasurementDatabaseHandler {
    public static final String TIMESTAMP = "timestamp";
    public static final String LOCATION_LONGITUDE = "location_longitude";
    public static final String LOCATION_LATITUDE = "location_latitude";
    public static final String OXYGEN = "oxygen";
    public static final String PULSE = "pulse";
    public static final String FEV1 = "fev1";
    public static final String FEV = "fev";
    public static final String TEMPERATURE = "temperature";
    public static final String Q1 = "q1";
    public static final String Q2 = "q2";
    public static final String Q3 = "q3";
    public static final String TABLE_NAME = "daily_mesurements";
    public static final String DATABASENAME = "epital_health";
    public static final int DATABASEVERION = 3;

    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " ("+OXYGEN+" INT, "+PULSE+" INT, "+FEV1+" FLOAT, "+TEMPERATURE+" FLOAT, "+Q1+" BOOLEAN, "+Q2+" BOOLEAN, "+Q3+"BOOLEAN);";

    String[] selection = new String[]  {OXYGEN, PULSE, FEV1, Q1, Q2, Q3};

    DataBaseHelper dbHelper;
    Context context;
    SQLiteDatabase db;

    public DailyMeasurementDatabaseHandler(Context context) {
        this.context = context;
        //init the DataBaseHelper
        dbHelper = new DataBaseHelper(context);
    }


    private static class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context context) {
            super(context, DATABASENAME, null, DATABASEVERION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL(TABLE_CREATE);
            } catch (Exception e) {

            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXSIST " + TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }

    public DailyMeasurementDatabaseHandler open(){
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public long insertCompleteDailyMeasurement(int pulse, int oxygen, double fev1, double temp, boolean q1, boolean q2, boolean q3){
        ContentValues content = new ContentValues();
        content.put(OXYGEN, oxygen);
        content.put(PULSE, pulse);
        content.put(FEV1, fev1);
        content.put(TEMPERATURE, temp);
        content.put(Q1, q1);
        content.put(Q2, q2);
        content.put(Q3, q3);
        return db.insert(TABLE_NAME, null, content);
    }

    public Cursor returnData(){
        return db.query(TABLE_NAME,selection, null, null, null, null, null);
    }

}
