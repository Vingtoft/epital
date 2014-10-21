package com.example.epital.tablettestapplication.dashboard.DailyMeasurement.Temperature;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.epital.tablettestapplication.R;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.DailyMeasurementFragmentCommunication;


/**
 * Created by oscarandersen on 18/10/14.
 */
public class DailyMeasurementTemperatureFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    DailyMeasurementFragmentCommunication comm;
    SeekBar temperatureBar;
    TextView temperaturResultText;
    Button temperatureContinue;
    double temperature;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.daily_measurement_temperature, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceType) {
        super.onActivityCreated(savedInstanceType);
        //init communication interface to dashboard container activity
        comm = (DailyMeasurementFragmentCommunication) getActivity();

        temperatureBar = (SeekBar) getActivity().findViewById(R.id.temperatureSeekBar);
        temperatureBar.setOnSeekBarChangeListener(this);
        temperaturResultText = (TextView) getActivity().findViewById(R.id.temperaturResultText);
        temperatureContinue = (Button) getActivity().findViewById(R.id.temperatureContinue);
        temperatureContinue.setOnClickListener(this);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        System.out.println("Rørt: " + seekBar + " Og i: " + i + " og en lille boolean? " + b);
        // Temperaturen går fra 35 - 42. Temperaturen måles med en decimal.
        double w = i;
        temperature = 35 + (1 * (w/10));
        temperaturResultText.setText(Double.toString(temperature));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        System.out.println("onTOUUIUCH");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        System.out.println("OFF TOUUIUCH");
    }

    @Override
    public void onClick(View button) {
        if (button == temperatureContinue){
            comm.setTemperature(temperature);
            comm.changeDailyMeasurementContent(4);
        }
    }
}