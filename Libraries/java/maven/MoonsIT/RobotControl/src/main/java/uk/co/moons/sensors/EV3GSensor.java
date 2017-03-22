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
package uk.co.moons.sensors;

import java.util.logging.Logger;
import uk.co.moons.sensors.impl.EV3GyroSensorImpl;
import uk.co.moons.sensors.states.BaseSensorState;

/**
 *
 * @author Rupert Young
 */
public class EV3GSensor extends BaseSensor {

    static final Logger logger = Logger.getLogger(EV3GSensor.class.getName());

    
    /**
     * Creates a new instance of USensor
     *
     * @param sp
     * @param st
     * @param mode
     *
     */
    public EV3GSensor(String sp, BaseSensorState st, String mode) {
        state = st;
        sensorImpl = new EV3GyroSensorImpl(sp, mode);
        ((EV3GyroSensorImpl)sensorImpl).setMode(mode);
    }

    public void setMode(String m) {
        ((EV3GyroSensorImpl)sensorImpl).setMode(m);
    }

}
