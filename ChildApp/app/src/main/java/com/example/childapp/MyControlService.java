package com.example.childapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyControlService extends Service implements LocationListener {
    private static final String CONTENT_SMS = "content://sms";

    MyBroadcastReceiver receiver;
    LocationManager LocMan;
    Location location;
    String messageID;

    FirebaseDB myDB = new FirebaseDB();
    ContentObserver SMSObserver;

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

        // REGISTER CONTENT OBSERVER FOR SENT SMS MESSAGES
        SMSObserver = new SMSObserver(new Handler());
        getContentResolver().registerContentObserver(Uri.parse(CONTENT_SMS),true, SMSObserver);

        // LOCATION TRACKING ACTIVATION
        activateTracking();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    public int onStartCommand(Intent intent, int flags, int startId) {
        showMessage("Monitoring Enabled");
        return START_STICKY; // START_STICKY is used for services that are explicitly started and stopped as needed
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister the receiver
        this.unregisterReceiver(receiver);
        // Unregister content observer
        getContentResolver().unregisterContentObserver(SMSObserver);
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
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Get last known location and execute onLocationChanged()
                location = LocMan.getLastKnownLocation(provider);
                onLocationChanged(location);
                // Request location updates from provider every 10s and minimum distance 2 meters and the listener
                LocMan.requestLocationUpdates(provider, 10000, 2, this);
            }
        }
    }

    // If location was Null, app would crash
    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.location = location;
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        myDB.logLocation(timeStamp, location.getLongitude(), location.getLatitude());
    }

    // This ContentObserver receives call backs for changes to content given, which will be used for SMS messages.
    // Based on answers from https://stackoverflow.com/questions/38963327/listening-to-outgoing-sms-not-working-android
    // plus answers from https://www.py4u.net/discuss/614153 & https://stackoverflow.com/questions/662420/android-sms-content-content-sms-sent and more

    public class SMSObserver extends ContentObserver {
        // Constructor matching super
        public SMSObserver(Handler handler) {
            super(handler);
        }
        // This method is called when a content change occurs.
        @SuppressLint("Range")
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            Uri smsURI = Uri.parse(CONTENT_SMS); // Get URI of content://sms
            Cursor curs = getContentResolver().query(smsURI, null, null, null, null);
            // This will make it point to the first record, which is the last SMS sent if it exists
            if(curs.moveToNext()) {
                // Get id, type, address, body from the corresponding columns
                String id = curs.getString(curs.getColumnIndex("_id"));
                String type = curs.getString(curs.getColumnIndex("type"));
                String address = curs.getString(curs.getColumnIndex("address"));
                String body = curs.getString(curs.getColumnIndex("body"));
                // Check if message type is sent (2)
                if(type.equals("2")) {
                    // Prevent multiple writes to database due to multiple triggers
                    if(!id.equals(messageID)) {
                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        messageID = id;
                        myDB.logSMS(timeStamp, "Sent", address, body);
                    }
                }
            }
            // Free cursor after use
            curs.close();
        }
    }
}