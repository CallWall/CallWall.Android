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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
public class BluetoothService {
    // Debugging
    private static final String TAG = "BluetoothService";

    // Unique UUID for this application
    private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

    // Member fields
    private final BluetoothAdapter mAdapter;

    public BluetoothService() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void Send(BluetoothDevice device, byte[] data)
    {
        Log.d(TAG, "Async sending data to " + device.getName());
        SendThread sender = new SendThread(device,MY_UUID, data);
        sender.start();
    }

    private class SendThread extends Thread {
        private final BluetoothDevice mDevice;
        private final UUID mServiceId;
        private final byte[] mData;

        public SendThread(BluetoothDevice device, UUID serviceId, byte[] data) {
            Log.d(TAG, "create ConnectedThread");
            mDevice = device;
            mServiceId = serviceId;
            mData = data;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            BluetoothSocket socket = null;
            try {
                Log.d(TAG,"Creating socket....") ;
                socket = mDevice.createRfcommSocketToServiceRecord(mServiceId);
                Log.d(TAG,"Connecting socket....") ;
                socket.connect();
                Log.d(TAG,"Getting socket output stream....") ;
                OutputStream out = socket.getOutputStream();
                Log.d(TAG,"Writing bytes to output stream....") ;
                out.write(mData);
                Log.d(TAG,"Closing output stream....") ;
                socket.close();
            } catch (IOException e) {
                //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                Log.e(TAG, "Failed to send data.", e);
            }
        }
    }
}
