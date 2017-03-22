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
import uk.co.moonsit.utils.Print;

/**
 *
 * @author Rupert Young Moon's Information Technology
 *
 */
public abstract class BaseSensor implements Runnable, SensorInterface {

    private static final Logger logger = Logger.getLogger(BaseSensor.class.getName());

    protected BaseSensorState state;
    protected BaseSensorImpl sensorImpl;
    protected Long interval = 5L;
    private final boolean debug=false;
   
    public BaseSensorState getState() {
        return state;
    }

    @SuppressWarnings("LoggerStringConcat")
    @Override
    public void close() {
        sensorImpl.close();
        //logger.info("+++ sensor closed "+ this.getClass().getSimpleName());
    }

    public BaseSensorImpl getSensorImpl() {
        return sensorImpl;
    }
    
    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    @Override
    public double getValue(int i) {
        return sensorImpl.getValue(i);
    }

    @Override
    public double getValue() {
        return sensorImpl.getValue();
    }

    @Override
    public float[] getData() {
        return sensorImpl.getData();
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        try {
            while (state.isSensorActive()) {
                float[] data = getData();
                if(debug)
                    Print.logArray("BS", data);
                state.setValues(data);
                if(interval>0)Thread.sleep(interval);
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

}
