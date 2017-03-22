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
import uk.co.moons.sensors.impl.EV3IRSensorImpl;
import uk.co.moons.sensors.states.BaseSensorState;

/**
 *
 * @author Rupert Young
 */
public class EV3IRSensor extends BaseSensor {

    static final Logger logger = Logger.getLogger(EV3IRSensor.class.getName());
    //private Long interval = null;
    //private final EV3IRSensorImpl sensorImpl;

    /**
     * Creates a new instance of IRSensor
     *
     * @param sp
     * @param st
     * @param mode
     * @param channel
     * @param type
     * @param i
     *
     */
    public EV3IRSensor(String sp, BaseSensorState st, String mode, String type, int channel, Long i) {
        interval = i;
        state = st;
        sensorImpl = new EV3IRSensorImpl(sp, mode, channel, type);
    }

    @Override
    public void close() {
        sensorImpl.close();
        logger.info("+++ EV3IRSensor closed");
    }

    public void setMode(String mode) {
        ((EV3IRSensorImpl) sensorImpl).setMode(mode);
    }

    public void setType(String t) {
        ((EV3IRSensorImpl) sensorImpl).setType(t);

    }

    public void setChannel(int c) {
        ((EV3IRSensorImpl) sensorImpl).setChannel(c);
    }

}
