package com.example.childapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDB {
    private final DatabaseReference myDatabaseRef;

    public FirebaseDB() {
        // Get database instance from URL
        FirebaseDatabase myDB = FirebaseDatabase.getInstance("https://parentalcontrol-c9137-default-rtdb.europe-west1.firebasedatabase.app/");
        // // Automatically stores the data offline when there is no internet connection. When the device connects to internet, all the data will be pushed to realtime database
        //myDB.setPersistenceEnabled(true);
        // getReference() gives us reference to realtime database JSON top node, to perform operations later
        myDatabaseRef = myDB.getReference();
    }

    // Used to log SMS messages
    void logSMS(String dateTime, String type, String number, String message) {
        // Create new unique SMS ID and create node using push() and return the unique ID using getKey()
        String smsID = myDatabaseRef.push().getKey();
        // Set values to path of the SMS logs using the unique generated key
        myDatabaseRef.child("logsSMS").child(smsID).child("timestamp").setValue(dateTime);
        myDatabaseRef.child("logsSMS").child(smsID).child("type").setValue(type);
        myDatabaseRef.child("logsSMS").child(smsID).child("number").setValue(number);
        myDatabaseRef.child("logsSMS").child(smsID).child("message").setValue(message);
    }

    // Used to log phone calls
    void logCall(String dateTime, String caller, String status, String duration, String type) {
        // Create new unique call ID and create node using push() and return the unique ID using getKey()
        String callID = myDatabaseRef.push().getKey();
        // Set values to path of the call logs using the unique generated key
        myDatabaseRef.child("logsCalls").child(callID).child("timestamp").setValue(dateTime);
        myDatabaseRef.child("logsCalls").child(callID).child("caller").setValue(caller);
        myDatabaseRef.child("logsCalls").child(callID).child("status").setValue(status);
        myDatabaseRef.child("logsCalls").child(callID).child("duration").setValue(duration);
        myDatabaseRef.child("logsCalls").child(callID).child("type").setValue(type);
    }

    // Used to log location
    void logLocation(String dateTime, Double longitude, Double latitude) {
        // Create new unique location update ID and create node using push() and return the unique ID using getKey()
        String locationID = myDatabaseRef.push().getKey();
        // Set values of longitude and latitude to the correct path
        myDatabaseRef.child("logsLocations").child(locationID).child("timestamp").setValue(dateTime);
        myDatabaseRef.child("logsLocations").child(locationID).child("longitude").setValue(longitude);
        myDatabaseRef.child("logsLocations").child(locationID).child("latitude").setValue(latitude);
    }
}
