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
import uk.co.moons.sensors.SerialCSVSensorSingleton;
import uk.co.moonsit.outputs.SerialCSVWrite;

/**
 *
 * @author ReStart
 */
public class SerialCSVSensorImpl extends BaseSensorImpl {

    static final Logger logger = Logger.getLogger(SerialCSVSensorImpl.class.getName());

    private final SerialCSVSensorSingleton singleton;
    private final int index;

    @SuppressWarnings("empty-statement")
    public SerialCSVSensorImpl(String port, int timeOut, int dataRate, int index) throws UnsupportedCommOperationException, IOException, TooManyListenersException, Exception {
        singleton = SerialCSVSensorSingleton.getInstance(port, timeOut, dataRate);
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
        logger.info("Start");
        String comPort = "COM73";
        int test = 1;

        switch (test) {
            case 1:
                SerialCSVSensorImpl s1 = new SerialCSVSensorImpl(comPort, 2000, 9600, 0);
                SerialCSVSensorImpl s2 = new SerialCSVSensorImpl(comPort, 2000, 9600, 1);
                logger.info("Connected ...");
                SerialCSVWrite sw = null;
                if (test == 1) {
                    sw = new SerialCSVWrite(comPort, 2000, 9600);
                    sw.init();
                }
                int iters = 100;
                long start = System.currentTimeMillis();
                //try {
                for (int i = 0; i < iters; i++) {
                    if (test == 1) {
                        sw.write(i);
                    }
                    s1.update();
                    //logger.log(Level.INFO, "data {0} {1} {2}", new Object[]{i, s1.getValue(), s2.getValue()});
                    //Thread.sleep(10);
                }
                //} catch (IOException | InterruptedException e) {
                //  logger.info(e.toString());
                // }
                long time = (System.currentTimeMillis() - start);
                logger.log(Level.INFO, "Time: {0} loop {1}", new Object[]{time, 1f * time / iters});

                s1.close();
                s2.close();
                if (sw != null) {
                    sw.close();
                }
                break;
            case 2:

                
                break;

        }
        //SerialSensorImpl s3 = new SerialSensorImpl(comPort, 2000, 9600, 0);
        //s3.close();
    }

}
