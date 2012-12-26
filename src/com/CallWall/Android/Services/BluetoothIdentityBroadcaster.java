package com.CallWall.Android.Services;

import android.util.Log;
import org.apache.http.util.EncodingUtils;

import java.util.UUID;

public class BluetoothIdentityBroadcaster implements IdentityBroadcaster {
    private static final String logTag = "BluetoothIdentityBroadcaster";
    //Generated UUID/GUID For CallWall clients.
    private static final UUID BluetoothServiceId = UUID.fromString("5dfee4fe-a594-4bfb-b21a-6d7184330669");
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