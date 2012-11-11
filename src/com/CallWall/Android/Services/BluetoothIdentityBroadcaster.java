package com.CallWall.Android.Services;

import android.util.Log;
import org.apache.http.util.EncodingUtils;

import java.util.UUID;

public class BluetoothIdentityBroadcaster implements IdentityBroadcaster {
    final String logTag = "BluetoothIdentityBroadcaster";
    private static final UUID BluetoothServiceId = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    BluetoothService bluetoothService;

    public BluetoothIdentityBroadcaster()
    {
        Log.d(logTag, "BluetoothIdentityBroadcaster()");

        bluetoothService = new BluetoothService(BluetoothServiceId);
    }

    public boolean isEnabled()
    {
        return bluetoothService.IsEnabled();
    }

    @Override
    public void Broadcast(String identity) {

        Log.d(logTag, "Broadcast("+identity+")");
        if(!isEnabled())
        {
            Log.d(logTag, "Bluetooth device is not enabled.");
            return;
        }
        if(identity.isEmpty())
        {
            Log.d(logTag, "No identity to broadcast");
            return;
        }

        Log.d(logTag, "Sending identity : '" + identity+ "'");
        byte[] bytes = EncodingUtils.getAsciiBytes(identity);
        Log.d(logTag, " identity as byte array '" + bytes + "'");
        bluetoothService.Send(bytes);
    }
}