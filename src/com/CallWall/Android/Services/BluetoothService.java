/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//package com.example.android.BluetoothChat;
//Taken from the BluetoothChat sample. This is a modified version of BluetoothChatService.
package com.CallWall.Android.Services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
public class BluetoothService {
    // Logging
    private static final String logTag = "BluetoothService";

    //TODO: Unique ID should probably be passed in so this class become agnostic -LC
    // Unique UUID for this application
    //private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    //  final UUID CallMeServiceId = UUID.fromString("5DFEE4FE-A594-4BFB-B21A-6D7184330669");   //Was commented out
    //final UUID CallMeServiceId = UUID.fromString("00001105-0000-1000-8000-00805F9B34FB");     //Was used int BluetoothIdentityBroadcaster
    private final UUID ServiceId;

    // Member fields
    private final BluetoothAdapter bluetoothAdapter;

    public BluetoothService(UUID serviceId) {
        Log.d(logTag, "BluetoothService(serviceId)");
        ServiceId = serviceId;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean IsEnabled()
    {
        return bluetoothAdapter!=null && bluetoothAdapter.isEnabled();
    }

    public void Send(byte[] data)
    {
        BluetoothDevice targetDevice = null;
        Log.d(logTag, "Bluetooth enabled and is paired with the following devices");
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                Log.d(logTag, device.getName() + " @ " + device.getAddress());
                if(targetDevice==null)
                {
                    targetDevice = device;
                }
            }
        }

        Log.d(logTag, "Async sending data to " + targetDevice.getName());
        SendThread sender = new SendThread(targetDevice, ServiceId, data);
        sender.start();
    }

    private class SendThread extends Thread {
        private final BluetoothDevice mDevice;
        private final UUID mServiceId;
        private final byte[] mData;

        public SendThread(BluetoothDevice device, UUID serviceId, byte[] data) {
            Log.d(logTag, "create ConnectedThread");
            mDevice = device;
            mServiceId = serviceId;
            mData = data;
        }

        public void run() {
            Log.i(logTag, "BEGIN mConnectedThread");
            // Always cancel discovery because it will slow down a connection
            bluetoothAdapter.cancelDiscovery();

            BluetoothSocket socket = null;
            try {
                Log.d(logTag,"Creating socket....") ;
                socket = mDevice.createRfcommSocketToServiceRecord(mServiceId);
                Log.d(logTag,"Connecting socket....") ;
                socket.connect();
                Log.d(logTag,"Getting socket output stream....") ;
                OutputStream out = socket.getOutputStream();
                Log.d(logTag,"Writing bytes to output stream....") ;
                out.write(mData);
                Log.d(logTag,"Closing output stream....") ;
                socket.close();
            } catch (IOException e) {
                //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                Log.e(logTag, "Failed to send data.", e);
            }
        }
    }
}
