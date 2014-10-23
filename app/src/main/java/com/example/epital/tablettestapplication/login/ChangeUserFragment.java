package com.example.epital.tablettestapplication.login;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.epital.tablettestapplication.R;

/**
 * Created by oscarandersen on 30/09/14.
 */
public class ChangeUserFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_change_user_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceType) {
        super.onActivityCreated(savedInstanceType);
        init();
    }

    private void init() {
        System.out.println("Change User Fragment init");
    }
}
