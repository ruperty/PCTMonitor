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
package uk.co.moonsit.sensors;

import java.util.ArrayList;
import java.util.List;

public class SensorData extends ArrayList<Float> {

    private static final String TAG = "SensorData";
    public static final int TYPE_ACCELEROMETER = 1;
    public static final int TYPE_GRAVITY = 9;
    public static final int TYPE_LIGHT = 5;
    public static final int TYPE_MAGNETIC_FIELD = 2;
    public static final int TYPE_PROXIMITY = 8;
    public static final int TYPE_ROTATION_VECTOR = 11;
    public static final int TYPE_LINEAR_ACCELERATION = 10;
    private static final long serialVersionUID = 1L;
    private Integer type = null;
    private String delimiter = "|";
    private String format = "%.3f";
    private Float smoothness = null;
    private Long timestamp = null;

    public SensorData(float smoothness) {

        this.smoothness = smoothness;
    }

    public SensorData(int t) {
        type = t;
    }

    public SensorData(int t, Float x, Float y, Float z) {
        type = t;
        this.add(x);
        this.add(y);
        this.add(z);
        timestamp = System.currentTimeMillis();
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void smooth(SensorData that) {
        if (type == null) {
            type = that.getType();
        }
        if (this.isEmpty()) {
            for (int i = 0; i < that.size(); i++) {
                this.add(that.get(i));
            }
        } else {
            for (int i = 0; i < that.size(); i++) {
                Float oldVal = this.get(i);
                Float newVal = that.get(i);
                this.set(i, newVal * smoothness + oldVal * (1 - smoothness));
            }
        }
    }

    public void smooth(Float value, int index) {

        if (this.isEmpty()) {
            int ind = index + 1;
            for (int i = 0; i < ind; i++) {
                this.add(value);
            }
        } else {
            Float oldVal = this.get(index);
            Float newVal = value;
            this.set(index, newVal * smoothness + oldVal * (1 - smoothness));
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(getType() + delimiter);
        for (Float f : this) {
            sb.append(String.format(format, f));
            sb.append(delimiter);

        }
        sb.append(timestamp);
        return sb.toString();
    }

    public String toString(List<Float> list) {
        StringBuilder sb = new StringBuilder();

        sb.append(getType() + delimiter);
        for (Float d : list) {
            sb.append(String.format(format, d));
            sb.append(delimiter);
        }
        sb.append(timestamp);
        return sb.toString();
    }

    /*
    public String getStringType() {
    String stType = null;
    
    if (type == SensorData.TYPE_LINEAR_ACCELERATION) {
    stType = "LinAcc";
    }
    
    if (type == SensorData.TYPE_ACCELEROMETER) {
    stType = "Acc";
    }
    
    return stType;
    }*/
    public static Integer getType(String st) {


        if (st.equals("TYPE_ACCELEROMETER")) {
            return 1;
        }
        if (st.equals("TYPE_GRAVITY")) {
            return 9;
        }
        if (st.equals("TYPE_LIGHT")) {
            return 5;
        }
        if (st.equals("TYPE_MAGNETIC_FIELD")) {
            return 2;
        }
        if (st.equals("TYPE_PROXIMITY")) {
            return 8;
        }
        if (st.equals("TYPE_ROTATION_VECTOR")) {
            return 11;
        }
        if (st.equals("TYPE_LINEAR_ACCELERATION")) {
            return 10;
        }

        return -1;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
