package com.example.epital.tablettestapplication.dashboard.DailyMeasurement.Complete;

import android.app.Fragment;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.epital.tablettestapplication.R;
import com.example.epital.tablettestapplication.bluetooth.BluetoothConnectionAgent;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.DailyMeasurementFragmentCommunication;

/**
 * Created by oscarandersen on 20/10/14.
 */
public class DailyMeasurementCompleteFragment extends Fragment implements View.OnClickListener {
    int pulse, oxygen;
    double fev1, temp;
    boolean q1, q2, q3;
    DailyMeasurementFragmentCommunication comm;
    TextView text_pulse, text_oxygen, text_fev1, text_temp, text_q1, text_q2, text_q3;
    Button continue_button;

    public void set_values(int pulse, int oxygen, double fev1, double temp, boolean q1, boolean q2, boolean q3) {
        this.pulse = pulse;
        this.oxygen = oxygen;
        this.fev1 = fev1;
        this.temp = temp;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
    }

    public DailyMeasurementCompleteFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.daily_measurement_complete, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceType) {
        super.onActivityCreated(savedInstanceType);
        //init communication interface to dashboard container activity
        comm = (DailyMeasurementFragmentCommunication) getActivity();
        init();
    }

    private void init() {
        text_pulse = (TextView) getActivity().findViewById(R.id.table_pulse_value);
        text_pulse.setText(Integer.toString(pulse));
        text_oxygen = (TextView) getActivity().findViewById(R.id.table_oxygen_value);
        text_oxygen.setText(Integer.toString(oxygen));
        text_fev1 = (TextView) getActivity().findViewById(R.id.table_fev_value);
        text_fev1.setText(Double.toString(fev1));
        text_temp = (TextView) getActivity().findViewById(R.id.table_temp_value);
        text_temp.setText(Double.toString(temp));
        text_q1 = (TextView) getActivity().findViewById(R.id.table_q1_value);
        text_q1.setText(getBooleanAnswer(q1));
        text_q2 = (TextView) getActivity().findViewById(R.id.table_q2_value);
        text_q2.setText(getBooleanAnswer(q2));
        text_q2 = (TextView) getActivity().findViewById(R.id.table_q2_value);
        text_q2.setText(getBooleanAnswer(q2));

    }

    private String getBooleanAnswer(boolean answer) {
        return answer ? "Ja" : "Nej";
    }

    @Override
    public void onClick(View button) {

    }
}


















