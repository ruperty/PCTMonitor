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
import uk.co.moons.sensors.impl.HiTechnicAccelerometerSensorImpl;
import uk.co.moons.sensors.states.BaseSensorState;

/**
 *
 * @author Rupert Young
 */
public class HiTechnicAccelerometerSensor extends BaseSensorMultiple {

    private static final Logger logger = Logger.getLogger(HiTechnicAccelerometerSensor.class.getName());

    //private final HiTechnicAccelerometerSensorImpl sensorImpl;
    //private final int[] values;
    private static HiTechnicAccelerometerSensor instance = null;

    /**
     * Creates a new instance of USensor
     *
     * @param sp
     * @param i
     */
    protected HiTechnicAccelerometerSensor(String sp, Long i) {
        interval=i;
        state = new BaseSensorState(3);
        sensorImpl = new HiTechnicAccelerometerSensorImpl(sp);
        //values = new int[3];
    }

    public static HiTechnicAccelerometerSensor getInstance(String sp, Long i) {
        if (instance == null) {
            instance = new HiTechnicAccelerometerSensor(sp,i);
        }
        return instance;
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        try {
            logger.info("Start HiTechnicAccelerometerSensor");
            while (state.isSensorActive()) {
                float[] data = getData();

                state.setValues(data);
                Thread.sleep(interval);
            }
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            logger.warning(ex.toString());
        } catch (Exception ex) {
            logger.warning(ex.toString());
            throw ex;
        } finally {
            close();
            instance = null;
        }
    }

    @Override
    @SuppressWarnings("LoggerStringConcat")
    public void close() {
        if (instance != null) {
            sensorImpl.close();
        }
        logger.info("+++ sensor closed " + this.getClass().getSimpleName());
    }

    /*
     public double getX() {
     return values[0];
     }

     public double getY() {
     return values[1];
     }

     public double getZ() {
     return values[2];
     }
     */
    /*
     private void convertToGravity() {
     gravityValues[0] = RMath.smooth(gravityValues[0], accValues[0], smoothness);
     gravityValues[1] = RMath.smooth(gravityValues[1], accValues[1], smoothness);
     gravityValues[2] = RMath.smooth(gravityValues[2], accValues[2], smoothness);
     if (!linear) {
     setValues(gravityValues);
     }
     }
    
     private void convertToLinear() {
     convertToGravity();
    
     // Remove the gravityValues contribution with the high-pass filter.
     values[0] = (int) (accValues[0] - gravityValues[0]);
     values[1] = (int) (accValues[1] - gravityValues[1]);
     values[2] = (int) (accValues[2] - gravityValues[2]);
     }
     * 
     */
}
