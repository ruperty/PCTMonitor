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

/**
 *
 * @author ReStart
 */
public interface SensorStateInterface {

    public double[] getValues();

    public double getValue();

    public double getValue(int i);

    public void setValues(double[] value);

    public void setValue(double value);

    public boolean isSensorActive();

    public void setSensorActive(boolean a);
}
