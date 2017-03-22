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
import uk.co.moons.sensors.impl.MindsensorsAbsoluteIMUSensorImpl;
import uk.co.moons.sensors.states.BaseSensorState;

/**
 *
 * @author Rupert Young
 */
public class MindsensorsAbsoluteIMUSensor extends BaseSensor {

    static final Logger logger = Logger.getLogger(MindsensorsAbsoluteIMUSensor.class.getName());
    //private final MindsensorsAbsoluteIMUSensorImpl sensor;

    /**
     * Creates a new instance of MindsensorsAbsoluteIMUSensor
     *
     * @param sp
     * @param st
     * @param mode
     * @param i
     *
     */
    
    public MindsensorsAbsoluteIMUSensor(String sp, BaseSensorState st, String mode, Long i) {
        interval = i;
        state = st;
        sensorImpl = new MindsensorsAbsoluteIMUSensorImpl(sp, mode);
    }

   
  

    
}
