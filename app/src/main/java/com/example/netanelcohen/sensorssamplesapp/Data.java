package com.example.netanelcohen.sensorssamplesapp;

import java.util.ArrayList;

/**
 * this class represents sensor's data
 */


public class Data {


    private ArrayList<Double> sampleData;

    public Data() {
        sampleData = new ArrayList<>();
    }

    public void addValue(Double value) {
        sampleData.add(value);
    }

    public ArrayList<Double> getSampleData() {
        return sampleData;
    }


}
