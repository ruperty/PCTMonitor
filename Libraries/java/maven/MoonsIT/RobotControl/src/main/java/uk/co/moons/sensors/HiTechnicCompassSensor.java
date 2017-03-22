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
import uk.co.moons.sensors.impl.HiTechnicCompassSensorImpl;
import uk.co.moons.sensors.states.BaseSensorState;

/**
 *
 * @author Rupert Young
 */
public class HiTechnicCompassSensor extends BaseSensor {

    static final Logger logger = Logger.getLogger(HiTechnicCompassSensor.class.getName());
    //private final HiTechnicCompassSensorImpl sensorImpl;

    /**
     * Creates a new instance of HiTechnicCompassSensor
     *
     * @param sp
     * @param st
     * @param mode
     * @param i
     *
     */
    public HiTechnicCompassSensor(String sp, BaseSensorState st, String mode, Long i) {
        interval = i;
        state = st;
        sensorImpl = new HiTechnicCompassSensorImpl(sp, mode);
    }

    

    @Override
    public double getValue() {
        return sensorImpl.getValue();
    }

    @Override
    public void close() {
        sensorImpl.close();
        logger.info("+++ HiTechnicCompass closed");
    }

    public static void main(String[] args) throws Exception {

        BaseSensorState ss = new BaseSensorState();
        HiTechnicCompassSensor compass = new HiTechnicCompassSensor("S4", ss, "Compass", 10L);
        Thread compasst = new Thread(compass);

        try {
            compasst.start();
            logger.info("main thread");

            Thread.sleep(10000);

            ss.setSensorActive(false);
            Thread.sleep(3000);

        } catch (InterruptedException ex) {
        }
        //NXTCommand.close();

    }
}
