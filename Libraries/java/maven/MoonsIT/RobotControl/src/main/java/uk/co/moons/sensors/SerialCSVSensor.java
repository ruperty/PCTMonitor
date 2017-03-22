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

import java.util.TooManyListenersException;
import java.util.logging.Logger;
import uk.co.moons.sensors.impl.SerialCSVSensorImpl;
import uk.co.moons.sensors.states.BaseSensorState;

/**
 *
 * @author Rupert Young
 */
public class SerialCSVSensor extends BaseSensor {

    static final Logger logger = Logger.getLogger(SerialCSVSensor.class.getName());

    private boolean update = false;

    /**
     * Creates a new instance of USensor
     *
     * @param comPort
     * @param st
     * @param timeout
     * @param datarate
     * @param index
     * @param update
     * @throws java.util.TooManyListenersException
     */
    public SerialCSVSensor(String comPort, BaseSensorState st, int timeout, int datarate, int index, boolean update) throws TooManyListenersException, Exception {
        state = st;
        this.update = update;
        sensorImpl = new SerialCSVSensorImpl(comPort, timeout, datarate, index);
    }

    @Override
    public double getValue() {
        if (update) {
            ((SerialCSVSensorImpl) sensorImpl).update();
        }
        return sensorImpl.getValue();
    }

}
