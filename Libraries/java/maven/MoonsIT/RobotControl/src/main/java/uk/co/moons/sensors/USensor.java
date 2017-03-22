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
import uk.co.moons.sensors.impl.UltrasonicSensorImpl;
import uk.co.moons.sensors.states.BaseSensorState;

/**
 *
 * @author Rupert Young
 */
public class USensor extends BaseSensor {

    static final Logger logger = Logger.getLogger(USensor.class.getName());

    //private final UltrasonicSensorImpl sensorImpl;
    //private final BaseSensorState state;
    /**
     * Creates a new instance of USensor
     */
    /**
     * Creates a new instance of USensor
     *
     * @param sp
     * @param st
     * @param i
     *
     */
    public USensor(String sp, BaseSensorState st, long i) {
        interval = i;
        state = st;
        sensorImpl = new UltrasonicSensorImpl(sp);
    }

   

}
