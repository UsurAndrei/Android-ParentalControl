package com.example.childapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MyControlService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand (Intent intent, int flags, int startId) {
        showMessage("Monitoring Enabled");
        return START_STICKY; // START_STICKY is used for services that are explicitly started and stopped as needed
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        showMessage("Monitoring Disabled");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Used for showing Toast msgs
    public void showMessage(String Message)
    {
        Toast toast = Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_LONG);
        toast.show ();
    }
}