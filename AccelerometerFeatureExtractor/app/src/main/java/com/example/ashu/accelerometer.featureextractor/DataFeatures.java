package com.example.ashu.accelerometer.featureextractor;


import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by ashu on 11/7/2016.
 */

// Feature vector creator class
class DataFeatures {

    private final String TAG = DataFeatures.class.getSimpleName();

    private Queue<ThreeData> points = new ArrayDeque<>();
    private final int DATA_LENGTH = 64;
    private final double[] VALUES = new double[12];
    private final String DIR = "collected";
    private final String FILE_NAME = "data.csv";

    // sliding windows for points
    void add(double x, double y, double z) {
        points.add(new ThreeData(x, y, z));

        if (points.size() > DATA_LENGTH + 1) {
            points.poll();
        }
    }


    public double[] getFeatures() {
        VALUES[0] = getAverageX();
        VALUES[1] = getAverageY();
        VALUES[2] = getAverageZ();
        VALUES[3] = getVarianceX();
        VALUES[4] = getVarianceY();
        VALUES[5] = getVarianceZ();
        VALUES[6] = getMagnitude();
        VALUES[7] = getRootMeanSquare();
        VALUES[8] = getSD("x");
        VALUES[9] = getSD("y");
        VALUES[10] = getSD("z");

        return VALUES;

    }


    private String getTimeStamp() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        return ts;
    }

    //standard deviation

    public double getSD(String axis) {
        double result = 0.0d;
        if (points.isEmpty()) {
            return 0;
        }
        if (axis.equalsIgnoreCase("x")) {
            result = Math.sqrt(getVarianceX());
            result = Math.round(result * 100. + 0.005) / 100.0;
            return result;
        }
        if (axis.equalsIgnoreCase("y")) {
            result = Math.sqrt(getVarianceY());
            result = Math.round(result * 100. + 0.005) / 100.0;
            return result;
        }
        if (axis.equalsIgnoreCase("z")) {
            result = Math.sqrt(getVarianceZ());
            result = Math.round(result * 100. + 0.005) / 100.0;
            return result;
        }
        return 0;
    }

    // return the magnitude of x y and z
    public double getMagnitude() {
        double x = 0.0d;
        double y = 0.0d;
        double z = 0.0d;
        double result = 0.0d;
        if (points.isEmpty()) {
            return 0;
        }
        for (ThreeData point : points) {
            x += point.x * point.x;
            y += point.y * point.y;
            z += point.z * point.z;
        }
        result = Math.sqrt(x + y + z) / points.size();
        result = Math.round(result * 100. + 0.005) / 100.0;
        return result;
    }

    // return the root mean square
    public double getRootMeanSquare() {
        double x = 0.0d;
        double y = 0.0d;
        double z = 0.0d;
        double result = 0.0d;
        if (points.isEmpty()) {
            return 0;
        }
        for (ThreeData point : points) {
            x += point.x * point.x;
            y += point.y * point.y;
            z += point.z * point.z;
        }

        result = Math.sqrt((x + y + z) / points.size());
        result = Math.round(result * 100. + 0.005) / 100.0;
        return result;
    }


    public double getZeroCrossings() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    public double getMedian(String axis) {
        double median = 0.0d;
        LinkedList<ThreeData> temp = (LinkedList<ThreeData>) points;

        if (!axis.equalsIgnoreCase("x") || !axis.equalsIgnoreCase("y") || !axis.equalsIgnoreCase("z")) {
            throw new IllegalArgumentException(axis);
        }
        if (axis.equalsIgnoreCase("x")) {
            Collections.sort(temp, new comparePointsX());
            if (points.size() % 2 == 0) {
                median = ((temp.get(points.size() / 2).x) + (temp.get(temp.size() / 2 - 1)).x) / 2;
            } else
                median = temp.get(temp.size() / 2).x;
        }

        if (axis.equalsIgnoreCase("y")) {
            Collections.sort(temp, new comparePointsY());
            if (points.size() % 2 == 0) {
                median = ((temp.get(points.size() / 2).y) + (temp.get(temp.size() / 2 - 1)).y) / 2;
            } else
                median = temp.get(temp.size() / 2).y;
        }

        if (axis.equalsIgnoreCase("z")) {
            Collections.sort(temp, new comparePointsZ());
            if (points.size() % 2 == 0) {
                median = ((temp.get(points.size() / 2).z) + (temp.get(temp.size() / 2 - 1)).z) / 2;
            } else
                median = temp.get(temp.size() / 2).z;
        }
        return median;
    }


    public double getAverageX() {
        double avgX = 0.0d;
        if (points.isEmpty())
            return 0;
        for (ThreeData point : points) {
            avgX += point.x;
        }
        double denominator = points.size();
        avgX = avgX / denominator;
        avgX = Math.round(avgX * 100. + 0.005) / 100.0;
        return avgX;
    }

    public double getAverageY() {
        double avgY = 0.0d;
        if (points.isEmpty())
            return 0;
        for (ThreeData point : points) {
            avgY += point.y;
        }
        double denominator = points.size();
        avgY = avgY / denominator;
        avgY = Math.round(avgY * 100. + 0.005) / 100.0;
        return avgY;
    }

    public double getAverageZ() {
        double avgZ = 0.0d;
        if (points.isEmpty())
            return 0;
        for (ThreeData point : points) {
            avgZ += point.z;
        }
        double denominator = points.size();
        avgZ = avgZ / denominator;
        avgZ = Math.round(avgZ * 100. + 0.005) / 100.0;
        return avgZ;
    }

    public double getVarianceX() {
        double xVar = 0.0d;

        if (points.isEmpty()) {
            return 0;
        }
        double avgX = getAverageX();

        for (ThreeData point : points) {
            xVar += (point.x - avgX) * (point.x - avgX);
        }

        xVar = xVar / points.size();
        xVar = Math.round(xVar * 1000000. + 0.0000005) / 1000000.0;
        return xVar;

    }

    public double getVarianceY() {
        double yVar = 0.0d;

        if (points.isEmpty()) {
            return 0;
        }
        double avgY = getAverageY();


        for (ThreeData point : points) {
            yVar += (point.y - avgY) * (point.y - avgY);

        }
        yVar = yVar / points.size();
        yVar = Math.round(yVar * 1000000. + 0.0000005) / 10000000.0;
        return yVar;

    }

    public double getVarianceZ() {
        double zVar = 0.0d;

        if (points.isEmpty()) {
            return 0;
        }
        double avgZ = getAverageZ();


        for (ThreeData point : points) {
            zVar += (point.z - avgZ) * (point.z - avgZ);

        }
        zVar = zVar / points.size();
        zVar = Math.round(zVar * 1000000. + 0.0000005) / 1000000.0;
        return zVar;

    }

    public double getCorrelationXY() {
        double cor = 0.0d;
        for (ThreeData point : points) {
            cor += ((point.x - getAverageX()) / getSD("x")) * ((point.y - getAverageY()) / getSD("y"));
        }
        cor = cor / (points.size() - 1);
        cor = Math.round(cor * 1000. + 0.0005) / 1000.0;
        return cor;
    }

    public double getCorrelationXZ() {
        double cor = 0.0d;
        for (ThreeData point : points) {
            cor += ((point.x - getAverageX()) / getSD("x")) * ((point.z - getAverageZ()) / getSD("z"));
        }
        cor = cor / (points.size() - 1);
        cor = Math.round(cor * 1000. + 0.0005) / 1000.0;
        return cor;
    }

    public double getCorrelationYZ() {
        double cor = 0.0d;
        for (ThreeData point : points) {
            cor += ((point.y - getAverageY()) / getSD("y")) * ((point.z - getAverageZ()) / getSD("z"));
        }
        cor = cor / (points.size() - 1);
        cor = Math.round(cor * 1000. + 0.0005) / 1000.0;
        return cor;
    }


    public double getAverage() {
        return ((getAverageX() + getAverageY() + getAverageZ()) / 3.0);
    }

    public double getVariance() {
        double xVar = 0.0d;
        double yVar = 0.0d;
        double zVar = 0.0d;
        if (points.isEmpty()) {
            return 0;
        }
        for (ThreeData point : points) {
            xVar += (point.x - getAverage()) * (point.x - getAverage());
            yVar += (point.y - getAverage()) * (point.y - getAverage());
            zVar += (point.z - getAverage()) * (point.z - getAverage());
        }
        return ((xVar + yVar + zVar) / points.size());
    }

    public void writeData(int label) throws NullPointerException {
        String seperator = System.lineSeparator();
        try {
            File directory = new File(Environment.getExternalStorageDirectory(), DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File gpxfile = new File(directory, FILE_NAME);
            if (!gpxfile.exists()) {
                gpxfile.createNewFile();
            }
            FileWriter writer = new FileWriter(gpxfile, true);

            writer.append(Double.toString(getFeatures()[0]));
            writer.append(",");
            writer.append(Double.toString(getFeatures()[1]));
            writer.append(",");
            writer.append(Double.toString(getFeatures()[2]));
            writer.append(",");
            writer.append(Double.toString(getFeatures()[3]));
            writer.append(",");
            writer.append(Double.toString(getFeatures()[4]));
            writer.append(",");
            writer.append(Double.toString(getFeatures()[5]));
            writer.append(",");
            writer.append(Double.toString(getFeatures()[6]));
            writer.append(",");
            writer.append(Double.toString(getFeatures()[7]));
            writer.append(",");
            writer.append(Double.toString(getFeatures()[8]));
            writer.append(",");
            writer.append(Double.toString(getFeatures()[9]));
            writer.append(",");
            writer.append(Double.toString(getFeatures()[10]));
            writer.append(",");
            writer.append(Integer.toString(label));
            writer.append(seperator);
            writer.flush();
            writer.close();
            Log.e(TAG, "file saved");

            // Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

