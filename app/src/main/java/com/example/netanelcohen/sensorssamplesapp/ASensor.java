package com.example.netanelcohen.sensorssamplesapp;

import android.content.Context;

import java.util.ArrayList;

/**
 * this class represents a sensor.
 * this class allows you to listen to the sensor and receive the sensor data
 */

public abstract class ASensor {

    protected Context mContext;
    protected ArrayList<String> attributes;

    public ASensor(Context context) {
        mContext = context;
        this.attributes = new ArrayList<String>();
    }

    public abstract Data getData();
    public abstract void stopListen();
    public abstract void startListen();

    public ArrayList<String> getAttributes() {
        return attributes;
    }
}
