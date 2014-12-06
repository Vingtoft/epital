package com.example.epital.tablettestapplication.login;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.epital.tablettestapplication.ApplicationObject;
import com.example.epital.tablettestapplication.R;
import com.example.epital.tablettestapplication.database.LogInDatabaseHandler;


public class LoginFragmentContainerActivity extends Activity implements LoginFragmentCommunication {

    LoginFragment loginFragment;
    LoadingFragment loadingFragment;
    ChangeUserFragment changeUserFragment;
    CreateShortPassword createShortPassword;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    LogInDatabaseHandler dbHandler;
    ApplicationObject applicationObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fragment_container);
        dbHandler = new LogInDatabaseHandler(this);
        loginFragment = new LoginFragment();
        loadingFragment = new LoadingFragment();
        changeUserFragment = new ChangeUserFragment();
        createShortPassword = new CreateShortPassword();

        //dbHandler.deletePasswordAndToken();

        applicationObject = (ApplicationObject) getApplication();

        stateMachine(1);

    }


    @Override
    public void stateMachine(int state) {
        switch (state) {
            case 1:
                // Findes der et short-password & en token i databasen?
                if (dbHandler.proceedToCreateUser()) {
                    moveToLoginFragment();
                } else {
                    moveToChangeUserFragment();
                }
                break;
            case 2:
                //Bruger er logget på.
                applicationObject.setLogged_in(true);
                // Fortsæt til Load
                moveToLoadFragment();
                break;
            case 3:
                // Fortsæt til Create Short Password
                moveToCreateShortPasswordFragment();
                break;
            case 4:
                //
                break;
            case 5:
                break;
            default:
                break;
        }
    }

    public void moveToCreateShortPasswordFragment() {
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, createShortPassword, "createShortPassword");
        fragmentTransaction.commit();
    }

    public void moveToLoadFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_in_left, R.anim.fragment_slide_out_right);
        fragmentTransaction.replace(R.id.fragment_container, loadingFragment, "loadingFragment");
        fragmentTransaction.commit();
    }

    public void moveToLoginFragment() {
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, loginFragment, "loginFragment");
        fragmentTransaction.commit();
    }

    public void moveToChangeUserFragment() {
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, changeUserFragment, "changeUserFragment");
        fragmentTransaction.commit();
    }


}












