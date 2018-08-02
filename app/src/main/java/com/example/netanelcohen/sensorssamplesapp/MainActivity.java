package com.example.netanelcohen.sensorssamplesapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ASensor sensor = new GPSSensor(this);
    SamplesPolicy samplesPolicy = null;
    private final int RequestPermissionCode = 7;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case RequestPermissionCode:

                boolean LocationPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(LocationPermission){
                        if (samplesPolicy == null) {
                            samplesPolicy = new SamplesPolicy(sensor, 1000, 6000,this );
                            samplesPolicy.startSample();
                        }
                    }
                break;
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean thereIsPermission = askForPermissions();

        if (thereIsPermission) {
            //whenever you want to sample some sensor , the only thing you have to do is to extends ASensor class
            //SamplesPolicy class does not really know which sensor is the actual sample, you need to pass the sensor as a parameter
            samplesPolicy = new SamplesPolicy(sensor, 10000, 6000,this );
            samplesPolicy.startSample();
        }


    }


    private boolean askForPermissions() {

        ArrayList<String> permissionsNeeded = new ArrayList<>();
        boolean permission = true;
        //ask for permission to write to external storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        //ask for permission to get location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permission = false;
        }
        if(!permission)
            ActivityCompat.requestPermissions(this, permissionsNeeded.toArray(new String[permissionsNeeded.size()]), RequestPermissionCode);

        return permission;
    }



}