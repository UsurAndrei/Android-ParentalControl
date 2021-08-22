package com.example.childapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDB {
    private final DatabaseReference myDatabaseRef;

    public FirebaseDB() {
        // Get database instance from URL
        FirebaseDatabase myDB = FirebaseDatabase.getInstance("https://parentalcontrol-c9137-default-rtdb.europe-west1.firebasedatabase.app/");
        // Automatically stores the data offline when there is no internet connection. When the device connects to internet, all the data will be pushed to realtime database
        myDB.setPersistenceEnabled(true);
        // getReference() gives us reference to realtime database JSON top node, to perform operations later
        myDatabaseRef = myDB.getReference();
    }

    // Used to log SMS messages
    void logSMS(String dateTime, String type, String sender, String message) {
        // Create new unique SMS ID and create node using push() and return the unique ID using getKey()
        String smsID = myDatabaseRef.push().getKey();
        // Set values to path of the SMS logs using the unique generated key
        myDatabaseRef.child("logsSMS").child(smsID).child("date-time").setValue(dateTime);
        myDatabaseRef.child("logsSMS").child(smsID).child("type").setValue(type);
        myDatabaseRef.child("logsSMS").child(smsID).child("sender").setValue(sender);
        myDatabaseRef.child("logsSMS").child(smsID).child("message").setValue(message);
    }

    // Used to log phone calls
    void logCall(String dateTime, String caller, String status, String duration, String type) {
        // Create new unique call ID and create node using push() and return the unique ID using getKey()
        String callID = myDatabaseRef.push().getKey();
        // Set values to path of the call logs using the unique generated key
        myDatabaseRef.child("logsCalls").child(callID).child("date-time").setValue(dateTime);
        myDatabaseRef.child("logsCalls").child(callID).child("caller").setValue(caller);
        myDatabaseRef.child("logsCalls").child(callID).child("status").setValue(status);
        myDatabaseRef.child("logsCalls").child(callID).child("duration").setValue(duration);
        myDatabaseRef.child("logsCalls").child(callID).child("type").setValue(type);
    }
}
