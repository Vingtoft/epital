package com.example.epital.tablettestapplication.dashboard;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.epital.tablettestapplication.R;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.BeforeYouStart.DailyMeasurementBeforeYouStartFragment;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.Complete.DailyMeasurementCompleteFragment;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.LungFunction.DailyMeasurementLungFunctionFragmenCOPD6;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.LungFunction.DailyMeasurementLungFunctionFragmentSPIROMAGIC;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.PulseAndOxygen.DailyMeasurementPulseFragment;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.DailyMeasurementDataObject;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.DailyMeasurementFragmentCommunication;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.Navigation.DailyMeasurementListFragment;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.Questions.DailyMeasurementQuestionFragment;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.Temperature.DailyMeasurementTemperatureFragment;
import com.example.epital.tablettestapplication.dashboard.History.CitizentHistoryFragment;
import com.example.epital.tablettestapplication.database.DailyMeasurementDatabaseHandler;

import java.util.ArrayList;
import java.util.List;

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
    DailyMeasurementLungFunctionFragmenCOPD6 dailyMeasurementLungFunctionFragmenCOPD6;
    DailyMeasurementTemperatureFragment dailyMeasurementTemperatureFragment;
    DailyMeasurementQuestionFragment dailyMeasurementQuestionFragment;
    DailyMeasurementLungFunctionFragmentSPIROMAGIC dailyMeasurementLungFunctionFragmentSPIROMAGIC;
    DailyMeasurementCompleteFragment dailyMeasurementCompleteFragment;

    //daily measurement list
    //history
    CitizentHistoryFragment citizentHistoryFragment;

    /*Arraylist containing all active fragments*/
    List<Fragment> activeFragments = new ArrayList<Fragment>();
    List<Fragment> activeDailyMeasurementFragments = new ArrayList<Fragment>();

    /* Data object containing data from Daily Measurements */
    DailyMeasurementDataObject dailyMeasurementDataObject;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_container);

        //Keep the screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Initiate the fragments
        //TODO: Spørgsmål til Jacob: Er det good practice at have alle referencer til objekter her?
        navigationFragment = new DashboardNavigationFragment();

        dailyMeasurementListFragment = new DailyMeasurementListFragment();
        dailyMeasurementPulseFragment = new DailyMeasurementPulseFragment();
        dailyMeasurementBeforeYouStartFragment = new DailyMeasurementBeforeYouStartFragment();
        dailyMeasurementLungFunctionFragmenCOPD6 = new DailyMeasurementLungFunctionFragmenCOPD6();
        dailyMeasurementTemperatureFragment = new DailyMeasurementTemperatureFragment();
        dailyMeasurementLungFunctionFragmentSPIROMAGIC = new DailyMeasurementLungFunctionFragmentSPIROMAGIC();

        citizentHistoryFragment = new CitizentHistoryFragment();

        //init daily measurement data object
        dailyMeasurementDataObject = new DailyMeasurementDataObject();

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.dashboard_container, navigationFragment, "navigationFragment");
        fragmentTransaction.commit();

        //databaseTest();
    }

    private void databaseTest() {
        System.out.println("DatabaseTest START");
        DailyMeasurementDatabaseHandler dataHandler = new DailyMeasurementDatabaseHandler(getApplicationContext());
        dataHandler.open();
        //dataHandler.insertData("Oscar", "VingtoftOscar@Gmail.com");
        Cursor test = dataHandler.returnData();
        System.out.println("Det her det sner: " + test.move(5));
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
                break;
            case 3:
                System.out.println("Min medicin");
                removeActiveFragments();
                removeDailyMeasurementActiveFragments();
                break;
            case 4:
                System.out.println("Mine tilbud");
                removeActiveFragments();
                removeDailyMeasurementActiveFragments();
                break;
            case 5:
                System.out.println("Min Historik");
                removeActiveFragments();
                removeDailyMeasurementActiveFragments();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                activeFragments.add(citizentHistoryFragment);
                fragmentTransaction.add(R.id.dashboard_container, citizentHistoryFragment, "citizentHistoryFragment");
                fragmentTransaction.commit();
                break;
            case 6:
                System.out.println("Indstillinger");
                removeActiveFragments();
                removeDailyMeasurementActiveFragments();
                break;
            default:
                break;
        }
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
                dailyMeasurementQuestionFragment = new DailyMeasurementQuestionFragment(1, 4);
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
                dailyMeasurementQuestionFragment = new DailyMeasurementQuestionFragment(2, 5);
                removeDailyMeasurementActiveFragments();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.dashboard_container, dailyMeasurementQuestionFragment, "dailyMeasurementQuestionFragment");
                activeDailyMeasurementFragments.add(dailyMeasurementQuestionFragment);
                fragmentTransaction.commit();
                dailyMeasurementListFragment.focus(5);
                break;
            case 6:
                dailyMeasurementQuestionFragment = new DailyMeasurementQuestionFragment(3, 6);
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
                saveCompleteDailyMeasurementToDatabase();
                dailyMeasurementCompleteFragment = new DailyMeasurementCompleteFragment(dailyMeasurementDataObject.getPulse(),
                        dailyMeasurementDataObject.getOxygen(),
                        dailyMeasurementDataObject.getFev1(),
                        dailyMeasurementDataObject.getTemperature(),
                        dailyMeasurementDataObject.getQuestion1(),
                        dailyMeasurementDataObject.getQuestion2(),
                        dailyMeasurementDataObject.getQuestion3());
                //move to new fragment
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

    private void saveCompleteDailyMeasurementToDatabase(){

    }

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
