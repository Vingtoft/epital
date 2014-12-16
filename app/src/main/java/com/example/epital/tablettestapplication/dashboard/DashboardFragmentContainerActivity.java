package com.example.epital.tablettestapplication.dashboard;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.WindowManager;

import com.example.epital.tablettestapplication.ApplicationObject;
import com.example.epital.tablettestapplication.R;
import com.example.epital.tablettestapplication.connection.SaveDailyMeasurementToServer;
import com.example.epital.tablettestapplication.connection.ServerLogIn;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.BeforeYouStart.DailyMeasurementBeforeYouStartFragment;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.Complete.DailyMeasurementCompleteFragment;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.LungFunction.DailyMeasurementLungFunctionFragmenCOPD6_bak;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.LungFunction.DailyMeasurementLungFunctionFragmentSPIROMAGIC;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.PulseAndOxygen.DailyMeasurementPulseFragment;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.DailyMeasurementDataObject;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.DailyMeasurementFragmentCommunication;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.Navigation.DailyMeasurementListFragment;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.Questions.DailyMeasurementQuestionFragment;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.Temperature.DailyMeasurementTemperatureFragment;
import com.example.epital.tablettestapplication.dashboard.History.CitizenHistoryFragment;
import com.example.epital.tablettestapplication.dashboard.Settings.SettingsFragment;
import com.example.epital.tablettestapplication.database.DailyMeasurementDatabaseHandler;
import com.example.epital.tablettestapplication.database.RealmDailyMeasurementDataObject;
import com.example.epital.tablettestapplication.login.LoginFragmentContainerActivity;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by oscarandersen on 03/10/14.
 */

public class DashboardFragmentContainerActivity extends Activity implements DashboardNavigationFragmentCommunication, DailyMeasurementFragmentCommunication {
    /*Initialize the fragments*/
    //dashboard
    DashboardNavigationFragment navigationFragment;
    //daily measurements
    DailyMeasurementListFragment dailyMeasurementListFragment;
    DailyMeasurementPulseFragment dailyMeasurementPulseFragment;
    DailyMeasurementBeforeYouStartFragment dailyMeasurementBeforeYouStartFragment;
    DailyMeasurementLungFunctionFragmenCOPD6_bak dailyMeasurementLungFunctionFragmenCOPD6;
    DailyMeasurementTemperatureFragment dailyMeasurementTemperatureFragment;
    DailyMeasurementQuestionFragment dailyMeasurementQuestionFragment;
    DailyMeasurementLungFunctionFragmentSPIROMAGIC dailyMeasurementLungFunctionFragmentSPIROMAGIC;
    DailyMeasurementCompleteFragment dailyMeasurementCompleteFragment;
    DailyMeasurementDatabaseHandler dailyMeasurementDatabaseHandler;
    SettingsFragment settingsFragment;
    ServerLogIn serverLogIn;

    //daily measurement list
    //history
    CitizenHistoryFragment citizentHistoryFragment;

    /*Arraylist containing all active fragments*/
    List<Fragment> activeFragments = new ArrayList<Fragment>();
    List<Fragment> activeDailyMeasurementFragments = new ArrayList<Fragment>();

    /* Data object containing data from Daily Measurements */
    DailyMeasurementDataObject dailyMeasurementDataObject;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ApplicationObject applicationObject;
    String auth_token;

    int menu_selected;


    @Override
    protected void onResume() {
        super.onResume();
        isLoggedIn();
        serverLogIn.changeLoggedInStatus("1", auth_token);

    }

    @Override
    protected void onPause() {
        super.onPause();
        applicationObject.setLogged_in(false);
        serverLogIn.changeLoggedInStatus("0", auth_token);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationObject = (ApplicationObject) getApplication();
        // Find out if the user is logged in
        isLoggedIn();

        auth_token = applicationObject.getAuth_token();
        //Keep the screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //Set activity to full screen
        setContentView(R.layout.dashboard_container);
        //Initiate the fragments
        navigationFragment = new DashboardNavigationFragment();
        dailyMeasurementListFragment = new DailyMeasurementListFragment();
        dailyMeasurementPulseFragment = new DailyMeasurementPulseFragment();
        dailyMeasurementBeforeYouStartFragment = new DailyMeasurementBeforeYouStartFragment();
        dailyMeasurementLungFunctionFragmenCOPD6 = new DailyMeasurementLungFunctionFragmenCOPD6_bak();
        dailyMeasurementTemperatureFragment = new DailyMeasurementTemperatureFragment();
        dailyMeasurementLungFunctionFragmentSPIROMAGIC = new DailyMeasurementLungFunctionFragmentSPIROMAGIC();
        settingsFragment = new SettingsFragment();
        serverLogIn = new ServerLogIn();
        citizentHistoryFragment = new CitizenHistoryFragment();
        //init daily measurement data object
        dailyMeasurementDataObject = new DailyMeasurementDataObject();

        dailyMeasurementDatabaseHandler = new DailyMeasurementDatabaseHandler(this);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.dashboard_container, navigationFragment, "navigationFragment");
        fragmentTransaction.commit();

        menuSelected(2);


    }

    private void isLoggedIn() {
        if (!applicationObject.isLoggedIn()) {
            //user is not logged in. Move to log-in screen.
            Intent intent = new Intent(this, LoginFragmentContainerActivity.class);
            startActivity(intent);
        }
    }

    private void removeActiveFragments() {
        if (activeFragments.size() > 0) {
            fragmentTransaction = fragmentManager.beginTransaction();
            for (Fragment activeFragment : activeFragments) {
                fragmentTransaction.remove(activeFragment);
            }
            activeFragments.clear();
            fragmentTransaction.commit();
        }
    }

    private void removeDailyMeasurementActiveFragments() {
        if (activeDailyMeasurementFragments.size() > 0) {
            fragmentTransaction = fragmentManager.beginTransaction();
            for (Fragment activeFragment : activeDailyMeasurementFragments) {
                fragmentTransaction.remove(activeFragment);
            }
            activeDailyMeasurementFragments.clear();
            fragmentTransaction.commit();
        }
    }

    @Override
    public void menuSelected(int selection) {
        switch (selection) {
            case 1:
                System.out.println("Ring til Call Center");
                removeActiveFragments();
                removeDailyMeasurementActiveFragments();
                break;
            case 2:
                if (menu_selected != 2) {
                    menu_selected = 2;
                    System.out.println("Daglig måling");
                    removeActiveFragments();
                    removeDailyMeasurementActiveFragments();
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    //add the fragment objects to the activeFragments arraylist (important this is part of the transaction)
                    activeFragments.add(dailyMeasurementListFragment);
                    fragmentTransaction.add(R.id.dashboard_container, dailyMeasurementListFragment, "dailyMeasurementListFragment");
                    fragmentTransaction.commit();
                    changeDailyMeasurementContent(0);
                }
                break;
            case 3:
                System.out.println("Min medicin");
                //move to new fragment
                removeActiveFragments();
                removeDailyMeasurementActiveFragments();
                break;
            case 4:
                System.out.println("Mine tilbud");
                removeActiveFragments();
                removeDailyMeasurementActiveFragments();
                break;
            case 5:
                if (menu_selected != 5) {
                    menu_selected = 5;
                    System.out.println("Min Historik");
                    removeActiveFragments();
                    removeDailyMeasurementActiveFragments();
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    activeFragments.add(citizentHistoryFragment);
                    fragmentTransaction.add(R.id.dashboard_container, citizentHistoryFragment, "citizentHistoryFragment");
                    fragmentTransaction.commit();
                }
                break;
            case 6:
                if (menu_selected != 6) {
                    menu_selected = 6;
                    System.out.println("Indstillinger");
                    removeActiveFragments();
                    removeDailyMeasurementActiveFragments();
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    activeFragments.add(settingsFragment);
                    fragmentTransaction.add(R.id.dashboard_container, settingsFragment, "settingsContainer");
                    fragmentTransaction.commit();
                }
                break;
            case 7:
                System.out.println("Afslut program");
                applicationObject.setLogged_in(false);
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            default:
                break;
        }
    }

    @Override
    public void printAuthToken() {
        System.out.println("Auth token: " + auth_token);
    }

    @Override
    public void generateTestData() {
        dailyMeasurementDatabaseHandler.generateTestData();
        System.out.println("Test data has been generated");
    }

    @Override
    public void printDailyMeasurements() {
        dailyMeasurementDatabaseHandler.printAllMeasurements();
    }

    @Override
    public void syncWithServer() {
        RealmResults<RealmDailyMeasurementDataObject> unsyncedData = dailyMeasurementDatabaseHandler.getUnsyncedData();
        for (RealmDailyMeasurementDataObject data : unsyncedData) {
            //convert the Realm object into the normal object (used for serialization)
            DailyMeasurementDataObject dailyMeasurementDataObject1 = new DailyMeasurementDataObject();
            dailyMeasurementDataObject1.setPulse(data.getPulse());
            dailyMeasurementDataObject1.setOxygen(data.getOxygen());
            dailyMeasurementDataObject1.setTemperature(data.getTemperature());
            dailyMeasurementDataObject1.setFev1(data.getFev1());
            dailyMeasurementDataObject1.setQuestion1(data.isQuestion1());
            dailyMeasurementDataObject1.setQuestion2(data.isQuestion2());
            dailyMeasurementDataObject1.setQuestion3(data.isQuestion3());
            //måske bøvl med dato format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(data.getDate_created());
            dailyMeasurementDataObject1.setDate_created_on_client(date);
            dailyMeasurementDataObject1.setClient_id(data.getClient_id());

            SaveDailyMeasurementToServer saveDailyMeasurementToServer = new SaveDailyMeasurementToServer(okHttpCallback);
            saveDailyMeasurementToServer.save(dailyMeasurementDataObject1, auth_token);


        }
    }

    @Override
    public void generateOneTestdata() {
        DailyMeasurementDatabaseHandler _databaseHandler = new DailyMeasurementDatabaseHandler(this);
        dailyMeasurementDataObject = new DailyMeasurementDataObject();
        dailyMeasurementDataObject.setQuestion1(false);
        dailyMeasurementDataObject.setQuestion2(false);
        dailyMeasurementDataObject.setQuestion3(false);
        dailyMeasurementDataObject.setFev1(4.4);
        dailyMeasurementDataObject.setOxygen(98);
        dailyMeasurementDataObject.setPulse(95);
        dailyMeasurementDataObject.setTemperature(37.4);
        //TODO: Gem HH:mm:ss
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        dailyMeasurementDataObject.setDate_created_on_client(date);
        dailyMeasurementDataObject.setClient_id(_databaseHandler.saveDailyMeasurement(dailyMeasurementDataObject));

        SaveDailyMeasurementToServer saveDailyMeasurementToServer = new SaveDailyMeasurementToServer(okHttpCallback);
        saveDailyMeasurementToServer.save(dailyMeasurementDataObject, auth_token);
    }

    @Override
    public void changeDailyMeasurementContent(int selectedItem) {
        switch (selectedItem) {
            case 0:
                //before you start fragment
                removeDailyMeasurementActiveFragments();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.dashboard_container, dailyMeasurementBeforeYouStartFragment, "dailyMeasurementBeforeYouStartFragment");
                activeDailyMeasurementFragments.add(dailyMeasurementBeforeYouStartFragment);
                fragmentTransaction.commit();
                break;
            case 1:
                //Pulse and Oxygen
                removeDailyMeasurementActiveFragments();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.dashboard_container, dailyMeasurementPulseFragment, "dailyMeasurementPulseFragment");
                activeDailyMeasurementFragments.add(dailyMeasurementPulseFragment);
                fragmentTransaction.commit();
                dailyMeasurementListFragment.focus(1);
                break;
            case 2:
                System.gc();
                //FEV1 (lung measurement)
                removeDailyMeasurementActiveFragments();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.dashboard_container, dailyMeasurementLungFunctionFragmenCOPD6, "dailyMeasurementLungFunctionFragment");
                activeDailyMeasurementFragments.add(dailyMeasurementLungFunctionFragmenCOPD6);
                /*
                fragmentTransaction.add(R.id.dashboard_container, dailyMeasurementLungFunctionFragmentSPIROMAGIC, "dailyMeasurementLungFunctionFragment");
                activeDailyMeasurementFragments.add(dailyMeasurementLungFunctionFragmentSPIROMAGIC);
                */
                fragmentTransaction.commit();
                dailyMeasurementListFragment.focus(2);
                break;
            case 3:
                //Measure Temperature
                removeDailyMeasurementActiveFragments();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.dashboard_container, dailyMeasurementTemperatureFragment, "dailyMeasurementTemperatureFragment");
                activeDailyMeasurementFragments.add(dailyMeasurementTemperatureFragment);
                fragmentTransaction.commit();
                dailyMeasurementListFragment.focus(3);
                break;
            case 4:
                dailyMeasurementQuestionFragment = new DailyMeasurementQuestionFragment();
                dailyMeasurementQuestionFragment.setQuestion(1, 4);
                removeDailyMeasurementActiveFragments();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.dashboard_container, dailyMeasurementQuestionFragment, "dailyMeasurementQuestionFragment");
                activeDailyMeasurementFragments.add(dailyMeasurementQuestionFragment);
                fragmentTransaction.commit();
                dailyMeasurementListFragment.focus(4);
                break;
            case 5:
                System.out.println("Kommer jeg nogensinde her over?");
                dailyMeasurementQuestionFragment = new DailyMeasurementQuestionFragment();
                dailyMeasurementQuestionFragment.setQuestion(2, 5);
                removeDailyMeasurementActiveFragments();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.dashboard_container, dailyMeasurementQuestionFragment, "dailyMeasurementQuestionFragment");
                activeDailyMeasurementFragments.add(dailyMeasurementQuestionFragment);
                fragmentTransaction.commit();
                dailyMeasurementListFragment.focus(5);
                break;
            case 6:
                dailyMeasurementQuestionFragment = new DailyMeasurementQuestionFragment();
                dailyMeasurementQuestionFragment.setQuestion(3, 6);
                removeDailyMeasurementActiveFragments();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.dashboard_container, dailyMeasurementQuestionFragment, "dailyMeasurementQuestionFragment");
                activeDailyMeasurementFragments.add(dailyMeasurementQuestionFragment);
                fragmentTransaction.commit();
                dailyMeasurementListFragment.focus(6);
                break;
            case 7:
                //save data to database
                DailyMeasurementDatabaseHandler databaseHandler = new DailyMeasurementDatabaseHandler(this);
                dailyMeasurementDataObject.setClient_id(databaseHandler.generateID());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = sdf.format(new Date());
                dailyMeasurementDataObject.setDate_created_on_client(date);
                date = sdf.format(new Date(0)); //epoch
                dailyMeasurementDataObject.setDate_saved_on_server(date);
                databaseHandler.saveDailyMeasurement(dailyMeasurementDataObject);
                // Upload daily measurement data to server
                SaveDailyMeasurementToServer saveDailyMeasurementToServer = new SaveDailyMeasurementToServer(okHttpCallback);
                saveDailyMeasurementToServer.save(dailyMeasurementDataObject, auth_token);

                dailyMeasurementCompleteFragment = new DailyMeasurementCompleteFragment();
                dailyMeasurementCompleteFragment.set_values(dailyMeasurementDataObject.getPulse(),
                        dailyMeasurementDataObject.getOxygen(),
                        dailyMeasurementDataObject.getFev1(),
                        dailyMeasurementDataObject.getTemperature(),
                        dailyMeasurementDataObject.getQuestion1(),
                        dailyMeasurementDataObject.getQuestion2(),
                        dailyMeasurementDataObject.getQuestion3());
                removeDailyMeasurementActiveFragments();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.dashboard_container, dailyMeasurementCompleteFragment, "dailyMeasurementCompleteFragment");
                activeDailyMeasurementFragments.add(dailyMeasurementCompleteFragment);
                fragmentTransaction.commit();
                break;
            case 8:
                //exit this sub state
                menuSelected(5);
                break;
            default:
                break;
        }
    }

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message inputMessage) {
            if (inputMessage.what == 1) {
                DailyMeasurementDataObject returnedDailyMeasurementDataObject = (DailyMeasurementDataObject) inputMessage.obj;
                System.out.println("Clinet ID " + returnedDailyMeasurementDataObject.getClient_id());
                System.out.println("Server ID " + returnedDailyMeasurementDataObject.getId());
                System.out.println("Date saved on server " + returnedDailyMeasurementDataObject.getDate_saved_on_server());
                dailyMeasurementDatabaseHandler.updateDailyMeasurement(returnedDailyMeasurementDataObject.getClient_id(), returnedDailyMeasurementDataObject.getId(), returnedDailyMeasurementDataObject.getDate_saved_on_server());
            } else if (inputMessage.what == 2) {

            }
        }
    };


    Callback okHttpCallback = new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {
            System.out.println("Failure in okHttpCallback (dashboard)");
            e.printStackTrace();
        }

        @Override
        public void onResponse(Response response) throws IOException {
            //check if respond status is OK (2xx)

            if (String.valueOf(response.code()).charAt(0) == '2') {
                Gson gson = new Gson();
                DailyMeasurementDataObject returnedData = gson.fromJson(response.body().string(), DailyMeasurementDataObject.class);
                Message message = mHandler.obtainMessage(1, returnedData);
                message.sendToTarget();

            } else {
                System.out.println("Svar fra server: " + response.body().string());
                //Send status indicator to main thread
                Message message = mHandler.obtainMessage(2);
                message.sendToTarget();
            }
        }
    };


    /* Setters for  data */
    @Override
    public void setPulse(int pulse) {
        dailyMeasurementDataObject.setPulse(pulse);
    }

    @Override
    public void setOxygen(int oxygen) {
        dailyMeasurementDataObject.setOxygen(oxygen);
    }

    @Override
    public void setFev1(double fev1) {
        dailyMeasurementDataObject.setFev1(fev1);
    }

    @Override
    public void setFev(double fev) {

    }

    @Override
    public void setQuestion1(boolean answer) {
        dailyMeasurementDataObject.setQuestion1(answer);
    }

    @Override
    public void setQuestion2(boolean answer) {
        dailyMeasurementDataObject.setQuestion2(answer);
    }

    @Override
    public void setQuestion3(boolean answer) {
        dailyMeasurementDataObject.setQuestion3(answer);
    }

    @Override
    public void setTemperature(double temperature) {
        dailyMeasurementDataObject.setTemperature(temperature);
    }

}
