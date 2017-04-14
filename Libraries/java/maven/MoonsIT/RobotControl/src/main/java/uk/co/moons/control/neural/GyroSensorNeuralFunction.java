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
import uk.co.moons.sensors.GSensor;

/**
 *
 * @author ReStart
 */
public class GyroSensorNeuralFunction extends BaseSensorNeuralFunction {

    public int offset = 0;
    private boolean calibrate = false;
    public boolean raw = false;
    public int sign = 1;

    public GyroSensorNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("SensorPort")) {
                sensorPort = param.getValue();
            }
            if (param.getName().equals("Offset")) {
                offset = Integer.parseInt(param.getValue());
            }
            if (param.getName().equals("Sign")) {
                sign = Integer.parseInt(param.getValue());
            }
            if (param.getName().equals("Calibrate")) {
                calibrate = Boolean.valueOf(param.getValue());
            }
            if (param.getName().equals("Raw")) {
                raw = Boolean.valueOf(param.getValue());
            }
            if (param.getName().equals("Threaded")) {
                threaded = Boolean.valueOf(param.getValue());
            }
        }
        if (sensorPort == null) {
            throw new Exception("SensorPort null for GyroSensorNeuralFunction");
        }
    }

    @Override
    public void init() throws Exception {
        super.init();
        sensor = new GSensor(sensorPort, sensorState, offset, calibrate, raw, sign);
        postInit();
    }

    @Override
    public void setParameter(String par) throws Exception {
        super.setParameter(par);
        String[] arr = par.split(":");
        if (arr[0].equals("Offset")) {
            ((GSensor) sensor).setOffset(Integer.valueOf(arr[1]));
        }
        if (arr[0].equals("Raw")) {
            ((GSensor) sensor).setRaw(Boolean.valueOf(arr[1]));
        }
    }
}
