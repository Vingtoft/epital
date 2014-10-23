package com.example.epital.tablettestapplication.login;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.epital.tablettestapplication.R;
import com.example.epital.tablettestapplication.dashboard.DashboardFragmentContainerActivity;


/**
 * Created by oscarandersen on 27/09/14.
 */
public class LoadingFragment extends Fragment {
    ImageView pulse;
    CountDownTimer timerCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loading_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceType) {
        super.onActivityCreated(savedInstanceType);
        init();
    }

    private void init() {

        pulse = (ImageView) getView().findViewById(R.id.pulse);
        pulse.setBackgroundResource(R.drawable.pulse);
        AnimationDrawable pulseAnimation = (AnimationDrawable) pulse.getBackground();
        pulseAnimation.start();
        countDown();
        timerCount.start();
    }

    private void countDown() {
        //counts down from 4 -> 0
        timerCount = new CountDownTimer(4 * 1000, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                moveToDashboard();
            }
        };
    }

    private void moveToDashboard() {
        Intent intent = new Intent(getActivity(), DashboardFragmentContainerActivity.class);
        startActivity(intent);

    }
}
