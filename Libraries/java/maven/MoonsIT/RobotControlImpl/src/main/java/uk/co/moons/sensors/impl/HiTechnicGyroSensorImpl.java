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
public class HiTechnicGyroSensorImpl extends BaseSensorImpl {

    private boolean raw = false;

    public HiTechnicGyroSensorImpl(String port, boolean r, int s) {

    }

   
    public void setOffset(int offset) {
    }

    public void getGyroOffset() {

    }

   

    public void warningBeeps() {
    }

    public boolean isRaw() {
        return raw;
    }

    public void setRaw(boolean raw) {
        this.raw = raw;
    }
}
