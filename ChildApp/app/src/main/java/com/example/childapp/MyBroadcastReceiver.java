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
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyBroadcastReceiver extends BroadcastReceiver {

    FirebaseDB myDB = new FirebaseDB();
    // Global variables for the doPhone method
    boolean answered = false;
    boolean phoneRang = false;
    long startTime;
    long endTime;
    long duration;

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

                    myDB.logSMS(timeStamp, "Received", smsSender, smsBody);
                }
            }
            catch(Exception e) {
                System.out.println("*** Exception: " + e);
            }
        }
    }

    private void doPhone(Intent phoneIntent) {
        String state = phoneIntent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String number = phoneIntent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            if(number != null) {
                phoneRang = true;
            }
        }
        // Receiver only receives OFF HOOK broadcasts when we call someone
        // So the calling duration will be calculate for the total time
        // Not sure if something else can be done
        if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            if(number != null) {
                answered = true;
                // Current time (call started) nanoseconds
                startTime = System.nanoTime();
            }
        }
        if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            if(number != null) {
                if(!answered) {
                    // Log missed call
                    myDB.logCall(timeStamp, number, "Missed Call", "00d:00h:00m:00s","Incoming");
                    phoneRang = false;
                }
                else {
                    // Current time (call ended) nanoseconds
                    endTime = System.nanoTime();
                    // Get duration in milliseconds
                    duration =  (endTime - startTime) / 1000000;
                    if(phoneRang) {
                        myDB.logCall(timeStamp, number, "Connected", formatDuration(duration), "Incoming");
                        phoneRang = false;
                    } else {
                        myDB.logCall(timeStamp, number, "Unknown", formatDuration(duration), "External");
                    }
                    answered = false;
                }
            }
        }
    }
    // Format time and return a nice String
    private String formatDuration(long time_ms) {
        String duration;
        long time_s = TimeUnit.MILLISECONDS.toSeconds(time_ms);
        long time_m = TimeUnit.SECONDS.toMinutes(time_s);
        long time_h = TimeUnit.MINUTES.toHours(time_m);
        long time_d = TimeUnit.HOURS.toDays(time_h);

        if(time_ms < 60000) { // Less than one minute
            duration = (time_d + "d:" + time_h + "h:" + time_m + "m:" + time_s + "s:");
        } else if(time_ms < 3600000) { // Less than one hour
            time_s = time_s % 60;
            duration = (time_d + "d:" + time_h + "h:" + time_m + "m:" + time_s + "s:");
        } else if(time_ms < 86400000) { // Less than one day
            time_s = time_s % 60;
            time_m = time_m % 60;
            duration = (time_d + "d:" + time_h + "h:" + time_m + "m:" + time_s + "s:");
        } else { // Thats a lot of time for a call, hope it's not more than a month :)
            time_s = time_s % 60;
            time_m = time_m % 60;
            time_h = time_h % 24;
            duration = (time_d + "d:" + time_h + "h:" + time_m + "m:" + time_s + "s:");
        }
        return duration;
    }
}