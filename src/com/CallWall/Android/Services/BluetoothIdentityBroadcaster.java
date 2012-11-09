package com.CallWall.Android.Services;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import org.apache.http.util.EncodingUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;
/**
 * Created with IntelliJ IDEA.
 * User: Lee
 * Date: 09/11/12
 * Time: 08:24
 * To change this template use File | Settings | File Templates.
 */
public class BluetoothIdentityBroadcaster implements IdentityBroadcaster {
    final String logTag = "BluetoothIdentityBroadcaster";
    //final UUID CallMeServiceId = UUID.fromString("5DFEE4FE-A594-4BFB-B21A-6D7184330669");
    final UUID CallMeServiceId = UUID.fromString("00001105-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter bluetoothAdapter;
    BluetoothService bluetoothService;

    public BluetoothIdentityBroadcaster()
    {
        Log.d(logTag, "BluetoothIdentityBroadcaster()");
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        bluetoothService = new BluetoothService();
    }

    public boolean isEnabled()
    {
        return bluetoothAdapter!=null && bluetoothAdapter.isEnabled();
    }

    @Override
    public void Broadcast(List<String> identities) {
        String message = "";
        for (String identity : identities) {
            message+=identity +",";
        }
        Log.d(logTag, "Broadcast("+message+")");
        if(!isEnabled())
        {
            Log.d(logTag, "Bluetooth device is not enabled.");
            return;
        }
        if(identities.isEmpty())
        {
            Log.d(logTag, "No identities to broadcast");
            return;
        }

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

        String payload = "";
        Boolean isFirst = true;
        for (String identity : identities) {
            if(isFirst)
            {
                payload +=identity;
                isFirst = false;
            }else
            {
                payload +="\n"+identity;
            }
        }

        Log.d(logTag, "Sending identities : '" + payload+ "'");
        //byte[] bytes = identity.getBytes();
        byte[] bytes = EncodingUtils.getAsciiBytes(payload);
        Log.d(logTag, " identities as byte array '" + bytes + "'");


        Log.d(logTag, "Calling Send.");
        bluetoothService.Send(targetDevice, bytes);
    }
}