package com.example.epital.tablettestapplication.dashboard.History;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.epital.tablettestapplication.R;
import com.example.epital.tablettestapplication.dashboard.DashboardFragmentContainerActivity;
import com.example.epital.tablettestapplication.dashboard.DashboardNavigationFragmentCommunication;
import com.example.epital.tablettestapplication.database.DailyMeasurementDatabaseHandler;
import com.example.epital.tablettestapplication.database.RealmDailyMeasurementDataObject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.joda.time.Years;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmQuery;
import io.realm.RealmResults;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ColumnValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.Utils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by oscarandersen on 15/10/14.
 */

public class CitizenHistoryFragment extends Fragment implements View.OnClickListener {

    DashboardNavigationFragmentCommunication comm;
    //creating a two dimensional arraylist containing the interval (length of first date) and the dates
    List<List<Date>> listOfDates;

    //The Array containing the actual data
    private RealmResults<RealmDailyMeasurementDataObject> realmResults;
    private RealmQuery<RealmDailyMeasurementDataObject> realmQuery;
    //Meta data
    int dm_total, dm_days, dm_weeks, dm_months, dm_years;
    Date max_date, min_date;
    DailyMeasurementDatabaseHandler databaseHandler;
    private List<Line> lines = new ArrayList<Line>();
    private ColumnChartView chartBottom;
    private LineChartView chartTop;
    private LineChartData lineData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.history, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceType) {
        super.onActivityCreated(savedInstanceType);
        init();
        comm = (DashboardNavigationFragmentCommunication) getActivity();
    }

    private void init() {
        //init charts
        chartBottom = (ColumnChartView) getActivity().findViewById(R.id.history_columnChart);
        chartTop = (LineChartView) getActivity().findViewById(R.id.history_lineChart);
        //fetch the data
        databaseHandler = new DailyMeasurementDatabaseHandler((DashboardFragmentContainerActivity) getActivity());
        realmResults = databaseHandler.getAllMeasurements();
        realmQuery = databaseHandler.getRealmQuery();
        calculateDates();
        setColumnChart();
        initTopGraph();
    }

    private void setColumnChart() {
        ColumnChartData columnData;
        int numSubColumns = 1;
        //int numColumns = (dm_months > 6) ? dm_months : (dm_weeks > 5) ? dm_weeks : dm_days;
        int numColumns = listOfDates.size();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<ColumnValue> columnValues;
        for (int i = 0; i < numColumns; i++) {
            columnValues = new ArrayList<ColumnValue>();
            for (int j = 0; j < numSubColumns; j++) {
                columnValues.add(new ColumnValue(getTotalMeasurementsFromIndex(i), Utils.pickColor()));
                axisValues.add(new AxisValue(i, "Test".toCharArray()));
            }
            columns.add(new Column(columnValues).setHasLabelsOnlyForSelected(true));
        }
        columnData = new ColumnChartData(columns);
        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));
        chartBottom.setOnValueTouchListener(new ValueTouchListener());
        chartBottom.setColumnChartData(columnData);
        chartBottom.setValueTouchEnabled(true);
        chartBottom.setZoomEnabled(false);
    }

    private void initTopGraph() {
        List<PointValue> pointValues = new ArrayList<PointValue>();
        Line line = new Line(pointValues);
        List<Line> lines1 = new ArrayList<Line>();
        lines1.add(line);
        lineData = new LineChartData(lines1);
        Viewport viewport = new Viewport(0, 0, 0, 0);
        chartTop.setLineChartData(lineData);
        chartTop.setMaximumViewport(viewport);
        chartTop.setCurrentViewport(viewport, true);
    }

    private void changeViewport(int x_min, int y_max, int x_max, int y_min) {
        Viewport viewport = chartTop.getCurrentViewport();
        viewport.contains(x_min, y_max, x_max, y_min);
        //chartTop.setMaximumViewport(viewport);
        //chartTop.setCurrentViewport(viewport, true);
    }

    private void deleteLineData() {

    }

    private void editLineData(List<PointValue> pointValues, List<AxisValue> axisValues, int color) {
        chartTop.cancelDataAnimation();
        Line line = lineData.getLines().get(0);
        line.setColor(color);
        //line.setValues(pointValues);
        for (int i = 0; i < pointValues.size(); i++) {
            try{
                line.getValues().get(i).setTarget(i, pointValues.get(i).getY());
            } catch (IndexOutOfBoundsException e) {
                line.getValues().add(i, pointValues.get(i));
            }
        }
        //lines.add(line);
        changeViewport(0, 42, pointValues.size() - 1, 35);
        lineData.setAxisXBottom(new Axis(axisValues));
        lineData.setAxisYLeft(new Axis().setHasLines(false).setMaxLabelChars(3));
        chartTop.setLineChartData(lineData);
        chartTop.startDataAnimation(1500);
    }

    private List<AxisValue> getAxisValuesFromPointArray(List<PointValue> dataInput) {
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        for (int i = 0; i < dataInput.size(); i++) {
            axisValues.add(new AxisValue(i, "Dato".toCharArray()));
        }
        return axisValues;
    }

    private List<PointValue> getPointValuesFromDoubleArray(ArrayList<Double> dataInput) {
        List<PointValue> pointValues = new ArrayList<PointValue>();
        for (int i = 0; i < dataInput.size(); i++) {
            double a = dataInput.get(i);
            float b = (float) a;
            pointValues.add(new PointValue(i, b));
        }
        return pointValues;
    }

    private void getDataForInterval(int index) {
        //TODO: at the moment we show temperature. This will be corrected later.
        Date start_date = listOfDates.get(index).get(0);
        Date end_date = listOfDates.get(index).get(1);
        //get the data
        ArrayList<Double> resultList = databaseHandler.getTemperatureFromDates(start_date, end_date);
        List<PointValue> pointValues = getPointValuesFromDoubleArray(resultList);
        List<AxisValue> axisValues = getAxisValuesFromPointArray(pointValues);
        editLineData(pointValues, axisValues, Utils.COLOR_GREEN);
    }

    private int getTotalMeasurementsFromIndex(int index) {
        Date date1 = listOfDates.get(index).get(0);
        Date date2 = listOfDates.get(index).get(1);
        return databaseHandler.getTotalMeasurementsFromDates(date1, date2);
    }

    private void calculateDates() {
        //get amount of daily measurements
        dm_total = realmResults.size();
        //get first date
        min_date = realmResults.minDate("time_stamp");
        //get last date
        max_date = realmResults.maxDate("time_stamp");

        dm_days = getDeltaDaysFromDates(max_date, min_date);
        dm_weeks = getDeltaWeeksFromDates(max_date, min_date);
        dm_months = getDeltaMonthsFromDates(max_date, min_date);
        dm_years = getDeltaYearsFromDates(max_date, min_date);

        if (dm_years > 5) {
            fillArrayRelativeToWeeks();
        } else if (dm_months > 5) {
            fillArrayRelativeToWeeks();
        } else if (dm_weeks > 5) {
            fillArrayRelativeToWeeks();
        } else {
            fillArrayRelativeToDays();
        }

        System.out.println("Size::" + dm_total);
        System.out.println("latest date: " + max_date.toString());
        System.out.println("earlist date: " + min_date.toString());
        System.out.println("Total days: " + dm_days + " weeks: " + dm_weeks + " months: " + dm_months + " years: " + dm_years);
    }

    private void fillArrayRelativeToDays() {
        //start date
        listOfDates = new ArrayList<List<Date>>();
        DateTime start_date = new DateTime(min_date);
        for (int a = 0; a < dm_days; a++) {
            DateTime iterative_date = start_date.plusDays(a);
            List<Date> dates = new ArrayList<Date>();
            dates.add(iterative_date.toDate());
            dates.add(iterative_date.toDate()); //why add two times? Because I need a start and end date.
            listOfDates.add(a, dates);
        }
    }

    private void fillArrayRelativeToWeeks() {
        //start date
        listOfDates = new ArrayList<List<Date>>();
        DateTime reference_date = new DateTime(min_date);
        for (int a = 0; a < dm_weeks; a++) {
            //get start date of week
            DateTime week_start = reference_date.withDayOfWeek(DateTimeConstants.MONDAY).withTimeAtStartOfDay();
            //get end date of week
            DateTime week_end = reference_date.withDayOfWeek(DateTimeConstants.SUNDAY).withTimeAtStartOfDay();
            //save the DateTime objects in the array
            List<Date> dates = new ArrayList<Date>();
            dates.add(week_start.toDate());
            dates.add(week_end.toDate());
            listOfDates.add(a, dates);
            //add 7 days to reference_date
            reference_date = reference_date.plusDays(7);
            //printing for test
        }
    }

    private int getDeltaDaysFromDates(Date date1, Date date2) {
        DateTime dateTime1 = new DateTime(date1);
        DateTime dateTime2 = new DateTime(date2);
        return Math.abs(Days.daysBetween(dateTime1, dateTime2).getDays());
    }

    private int getDeltaWeeksFromDates(Date date1, Date date2) {
        DateTime dateTime1 = new DateTime(date1);
        DateTime dateTime2 = new DateTime(date2);
        return Math.abs(Weeks.weeksBetween(dateTime1, dateTime2).getWeeks());
    }

    private int getDeltaMonthsFromDates(Date date1, Date date2) {
        DateTime dateTime1 = new DateTime(date1);
        DateTime dateTime2 = new DateTime(date2);
        return Math.abs(Months.monthsBetween(dateTime1, dateTime2).getMonths());
    }

    private int getDeltaYearsFromDates(Date date1, Date date2) {
        DateTime dateTime1 = new DateTime(date1);
        DateTime dateTime2 = new DateTime(date2);
        return Math.abs(Years.yearsBetween(dateTime1, dateTime2).getYears());
    }

    @Override
    public void onClick(View view) {

    }

    private class ValueTouchListener implements ColumnChartView.ColumnChartOnValueTouchListener {

        @Override
        public void onValueTouched(int selectedLine, int selectedValue, ColumnValue value) {
            System.out.println("Selected line: " + selectedLine + " selected value: " + selectedValue + " calumn value: " + value);
            getDataForInterval(selectedLine);
        }

        @Override
        public void onNothingTouched() {

            //generateLineData(Utils.COLOR_GREEN, 0);

        }
    }

    private void printResults() {
        int a = 0;
        for (RealmDailyMeasurementDataObject result : realmResults) {
            System.out.println("Måling nummer: " + a);
            System.out.println("Pulse: " + result.getPulse());
            System.out.println("Oxygen: " + result.getOxygen());
            System.out.println("Fev1: " + result.getFev1());
            System.out.println("Temp: " + result.getTemperature());
            System.out.println("Q1: " + result.isQuestion1());
            System.out.println("Q2: " + result.isQuestion2());
            System.out.println("Q3: " + result.isQuestion3());
            System.out.println("Timestamp: " + result.getTime_stamp());
            a++;
        }
    }

}

