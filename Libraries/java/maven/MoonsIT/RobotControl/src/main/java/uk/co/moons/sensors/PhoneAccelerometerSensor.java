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

import java.io.IOException;
import java.util.logging.Logger;
import uk.co.moons.sensors.states.BaseSensorState;
import uk.co.moonsit.sockets.WifiPhoneDoubleDataServer;

/**
 *
 * @author Rupert Young
 */
public class PhoneAccelerometerSensor extends BaseSensorServer {

    static final Logger logger = Logger.getLogger(PhoneAccelerometerSensor.class.getName());
    //private double x;
    //private double y;
    //private double z;
    private static PhoneAccelerometerSensor instance = null;

    //private final BaseSensorState state;
    /**
     * Creates a new instance of USensor
     */
    /**
     * Creates a new instance of USensor
     *
     * @param port
     * @param timeout
     * @param rtimeout
     * @param i
     * @param initial
     * @throws java.io.IOException
     *
     */
    protected PhoneAccelerometerSensor(int port, int timeout, int rtimeout, long i, double initial) throws IOException {
        interval = i;
        state = new BaseSensorState(3);
        server = new WifiPhoneDoubleDataServer(port, timeout, rtimeout, 3, initial);
    }

    public static PhoneAccelerometerSensor getInstance(int port, int timeout, int rtimeout, long i, double initial) throws IOException {
        if (instance == null) {
            instance = new PhoneAccelerometerSensor(port, timeout, rtimeout, i, initial);
        }
        return instance;
    }

   
    @SuppressWarnings({"SleepWhileInLoop", "CallToThreadDumpStack"})
    @Override
    public void run() {
        logger.info("+++ phoneAcc started-");
        server.start();
        try {
            while (state.isSensorActive()) {
                double[] data = server.getData();
                state.setValues(data);
                Thread.sleep(interval);
            }
            logger.info("phone acc finished");
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            logger.warning(ex.toString());
        } finally {
            logger.info("phone acc close");
            close();
            instance = null;
        }

    }

}
