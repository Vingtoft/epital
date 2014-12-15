package com.example.epital.tablettestapplication.dashboard.DailyMeasurement.PulseAndOxygen;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.epital.tablettestapplication.R;
import com.example.epital.tablettestapplication.bluetooth.BluetoothConnectionAgent;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.DailyMeasurementFragmentCommunication;

import java.util.ArrayList;
import java.util.List;

import android.os.CountDownTimer;

/**
 * Created by oscarandersen on 03/10/14.
 */

public class DailyMeasurementPulseFragment extends Fragment {
    private static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothDevice noninDevice;
    TextView oxygen, pulse, isBluetoothOn, saturationCountDown;
    int current_state, previous_state;
    RelativeLayout currentLayout;
    CountDownTimer timerCount;
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
    List<Integer> oxygenHolder = new ArrayList<Integer>();
    List<Integer> pulseHolder = new ArrayList<Integer>();
    DailyMeasurementFragmentCommunication comm;
    String paired_device = "Nonin_Medical_Inc._569799";
    //String paired_device = "Nonin_Medical_Inc._396502";

    BluetoothConnectionAgent bluetoothConnectionAgent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.daily_measurement_content, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceType) {
        super.onActivityCreated(savedInstanceType);
        IntentFilter intentFilter;
        intentFilter = new IntentFilter();
        intentFilter.addAction("ACTION_FOUND");
        //init communication interface to dashboard container activity
        comm = (DailyMeasurementFragmentCommunication) getActivity();
        bluetoothConnectionAgent = new BluetoothConnectionAgent(mHandler, paired_device);
        dailyMeasurementStateMachine(1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //System.out.println("onActivityResult(), request_code: " + Integer.toString(requestCode) + " resultcode: " + Integer.toString(resultCode));
        if (requestCode == 1 && resultCode == -1) {
            //Bluetooth has been enabled by user at this point

            if (current_state == 2) {
                //we check if current state is 2, in order to avoid a null pointer exception
                dailyMeasurementStateMachine(3);
            }
        }
    }

    public void dailyMeasurementStateMachine(int state) {
        switch (state) {
            case 1:
                previous_state = current_state;
                current_state = 1;
                //set the right content layout
                changeLayout(1);
                changeLayout(3);
                dailyMeasurementStateMachine(2);
                break;

            case 2:
                // Vi gør klar til måling af puls...
                previous_state = current_state;
                current_state = 2;
                changeLayout(3);
                //turn on bluetooth
                turnOnBluetooth();
                break;

            case 3:
                //Bluetooth er blevet tændt!
                previous_state = current_state;
                current_state = 3;
                changeLayout(3);
                isBluetoothOn.setText("1. Bluetooth er tændt!");
                dailyMeasurementStateMachine(4);
                break;

            case 4:
                previous_state = current_state;
                current_state = 4;
                changeLayout(3);
                //Check if the NONIN saturation device is a part of already paired devices:
                noninDevice = bluetoothConnectionAgent.checkForPairedDevices(paired_device);
                if (noninDevice != null) {
                    //The NONIN device is already paired. Initiate connection!
                    dailyMeasurementStateMachine(5);
                } else {
                    //TODO: Pair with device!
                }
                break;

            case 5:
                previous_state = current_state;
                changeLayout(3);
                changeLayout(5);
                isBluetoothOn.setText("Tænd for pulsmåler");

                try {
                    // Deactivate existing connection
                    bluetoothConnectionAgent.close();
                } catch (Exception e) {
                }

                bluetoothConnectionAgent.connect(noninDevice);

                current_state = 5;
                break;

            case 6:
                //Tag NONIN på fingeren
                previous_state = current_state;
                current_state = 6;
                changeLayout(3);
                changeLayout(6);
                isBluetoothOn.setText("Put en finger i NONIN pulsmåleren");
                try {
                    timerCount.cancel();
                } catch (Exception e) {
                }
                break;

            case 7:
                previous_state = current_state;
                current_state = 7;
                changeLayout(4);
                //begin count down (10 seconds measurement)
                countDown();
                timerCount.start();
                break;

            case 9:
                // Remove finger from NONIN device
                previous_state = current_state;
                current_state = 9;
                changeLayout(3);
                changeLayout(7);
                isBluetoothOn.setText("Tag din finger ud af måleren");
                break;

            case 8:
                System.out.println("Pulse & oxygen state machine 8");
                //close the party!
                previous_state = current_state;
                current_state = 8;
                //terminate the bluetooth connection to nonin device
                bluetoothConnectionAgent.close();
                // calculate average
                int sum = 0;
                for (int d : oxygenHolder) sum += d;
                int oxygen_average = sum / oxygenHolder.size();
                sum = 0;
                for (int d : pulseHolder) sum += d;
                int pulse_average = sum / pulseHolder.size();
                //save the average
                comm.setOxygen(oxygen_average);
                comm.setPulse(pulse_average);
                comm.changeDailyMeasurementContent(2);
                break;
        }
    }

    public void changeLayout(int layout) {
        switch (layout) {
            case 1:
                //blank view
                currentLayout = (RelativeLayout) getActivity().findViewById(R.id.daily_measurement_content_base);
                break;

            case 2:
                //udgået
                //TODO: Ret til
                break;

            case 3:
                //daily_measurement_before_you_start
                currentLayout.removeAllViews();
                currentLayout.addView(View.inflate(getActivity().getBaseContext(), R.layout.daily_measurement_init_saturation, null), params);
                isBluetoothOn = (TextView) getActivity().findViewById(R.id.daily_measurement_init_bluetooth_enabled_validation);
                System.out.println("is BL ON ? " + isBluetoothOn);
                break;

            case 4:
                //daily_measurement_saturation
                currentLayout.removeAllViews();
                currentLayout.addView(View.inflate(getActivity().getBaseContext(), R.layout.daily_measurement_saturation, null), params);
                oxygen = (TextView) getActivity().findViewById(R.id.oxygen_level);
                pulse = (TextView) getActivity().findViewById(R.id.pulse);
                saturationCountDown = (TextView) getActivity().findViewById(R.id.saturation_count_down);
                break;

            case 5:
                System.gc();
                //Turn on nonin animation
                ImageView turn_on_instruction = (ImageView) getView().findViewById(R.id.nonin_animation);
                turn_on_instruction.getLayoutParams().height = 469;
                turn_on_instruction.getLayoutParams().width = 405;
                turn_on_instruction.setBackgroundResource(R.drawable.turn_on_nonin_animation);
                AnimationDrawable turn_on_animation = (AnimationDrawable) turn_on_instruction.getBackground();
                turn_on_animation.start();
                break;

            case 6:
                System.gc();
                // Put finger in nonin animation
                ImageView finger_in_instruction = (ImageView) getView().findViewById(R.id.nonin_animation);
                finger_in_instruction.setBackgroundResource(R.drawable.put_a_finger_in_nonin_animation);
                AnimationDrawable finger_in_animation = (AnimationDrawable) finger_in_instruction.getBackground();
                finger_in_animation.start();
                break;

            case 7:
                System.gc();
                // Remove finger from nonin device animation
                ImageView insert_finger_instruction = (ImageView) getView().findViewById(R.id.nonin_animation);
                insert_finger_instruction.setBackgroundResource(R.drawable.remove_finger_from_nonin);
                AnimationDrawable insert_finger_animation = (AnimationDrawable) insert_finger_instruction.getBackground();
                insert_finger_animation.start();
                break;

            default:
                //do nothing
                break;
        }
    }

    public void countDown() {
        //counts down from 10 -> 0
        timerCount = new CountDownTimer(10 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                saturationCountDown.setText("Færdig om " + (l / 1000) + " sekunder");
            }

            @Override
            public void onFinish() {
                dailyMeasurementStateMachine(9);
            }
        };
    }

    private void turnOnBluetooth() {
        if (bluetoothAdapter == null) {
            //device does not support bluetooth. Story ends here (for now)
            //TODO: Move to a "Your device does not support BT" fragment
        } else if (!bluetoothAdapter.isEnabled()) {
            //bluetooth is not turned on. Ask user to turn it off.
            Intent enableBluetoothIntent = new Intent(bluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
        } else {
            //bluetooth is supported and already turned on
            System.out.println("Er det nogen=!");
            dailyMeasurementStateMachine(3);
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message inputMessage) {
            //set the UI with pulse and oxygenlevel
            System.out.println("pulse&oxygen handleMessage, what = " + inputMessage.what);
            if (inputMessage.what == 2) {
                //change state call
                dailyMeasurementStateMachine(6);
            } else if (inputMessage.what == 1) {

                byte[] buffer = (byte[]) inputMessage.obj;
                if (buffer.length > 3) {
                    int finger_inserted = (buffer[3] >> 3) & 1; //1 finger not inserted, 0 finger is inserted
                    int out_of_track = ((buffer[0] >> 5) & 1); //1 out of track, 0 everything is OK
                    System.out.println("Finger inserted: " + finger_inserted + " Out of track: " + out_of_track + " buffer1: " + buffer[1] + " buffer 2: " + buffer[2]);
                    if (finger_inserted == 1 && out_of_track == 1 ||
                            buffer[1] == 127 && buffer[2] == 127 ||
                            buffer[1] < 35 || buffer[2] < 35) {
                        if (current_state != 6) {
                            if (current_state == 9) {
                                dailyMeasurementStateMachine(8);
                            } else {
                                dailyMeasurementStateMachine(6);
                            }
                        }
                    } else if (current_state != 9) {
                        if (current_state != 7) {
                            dailyMeasurementStateMachine(7);
                        }
                        int pulse_holder = buffer[1];
                        int oxygen_holder = buffer[2];
                        pulseHolder.add(pulse_holder);
                        oxygenHolder.add(oxygen_holder);
                        pulse.setText(Integer.toString(buffer[1]));
                        oxygen.setText(Integer.toString(buffer[2]));
                    }
                }
            }
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                System.out.println(device.getName() + "\n" + device.getAddress());
            }
        }
    };
}











