package com.example.ashu.accelerometer.featureextractor;
 /* Created by ashu on 11/7/2016.
 */


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ashu.accelerometerfeatureextractor.R;


public class FeatureActivity extends AppCompatActivity implements SensorEventListener {

    /* All the variables are defined here */
    private final int WALK = 0;
    private final int WALK_UPSTAIR = 1;
    private final int WALK_DOWNSTAIR = 2;
    private final int RUN = 3;
    private final int SIT = 4;
    private final int DRIVE = 5;


    private Sensor mySensor;
    private SensorManager SM;
    private MovingAverage SMA_x;
    private MovingAverage SMA_y;
    private MovingAverage SMA_z;
    private DataFeatures features;
    private int startProcess = 0;

    // radio group
    private RadioGroup labels;
    ScheduleTask sche;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature);

        // Sensor manager
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        // get the label radio group
        labels = (RadioGroup) findViewById(R.id.labels);

        // Low pass filters to stabilise the readings
        SMA_x = new MovingAverage();
        SMA_y = new MovingAverage();
        SMA_z = new MovingAverage();

        // create Data features class
        features = new DataFeatures();

        //task scheduler
        sche = new ScheduleTask();

        Runnable stopAcc = new Thread(new Runnable() {
            @Override
            public void run() {
                // Moves the current Thread into the background to avoid resources competition between UI thread and background threads
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

                stopAcc();
            }
        });

        Runnable startAcc = new Thread(new Runnable() {
            @Override
            public void run() {
                // Moves the current Thread into the background to avoid resources competition between UI thread and background threads
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

                startAcc();
            }
        });

        sche.setMethods(stopAcc, startAcc);

    }


    // event linked to collect data button
    public void collectData(View view) {

        if (labels.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getBaseContext(), "Select an activity ", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getBaseContext(), "Activity started", Toast.LENGTH_LONG).show();
            //  startCollecting();
            sche.start();
            startProcess = 1;

        }
    }

    public void stopD(View view) {
        if (startProcess == 0) {
            Toast.makeText(getBaseContext(), "No activity to stop", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getBaseContext(), "Activity stopped", Toast.LENGTH_LONG).show();
            startProcess = 0;
            sche.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // stopCollecting();
        sche.stop();
    }


    // method to start accelerator
    public void startAcc() {
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_FASTEST);
        // flag = 0;
        if (startProcess == 1) {
            int selectedID = labels.getCheckedRadioButtonId();
            switch (selectedID) {
                case R.id.walk:
                    features.writeData(WALK);
                    break;
                case R.id.walkUpstair:
                    features.writeData(WALK_UPSTAIR);
                    break;
                case R.id.walkDownstair:
                    features.writeData(WALK_DOWNSTAIR);
                    break;
                case R.id.run:
                    features.writeData(RUN);
                    break;
                case R.id.sit:
                    features.writeData(SIT);
                    break;
            }
        }
    }

    // method to stop accelerator and write data
    public void stopAcc() {

        // flag = 1;
        SM.unregisterListener(this);

    }

    // Register Sensor listener when app starts
    @Override
    protected void onResume() {
        super.onResume();
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing to do right now
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        SMA_x.add(event.values[0]);
        SMA_y.add(event.values[1]);
        SMA_z.add(event.values[2]);

        //filters to stabilise the values
        double x = SMA_x.filter();
        double y = SMA_y.filter();
        double z = SMA_z.filter();

        // add values to feature object of Data features class
        features.add(x, y, z);
    }
}


