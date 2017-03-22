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
package uk.co.moons.sensors.impl;

import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moons.sensors.SerialTerabotSensorSingleton;

/**
 *
 * @author ReStart
 */
public class SerialTerabotSensorImpl extends BaseSensorImpl {

    static final Logger LOG = Logger.getLogger(SerialTerabotSensorImpl.class.getName());

    private final SerialTerabotSensorSingleton singleton;
    private final int index;

    @SuppressWarnings("empty-statement")
    public SerialTerabotSensorImpl(String port, int timeOut, int dataRate, int index) throws UnsupportedCommOperationException, IOException, TooManyListenersException, Exception {
        singleton = SerialTerabotSensorSingleton.getInstance(port, timeOut, dataRate);
        this.index = index;
    }

    public void update() {
        singleton.update();
    }

    @Override
    public double getValue() throws Exception {
        return singleton.getData(index);
    }

    @Override
    public void close() {
        singleton.close();
    }

    public boolean available() {
        return singleton.available();
    }

    // INFO: Time: 3,288 loop 32.88
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) throws InterruptedException, IOException, TooManyListenersException, Exception {
        LOG.info("Start");
        String comPort = "COM7";
        int test = 1;

        switch (test) {
            case 1:
                SerialTerabotSensorImpl s1 = new SerialTerabotSensorImpl(comPort, 2000, 19200, 0);
                LOG.info("Connected ...");
                int iters = 1;
                long start = System.currentTimeMillis();
                //try {

                for (int i = 0; i < iters; i++) {
                    s1.update();
                    
                    //logger.log(Level.INFO, "data {0} {1} {2}", new Object[]{i, s1.getValue(), s2.getValue()});
                    Thread.sleep(1000);
                }
                //} catch (IOException | InterruptedException e) {
                //  logger.info(e.toString());
                // }
                long time = (System.currentTimeMillis() - start);
                LOG.log(Level.INFO, "Time: {0} loop {1}", new Object[]{time, 1f * time / iters});

                s1.close();
                break;
            case 2:

                break;

        }
        //SerialSensorImpl s3 = new SerialSensorImpl(comPort, 2000, 9600, 0);
        //s3.close();
    }

}
