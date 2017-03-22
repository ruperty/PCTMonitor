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
 * @author Rupert Young
 */
public class LightSensorState {

    private int light = 0;
    private boolean lightThreadFlag = true;

    /** Creates a new instance of Sensors */
    public LightSensorState() {
    }

    public synchronized int getLight() {
        return light;
    }

    public synchronized void setLight(int s) {
        light = s;
    }

    public synchronized boolean getLightFlag() {
        return lightThreadFlag;
    }

    public synchronized void setLightFlag(boolean lightThreadFlag) {
        this.lightThreadFlag = lightThreadFlag;
    }

}
