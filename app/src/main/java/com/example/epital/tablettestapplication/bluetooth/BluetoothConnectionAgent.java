package com.example.epital.tablettestapplication.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by oscarandersen on 06/10/14.
 */

public class BluetoothConnectionAgent {
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    ConnectedThread connectedThread;
    ConnectThread connectThread;
    Handler mHandler;
    String device_name;
    BluetoothDevice bluetoothDevice;

    public BluetoothConnectionAgent(Handler mHandler, String device_name){
        //get all info
        this.mHandler = mHandler;
        this.device_name = device_name;
    }

    public void connect(BluetoothDevice device){
        connectThread = new ConnectThread(device);
        connectThread.start();
    }

    public void close(){
        connectThread.cancel();
        connectedThread.cancel();
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
                    mmSocket.connect();
                    System.out.println("Connection Successfull!");
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
            byte[] buffer = new byte[20];  // buffer store for the stream
            int bytes; // bytes returned from read()
            int frame = 1;

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    byte[] new_buffer = new byte[bytes];

                    for (int i = 0; i < bytes; i++){
                        new_buffer[i] = buffer[i];
                    }

                    System.out.println("Bytes returned from read: " + bytes);
                    Message testMessage = mHandler.obtainMessage(1, new_buffer);
                    testMessage.sendToTarget();

                } catch (IOException e) {
                    break;
                }
            }
        }
        /* Call this from the main activity to send data to the remote device */
        public void write() {

        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    public BluetoothDevice checkForPairedDevices(String device_name) {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals(device_name)) {
                    return device;
                }
            }
        } else {
            return null;
        }
        return null;
    }

}
