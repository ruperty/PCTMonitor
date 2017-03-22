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
import uk.co.moons.sensors.impl.EV3UltrasonicSensorImpl;
import uk.co.moons.sensors.states.BaseSensorState;

/**
 *
 * @author Rupert Young
 */
public class EV3USensor extends BaseSensor {

    static final Logger logger = Logger.getLogger(EV3USensor.class.getName());

    //private final EV3UltrasonicSensorImpl sensorImpl;
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
    public EV3USensor(String sp, BaseSensorState st, long i, String mode) {
        interval = i;
        state = st;
        sensorImpl = new EV3UltrasonicSensorImpl(sp, mode);
    }

    @Override
    public double getValue() {
        return 100 * sensorImpl.getValue();
    }

    
    public void setMode(String m) {
        ((EV3UltrasonicSensorImpl)sensorImpl).setMode(m);
    }
}
