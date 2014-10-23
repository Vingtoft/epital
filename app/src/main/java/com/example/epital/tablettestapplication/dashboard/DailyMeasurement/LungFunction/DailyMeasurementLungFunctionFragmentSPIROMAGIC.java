package com.example.epital.tablettestapplication.dashboard.DailyMeasurement.LungFunction;

import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.epital.tablettestapplication.R;
import com.example.epital.tablettestapplication.bluetooth.BluetoothConnectionAgent;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.DailyMeasurementFragmentCommunication;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by oscarandersen on 20/10/14.
 */
public class DailyMeasurementLungFunctionFragmentSPIROMAGIC extends Fragment {
    DailyMeasurementFragmentCommunication comm;
    BluetoothConnectionAgent bluetoothConnectionAgent;
    String device_name = "203";
    BluetoothDevice device;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.daily_measurement_content, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceType) {
        super.onActivityCreated(savedInstanceType);
        IntentFilter intentFilter;
        intentFilter = new IntentFilter();
        intentFilter.addAction("ACTION_FOUND");
        //init communication interface to dashboard container activity
        comm = (DailyMeasurementFragmentCommunication) getActivity();
        //init bluetooth connection agent
        bluetoothConnectionAgent = new BluetoothConnectionAgent(mHandler, device_name);
        dailyMeasurementStateMachine(1);
    }

    private void dailyMeasurementStateMachine(int state) {
        switch (state) {
            case 1:
                System.out.println("Er den parret?");
                device = bluetoothConnectionAgent.checkForPairedDevices(device_name);
                if (device == null){
                    System.out.println("De er desværre ikke parret");
                } else {
                    System.out.println("Jubii, de er parret!");
                    bluetoothConnectionAgent.connect(device);
                }
                break;
            default:
                break;
        }
    }

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message inputMessage) {
            byte[] buffer = (byte[]) inputMessage.obj;
            printByteArray(buffer);
        }
    };

    private void printByteArray(byte[] byteArray) {
        int e = 0;
        for (byte b : byteArray) {
            System.out.println("Nummer: " + e + " Value: " + new String(new byte[]{b}) + " Raw: " + b + " Hex: " + String.format("%02X ", b));
            e++;
        }
    }

}
