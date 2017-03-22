/* 
  *  This software is the property of Moon's Information Technology Ltd.
  * 
  *  All rights reserved.
  * 
  *  The software is only to be used for development and research purposes.
  *  Commercial use is only permitted under license or agreement.
  * 
  *  Copyright (C)  Moon's Information Technology Ltd.
  *  
  *  Author: rupert@moonsit.co.uk
  * 
  * 
 */
package uk.co.moonsit.utils.timing;

/**
 *
 * @author @version
 */
public class StopWatch extends Object {

    private long starttime;
    private long last;
    private long elapsed;
    private long sum=0;
    private int iterations;
    private int ctr;

    public StopWatch() {
        last = starttime = System.currentTimeMillis();
    }

    public StopWatch(int iterations, int ctr) {
        this.iterations = iterations;
        this.ctr = ctr;
        last = starttime = System.currentTimeMillis();
    }

    public Double lap() {
        Double duration = null;
        if (ctr % iterations == 0) {
            long now = System.currentTimeMillis();
            duration = (now - starttime) / (double) ctr;
            starttime = now;
            ctr = 0;
        }
        ctr++;
        return duration;
    }

    public Double durations() {
        Double duration = null;
        long now = System.currentTimeMillis();
        sum += now - last;
        if (ctr % iterations == 0) {
            duration = sum / (double) ctr;
            sum = 0;
            ctr = 0;
        }
        ctr++;
        return duration;
    }

    public double Stop() {
        long now = Now();
        long sinceLast = now - last;
        elapsed += sinceLast;
        last = now;
        return sinceLast / 1000;
    }

    public double Start() {
        last = Now();
        return last / 1000;
    }

    public void mark() {
        last = Now();
    }

    public long Now() {
        return System.currentTimeMillis();
    }

    public double Elapsed() {
        return elapsed / 1000;
    }

    public double Time() {
        return (Now() - starttime) / 1000;
    }

}
