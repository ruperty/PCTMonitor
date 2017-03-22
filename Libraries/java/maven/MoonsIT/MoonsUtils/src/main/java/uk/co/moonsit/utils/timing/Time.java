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

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 *
 * @author ReStart
 */
public class Time extends Object {

    int hours;
    int minutes;
    double seconds;
    double dTime;
    private final DecimalFormat dfFormat2 = new DecimalFormat("00.000");
    private final DecimalFormat dfInt = new DecimalFormat("00");

    public Time() {
        hours = 0;
        minutes = 0;
        seconds = 0;
        dTime = getDoubleValue();
    }

    public Time(double t) {
        double fracm, minsd;
        dTime = t;

        hours = (int) Math.floor(t);
        fracm = t - hours;

        minsd = fracm * 60;
        minutes = (int) Math.floor(minsd);
        if (minutes >= 60) {
            hours += 1;
            minutes = 0;
        }
        seconds = 60 * (minsd - minutes);
        //seconds = Math.round(60 * (minsd - minutes));
        if (seconds >= 60) {
            minutes += 1;
            seconds = 0;
        }
        //System.out.println(toString() + " " + t);
    }

    public Time(int h, int m, double s) {
        hours = h;
        minutes = m;
        seconds = s;
        dTime = getDoubleValue();
    }

    public Time(String t) {
        String time;
        if (t.length() > 12) {
            time = t.substring(11);
        } else {
            time = t;
        }
        hours = new Integer(time.substring(0, 2));

        minutes = new Integer(time.substring(3, 5));

        Double s;
        if (time.length() == 8) {
            s = Double.parseDouble(time.substring(6, 8));
        } else {
            s = Double.parseDouble(time.substring(6, 12));
        }
        seconds = s;//.doubleValue();
        dTime = getDoubleValue();
    }

    public double getTime() {
        return dTime;
    }

    private double getDoubleValue() {
        return hours + minutes / 60.0 + seconds / 3600.0;
    }

    public int compareTo(Time t) {
        double thisTime = secondsSinceMidnight();
        double thatTime = t.secondsSinceMidnight();

        if (thisTime < thatTime) {
            return -1;
        }
        if (thisTime > thatTime) {
            return 1;
        }
        return 0;
    }

    public int secondsSinceMidnight() {
        return hours * 3600 + minutes * 60 + (int) Math.floor(seconds);
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public String toString() {

        Integer h = hours;
        Integer m = new Integer(minutes);
        String strSecs = dfFormat2.format(seconds);

        String hspace = "";
        String minspace = ":";
        String sspace = ":";

        if (hours < 10) {
            hspace = "0";
        }
        if (minutes < 10) {
            minspace = ":0";
        }
        String time = hspace + h.toString() + minspace + m.toString() + sspace + strSecs;
        //System.out.println(time);

        return time;
    }

    public String HMS() {
        String strSecs = dfInt.format(seconds);
        String strH = dfInt.format(hours);
        String strM = dfInt.format(minutes);

        String time = strH + strM + strSecs;

        return time;
    }

    public String HMS(boolean b) {
        String strSecs = dfInt.format(seconds);
        String strH = dfInt.format(hours);
        String strM = dfInt.format(minutes);

        String time = strH + ":" + strM + ":" + strSecs;

        return time;
    }

    public String HMSS() {
        String strSecs = String.format("%06.3f", seconds);
        String strH = dfInt.format(hours);
        String strM = dfInt.format(minutes);

        String time = strH + ":" + strM + ":" + strSecs;

        return time;
    }

    public String HMSS(String delimiter) {
        String strSecs = String.format("%06.3f", seconds);
        String strH = dfInt.format(hours);
        String strM = dfInt.format(minutes);

        String time = strH + delimiter + strM + delimiter+ strSecs;

        return time;
    }
    
    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 10; i++) {
            Calendar rightNow = Calendar.getInstance();
            System.out.println(rightNow.getTimeInMillis());
            System.out.println(System.currentTimeMillis());
 Thread.sleep(2);
            //DateAndTime dat = new DateAndTime();
            //String t = dat.HMSS();
            //System.out.println(t);
        }

        /*
         String st = null;
         {
         Time t = new Time(0.01 / 3600);
         st = t.HMSS();
         System.out.println(st);
         System.out.println(t.toString());
         }
         {
         Time t1 = new Time(st);
         Double  s = new Double(st.substring(6, 12));
         System.out.println(s);
         Time t = new Time(t1.getTime());

         System.out.println(t.HMSS());
         System.out.println(t.toString());
         }*/
    }
}
