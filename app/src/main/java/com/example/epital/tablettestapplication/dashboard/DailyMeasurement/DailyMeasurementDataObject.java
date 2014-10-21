package com.example.epital.tablettestapplication.dashboard.DailyMeasurement;

/**
 * Created by oscarandersen on 13/10/14.
 */
public class DailyMeasurementDataObject {
    int pulse, oxygen;
    double temperature, fev1;
    boolean question1, question2, question3;

    //TODO: Get timestamp, weather, location, device_id and more.

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
