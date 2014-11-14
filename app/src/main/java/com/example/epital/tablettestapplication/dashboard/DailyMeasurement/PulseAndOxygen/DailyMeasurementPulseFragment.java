package com.example.epital.tablettestapplication.dashboard.DailyMeasurement.PulseAndOxygen;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.epital.tablettestapplication.R;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.DailyMeasurementFragmentCommunication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    ConnectThread connectThread;
    ConnectedThread connectedThread;
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
    List<Integer> oxygenHolder = new ArrayList<Integer>();
    List<Integer> pulseHolder = new ArrayList<Integer>();
    DailyMeasurementFragmentCommunication comm;
    String paired_device = "Nonin_Medical_Inc._569799";
    //String paired_device = "Nonin_Medical_Inc._396502";

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
                if (checkForPairedDevices(paired_device)) {
                    //The NONIN device is already paired. Initiate connection!
                    dailyMeasurementStateMachine(5);
                } else {
                    //TODO: Pair with device!
                }
                break;

            case 5:
                previous_state = current_state;
                changeLayout(3);
                isBluetoothOn.setText("Tænd for NONIN pulsmåler (illustration/vejledning kommer)");
                connectThread = new ConnectThread(noninDevice);
                connectThread.start();
                current_state = 5;
                break;

            case 6:
                //Tag NONIN på fingeren
                previous_state = current_state;
                current_state = 6;
                changeLayout(3);
                isBluetoothOn.setText("Put en finger i NONIN pulsmåleren");
                try {
                    timerCount.cancel();
                } catch (Exception e) {
                    //do nothing
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

            case 8:
                //close the party!
                previous_state = current_state;
                current_state = 8;
                //terminate the bluetooth connection to nonin device
                connectedThread.cancel();
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
                break;
            case 4:
                //daily_measurement_saturation
                currentLayout.removeAllViews();
                currentLayout.addView(View.inflate(getActivity().getBaseContext(), R.layout.daily_measurement_saturation, null), params);
                oxygen = (TextView) getActivity().findViewById(R.id.oxygen_level);
                pulse = (TextView) getActivity().findViewById(R.id.pulse);
                saturationCountDown = (TextView) getActivity().findViewById(R.id.saturation_count_down);
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
                dailyMeasurementStateMachine(8);
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

    private boolean checkForPairedDevices(String device_name) {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals(device_name)) {
                    noninDevice = device;
                    System.out.println("Device fundet!");
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    private void startDiscovering() {
        boolean success = bluetoothAdapter.startDiscovery(); //async process for discovering bt-devices.
        System.out.println("Success?: ");
        System.out.println(success);

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        //AcceptThread acceptThread = new AcceptThread();
        //acceptThread.run();

    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private int failed_rate = 0;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                //TODO: If device Android version is 4.0.4 or 4.0.3 do this:
                tmp = device.createInsecureRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
                //TODO: Else: Do this:
                //tmp = device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
            } catch (IOException e) {
                System.out.println("Noget gik galt i ConnectThread " + e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            bluetoothAdapter.cancelDiscovery();
            while (failed_rate < 20) {
                try {
                    // Connect the device through the socket. This will block
                    // until it succeeds or throws an exception
                    System.out.println("Hvorfor gør den det?");
                    mmSocket.connect();
                    System.out.println("Argh!");
                    break;
                } catch (IOException connectException) {
                    System.out.println("buuuh" + failed_rate + connectException);
                    failed_rate++;
                    try {
                        Thread.sleep(1500);
                    } catch (Exception e) {
                        //go fuck thyself
                    }
                }
            }
            if (failed_rate < 20) {
                //change the main thread state machine
                Message message = mHandler.obtainMessage(2);
                message.sendToTarget();

                // Do work to manage the connection (in a separate thread)
                connectedThread = new ConnectedThread(mmSocket);
                connectedThread.start();
                connectedThread.write();
            } else {
                //TODO: Failed to connect!
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                }
            }
        }

        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[5];  // buffer store for the stream
            int bytes; // bytes returned from read()
            int frame = 1;

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    Message testMessage = mHandler.obtainMessage(1, buffer);
                    testMessage.sendToTarget();

                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write() {
            try {
                //set data mode #8
                mmOutStream.write(0x44);
                mmOutStream.write(0x38);
            } catch (IOException e) {
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message inputMessage) {
            //set the UI with pulse and oxygenlevel
            if (inputMessage.what == 2) {
                //change state call
                dailyMeasurementStateMachine(6);
            } else if (inputMessage.what == 1) {

                byte[] buffer = (byte[]) inputMessage.obj;

                int finger_inserted = (buffer[3] >> 3) & 1; //1 finger not inserted, 0 finger is inserted
                int out_of_track = ((buffer[0] >> 5) & 1); //1 out of track, 0 everything is OK
                System.out.println("Finger inserted: " + finger_inserted + " Out of track: " + out_of_track + " buffer1: " + buffer[1] + " buffer 2: " + buffer[2]);
                if (finger_inserted == 1 && out_of_track == 1 ||
                        buffer[1] == 127 && buffer[2] == 127 ||
                        buffer[1] < 35 || buffer[2] < 35) {
                    if (current_state != 6) {
                        dailyMeasurementStateMachine(6);
                    }
                } else {
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











