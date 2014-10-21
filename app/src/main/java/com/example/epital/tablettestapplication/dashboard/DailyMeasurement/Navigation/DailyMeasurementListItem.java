package com.example.epital.tablettestapplication.dashboard.DailyMeasurement.Navigation;

/**
 * Created by oscarandersen on 04/10/14.
 */
public class DailyMeasurementListItem {
    private String title;
    private int icon;

    public DailyMeasurementListItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }


}
