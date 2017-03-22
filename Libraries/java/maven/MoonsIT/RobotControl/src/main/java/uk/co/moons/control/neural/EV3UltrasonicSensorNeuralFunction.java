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
import uk.co.moons.sensors.EV3USensor;

/**
 *
 * @author ReStart
 */
public class EV3UltrasonicSensorNeuralFunction extends BaseSensorNeuralFunction {

    public Integer max = null;
    public String mode = "Distance";

    public EV3UltrasonicSensorNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("SensorPort")) {
                sensorPort = param.getValue();
            }

            if (param.getName().equals("Mode")) {
                mode = param.getValue();
            }
            if (param.getName().equals("Max")) {
                max = Integer.parseInt(param.getValue());
            }
        }

    }

    @Override
    public void init() throws Exception {
        super.init();
        sensor = new EV3USensor(sensorPort, sensorState, interval, mode);

        postInit();

    }

    @Override
    public double compute() {
        output = 100 * super.compute();

        if (max != null && output > max) {
            output = max;
        }
        return output;
    }

    @Override
    public void setParameter(String par) {
        super.setParameter(par);
        String[] arr = par.split(":");
        if (arr[0].equals("Mode")) {
            if (sensor != null) {
                ((EV3USensor) sensor).setMode(arr[1]);
            }
        }
    }
}
