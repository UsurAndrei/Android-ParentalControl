package com.example.childapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button BtEnable;
    Button BtDisable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BtEnable = (Button)findViewById(R.id.buttonEnable);
        BtDisable = (Button)findViewById(R.id.buttonDisable);

        BtEnable.setOnClickListener(this); // Register Enable Button
        BtDisable.setOnClickListener(this); // Register Disable Button
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MyControlService.class);
        // Get clicked view ID
        switch (v.getId()) {
            case R.id.buttonEnable:
                startService(intent);
                break;
            case R.id.buttonDisable:
                stopService(intent);
                break;
            default:
                break;
        }
    }
}