package com.example.ashu.accelerometer.featureextractor;

import android.os.Handler;

/**
 * Created by ashu on 11/23/2016.
 */
class ScheduleTask {
    //run period initialised to 5secs by default
    private int runPeriod = 5000;

    //sleep period initialised to 2 secs by default
    private int sleepPeriod = 2000;

    // handler to schedule task
    private Handler scheduler = new Handler();

    private int flag = 1;

    private Runnable p1;

    private Runnable p2;

    void setMethods(Runnable p1, Runnable p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    // a runnable to alternate p1 and p2
    private Runnable sch = new Runnable() {
        @Override
        public void run() {
            try {
                if (flag == 0) {
                    // method to stop
                    p1.run();
                    flag = 1;
                } else {
                    // method to run
                    p2.run();
                    flag = 0;
                }
            } finally {
                if (flag == 0) {
                    scheduler.postDelayed(sch, runPeriod);
                } else {
                    scheduler.postDelayed(sch, sleepPeriod);
                }
            }
        }
    };

    // change the run period
    public void setRunPeriod(int runPeriod) {
        this.runPeriod = runPeriod;
    }

    // change the sleep period
    public void setSleepPeriod(int sleepPeriod) {
        this.sleepPeriod = sleepPeriod;
    }

    void start() {
        sch.run();
    }

    void stop() {
        scheduler.removeCallbacks(sch);
    }


}
