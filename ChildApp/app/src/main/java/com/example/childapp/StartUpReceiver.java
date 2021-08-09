package com.example.childapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            doBoot();
        }
        if(action.equals("android.provider.Telephony.SMS_RECEIVED")) {
            doSms(intent);
        }
        if(action.equals("android.intent.action.PHONE_STATE")) {
            doPhone(intent);
        }
    }

    private void doBoot() {
        //What to do when phone boot broadcast received
    }

    private void doSms(Intent smsIntent) {
        //What to do when SMS Received broadcast received
    }

    private void doPhone(Intent phoneIntent) {
        //What to do when Phone_STATE broadcast received
    }
}