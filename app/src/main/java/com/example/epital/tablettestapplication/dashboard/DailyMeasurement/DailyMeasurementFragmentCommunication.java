package com.example.epital.tablettestapplication.dashboard.DailyMeasurement;

/**
 * Created by oscarandersen on 05/10/14.
 */
public interface DailyMeasurementFragmentCommunication {

    public void changeDailyMeasurementContent(int selectedItem);

    public void setPulse(int pulse);

    public void setOxygen(int oxygen);

    public void setFev1(double fev1);

    public void setFev(double fev);

    public void setQuestion1(boolean answer);

    public void setQuestion2(boolean answer);

    public void setQuestion3(boolean answer);

    public void setTemperature(double temperature);

}
