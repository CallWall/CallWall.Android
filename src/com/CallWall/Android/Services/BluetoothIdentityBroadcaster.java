package com.CallWall.Android.Services;

import android.util.Log;
import org.apache.http.util.EncodingUtils;

import java.util.UUID;

public class BluetoothIdentityBroadcaster implements IdentityBroadcaster {
    private static final String logTag = "BluetoothIdentityBroadcaster";
    private static final UUID CallWallServiceId = UUID.fromString("5dfee4fe-a594-4bfb-b21a-6d7184330669");
    private static final UUID CommonSerialBoardServiceId = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothService bluetoothService;

    public BluetoothIdentityBroadcaster()
    {
        Log.d(logTag, "BluetoothIdentityBroadcaster()");
        //bluetoothService = new BluetoothService(CallWallServiceId);
        bluetoothService = new BluetoothService(CommonSerialBoardServiceId);
    }

    public boolean isEnabled()
    {
        return bluetoothService.IsEnabled();
    }

    @Override
    public void Broadcast(String identity) {

        Log.d(logTag, "Broadcast('" + identity + "')");
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