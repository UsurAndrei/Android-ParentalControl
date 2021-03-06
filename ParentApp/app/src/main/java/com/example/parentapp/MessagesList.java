package com.example.parentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MessagesList extends AppCompatActivity {
    RecyclerView recyclerView;
    MessageAdapter adapter;
    DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        FirebaseDatabase myDB = FirebaseDatabase.getInstance("https://parentalcontrol-c9137-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseRef = myDB.getReference("logsSMS");

        // Create layout manager (Linear) and set it as manager for our recyclerview
        recyclerView = findViewById(R.id.recyclerMessages);
        RecyclerView.LayoutManager layoutMan = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutMan);

        // Make a query from databaseRef and use Message class for the data, will be used for the adapter
        FirebaseRecyclerOptions<Message> options = new FirebaseRecyclerOptions.Builder<Message>().setQuery(databaseRef, Message.class).build();

        // Create adapter object based on the options above
        adapter = new MessageAdapter(options);

        // Finally set the adapter as our recyclerView adapter
        recyclerView.setAdapter(adapter);
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