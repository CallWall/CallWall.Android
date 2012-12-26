package com.CallWall.Android;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.CallWall.Android.Services.BluetoothIdentityBroadcaster;
import com.CallWall.Android.Services.CompositeIdentityBroadcaster;
import com.CallWall.Android.Services.IdentityBroadcaster;

public class IncomingCallActivity extends Activity implements IdentityBroadcaster  {
    String logTag = "IncomingCallActivity";
    TelephonyManager tm;
    PhoneStateListener phoneListener;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(logTag, "onCreate(...)");
        setContentView(R.layout.main);

        CompositeIdentityBroadcaster compositeBroadcasters = new CompositeIdentityBroadcaster();
        compositeBroadcasters.Add(this);
        compositeBroadcasters.Add(new BluetoothIdentityBroadcaster());
        phoneListener = new IncomingCallListener(compositeBroadcasters);

        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        tm.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }
    @Override
    public void Broadcast(String identity) {
        String msg = identity + " is ringing";
        Toast.makeText(IncomingCallActivity.this , msg, Toast.LENGTH_SHORT).show();
    }
}

