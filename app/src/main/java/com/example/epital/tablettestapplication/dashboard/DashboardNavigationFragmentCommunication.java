package com.example.epital.tablettestapplication.dashboard;

/**
 * Created by oscarandersen on 03/10/14.
 */

public interface DashboardNavigationFragmentCommunication {
    public void menuSelected(int selection);

    public void printAuthToken();

    public void generateTestData();

    public void printDailyMeasurements();

    public void syncWithServer();

    public void generateOneTestdata();

}
