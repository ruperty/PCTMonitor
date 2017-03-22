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

import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.sensors.SerialCSVSensor;

/**
 *
 * @author ReStart
 */
public class SerialCSVSensorNeuralFunction extends BaseSensorNeuralFunction {


    public int timeout = 2000;
    public int datarate = 9600;
    public int position = 0;
    public boolean update = false;

    public SerialCSVSensorNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        threaded = false;
        for (Parameters param : ps) {
            if (param.getName().equals("TimeOut")) {
                timeout = Integer.parseInt(param.getValue());
            }
            if (param.getName().equals("DataRate")) {
                datarate = Integer.parseInt(param.getValue());
            }
            if (param.getName().equals("Position")) {
                position = Integer.parseInt(param.getValue());
            }
            if (param.getName().equals("Update")) {
                update = Boolean.parseBoolean(param.getValue());
            }
        }        
    }

    @Override
    public void init() throws Exception {
        super.init();
        sensor = new SerialCSVSensor(sensorPort, sensorState, timeout, datarate, position, update);
    }

    @Override
    public double compute() {
        output = super.compute();
        return output;
    }

}
