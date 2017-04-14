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
import uk.co.moons.sensors.BaseSensorServer;
import uk.co.moons.sensors.states.BaseSensorState;

/**
 *
 * @author ReStart
 */
public class BaseSensorServerNeuralFunction extends NeuralFunction {

    private static final Logger logger = Logger.getLogger(BaseSensorServerNeuralFunction.class.getName());

    protected BaseSensorServer sensor;
    protected BaseSensorState sensorState;
    protected String sensorPort = null;
    public Long interval = 20L;

    public BaseSensorServerNeuralFunction() {
    }

    public BaseSensorServerNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        sensorState = new BaseSensorState();
        for (Parameters param : ps) {
            if (param.getName().equals("Interval")) {
                interval = Long.parseLong(param.getValue());
            }
        }       
    }

    
    public void init(int size) throws Exception {
        sensorState = new BaseSensorState(size);
        sensorState.setSensorActive(true);
    }

    @Override
    public void close() {
        if (sensorState != null) {
            sensorState.setSensorActive(false);
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
        }

    }
}
