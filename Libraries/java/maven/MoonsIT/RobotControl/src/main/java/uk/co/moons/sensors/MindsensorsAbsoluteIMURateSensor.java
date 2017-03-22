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
import uk.co.moons.sensors.impl.MindsensorsAbsoluteIMUSensorImpl;
import uk.co.moons.sensors.states.BaseSensorState;

/**
 *
 * @author Rupert Young
 */
public class MindsensorsAbsoluteIMURateSensor extends BaseSensorMultiple {

    static final Logger logger = Logger.getLogger(MindsensorsAbsoluteIMURateSensor.class.getName());
    private static  MindsensorsAbsoluteIMURateSensor instance = null;

    /**
     * Creates a new instance of MindsensorsAbsoluteIMUSensor
     *
     * @param sp
     * @param mode
     * @param i
     *
     */
    
    protected MindsensorsAbsoluteIMURateSensor(String sp,  String mode, Long i) {
        interval = i;
        state = new BaseSensorState(3);
        sensorImpl = new MindsensorsAbsoluteIMUSensorImpl(sp, mode);
    }

      public static MindsensorsAbsoluteIMURateSensor getInstance(String sp,  String mode, Long i)  {
        if (instance == null) {
            instance = new MindsensorsAbsoluteIMURateSensor(sp,  mode, i);
        }
        return instance;
    }
   
  
    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        try {
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

}
