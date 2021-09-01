package com.example.childapp;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyControlService extends Service implements LocationListener {
    MyBroadcastReceiver receiver;
    LocationManager LocMan;
    Location location;

    FirebaseDB myDB = new FirebaseDB();

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onCreate() {
        super.onCreate();

        // BROADCAST RECEIVER REGISTRATION
        receiver = new MyBroadcastReceiver();
        // Create intent filter
        IntentFilter filter = new IntentFilter();
        // Add actions for receiver to listen for
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction("android.intent.action.PHONE_STATE");
        // register Receiver to listen for the actions above
        this.registerReceiver(receiver, filter);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    public int onStartCommand(Intent intent, int flags, int startId) {
        showMessage("Monitoring Enabled");
        // LOCATION TRACKING ACTIVATION
        activateTracking();
        return START_STICKY; // START_STICKY is used for services that are explicitly started and stopped as needed
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister the receiver after service is Destroyed
        this.unregisterReceiver(receiver);
        // Deactivate Location Tracking
        LocMan.removeUpdates (this);
        showMessage("Monitoring Disabled");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Used for showing Toast messages
    private void showMessage(String Message) {
        Toast toast = Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_LONG);
        toast.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void activateTracking() {
        LocMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider;

        // As the app runs continuously, network as provider is preferred instead of GPS
        // Comes with lower power consumption and less accuracy, but that is not that big of a problem
        // GPS can be turned off easily
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        // Don't provide Altitude and Bearing and Speed
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        // Provider not allowed to incur monetary cost.
        criteria.setCostAllowed(false);

        // Get provider according to Criteria, enabled only ones (we only want the Network anyway)
        provider = LocMan.getBestProvider(criteria, true);
        // Check if we've got a provider, might be no providers available
        if (provider != null) {
            // Check if Permission ACCESS_COARSE_LOCATION is granted
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Get last known location and execute onLocationChanged()
                location = LocMan.getLastKnownLocation(provider);
                onLocationChanged(location);
                // Request location updates from provider every 10s and minimum distance 2 meters and the listener
                LocMan.requestLocationUpdates(provider, 10000, 2, this);
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        myDB.logLocation(location.getLongitude(), location.getLatitude());
        showMessage("LOCATION UPDATE");
    }
}