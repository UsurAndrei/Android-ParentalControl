package com.example.parentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LocationsList extends AppCompatActivity {
    RecyclerView recyclerView;
    LocationAdapter adapter;
    DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        FirebaseDatabase myDB = FirebaseDatabase.getInstance("https://parentalcontrol-c9137-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseRef = myDB.getReference("logsLocations");

        // Create layout manager (Linear) and set it as manager for our recyclerview
        recyclerView = findViewById(R.id.recyclerLocations);
        RecyclerView.LayoutManager layoutMan = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutMan);

        // Make a query from databaseRef and use Message class for the data, will be used for the adapter
        FirebaseRecyclerOptions<Location> options = new FirebaseRecyclerOptions.Builder<Location>().setQuery(databaseRef, Location.class).build();

        // Create adapter object based on the options above
        adapter = new LocationAdapter(options, this);

        // Finally set the adapter as our recyclerView adapter
        recyclerView.setAdapter(adapter);

        // Pressing back from MapsActivity, going back to LocationsList used to crash the application
        // This fixed it, no idea why though
        recyclerView.setItemAnimator(null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start listening, adapter is getting data from the DB, forming the views and sending the data to the RecyclerView
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening for data
        adapter.stopListening();
    }
}
