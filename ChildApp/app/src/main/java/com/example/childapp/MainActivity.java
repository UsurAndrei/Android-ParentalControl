package com.example.childapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button BtEnable;
    Button BtDisable;
    TextView MainMessage;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BtEnable = (Button)findViewById(R.id.buttonEnable);
        BtDisable = (Button)findViewById(R.id.buttonDisable);

        BtEnable.setOnClickListener(this); // Register Enable Button
        BtDisable.setOnClickListener(this); // Register Disable Button

        String[] permissions = {Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.SEND_SMS,
                                Manifest.permission.READ_SMS,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.READ_CALL_LOG,
                                Manifest.permission.INTERNET};
        reqPermissions(permissions);
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

    // Check which permissions are not granted, add them in an ArrayList, then request them from the user
    // requestPermissions method takes String[] as second parameter so convertion is needed
    // requestCode is not used here but it's required as a parameter
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void reqPermissions(String[] reqPermissions) {
        int requestCode = 1;
        ArrayList<String> nonGrantedPerms = new ArrayList<String>();
        for(String reqPermission : reqPermissions) {
            if (ContextCompat.checkSelfPermission(this, reqPermission) == PackageManager.PERMISSION_DENIED) {
                nonGrantedPerms.add(reqPermission);
            }
        }
        ActivityCompat.requestPermissions(this, nonGrantedPerms.toArray(new String[nonGrantedPerms.size()]), requestCode);
    }
}