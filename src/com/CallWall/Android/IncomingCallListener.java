package com.CallWall.Android;


import android.app.Activity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.CallWall.Android.Services.BluetoothIdentityBroadcaster;
import com.CallWall.Android.Services.IdentityBroadcaster;

public class IncomingCallListener extends Activity  {
    String logTag = "IncomingCallSpike";
    TelephonyManager tm;
    IdentityBroadcaster identityBroadcaster;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(logTag, "onCreate(...)");
        setContentView(R.layout.main);

        //TODO: Use a factory or inject - LC
        identityBroadcaster = new BluetoothIdentityBroadcaster();

        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private PhoneStateListener mPhoneListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {
            try {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d(logTag, "Incoming call from " + incomingNumber);
                        String msg = incomingNumber + " is ringing";
                        Toast.makeText(IncomingCallListener.this , msg, Toast.LENGTH_SHORT).show();

                        identityBroadcaster.Broadcast(incomingNumber);

                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        //Seems to never get called.
                        //TODO: Investigate when this event is fired.
                        String offHookMsg = "CALL_STATE_OFFHOOK. (" + incomingNumber + ")";
                        //

                        //TODO: log stuff here...


                        Toast.makeText(IncomingCallListener.this, offHookMsg, Toast.LENGTH_SHORT).show();
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        //TODO: Should broadcast a CallFinished event
                        Toast.makeText(IncomingCallListener.this, "CALL_STATE_IDLE", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(IncomingCallListener.this, "default", Toast.LENGTH_SHORT).show();
                        Log.i("Default", "Unknown phone state=" + state);
                }
            } catch (Exception e) {
                Log.i(logTag, "PhoneStateListener() e = " + e);
            }
        }
    };
}

