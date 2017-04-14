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
package uk.co.moons.control.neural;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.sensors.BaseSensorMultiple;
import uk.co.moons.sensors.states.BaseSensorState;

/**
 *
 * @author ReStart
 */
public class BaseSensorMultipleNeuralFunction extends NeuralFunction {

    private static final Logger logger = Logger.getLogger(BaseSensorMultipleNeuralFunction.class.getName());

    protected BaseSensorMultiple sensor;
    protected BaseSensorState sensorState;
    protected String sensorPort = null;
    public Long interval = 5L;
    public boolean threaded = true;

    public BaseSensorMultipleNeuralFunction() {
    }

    public BaseSensorMultipleNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        sensorState = new BaseSensorState();
        for (Parameters param : ps) {
            if (param.getName().equals("Interval")) {
                interval = Long.parseLong(param.getValue());
            }
            if (param.getName().equals("SensorPort")) {
                sensorPort = param.getValue();
            }
            if (param.getName().equals("Threaded")) {
                threaded = Boolean.parseBoolean(param.getValue());
            }
        }

        if (sensorPort == null) {
            throw new Exception("SensorPort null for " + this.getName());
        }
    }

    public void init(int size) throws Exception {
        sensorState = new BaseSensorState(size);
        sensorState.setSensorActive(true);
    }

     public void postInit() throws Exception {
        if (threaded) {
            startThread();
        } 

    }

    protected void startThread() {

        sensorState = sensor.getState();
        sensorState.setSensorActive(true);
        sensor.start();
    }
    
    
    @Override
    public void close() {
         if (threaded) {
            if (sensorState != null) {
                sensorState.setSensorActive(false);
            }
        } else {
            if (sensor != null) {
                sensor.close();
            }
        }

    }

    @Override
    public void setParameter(String par) throws Exception {
        super.setParameter(par);
        if (sensor == null) {
            return;
        }
        String[] arr = par.split(":");

        if (arr[0].equals("Interval")) {
            sensor.setInterval(Long.valueOf(arr[1]));
            logger.log(Level.INFO, "Interval set to {0}", Long.valueOf(arr[1]));
        }

    }
}
