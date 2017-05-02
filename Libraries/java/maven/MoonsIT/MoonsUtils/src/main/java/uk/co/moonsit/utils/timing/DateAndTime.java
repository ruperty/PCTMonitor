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

import java.util.*;
import java.io.*;

/**
 *
 * @author Rupert Young
 * @version
 */
public class DateAndTime implements DateAndTimeInterface {

    int timeDiff = 0;
    Calendar rightNow;
    TimeZone tzone;
    Time time;
    int minutesOffset;

    public DateAndTime() {
        tzone = TimeZone.getDefault();
        pNow();
    }

    public DateAndTime(String zoneId) {
        tzone = TimeZone.getTimeZone(zoneId);
        System.out.println(tzone);
        pNow();
    }

    public DateAndTime(int td) {
        timeDiff = td;
        tzone = TimeZone.getTimeZone("GMT");
        //System.out.println(tzone);
        pNow();
    }

    public DateAndTime(TimeZone tz) {
        //System.out.println("c 1");
        tzone = tz;

        pNow();
    }

    public void SetMinutesOffset(int off) {
        minutesOffset = off;
    }

    @Override
    public void Now() {
        pNow();
    }

    private void pNow() {
        rightNow = Calendar.getInstance();
        rightNow.setTimeZone(tzone);
        int mins = Minutes() + minutesOffset;
        int hour = HourOfDay();
        if (mins < 0) {
            mins += 60;
            hour -= 1;
        }
        time = new Time(hour, mins, Seconds() + MilliSeconds() / 1000.0);
    }

    @Override
    public File DateFileName(String name) {
        File file = new File(YMD() + name + ".dat");

        return file;
    }

    @Override
    public String YMD() {
        //System.out.println("YMD");
        Integer year = rightNow.get(Calendar.YEAR);
        Integer month = rightNow.get(Calendar.MONTH) + 1;
        Integer day = rightNow.get(Calendar.DAY_OF_MONTH);

        //System.out.println(month);
        String mspace = "";
        String dspace = "";
        if (month < 10) {
            mspace = "0";
        }

        if (day < 10) {
            dspace = "0";
        }

        String date = year.toString() + mspace + month.toString() + dspace + day.toString();
        //System.out.println(date);
        return date;
    }

    @Override
    public String HMS() {
        return time.HMS();//.toString();
    }

    public String HMS(boolean b) {
        return time.HMS(b);//.toString();
    }

    public String HMSS() {
        return time.HMSS();//.toString();
    }

    public String HMSS(String delim) {
        return time.HMSS(delim);//.toString();
    }

    public Time Time() {
        return time;
    }

    public Integer HourOfDayInteger() {

        return new Integer(HourOfDay());
    }

    public int HourOfDay() {
        //System.out.println("Hour "+rightNow.get(Calendar.HOUR_OF_DAY));
        int hour = rightNow.get(Calendar.HOUR_OF_DAY) + timeDiff;
        //System.out.println("Hour "+hour);
        if (hour < 0) {
            hour += 24;
        }

        if (hour > 24) {
            hour -= 24;
        }

        return hour;
    }

    public Integer MinutesInteger() {
        return rightNow.get(Calendar.MINUTE);
    }

    public int Minutes() {
        Integer minutes = rightNow.get(Calendar.MINUTE);
        return minutes;
    }

    public Integer SecondsInteger() {
        return rightNow.get(Calendar.SECOND);
    }

    public int Seconds() {
        Integer seconds = rightNow.get(Calendar.SECOND);
        return seconds;
    }

    public int MilliSeconds() {
        Integer seconds = rightNow.get(Calendar.MILLISECOND);
        return seconds;
    }

    public int DayOfWeek() {
        return rightNow.get(Calendar.DAY_OF_WEEK);
    }
}
