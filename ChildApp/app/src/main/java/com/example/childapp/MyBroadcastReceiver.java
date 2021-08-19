package com.example.childapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

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
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

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

                    myDB.logSMS(timeStamp, "Incoming", smsSender, smsBody);
                }
            }
            catch(Exception e) {
                System.out.println("*** Exception: " + e);
            }
        }
    }

    private void doPhone(Intent phoneIntent) {
        String state = phoneIntent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String caller = phoneIntent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        String status = "Default";
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        long startTime = 0;
        long endTime;
        long duration;

        if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            if(caller != null) {
                status = "Ringing";
            }
        }
        if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            if(caller != null) {
                status = "Answered";
                // Current time (call started) nanoseconds
                startTime = System.nanoTime();
            }
        }
        if(state.equals(TelephonyManager.EXTRA_STATE_IDLE))
            if(caller != null) {
                if(status.equals("Ringing")) {
                    myDB.logCall(timeStamp, caller, "Missed Call", "0h:0m:0s");
                }
                else if(status.equals("Answered")) {
                    // Current time (call ended) nanoseconds
                    endTime = System.nanoTime();
                    // Get duration in milliseconds
                    duration =  (endTime - startTime) / 1000000;

                }
            }
    }
}