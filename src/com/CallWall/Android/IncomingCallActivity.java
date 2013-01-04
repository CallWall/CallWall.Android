package com.CallWall.Android;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.CallWall.Android.Services.BluetoothIdentityBroadcaster;
import com.CallWall.Android.Services.CompositeIdentityBroadcaster;
import com.CallWall.Android.Services.IdentityBroadcaster;

public class IncomingCallActivity extends Activity implements IdentityBroadcaster  {
    String logTag = "IncomingCallActivity";
    TelephonyManager tm;
    PhoneStateListener phoneListener;
    IdentityBroadcaster broadcaster;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(logTag, "onCreate(...)");
        setContentView(R.layout.main);

        this.broadcaster = CreateBroadcaster();
        phoneListener = new IncomingCallListener(this.broadcaster);

        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        tm.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        //Not all phones/SIM cards expose the current phone number.
        SetDefaultTestNumber(tm.getLine1Number());
    }

    private void SetDefaultTestNumber(String phoneNumber) {
        EditText text_TestNumber = (EditText)findViewById(R.id.text_testNumber);
        text_TestNumber.getText().clear();
        text_TestNumber.getText().append(phoneNumber);
    }

    @Override
    public void Broadcast(String identity) {
        String msg = identity + " is ringing";
        Toast.makeText(IncomingCallActivity.this , msg, Toast.LENGTH_SHORT).show();
    }

    public void TestButtonClicked(View view)
    {
        Log.i(logTag, "Testing connection");
        EditText textNumber = (EditText)findViewById(R.id.text_testNumber);
        String numberToBroadcast = textNumber.getText().toString();
        broadcaster.Broadcast(numberToBroadcast);
    }

    private IdentityBroadcaster CreateBroadcaster()
    {
        CompositeIdentityBroadcaster compositeBroadcaster = new CompositeIdentityBroadcaster();
        compositeBroadcaster.Add(this);
        compositeBroadcaster.Add(new BluetoothIdentityBroadcaster());
        return compositeBroadcaster;
    }
}

