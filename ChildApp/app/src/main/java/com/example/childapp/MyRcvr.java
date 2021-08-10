package com.example.childapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class MyRcvr extends BroadcastReceiver {

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
        SmsMessage[] Messages = null; // This will be used for
        String Sender;
        String ToService; // Send


    }

    private void doPhone(Intent phoneIntent) {
        //What to do when Phone_STATE broadcast received
    }
}