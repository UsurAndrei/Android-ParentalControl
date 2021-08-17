package com.example.childapp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyControlService extends Service {
    MyBroadcastReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new MyBroadcastReceiver();
        // Create intent filter
        IntentFilter filter = new IntentFilter();
        // Add actions for receiver to listen for
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction("android.intent.action.PHONE_STATE");
        // register Receiver to listen for the actions above
        this.registerReceiver(receiver, filter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int onStartCommand (Intent intent, int flags, int startId) {
        showMessage("Monitoring Enabled");
        return START_STICKY; // START_STICKY is used for services that are explicitly started and stopped as needed
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister the receiver after service is Destroyed
        this.unregisterReceiver(receiver);
        showMessage("Monitoring Disabled");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Used for showing Toast msgs
    private void showMessage(String Message)
    {
        Toast toast = Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_LONG);
        toast.show();
    }
}