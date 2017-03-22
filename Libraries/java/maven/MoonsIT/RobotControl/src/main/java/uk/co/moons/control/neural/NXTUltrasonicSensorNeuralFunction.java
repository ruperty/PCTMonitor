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
import uk.co.moons.sensors.USensor;

/**
 *
 * @author ReStart
 */
public class NXTUltrasonicSensorNeuralFunction extends BaseSensorNeuralFunction {

    private static final Logger logger = Logger.getLogger(NXTUltrasonicSensorNeuralFunction.class.getName());

    public Integer max = null;

    public NXTUltrasonicSensorNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("SensorPort")) {
                sensorPort = param.getValue();
            }
            if (param.getName().equals("Interval")) {
                interval = Long.parseLong(param.getValue());
            }
            if (param.getName().equals("Max")) {
                max = Integer.parseInt(param.getValue());
            }
        }
        logger.info("Max: " + max);

        if (sensorPort == null) {
            throw new Exception("SensorPort null for USensorNeuralFunction");
        }
    }

    @Override
    public void init() throws Exception {
        super.init();
        sensor = new USensor(sensorPort, sensorState, interval);
        postInit();
    }

    @Override
    public double compute() {
        output = 100 * super.compute();

        if (max != null && output > max) {
            //logger.log(Level.INFO, "--- {0}", output);
            output = max;
            //logger.log(Level.INFO, "--- {0}", output);
        }

        //logger.info("US: " + output + " " + max);
        return output;
    }

}
