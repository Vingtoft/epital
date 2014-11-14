package com.example.epital.tablettestapplication.dashboard.DailyMeasurement.LungFunction;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.epital.tablettestapplication.R;
import com.example.epital.tablettestapplication.bluetooth.BluetoothConnectionAgent;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.DailyMeasurementFragmentCommunication;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * Created by oscarandersen on 17/10/14.
 */
public class DailyMeasurementLungFunctionFragmenCOPD6 extends Fragment implements View.OnClickListener {

    private static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    DailyMeasurementFragmentCommunication comm;
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
    BluetoothConnectionAgent bluetoothConnectionAgent;
    String device_name = "COPD_6346";
    //String device_name = "COPD_9943";
    int current_state, previous_state;
    BluetoothDevice device;
    RelativeLayout currentLayout;
    TextView isBluetoothOn, header;
    double result = 0.0;
    Button proceed, repeat;

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
        //init bluetooth connection agent
        bluetoothConnectionAgent = new BluetoothConnectionAgent(mHandler, device_name);
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
                //Initial state
                //Set the right initial content layout
                changeLayout(1);
                dailyMeasurementStateMachine(2);
                break;
            case 2:
                //Turn on bluetooth
                changeLayout(2);
                changeLayout(3);
                hideButton();
                turnOnBluetooth();
                break;
            case 3:
                hideButton();
                //Bluetooth has been turned on!
                isBluetoothOn.setText("Bluetooth er tændt!");
                dailyMeasurementStateMachine(4);
                break;
            case 4:
                hideButton();
                //Check if device is already paired with android
                device = bluetoothConnectionAgent.checkForPairedDevices(device_name);
                if (device != null) {
                    dailyMeasurementStateMachine(5);
                } else {
                    //TODO: Move to a "device is not paired" fragment
                    System.out.println("Ikke paret");
                }
                break;
            case 5:
                hideButton();
                //Connect to device
                //illustration to turn on device
                isBluetoothOn.setText("Tænd COPD-6 (illustration kommer)");
                try {
                    bluetoothConnectionAgent.close();
                } catch (Exception e) {

                }

                bluetoothConnectionAgent.connect(device);
                break;
            case 6:
                hideButton();
                //illustration to configure the device, and wait for blow
                isBluetoothOn.setText("COPD-6 er tilsluttet. Tryk på enter 3 gange og foretag måling");
                //wait for result
                break;
            case 7:
                isBluetoothOn.setText("Måling færdig!\nResultat: " + result);
                //ask user if the result is accepted (yes and no button)
                showButton();
                //close down and move the fuck on!
                break;
            case 8:
                hideButton();
                //save the result to and move to next question!
                bluetoothConnectionAgent.close();
                comm.setFev1(result);
                comm.changeDailyMeasurementContent(3);
                break;
            default:
                //Do nothing here
                break;
        }
    }

    public void showButton() {
        System.out.println("Kommer jeg nogensinde her ned?!");
        proceed.setVisibility(View.VISIBLE);
        repeat.setVisibility(View.VISIBLE);
    }

    public void hideButton() {
        proceed.setVisibility(View.GONE);
        repeat.setVisibility(View.GONE);
    }

    public void changeLayout(int layout) {
        switch (layout) {
            case 1:
                //blank view
                currentLayout = (RelativeLayout) getActivity().findViewById(R.id.daily_measurement_content_base);
                break;
            case 2:
                currentLayout.removeAllViews();
                currentLayout.addView(View.inflate(getActivity().getBaseContext(), R.layout.daily_measurement_init_saturation, null), params);
                isBluetoothOn = (TextView) getActivity().findViewById(R.id.daily_measurement_init_bluetooth_enabled_validation);
                header = (TextView) getActivity().findViewById(R.id.daily_measurement_init_sautration_header);
                header.setText("Vi gør klar til måling af FEV1");
                break;
            case 3:
                proceed = (Button) getActivity().findViewById(R.id.proceed);
                proceed.setOnClickListener(this);
                repeat = (Button) getActivity().findViewById(R.id.repeat);
                repeat.setOnClickListener(this);
                break;
            case 4:
                break;
            case 5:
                break;
            default:
                //Do nothing here
                break;
        }
    }

    private void turnOnBluetooth() {
        if (bluetoothAdapter == null) {
            //device does not support bluetooth. Story ends here (for now)
            //TODO: Move to a "Your device does not support BT" fragment
        } else if (!bluetoothAdapter.isEnabled()) {
            //bluetooth is not turned on. Ask user to turn it on.
            Intent enableBluetoothIntent = new Intent(bluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
        } else {
            //bluetooth is supported and already turned on
            dailyMeasurementStateMachine(3);
        }
    }


    //TODO: Sjuskekode, omstruktur!
    int a = 0;
    byte[] container = new byte[1];
    boolean start_flag = false;
    int protocol; //, 0 = no protoocol, 1 = patient blow in device, 2 = device is been shut
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message inputMessage) {
            if (inputMessage.what == 2) {
                dailyMeasurementStateMachine(6);
                //change state call
            } else if (inputMessage.what == 1) {
                byte[] buffer = (byte[]) inputMessage.obj;

                if (isLastMessage(buffer)) {
                    System.out.println("This is the last message");
                    //merge array
                    container = mergeArrays(container, buffer);
                    //ro routine
                    doRoutine(container);
                    //reset array data container
                    container = new byte[0];
                    protocol = 0;
                } else {
                    System.out.println("First message");
                    container = mergeArrays(container, buffer);
                }
            }
        }
    };
    private void printByteArray(byte[] byteArray){
        int e = 0;
        for (byte b : byteArray) {
            System.out.println("Nummer: " + e + " Value: " + new String(new byte[]{b}) + " Raw: " + b + " Hex: " + String.format("%02X ", b));
            e++;
        }
    }

    private void doRoutine(byte[] buffer) {
        System.out.println("In doRoutine");
        System.out.println("Before CLEAN:");
        printByteArray(buffer);
        System.out.println("After CLEAN:");
        buffer = cleanArray(buffer);
        printByteArray(buffer);
        setDataProtocol(buffer);
        if (protocol == 1) {
            System.out.println("In dorRoutine, protocol is 1");
            behandlArray(buffer);
        } else if (protocol == 2) {
            System.out.println("In dorRoutine, protocol is 2");
            devicePowersDown();
        }
    }

    private byte[] cleanArray(byte[] byteArray){
        if (byteArray[0] != 0x02 && byteArray[1] == 0x02){
            byte[] tempArray = new byte[byteArray.length-1];
            for (int a = 0; a < byteArray.length - 1; a++){
                tempArray[a] = byteArray[a + 1];
            }
            return tempArray;
        }
        return byteArray;
    }


    private boolean isLastMessage(byte[] buffer) {
        for (byte b : buffer) {
            if (b == 0x03) {
                return true;
            }
        }
        return false;
    }

    private boolean isFirstMessage(byte[] buffer) {
        if (buffer[0] == 0x02) {
            return true;
        }
        return false;
    }

    private void setDataProtocol(byte[] buffer) {
        if (buffer[1] == 0x44 &&
                buffer[2] == 0x54 &&
                buffer[3] == 0x44) {
            protocol = 1;
        } else if (buffer[1] == 0x44 &&
                buffer[2] == 0x50 &&
                buffer[3] == 0x44) {
            protocol = 2;
        }
    }

    private void behandlArray(byte[] buffer) {
        System.out.println("Behandl Array: Længde på buffer: " + buffer.length);
        String number, decimal1, decimal2;
        number = new String(new byte[]{buffer[29]});
        decimal1 = new String(new byte[]{buffer[30]});
        decimal2 = new String(new byte[]{buffer[31]});
        String total = number + "." + decimal1 + decimal2;
        System.out.println("Resultat: " + total);
        result = Double.parseDouble(total);
        dailyMeasurementStateMachine(7);
        //System.out.println("Du har blæst:  " + result);
        //System.out.println("Ciffer 1: " + number + " Ciffer 2: " + decimal1 + " Ciffer 3: " + decimal2);
        int length = buffer.length;
        for (int i = 0; i < length; i++) {
            if (buffer[i] == 0x02) {
                System.out.println("TRANSMISSION START: " + buffer[i]);
            } else if (buffer[i] == 0x03) {
                System.out.println("TRANSMISSION END: " + buffer[i]);
            } else {
                if (i == 30) {
                }
                System.out.println("Index: " + i + " Value: " + new String(new byte[]{buffer[i]}));
            }
            // System.out.println(buffer[i]);
        }
    }

    private byte[] mergeArrays(byte[] first, byte[] second) {
        int firstLen = first.length;
        int secondLen = second.length;
        byte[] newBuffer = new byte[secondLen + firstLen];
        System.arraycopy(first, 0, newBuffer, 0, firstLen);
        System.arraycopy(second, 0, newBuffer, firstLen, secondLen);
        return newBuffer;
    }

    private void devicePowersDown() {
        System.out.println("SHOTDOWN!");
    }

    @Override
    public void onClick(View button) {
        if (button == proceed) {
            dailyMeasurementStateMachine(8);
        } else if (button == repeat) {
            dailyMeasurementStateMachine(4);
        }
    }
}
