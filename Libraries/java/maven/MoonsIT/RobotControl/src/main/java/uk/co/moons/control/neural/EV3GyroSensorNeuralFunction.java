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
import uk.co.moons.sensors.EV3GSensor;

/**
 *
 * @author ReStart
 */
public class EV3GyroSensorNeuralFunction extends BaseSensorNeuralFunction {

    public String mode = null;

    public EV3GyroSensorNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("SensorPort")) {
                sensorPort = param.getValue();
            }
            if (param.getName().equals("Mode")) {
                mode = param.getValue();
            }
            if (param.getName().equals("Threaded")) {
                threaded = Boolean.valueOf(param.getValue());
            }
        }
        if (sensorPort == null) {
            throw new Exception("SensorPort null for EV3GyroSensorNeuralFunction");
        }
    }

    @Override
    public void init() throws Exception {
        super.init();
        sensor = new EV3GSensor(sensorPort, sensorState, mode);
                postInit();

    }

    @Override
    public void setParameter(String par) {
        super.setParameter(par);
        if (sensor != null) {
            String[] arr = par.split(":");
            if (arr[0].equals("Mode")) {
                ((EV3GSensor)sensor).setMode(arr[1]);
            }
        }
    }
}
