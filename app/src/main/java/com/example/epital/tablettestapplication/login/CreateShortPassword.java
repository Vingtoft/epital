package com.example.epital.tablettestapplication.login;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.epital.tablettestapplication.R;
import com.example.epital.tablettestapplication.database.LogInDatabaseHandler;

/**
 * Created by oscarandersen on 27/11/14.
 */


public class CreateShortPassword extends Fragment implements View.OnClickListener {

    TextView overskrift, kode1, kode2, kode3, kode4;
    ImageButton knap1, knap2, knap3, knap4, knap5, knap6, knap7, knap8, knap9, knap0;
    ImageView image_password1, image_password2, image_password3, image_password4;
    LoginFragmentCommunication comm;
    Animation shake;
    int input_count = 0;
    int password_count = 0;
    String input_data, input_data_holder;
    LogInDatabaseHandler dbHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_short_password_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        //init interface to container activity
        comm = (LoginFragmentCommunication) getActivity();

        shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_password);
        //remove Hjælp and Opret Bruger buttons
        ImageButton opret_bruger = (ImageButton) getActivity().findViewById(R.id.short_btn_round_opret_bruger);
        ((ViewManager)opret_bruger.getParent()).removeView(opret_bruger);
        ImageButton help = (ImageButton) getActivity().findViewById(R.id.short_btn_round_hjaelp);
        ((ViewManager)help.getParent()).removeView(help);
        //init all variables
        overskrift = (TextView) getActivity().findViewById(R.id.short_logintekst);
        image_password1 = (ImageView) getActivity().findViewById(R.id.short_kode_image1);
        image_password2 = (ImageView) getActivity().findViewById(R.id.short_kode_image2);
        image_password3 = (ImageView) getActivity().findViewById(R.id.short_kode_image3);
        image_password4 = (ImageView) getActivity().findViewById(R.id.short_kode_image4);
        kode1 = (TextView) getActivity().findViewById(R.id.short_kode1);
        kode1.setTextSize(50);
        kode2 = (TextView) getActivity().findViewById(R.id.short_kode2);
        kode2.setTextSize(50);
        kode3 = (TextView) getActivity().findViewById(R.id.short_kode3);
        kode3.setTextSize(50);
        kode4 = (TextView) getActivity().findViewById(R.id.short_kode4);
        kode4.setTextSize(50);
        knap1 = (ImageButton) getActivity().findViewById(R.id.short_btn_round_btn1);
        knap1.setOnClickListener(this);
        knap2 = (ImageButton) getActivity().findViewById(R.id.short_btn_round_btn2);
        knap2.setOnClickListener(this);
        knap3 = (ImageButton) getActivity().findViewById(R.id.short_btn_round_btn3);
        knap3.setOnClickListener(this);
        knap4 = (ImageButton) getActivity().findViewById(R.id.short_btn_round_btn4);
        knap4.setOnClickListener(this);
        knap5 = (ImageButton) getActivity().findViewById(R.id.short_btn_round_btn5);
        knap5.setOnClickListener(this);
        knap6 = (ImageButton) getActivity().findViewById(R.id.short_btn_round_btn6);
        knap6.setOnClickListener(this);
        knap7 = (ImageButton) getActivity().findViewById(R.id.short_btn_round_btn7);
        knap7.setOnClickListener(this);
        knap8 = (ImageButton) getActivity().findViewById(R.id.short_btn_round_btn8);
        knap8.setOnClickListener(this);
        knap9 = (ImageButton) getActivity().findViewById(R.id.short_btn_round_btn9);
        knap9.setOnClickListener(this);
        knap0 = (ImageButton) getActivity().findViewById(R.id.short_btn_round_btn0);
        knap0.setOnClickListener(this);
        overskrift.setText("Indtast en ny 4-cifret pin-kode");
    }

    @Override
    public void onClick(View button) {
        if (button == knap1) {
            calculate_input("1");
        } else if (button == knap2) {
            calculate_input("2");
        } else if (button == knap3) {
            calculate_input("3");
        } else if (button == knap4) {
            calculate_input("4");
        } else if (button == knap5) {
            calculate_input("5");
        } else if (button == knap6) {
            calculate_input("6");
        } else if (button == knap7) {
            calculate_input("7");
        } else if (button == knap8) {
            calculate_input("8");
        } else if (button == knap9) {
            calculate_input("9");
        } else if (button == knap0) {
            calculate_input("0");
        }
    }

    private void calculate_input(String button_value) {
        if (input_count == 0) {
            kode1.setText(button_value);
            input_data = button_value;
        } else if (input_count == 1) {
            kode2.setText(button_value);
            input_data += button_value;
        } else if (input_count == 2) {
            kode3.setText(button_value);
            input_data += button_value;
        } else if (input_count == 3) {
            kode4.setText(button_value);
            input_data += button_value;
        }
        input_count++;

        if (input_count == 4 && password_count == 0) {
            password_count = 1;
            input_count = 0;
            input_data_holder = input_data;
            overskrift.setText("Indtast den samme kode igen");
            kode1.setText("");
            kode2.setText("");
            kode3.setText("");
            kode4.setText("");

        } else if (input_count == 4 && password_count == 1) {
            //check for match
            if (input_data_holder.equals(input_data)) {
                //Password mathes! Save password in the database
                dbHandler = new LogInDatabaseHandler(getActivity());
                dbHandler.saveShortPassword(input_data);
                comm.stateMachine(1);
            } else {
                Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(300);
                kode1.startAnimation(shake);
                kode2.startAnimation(shake);
                kode3.startAnimation(shake);
                kode4.startAnimation(shake);
                image_password1.startAnimation(shake);
                image_password2.startAnimation(shake);
                image_password3.startAnimation(shake);
                image_password4.startAnimation(shake);
                shake.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        overskrift.setText("Fejl - Indtast 4-cifret pinkode");
                        kode1.setText("");
                        kode2.setText("");
                        kode3.setText("");
                        kode4.setText("");
                        input_count = 0;
                        password_count = 0;
                        input_data = "";
                        input_data_holder = "";
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }
    }
}



