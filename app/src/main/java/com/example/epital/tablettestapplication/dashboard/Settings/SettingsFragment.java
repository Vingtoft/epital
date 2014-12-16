package com.example.epital.tablettestapplication.dashboard.Settings;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.epital.tablettestapplication.R;
import com.example.epital.tablettestapplication.dashboard.DashboardNavigationFragmentCommunication;

/**
 * Created by oscarandersen on 21/10/14.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    DashboardNavigationFragmentCommunication comm;
    Button printAuthToken, generateTestData, printDailyMeasurements, syncWithServer, generateSingleData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comm = (DashboardNavigationFragmentCommunication) getActivity();

        printAuthToken = (Button) getActivity().findViewById(R.id.settingsPrintAuthToken);
        printAuthToken.setOnClickListener(this);
        generateTestData = (Button) getActivity().findViewById(R.id.settingsGenerateTestData);
        generateTestData.setOnClickListener(this);
        printDailyMeasurements = (Button) getActivity().findViewById(R.id.settingsPrintDailyMeasurements);
        printDailyMeasurements.setOnClickListener(this);
        syncWithServer = (Button) getActivity().findViewById(R.id.settingsSyncWithServer);
        syncWithServer.setOnClickListener(this);
        generateSingleData = (Button) getActivity().findViewById(R.id.settingsGenerateOneTestData);
        generateSingleData.setOnClickListener(this);
    }

    @Override
    public void onClick(View button) {
        if (button == printAuthToken) {
            comm.printAuthToken();
        } else if (button == generateTestData) {
            comm.generateTestData();
        } else if (button == printDailyMeasurements) {
            comm.printDailyMeasurements();
        } else if (button == syncWithServer) {
            comm.syncWithServer();
        } else if (button == generateSingleData) {
            comm.generateOneTestdata();
        }
    }
}
