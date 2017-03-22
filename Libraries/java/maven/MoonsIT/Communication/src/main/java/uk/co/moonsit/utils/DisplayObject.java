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

//import java.util.logging.Logger;
public class DisplayObject {
    //private static final Logger logger = Logger.getLogger(DisplayObject.class.getName());

    float[] values;
    private final long time;
    private int size = 0;
    private boolean finished = false;
    private int displayType = 0;
    private boolean dataStarted = false;

        private boolean receivedConfig = false;
    private boolean configChanged = false;

    public DisplayObject() {
        time = System.currentTimeMillis();

        //DataLogging.getInstance().init(datalogs, true, LogColumn.DT_FLOAT);
    }

    public boolean isReceivedConfig() {
        return receivedConfig;
    }

    public void setReceivedConfig(boolean receivedConfig) {
        this.receivedConfig = receivedConfig;
    }

    public boolean isConfigChanged() {
        return configChanged;
    }

    public void setConfigChanged(boolean configChanged) {
        this.configChanged = configChanged;
    }

    public boolean isDataStarted() {
        return dataStarted;
    }

    public void setDataStarted(boolean dataStarted) {
        this.dataStarted = dataStarted;
    }

    public void createArray(int s) {
        dataStarted = false;
        size = s;
        values = new float[size];
        for (int i = 0; i < size; i++) {
            values[i] = 0;
        }
    }

    public int getSize() {
        return size;
    }

    public synchronized float[] getValues() {
        //print("GV: ");

        return values;
    }

    public synchronized float getValue(int i) {
        return values[i];
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    /*
     public synchronized void setValue(int i, float a) {
     values[i] = a;
     }*/
    public synchronized void setValues(float[] vs) {
        long elapsed = System.currentTimeMillis() - time;
        values[0] = elapsed;
        System.arraycopy(vs, 0, values, 1, vs.length);
        dataStarted=true;
    }

    public synchronized void setValues(double[] vs) {
        long elapsed = System.currentTimeMillis() - time;
        values[0] = elapsed;
        for (int i = 0; i < vs.length; i++) {
            values[i + 1] = (float) vs[i];
        }
        dataStarted=true;
    }

    public synchronized void setValues(long elapsed, double[] vs) {

        values[0] = elapsed;
        for (int i = 0; i < vs.length; i++) {
            values[i + 1] = (float) vs[i];
        }
        dataStarted=true;
    }

    public synchronized void setValues(long elapsed, float[] vs) {

        values[0] = elapsed;
        System.arraycopy(vs, 0, values, 1, vs.length);
        //print("SV: ");
        dataStarted=true;
    }

    private void print(String st) {
        System.out.print(st);
        for (int i = 0; i < values.length; i++) {
            System.out.print(values[i]+" ");
        }
        System.out.println();
    }

    public synchronized int size() {
        return values.length;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

}
