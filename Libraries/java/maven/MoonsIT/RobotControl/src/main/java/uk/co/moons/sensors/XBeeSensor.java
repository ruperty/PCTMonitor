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
import uk.co.moons.sensors.impl.XBeeSensorImpl;
import uk.co.moons.sensors.states.BaseSensorState;

/**
 *
 * @author Rupert Young
 */
public class XBeeSensor extends BaseSensor {

    static final Logger logger = Logger.getLogger(XBeeSensor.class.getName());

    private final XBeeSensorImpl sensor;
    private final int limit;

    /**
     * Creates a new instance of USensor
     *
     * @param sp
     * @param mode
     * @param st
     * @param interval
     * @param limit
     *
     */
    public XBeeSensor(String sp, String mode, BaseSensorState st, Long interval, int limit) {
        this.limit = limit;
        this.interval = interval;
        state = st;
        sensor = new XBeeSensorImpl(sp, mode);
    }

   


    /*
     public float getValue() {
     float rssi = sensor.getRSSI();
     if (rssi > 100) {
     logger.info("RSSI = " + rssi);
     }
     return sensor.getRSSI();
     }*/
    @Override
    @SuppressWarnings({"CallToPrintStackTrace", "SleepWhileInLoop", "LoggerStringConcat"})
    public void run() {
        try {
            while (state.isSensorActive()) {
                double rssi = sensor.getValue();
                if (rssi <= limit) {
                    if (rssi == 65) {
                        logger.info("RSSI = " + rssi);
                    } else {
                        state.setValue(rssi);
                    }
                }
                Thread.sleep(interval);
            }
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            close();
        }
    }

   

    @Override
    public double getValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
