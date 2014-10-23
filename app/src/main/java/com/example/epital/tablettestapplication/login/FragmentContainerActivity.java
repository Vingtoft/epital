package com.example.epital.tablettestapplication.login;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.epital.tablettestapplication.R;


public class FragmentContainerActivity extends Activity implements FragmentCommunication {

    LoginFragment loginFragment;
    LoadingFragment loadingFragment;
    ChangeUserFragment changeUserFragment;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fragment_container);

        loginFragment = new LoginFragment();
        loadingFragment = new LoadingFragment();
        changeUserFragment = new ChangeUserFragment();

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, loginFragment, "loginFragment");
        fragmentTransaction.commit();

        /*
        fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_in_left, R.anim.fragment_slide_out_right);
        fragmentTransaction.replace(R.id.login_fragment_container, loadingFragment, "loadingFragment");
        fragmentTransaction.commit();
        */

    }

    @Override
    public void moveToLoadFragment() {
        System.out.println("slide");
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_in_left,R.anim.fragment_slide_out_right);
        fragmentTransaction.replace(R.id.fragment_container, loadingFragment, "loadingFragment");
        fragmentTransaction.commit();
    }
}












