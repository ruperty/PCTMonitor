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
package uk.co.moons.sensors.states;

import java.util.logging.Logger;

/**
 *
 * @author Rupert Young Moon's Information Technology
 *
 */
public class BaseSensorState implements SensorStateInterface {
    private static final Logger logger = Logger.getLogger(BaseSensorState.class.getName());

    private double[] values;
    private boolean active = true;

    public BaseSensorState() {
        values = new double[1];
    }

    public BaseSensorState(int size) {
        values = new double[size];
    }

    public void setSize(int size) {
        values = new double[size];
    }

    @Override
    public synchronized double[] getValues() {
        return values;
    }

    @Override
    public synchronized void setValues(double[] data) {
        System.arraycopy(data, 0, values, 0, data.length);
    }

    @Override
    public boolean isSensorActive() {
        return active;
    }

    @Override
    public void setSensorActive(boolean a) {
        active = a;
    }

    @Override
    public void setValue(double value) {
        values[0] = value;
    }

    @Override
    public double getValue() {
        return values[0];
    }

    @Override
    public synchronized double getValue(int i) {
        return values[i];
    }

    public synchronized void setValues(float[] data) {
        //logger.info("data size "+data.length + " vals size "+values.length);
        
        for (int i = 0; i < data.length; i++) {
            values[i] = data[i];
        }
    }
}
