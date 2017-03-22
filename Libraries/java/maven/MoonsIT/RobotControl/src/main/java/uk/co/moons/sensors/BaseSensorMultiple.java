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
import uk.co.moons.sensors.impl.BaseSensorImpl;
import uk.co.moons.sensors.states.BaseSensorState;

/**
 *
 * @author Rupert Young Moon's Information Technology
 *
 */
public abstract class BaseSensorMultiple implements Runnable, SensorMultipleInterface {

    private static final Logger logger = Logger.getLogger(BaseSensor.class.getName());

    protected BaseSensorState state;
    protected BaseSensorImpl sensorImpl;
    private boolean isStarted = false;
    protected Long interval = 5L; 
    public static final int X = 0;
    public static final int Y = 1;
    public static final int Z = 2;

    public BaseSensorState getState() {
        return state;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setIsStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    public void start() {
        if (!isStarted()) {
            Thread t = new Thread(this);
            t.start();
            setIsStarted(true);
        }
    }

    /*
     @Override
     @SuppressWarnings("SleepWhileInLoop")
     public void run() {
     try {
     while (state.isSensorActive()) {
     double[] data = getData();

               
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
     }
     }
     */
    
    @SuppressWarnings("LoggerStringConcat")
    @Override
    public void close() {
        sensorImpl.close();
        logger.info("+++ sensor multiple closed " + this.getClass().getSimpleName());
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    @Override
    public float[] getData() {
        return sensorImpl.getData();
    }
    
     @Override
    public double getValue(int i) {
        return sensorImpl.getValue(i);
    }

    @Override
    public double getValue() {
        return sensorImpl.getValue();
    }
    /*
     @Override
     public double[] getData() {
     return server.getData();
     }*/

}
