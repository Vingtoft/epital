package com.example.epital.tablettestapplication.dashboard;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.epital.tablettestapplication.R;

/**
 * Created by oscarandersen on 03/10/14.
 */
public class DashboardNavigationFragment extends Fragment implements View.OnClickListener {
    Button button1, button2, button3, button4, button5, button6;
    DashboardNavigationFragmentCommunication comm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dashboard_navigation, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceType) {
        super.onActivityCreated(savedInstanceType);
        init();
        comm = (DashboardNavigationFragmentCommunication) getActivity();
    }

    private void init() {
        button1 = (Button) getActivity().findViewById(R.id.dashboard_navigation_btn1);
        button1.setOnClickListener(this);
        button2 = (Button) getActivity().findViewById(R.id.dashboard_navigation_btn2);
        button2.setOnClickListener(this);
        button3 = (Button) getActivity().findViewById(R.id.dashboard_navigation_btn3);
        button3.setOnClickListener(this);
        button4 = (Button) getActivity().findViewById(R.id.dashboard_navigation_btn4);
        button4.setOnClickListener(this);
        button5 = (Button) getActivity().findViewById(R.id.dashboard_navigation_btn5);
        button5.setOnClickListener(this);
        button6 = (Button) getActivity().findViewById(R.id.dashboard_navigation_btn6);
        button6.setOnClickListener(this);
    }

    @Override
    public void onClick(View button) {
        if (button == button1) {
            comm.menuSelected(1);
        } else if (button == button2) {
            comm.menuSelected(2);
        } else if (button == button3) {
            comm.menuSelected(3);
        } else if (button == button4) {
            comm.menuSelected(4);
        } else if (button == button5) {
            comm.menuSelected(5);
        } else if (button == button6) {
            comm.menuSelected(6);
        }
    }
}
