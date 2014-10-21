package com.example.epital.tablettestapplication.dashboard.history;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.epital.tablettestapplication.R;
import com.example.epital.tablettestapplication.dashboard.DashboardNavigationFragmentCommunication;

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

    }

    @Override
    public void onClick(View view) {

    }
}
