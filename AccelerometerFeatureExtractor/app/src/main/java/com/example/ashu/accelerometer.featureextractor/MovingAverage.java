package com.example.ashu.accelerometer.featureextractor;

import java.util.ArrayDeque;
import java.util.Queue;


/**
 * Created by ashu on 11/7/2016.
 */

public class MovingAverage {
    private final Queue<Double> windowLength = new ArrayDeque<>();
    private int period;
    private double avg = 0.0d;

    MovingAverage(){
        period = 7;
    }

    MovingAverage(int period){
        this.period = period;
    }

    public void add(double dataPoint){
        avg += dataPoint;
        windowLength.add(dataPoint);
        if(windowLength.size() > period){
            avg -= windowLength.remove();
        }
    }

    public double filter(){
        double average = 0.0d;
         if(windowLength.isEmpty())
             return 0;
         double denominator = windowLength.size();
         average = avg/denominator;
         average = Math.round(average * 100. + 0.005) / 100.0;
         return average;
    }

}
