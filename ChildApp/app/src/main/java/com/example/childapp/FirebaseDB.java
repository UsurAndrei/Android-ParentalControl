package com.example.childapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDB {
    private final DatabaseReference myDatabase;

    public FirebaseDB() {
        myDatabase = FirebaseDatabase.getInstance("https://parentalcontrol-c9137-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    }
}
