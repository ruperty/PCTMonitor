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
package uk.co.moonsit.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rupert Young Moon's Information Technology
 *
 */
public class DebugTimes {

    private static DebugTimes instance = null;
    private Long start = null;
    private final int size = 10000;
    private final List<TimeList> list;
    private int ctr = 0;
    private final boolean debug = false;

    protected DebugTimes() {
        // Exists only to defeat instantiation.
        list = new ArrayList<>();
    }

    public static DebugTimes getInstance() {
        if (instance == null) {
            instance = new DebugTimes();
        }
        return instance;
    }

    public void mark(String msg) {
        if (debug) {
            if (ctr == 0) {
                start = System.currentTimeMillis();
            }
            if (ctr >= 0) {
                if (ctr++ > size) {
                    dumpList();
                    ctr = -1;
                } else {
                    TimeList tl = new TimeList(msg, System.currentTimeMillis() - start);
                    list.add(tl);
                }
            }
        }
    }

    private void dumpList() {
        long prev = 0;
        for (TimeList t : list) {
            System.out.println(t.getTime() + " " + (t.getTime()-prev)  + " "+ t.getMsg());
            prev = t.getTime();
        }
    }

    private class TimeList {

        private long time;
        private String msg;

        public TimeList(String m, long t) {
            msg = m;
            time = t;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

    }

}
