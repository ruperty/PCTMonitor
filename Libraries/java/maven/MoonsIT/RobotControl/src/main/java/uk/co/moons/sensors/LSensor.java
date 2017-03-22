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
import uk.co.moons.sensors.impl.LightSensorImpl;
import uk.co.moons.sensors.states.BaseSensorState;

/**
 *
 * @author Rupert Young
 */
public class LSensor extends BaseSensor {

    static final Logger logger = Logger.getLogger(LSensor.class.getName());
   
    //private final LightSensorImpl sensorImpl;
   
  
    /**
     * Creates a new instance of USensor
     * @param spst
     * @param st
     * @param floodlight
     */
    public LSensor(String spst, BaseSensorState st, boolean floodlight) {
        state = st;
        sensorImpl = new LightSensorImpl(spst, floodlight);
       
    }

   

    

/*
   @Override
    public double getValue() {
        return 100*sensorImpl.getValue();
    }
*/
    public void setFloodLight(int flood) {
         ((LightSensorImpl)sensorImpl).setFloodLight(flood);
    }

   
}
