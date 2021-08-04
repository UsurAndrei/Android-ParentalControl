package com.example.childapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button BtEnable;
    Button BtDisable;
    TextView MainMessage;

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
        MainMessage = (TextView)findViewById(R.id.editTextWelcomeMsg);
        // Get clicked view ID
        switch (v.getId()) {
            case R.id.buttonEnable:
                startService(intent);
                MainMessage.setText(R.string.MonActive);
                break;
            case R.id.buttonDisable:
                stopService(intent);
                MainMessage.setText(R.string.MonInactive);
                break;
            default:
                break;
        }
    }
}