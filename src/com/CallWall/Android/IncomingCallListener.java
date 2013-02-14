package com.CallWall.Android;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.CallWall.Android.Services.ActivatedIdentityObserver;

/**
 * Created with IntelliJ IDEA.
 * User: Lee
 * Date: 24/12/12
 * Time: 09:02
 * To change this template use File | Settings | File Templates.
 */
public class IncomingCallListener extends PhoneStateListener
{
    private final String logTag = "IncomingCallListener";
    private final ActivatedIdentityObserver identityObserver;

    public IncomingCallListener(ActivatedIdentityObserver identityObserver)
    {
        Log.d(logTag, "IncomingCallListener(" + identityObserver.getClass().getName() + ")");
        this.identityObserver = identityObserver;
    }

    public void onCallStateChanged(int state, String incomingNumber)
    {
        try {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(logTag, "Incoming call from " + incomingNumber);
                    Broadcast(incomingNumber);
                    break;
            }
        } catch (Exception e) {
            Log.i(logTag, "PhoneStateListener() e = " + e);
        }
    }

    private void Broadcast(String incomingNumber)
    {
        this.identityObserver.IdentityActivated(incomingNumber);
    }
}