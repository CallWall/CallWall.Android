package com.CallWall.Android.Services;

import android.util.Log;
import com.CallWall.Android.Entities.PersonalIdentifier;
import com.CallWall.Android.Entities.Profile;
import org.apache.http.util.EncodingUtils;

import java.util.UUID;

public class BluetoothProfileBroadcaster implements ProfileBroadcaster {
    private static final String logTag = "BluetoothProfileBroadcaster";
    private static final UUID CallWallServiceId = UUID.fromString("5dfee4fe-a594-4bfb-b21a-6d7184330669");
    private static final UUID CommonSerialBoardServiceId = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothService bluetoothService;

    public BluetoothProfileBroadcaster()
    {
        Log.d(logTag, "BluetoothProfileBroadcaster()");
        //bluetoothService = new BluetoothService(CallWallServiceId);
        bluetoothService = new BluetoothService(CommonSerialBoardServiceId);
    }

    public boolean isEnabled()
    {
        return bluetoothService.IsEnabled();
    }

    @Override
    public void Broadcast(Profile profile) {

        //Log.d(logTag, "Broadcast('" + identity + "')");
        if(!isEnabled())
        {
            Log.d(logTag, "Bluetooth device is not enabled.");
            return;
        }


        int idx = 0;
        String payload = "";
        for (PersonalIdentifier identity : profile.get_PersonalIdentifiers()) {
            if(idx++ > 1) payload += "\n";
            payload += identity.get_Value() ;
        }
        if(payload.length()==0)
        {
            Log.d(logTag, "Profile is empty. No identities to broadcast");
            return;
        }
        Log.d(logTag, "Sending payload : '" + payload+ "'");
        byte[] bytes = EncodingUtils.getAsciiBytes(payload);
        Log.d(logTag, " profile as byte array '" + bytes + "'");
        bluetoothService.Send(bytes);
    }
}