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
import uk.co.moons.sensors.states.BaseSensorState;
import uk.co.moonsit.sockets.WifiPhoneDoubleDataServer;

/**
 *
 * @author Rupert Young Moon's Information Technology
 *
 */
public abstract class BaseSensorServer implements Runnable, SensorServerInterface {

    private static final Logger logger = Logger.getLogger(BaseSensor.class.getName());

    protected BaseSensorState state;
    protected WifiPhoneDoubleDataServer server;
    private boolean isStarted = false;

    protected Long interval = 20L; //protected static BaseSensorServer instance = null;

    public BaseSensorState getState() {
        return state;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setIsStarted(boolean isStarted) {
        if (isStarted) {
            server.setFinished(false);
        }
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
        server.close();
        logger.info("+++ sensor server closed " + this.getClass().getSimpleName());
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    /*
    @Override
    public double[] getData() {
        return server.getData();
    }*/

}
