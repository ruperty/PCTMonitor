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
import uk.co.moons.sensors.impl.SoundSensorImpl;
import uk.co.moons.sensors.states.BaseSensorState;

/**
 *
 * @author Rupert Young
 */
public class SSensor extends BaseSensor {

    static final Logger logger = Logger.getLogger(SSensor.class.getName());
    //private int readingDB = 0;
    //private int readingDBA = 0;
    //private long time;
    //private final SoundSensorImpl sensorImpl;

    /**
     * Creates a new instance of USensor
     *
     * @param sp
     * @param st
     */
    public SSensor(String sp, BaseSensorState st) {
        state = st;
        sensorImpl = new SoundSensorImpl(sp);
    }

    @Override
    public void close() {
        sensorImpl.close();
        logger.info("+++ SSensor closed");
    }

    public void setDBA(boolean b) {
        ((SoundSensorImpl)sensorImpl).setDBA(b);
    }

    @SuppressWarnings("SleepWhileInLoop")
    public static void main(String[] args) throws Exception {

        //NXTCommand.open();
        //NXTCommand.setVerify(true);
        int type = 1;

        switch (type) {

            case 0:
                BaseSensorState ss = new BaseSensorState();
                SSensor us = new SSensor("S3", ss);
                Thread t = new Thread(us);
                try {
                    t.start();
                    logger.info("main thread");
                    for (int i = 0; i < 200; i++) {
                        ss.setValue(SoundSensorImpl.DB);
                        System.out.print("DB " + ss.getValue());
                        /*
                         ss.setSoundType(SSensor.DB);
                         System.out.print("DB " + ss.getSoundDB());
                         ss.setSoundType(SSensor.DBA);
                         System.out.println(" DBA " + ss.getSoundDBA());*/
                        Thread.sleep(25);
                    }
                    ss.setSensorActive(false);
                    Thread.sleep(3000);

                } catch (InterruptedException ex) {
                }
                break;
            case 1:
        }

//NXTCommand.close();
    }

    

}
