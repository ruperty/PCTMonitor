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
package uk.co.moons.sensors.impl;

/**
 *
 * @author ReStart
 */
public abstract class BaseSensorImpl implements SensorImplInterface {

    protected void init() {
    }

    @Override
    public double getValue(int index) {
        return 0;
    }

    @Override
    public double getValue() {
        return 0;
    }

    
    public float [] getData() {
        float []d =null;
        return d;
    }
    @Override
    public void close() {
        throw new UnsupportedOperationException(" nyi");
    }
}
