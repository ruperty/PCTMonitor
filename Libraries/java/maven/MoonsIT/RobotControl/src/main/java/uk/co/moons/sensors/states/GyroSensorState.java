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
public class GyroSensorState {

    private float gyro = 0;
    private boolean gyroThreadFlag = true;

    /** Creates a new instance of Sensors */
    public GyroSensorState() {
    }

    public synchronized float getGyro() {
        return gyro;
    }

    public synchronized void setGyro(float g) {
        gyro=g;
    }


    public synchronized boolean isGyroThreadFlag() {
        return gyroThreadFlag;
    }

    public synchronized void setgyroThreadFlag(boolean gyroThreadFlag) {
        this.gyroThreadFlag = gyroThreadFlag;
    }

}
