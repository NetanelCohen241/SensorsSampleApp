package com.example.netanelcohen.sensorssamplesapp;

import android.app.Activity;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * this class allows us to sample a sensor at a certain frequency and for a certain period of time,
 * without knowing which sensor we are actually sampling.
 */

public class SamplesPolicy {

    private int milisecondsToSample;
    private int milisecondsFrequency;
    private ASensor sensor;
    private File file;
    private Activity activity;
    private ArrayList<Data> samplesData;


    /**
     * Constructor - initialize the all fields
     * @param sensor - which sensor to sample
     * @param milisecondsFrequency - specifies the frequency at which the sensor should be sampled
     * @param milisecondsToSample - specifies the time to sample the sensor in mili seconds
     * @param activity -
     */
    public SamplesPolicy(ASensor sensor, int milisecondsFrequency, int milisecondsToSample, Activity activity) {
        this.milisecondsFrequency = milisecondsFrequency;
        this.milisecondsToSample = milisecondsToSample;
        this.sensor = sensor;
        this.activity = activity;
        this.file = new File(activity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) , sensor.getClass().getSimpleName()+".txt");

    }

    // start to sample the sensor according the given frequency
    public void startSample(){

        final Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {

                sensor.startListen();
                samplesData = new ArrayList<>();
                TextView tv = (TextView) activity.findViewById(R.id.name);
                tv.setText("Sensor Name: " + sensor.getClass().getSimpleName().split("Sensor")[0]);
                // code to be run every second
                new CountDownTimer(milisecondsToSample+1000, 1000) {

                    // Coundown is counting down (every second)
                    public void onTick(long millisecondsUntilDone) {
                        //store current sensor data
                        samplesData.add(sensor.getData());
                    }

                    public void onFinish() {
                        // Counter is finished!
                        Log.i("Done!", "Coundown Timer Finished");
                         //wrire to file the summarized data we have got from the sensor
                        String summarize = getSummarizedData();
                        writeToFile(summarize);
                        displayOnScreen(summarize);
                        samplesData =null;
                        //stop listen until the next time
                        sensor.stopListen();

                    }
                }.start();

                //schedule "run" method to run every "milisecondsFrequency"
                handler.postDelayed(this, milisecondsFrequency);
            }
        };

        handler.post(run);
    }

    //show sensor values on device screen
    private void displayOnScreen(String summarize) {

        String[] output = summarize.split("\n");
        TextView[] tv = new TextView[4];
        tv[0] = (TextView) activity.findViewById(R.id.att1);
        tv[1] = (TextView) activity.findViewById(R.id.att2);
        tv[2] = (TextView) activity.findViewById(R.id.att3);

        for (int i = 0; i < output.length; i++)
            tv[i].setText(output[i]);

    }

    // Saves the sampled data on the device in the file
    private void writeToFile(String summarizedData) {

        //write the date and time that the sample was taken
        String output = (new SimpleDateFormat("yyyy/MM/dd_HH:mm").format(new Date()))+"\n" + summarizedData;
        if(isExternalStorageWritable()) {
            try {
                FileOutputStream outputStream = new FileOutputStream(file, true);
                outputStream.write(output.getBytes());
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     *
     * @return the summarized data for all samples
     */
    private String getSummarizedData() {

        String summarizedData="";
        List<String> attributes = sensor.getAttributes();
        //sum for each attribute value
        double[] sensorValuesSum = new double[attributes.size()];

        for (int i = 0; i < sensorValuesSum.length; i++)
            sensorValuesSum[i] = 0;

        ArrayList<Double> data;
        //update sum
        for (int i = 0; i < samplesData.size(); i++) {
            data = samplesData.get(i).getSampleData();

            for (int j = 0; j < sensorValuesSum.length; j++) {
                sensorValuesSum[j] += data.get(j);
            }
        }

        //calculetes the average value of each sensor's attribute
        for (int i = 0; i < sensorValuesSum.length; i++) {
            summarizedData += attributes.get(i) + ": " + String.format("%.12f", sensorValuesSum[i] / (milisecondsToSample / 1000)) + "\n";
        }

        return summarizedData;
    }

}


