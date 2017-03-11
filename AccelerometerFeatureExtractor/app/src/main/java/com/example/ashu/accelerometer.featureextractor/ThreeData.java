package com.example.ashu.accelerometer.featureextractor;

import java.util.Comparator;

/**
 * Created by ashu on 11/7/2016.
 */

// class to have coordinates of accelerometer as points
class ThreeData {
    double x;
    double y;
    double z;

    ThreeData(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

// sorts the the list according to the value of x
class comparePointsX implements Comparator<ThreeData> {

    @Override
    public int compare(ThreeData p1, ThreeData p2) {
        if (p1.x < p2.x) {
            return 1;
        }
        return -1;
    }

}

// sorts the the list according to the value of y
class comparePointsY implements Comparator<ThreeData> {

    @Override
    public int compare(ThreeData p1, ThreeData p2) {
        if (p1.y < p2.y) {
            return 1;
        }
        return -1;
    }

}

// sorts the the list according to the value of z
class comparePointsZ implements Comparator<ThreeData> {

    @Override
    public int compare(ThreeData p1, ThreeData p2) {
        if (p1.z < p2.z) {
            return 1;
        }
        return -1;
    }

}
