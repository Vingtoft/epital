package com.example.epital.tablettestapplication.login;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.epital.tablettestapplication.R;

import java.util.*;

public class LoginFragment extends Fragment implements View.OnClickListener {
    ImageButton knap1, knap2, knap3, knap4, knap5, knap6, knap7, knap8, knap9, knap0;
    ImageView image_password1, image_password2, image_password3, image_password4;
    TextView kode1, kode2, kode3, kode4;
    FragmentCommunication comm;
    Animation shake;
    int input_count = 0;
    int input_data[] = new int[]{-1, -1, -1, -1};
    int password[] = new int[]{2, 5, 8, 0};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceType) {
        /* Bliver kaldt når fragmenten er helt færdig med at loade. Det er derfor her man
         * skal tilgå UI elementer (så er man sikker på at de er blevet initialiseret,
         * og undgår derfor en Null Pointer Exception */
        super.onActivityCreated(savedInstanceType);
        init();
        comm = (FragmentCommunication) getActivity();
    }

    private void init() {
        shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_password);
        image_password1 = (ImageView) getActivity().findViewById(R.id.kode_image1);
        image_password2 = (ImageView) getActivity().findViewById(R.id.kode_image2);
        image_password3 = (ImageView) getActivity().findViewById(R.id.kode_image3);
        image_password4 = (ImageView) getActivity().findViewById(R.id.kode_image4);
        kode1 = (TextView) getActivity().findViewById(R.id.kode1);
        kode2 = (TextView) getActivity().findViewById(R.id.kode2);
        kode3 = (TextView) getActivity().findViewById(R.id.kode3);
        kode4 = (TextView) getActivity().findViewById(R.id.kode4);
        knap1 = (ImageButton) getActivity().findViewById(R.id.btn_round_btn1);
        knap1.setOnClickListener(this);
        knap2 = (ImageButton) getActivity().findViewById(R.id.btn_round_btn2);
        knap2.setOnClickListener(this);
        knap3 = (ImageButton) getActivity().findViewById(R.id.btn_round_btn3);
        knap3.setOnClickListener(this);
        knap4 = (ImageButton) getActivity().findViewById(R.id.btn_round_btn4);
        knap4.setOnClickListener(this);
        knap5 = (ImageButton) getActivity().findViewById(R.id.btn_round_btn5);
        knap5.setOnClickListener(this);
        knap6 = (ImageButton) getActivity().findViewById(R.id.btn_round_btn6);
        knap6.setOnClickListener(this);
        knap7 = (ImageButton) getActivity().findViewById(R.id.btn_round_btn7);
        knap7.setOnClickListener(this);
        knap8 = (ImageButton) getActivity().findViewById(R.id.btn_round_btn8);
        knap8.setOnClickListener(this);
        knap9 = (ImageButton) getActivity().findViewById(R.id.btn_round_btn9);
        knap9.setOnClickListener(this);
        knap0 = (ImageButton) getActivity().findViewById(R.id.btn_round_btn0);
        knap0.setOnClickListener(this);
    }

    @Override
    public void onClick(View button) {
        if (button == knap1) {
            calculate_input(1);
        } else if (button == knap2) {
            calculate_input(2);
        } else if (button == knap3) {
            calculate_input(3);
        } else if (button == knap4) {
            calculate_input(4);
        } else if (button == knap5) {
            calculate_input(5);
        } else if (button == knap6) {
            calculate_input(6);
        } else if (button == knap7) {
            calculate_input(7);
        } else if (button == knap8) {
            calculate_input(8);
        } else if (button == knap9) {
            calculate_input(9);
        } else if (button == knap0) {
            calculate_input(0);
        }
    }

    private void calculate_input(int button_value) {
        if (input_count == 0) {
            kode1.setText("*");
            input_data[0] = button_value;
            System.out.println("Er hos 1");
        } else if (input_count == 1) {
            kode2.setText("*");
            input_data[1] = button_value;
            System.out.println("Er hos 2");
        } else if (input_count == 2) {
            kode3.setText("*");
            input_data[2] = button_value;
            System.out.println("Er hos 3");
        } else if (input_count == 3) {
            kode4.setText("*");
            input_data[3] = button_value;
            System.out.println("Er hos 4");
        }
        input_count++;
        if (input_count == 4) {
            if (Arrays.equals(input_data, password)) {
                //Proceed to new view / fragment
                comm.moveToLoadFragment();
                reset_input();
            } else {
                //Reset det hele + lav en lille animation
                perform_animation();
                System.out.println("Not a match!!");
            }
        }
    }

    private void reset_input() {
        kode1.setText("");
        kode2.setText("");
        kode3.setText("");
        kode4.setText("");
        input_count = 0;
    }

    private void perform_animation() {
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
                reset_input();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}












