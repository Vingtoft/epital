package com.example.epital.tablettestapplication.login;


import android.app.Fragment;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.epital.tablettestapplication.R;

/**
 * Created by oscarandersen on 27/09/14.
 */
public class LoadingFragment extends Fragment {
    ImageView pulse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loading_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceType) {
        super.onActivityCreated(savedInstanceType);
        init();
    }

    private void init(){
        pulse = (ImageView) getView().findViewById(R.id.pulse);
        pulse.setBackgroundResource(R.drawable.pulse);
        AnimationDrawable pulseAnimation = (AnimationDrawable) pulse.getBackground();
        pulseAnimation.start();
    }
}
