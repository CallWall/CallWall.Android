package com.CallWall.Android;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.CallWall.Android.Entities.PersonalIdentifier;
import com.CallWall.Android.Entities.Profile;
import com.CallWall.Android.Services.ActivatedIdentityObserver;
import com.CallWall.Android.Services.BluetoothProfileBroadcaster;
import com.CallWall.Android.Services.CompositeProfileBroadcaster;
import com.CallWall.Android.Services.ProfileBroadcaster;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class IncomingCallActivity extends Activity implements ProfileBroadcaster, ActivatedIdentityObserver {
    String logTag = "IncomingCallActivity";
    TelephonyManager tm;
    PhoneStateListener phoneListener;
    ProfileBroadcaster broadcaster;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(logTag, "onCreate(...)");
        setContentView(R.layout.main);

        this.broadcaster = CreateBroadcaster();
        phoneListener = new IncomingCallListener(this);

        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        tm.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        //Not all phones/SIM cards expose the current phone number.
        SetDefaultTestNumber(tm.getLine1Number());
    }

    /*ActivatedIdentityObserver.IdentityActivated(String) called by the IncomingCallListener*/
    @Override
    public void IdentityActivated(String phoneNumber) {
        Profile activatedProfile = FindProfile(phoneNumber);
        this.broadcaster.Broadcast(activatedProfile);
    }

    /*ActivatedIdentityObserver.IdentityActivated(String) called by the IncomingCallListener*/
    @Override
    public void Broadcast(Profile profile)
    {
        String msg = profile.ToString() + " is ringing";
        Toast.makeText(IncomingCallActivity.this , msg, Toast.LENGTH_SHORT).show();
    }

    public void TestButtonClicked(View view)
    {
        Log.i(logTag, "Testing connection");
        EditText textNumber = (EditText)findViewById(R.id.text_testNumber);
        String numberToBroadcast = textNumber.getText().toString();

        Profile profileToBroadcast = FindProfile(numberToBroadcast);

        broadcaster.Broadcast(profileToBroadcast);
    }

    private void SetDefaultTestNumber(String phoneNumber)
    {
        EditText text_TestNumber = (EditText)findViewById(R.id.text_testNumber);
        text_TestNumber.getText().clear();
        text_TestNumber.getText().append(phoneNumber);
    }

    private ProfileBroadcaster CreateBroadcaster()
    {
        CompositeProfileBroadcaster compositeBroadcaster = new CompositeProfileBroadcaster();
        compositeBroadcaster.Add(this);
        compositeBroadcaster.Add(new BluetoothProfileBroadcaster());
        return compositeBroadcaster;
    }

    private Profile FindProfile(String phoneNumber)
    {
        Log.d(logTag, "ContactService.FindProfile(" + phoneNumber + ")");

        //From
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        // define the columns I want the query to return
        String[] projection = new String[] {
                ContactsContract.PhoneLookup._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME
                //,ContactsContract.CommonDataKinds.Email.DATA
        };
        CursorLoader cursorLoader = new CursorLoader(
                this,
                contactUri,
                projection,
                null,
                null,
                null);

        Cursor cursor = cursorLoader.loadInBackground();

        Set<String> allUniqueEmails = new HashSet<String>();
        Log.d(logTag, "getContacts(" + phoneNumber + ")");
        while(cursor.moveToNext())
        {
            String id = cursor.getString(0);
            String name = cursor.getString(1);

            ArrayList<String> emails =  getNameEmailDetails(id);
            for(String email : emails)
            {
                allUniqueEmails.add(email);
            }
        }
        cursor.close();

        ArrayList<PersonalIdentifier> ids = new ArrayList<PersonalIdentifier>();
        PersonalIdentifier phoneId = new PersonalIdentifier("phone","android", phoneNumber);
        ids.add(phoneId);
        for(String email : allUniqueEmails )
        {
            if (email !=null && !TextUtils.isEmpty(email))
            {
                PersonalIdentifier id = new PersonalIdentifier("email","android", email);
                ids.add(id);
            }
        }

        return new Profile(ids);
    }

    private ArrayList<String> getNameEmailDetails(String contactId)
    {
        ContentResolver cr = this.getContentResolver();
        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Email.DATA
        };
        Cursor cur1 = cr.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                projection,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                new String[]{contactId}, null);
        ArrayList<String> emails = new ArrayList<String>();
        while (cur1.moveToNext()) {
            String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            Log.e("Email", email);
            email = email.toLowerCase();
            emails.add(email);
        }
        cur1.close();
        /*
        02-13 08:45:20.880: D/ContactService(3208): ContactService.FindProfile(+447554257819)
        02-13 08:45:20.885: D/ContactService(3208): 1
        02-13 08:45:20.885: D/ContactService(3208): 2
        02-13 08:45:20.885: D/ContactService(3208): 3
        02-13 08:45:20.895: D/ContactService(3208): 4
        02-13 08:45:20.895: D/ContactService(3208): getContacts(+447554257819)
        02-13 08:45:20.895: D/ContactService(3208):   Erynne Campbell
        02-13 08:45:20.905: E/Email(3208): Erynne.Amm@gmail.com
        02-13 08:45:20.905: E/Email(3208): erynne.campbell@gmail.com
        02-13 08:45:20.905: E/Email(3208): Erynne.Campbell@googlemail.com
        02-13 08:45:20.905: E/Email(3208): erynne.amm@gmail.com
        02-13 08:45:20.910: D/ContactService(3208):   Erynne Campbell
        02-13 08:45:20.915: E/Email(3208): Erynne.Amm@gmail.com
        02-13 08:45:20.915: E/Email(3208): erynne.campbell@gmail.com
        02-13 08:45:20.915: E/Email(3208): Erynne.Campbell@googlemail.com
        02-13 08:45:20.915: E/Email(3208): erynne.amm@gmail.com
          */
        return emails;
    }
}

