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
import uk.co.moons.sensors.LSensor;

/**
 *
 * @author ReStart
 */
public class LightSensorNeuralFunction extends BaseSensorNeuralFunction {

    public boolean floodlight = false;

    public LightSensorNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("SensorPort")) {
                sensorPort = param.getValue();
            }
            if (param.getName().equals("Floodlight")) {
                floodlight = Boolean.parseBoolean(param.getValue());
            }
        }
        if (sensorPort == null) {
            throw new Exception("SensorPort null for LightSensorNeuralFunction");
        }
    }

    @Override
    public void init() throws Exception {
        super.init();
        sensor = new LSensor(sensorPort, sensorState, floodlight);
        postInit();
    }

     @Override
    public double compute() {
        output=(int)(100*super.compute());
        
       
        return output;
    }
    
    @Override
    public void setParameter(String par) throws Exception {
        super.setParameter(par);
        String[] arr = par.split(":");
        if (arr[0].equals("Floodlight")) {
            floodlight = Boolean.valueOf(arr[1]);
            if (sensor != null) {
                ((LSensor) sensor).setFloodLight(floodlight ? 1 : 0);
            }
        }
    }
}
