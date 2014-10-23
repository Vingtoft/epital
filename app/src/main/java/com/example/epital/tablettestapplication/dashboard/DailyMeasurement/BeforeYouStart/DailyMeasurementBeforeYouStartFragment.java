package com.example.epital.tablettestapplication.dashboard.DailyMeasurement.BeforeYouStart;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.epital.tablettestapplication.R;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.DailyMeasurementFragmentCommunication;

/**
 * Created by oscarandersen on 16/10/14.
 */
public class DailyMeasurementBeforeYouStartFragment extends Fragment implements View.OnClickListener {

    DailyMeasurementFragmentCommunication comm;
    Button startButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.daily_measurement_before_you_start, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceType) {
        super.onActivityCreated(savedInstanceType);
        //init communication interface to dashboard container activity
        comm = (DailyMeasurementFragmentCommunication) getActivity();
        //init the start button
        startButton = (Button) getActivity().findViewById(R.id.before_you_start_button);
        startButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View button) {
        if (button == startButton) {
            comm.changeDailyMeasurementContent(1);
            //the user is ready to start the daily measurement. Proceed!

        }
    }
}
