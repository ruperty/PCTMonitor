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
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.sensors.BaseSensor;
import uk.co.moons.sensors.states.BaseSensorState;

/**
 *
 * @author ReStart
 */
public class BaseSensorNeuralFunction extends NeuralFunction {

    private static final Logger logger = Logger.getLogger(BaseSensorNeuralFunction.class.getName());

    protected BaseSensor sensor;
    protected BaseSensorState sensorState;
    protected String sensorPort = null;
    public boolean threaded = true;
    private Thread thread = null;
    public Long interval = 5L;

    public BaseSensorNeuralFunction() {
    }

    public BaseSensorNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("SensorPort")) {
                sensorPort = param.getValue();
            }
            if (param.getName().equals("Interval")) {
                interval = Long.parseLong(param.getValue());
            }
        }
        //if (sensorPort == null) {
          //  throw new Exception("SensorPort null for " + this.getName());
        //}
    }

    @Override
    public void init() throws Exception {
        sensorState = new BaseSensorState();
        sensorState.setSensorActive(true);
    }

    
    public void init(int size) throws Exception {
        sensorState = new BaseSensorState(size);
        sensorState.setSensorActive(true);
        

    }

    public BaseSensor getSensor() {
        return sensor;
    }

    public void postInit() throws Exception {
        if (threaded) {
            startThread();
        } else {
            sensor.getValue();
        }

    }

    protected void startThread() {

        thread = new Thread(sensor);

        thread.start();
        sensorState.getValue();
        sensorState.setSensorActive(true);
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
    public double compute() {
        if (threaded) {
            output = sensorState.getValue();
        } else {
            output = sensor.getValue();
        }

        return output;
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
        }

        if (arr[0].equalsIgnoreCase("threaded")) {
            boolean b = Boolean.parseBoolean(arr[1].trim());
            threaded = b;
            if (b) {
                if (thread == null) {
                    startThread();
                }
            } else {
                sensorState.setSensorActive(false);
                while (thread.isAlive()) {
                    logger.info("BaseSensorNeuralFunction still alive");
                }
                logger.info("BaseSensorNeuralFunction null");
                thread = null;
            }
        }

    }
}
