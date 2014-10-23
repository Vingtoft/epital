
package com.example.epital.tablettestapplication.dashboard.History;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.epital.tablettestapplication.R;
import com.example.epital.tablettestapplication.dashboard.DashboardNavigationFragmentCommunication;
import com.example.epital.tablettestapplication.database.DailyMeasurementDatabaseHandler;

/**
 * Created by oscarandersen on 15/10/14.
 */
public class CitizentHistoryFragment extends Fragment implements View.OnClickListener {

    DashboardNavigationFragmentCommunication comm;

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

    public void init(){
        getData();
    }

    private void getData(){
        DailyMeasurementDatabaseHandler databaseHandler = new DailyMeasurementDatabaseHandler(getActivity().getApplicationContext());
        databaseHandler.open();
        Cursor cursor = databaseHandler.returnData();

        int a = 0;
        while (cursor.moveToNext()){
            System.out.println("Række: " + a + " Oxygen: " + cursor.getInt(0) + " Pulse: " + cursor.getInt(1));
            a++;
        }
        databaseHandler.close();
    }

    @Override
    public void onClick(View view) {

    }
}
