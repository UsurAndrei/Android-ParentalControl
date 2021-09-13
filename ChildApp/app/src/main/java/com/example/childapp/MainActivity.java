package com.example.childapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button BtEnable;
    Button BtDisable;
    TextView MainMessage;
    Boolean isEnabled;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isEnabled = false;

        BtEnable = (Button)findViewById(R.id.buttonEnable);
        BtDisable = (Button)findViewById(R.id.buttonDisable);

        BtEnable.setOnClickListener(this); // Register Enable Button
        BtDisable.setOnClickListener(this); // Register Disable Button

        // Add needed permissions to an array of Strings (not all of them need to be granted by the user but anyway)
        String[] permissions = {Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.SEND_SMS,
                                Manifest.permission.READ_SMS,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.READ_CALL_LOG,
                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        reqPermissions(permissions);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MyControlService.class);
        MainMessage = (TextView)findViewById(R.id.editTextWelcomeMsg);
        // Get clicked view ID
        switch (v.getId()) {
            case R.id.buttonEnable:
                if(!isEnabled) {
                    startService(intent);
                    MainMessage.setText(R.string.MonActive);
                    isEnabled = true;
                }
                break;
            case R.id.buttonDisable:
                if(isEnabled) {
                    stopService(intent);
                    MainMessage.setText(R.string.MonInactive);
                    isEnabled = false;
                }
                break;
            default:
                break;
        }
    }

    // Check which permissions are not granted, add them in an ArrayList, then request them from the user if needed (not all are needed but I added them anyway)
    // requestPermissions method takes String[] as second parameter so conversion is needed
    // requestCode is not used here but it's required as a parameter, should be >=0
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void reqPermissions(String[] reqPermissions) {
        int requestCode = 1;
        ArrayList<String> nonGrantedPerms = new ArrayList<String>();
        for(String reqPermission : reqPermissions) {
            if (ContextCompat.checkSelfPermission(this, reqPermission) == PackageManager.PERMISSION_DENIED) {
                nonGrantedPerms.add(reqPermission);
            }
        }
        // App was crashing if the list was empty
        if(!nonGrantedPerms.isEmpty()) {
            ActivityCompat.requestPermissions(this, nonGrantedPerms.toArray(new String[nonGrantedPerms.size()]), requestCode);
        }
    }
}