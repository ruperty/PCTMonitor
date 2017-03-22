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
public class EV3IRSensorImpl extends BaseSensorImpl {

  

    public EV3IRSensorImpl(String port, String m, int c, String t) {
        super.init();
    }

   

    public void setMode(String mode) {
        throw new UnsupportedOperationException("IRSensorImpl nyi");
    }

   

    public void setType(String t) {
        throw new UnsupportedOperationException("IRSensorImpl nyi");
    }

    public void setChannel(int c) {
        throw new UnsupportedOperationException("IRSensorImpl nyi");
    }
}
