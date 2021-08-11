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
    void logSMS(String date, String type, String sender, String message) {
        // Create new unique SMS ID and create node using push() and return the unique ID using getKey()
        String smsID = myDatabaseRef.push().getKey();
        // Set values to path of the SMS using the unique generated key
        myDatabaseRef.child("logsSMS").child(smsID).child("date").setValue(date);
        myDatabaseRef.child("logsSMS").child(smsID).child("type").setValue(type);
        myDatabaseRef.child("logsSMS").child(smsID).child("sender").setValue(sender);
        myDatabaseRef.child("logsSMS").child(smsID).child("message").setValue(message);
    }
}
