package com.example.parentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button BtCalls, BtSMS, BtLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BtCalls = (Button)findViewById(R.id.callButton);
        BtSMS = (Button)findViewById(R.id.smsButton);
        BtLocation = (Button)findViewById(R.id.locationButton);

        // Register buttons
        BtCalls.setOnClickListener(this);
        BtSMS.setOnClickListener(this);
        BtLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.callButton:
                Intent intentCalls = new Intent(this, CallsList.class);
                startActivity(intentCalls);
                break;
            case R.id.smsButton:
                Intent intentSMS = new Intent(this, MessagesList.class);
                startActivity(intentSMS);
                break;
            case R.id.locationButton:
                Intent intentLocation = new Intent(this, LocationsList.class);
                startActivity(intentLocation);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}