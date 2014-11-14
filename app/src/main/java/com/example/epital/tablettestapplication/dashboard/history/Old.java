
package com.example.epital.tablettestapplication.dashboard.History;

import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.epital.tablettestapplication.R;
import com.example.epital.tablettestapplication.dashboard.DashboardNavigationFragmentCommunication;
import com.example.epital.tablettestapplication.database.DailyMeasurementDatabaseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
public class Old extends Fragment implements View.OnClickListener {

    DashboardNavigationFragmentCommunication comm;
    List<PointValue> values = new ArrayList<PointValue>();
    List<PointValue> values_two = new ArrayList<PointValue>();

    ColumnChartView chartBottom;
    private LineChartView chartTop;
    private LineChartData lineData;
    public final static String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
            "Sep", "Oct", "Nov", "Dec",};

    public final static String[] days = new String[]{"Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun",};

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

    public void init() {
        chartBottom = (ColumnChartView) getActivity().findViewById(R.id.history_columnChart);
        chartTop = (LineChartView) getActivity().findViewById(R.id.history_lineChart);
        generateInitialLineData();
        setColumnChart();
    }

    private void setColumnChart() {
        ColumnChartData columnData;

        int numSubcolumns = 1;
        int numColumns = months.length;
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<ColumnValue> values;

        for (int i = 0; i < numColumns; i++) {
            values = new ArrayList<ColumnValue>();
            for (int j = 0; j < numSubcolumns; j++) {
                values.add(new ColumnValue((float) Math.random() * 50f + 5, Utils.pickColor()));
                axisValues.add(new AxisValue(i, months[i].toCharArray()));
            }
            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }
        columnData = new ColumnChartData(columns);
        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));
        chartBottom.setOnValueTouchListener(new ValueTouchListener());
        chartBottom.setColumnChartData(columnData);
        chartBottom.setValueSelectionEnabled(true);
        chartBottom.setZoomEnabled(false);


    }

    private void generateInitialLineData() {
        int numValues = 7;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, 0));
            axisValues.add(new AxisValue(i, days[i].toCharArray()));
        }

        Line line = new Line(values);
        line.setColor(Utils.COLOR_GREEN).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        lineData = new LineChartData(lines);
        lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

        chartTop.setLineChartData(lineData);

        // For build-up animation you have to disable viewport recalculation.
        chartTop.setViewportCalculationEnabled(false);

        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, 110, 6, 0);
        chartTop.setMaximumViewport(v);
        chartTop.setCurrentViewport(v, false);

        chartTop.setZoomType(ZoomType.HORIZONTAL);
    }

    private void generateLineData(int color, float range) {
        // Cancel last animation if not finished.
        chartTop.cancelDataAnimation();

        // Modify data targets
        Line line = lineData.getLines().get(0);// For this example there is always only one line.
        line.setColor(color);
        for (PointValue value : line.getValues()) {
            // Change target only for Y value.
            value.setTarget(value.getX(), (float) Math.random() * range);
        }

        // Start new data animation with 300ms duration;
        chartTop.startDataAnimation(300);
    }


    @Override
    public void onClick(View view) {

    }

    private class ValueTouchListener implements ColumnChartView.ColumnChartOnValueTouchListener {

        @Override
        public void onValueTouched(int selectedLine, int selectedValue, ColumnValue value) {
            generateLineData(value.getColor(), 100);

        }

        @Override
        public void onNothingTouched() {

            generateLineData(Utils.COLOR_GREEN, 0);

        }
    }
}

