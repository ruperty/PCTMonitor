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
import uk.co.moons.sensors.BaseSensorMultiple;
import uk.co.moons.sensors.MindsensorsAbsoluteIMUAccelerationSensor;
import uk.co.moons.sensors.MindsensorsAbsoluteIMUMagneticSensor;
import uk.co.moons.sensors.MindsensorsAbsoluteIMURateSensor;

/**
 *
 * @author ReStart
 */
public class MindsensorsAbsoluteIMUMultipleSensorNeuralFunction extends BaseSensorMultipleNeuralFunction {

    static final Logger logger = Logger.getLogger(MindsensorsAbsoluteIMUMultipleSensorNeuralFunction.class.getName());

    private String mode = "Magnetic";

    public String axis = null;
    private int iAxis = BaseSensorMultiple.X;

    public MindsensorsAbsoluteIMUMultipleSensorNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("Mode")) {
                mode = param.getValue();
            }
            if (param.getName().equals("Axis")) {
                axis = param.getValue();
                setIAxis();
            }
        }
    }

    @Override
    public void init() throws Exception {

        switch (mode) {
            case "Acceleration":
                sensor = MindsensorsAbsoluteIMUAccelerationSensor.getInstance(sensorPort, mode, interval);
                break;
            case "Magnetic":
                sensor = MindsensorsAbsoluteIMUMagneticSensor.getInstance(sensorPort, mode, interval);
                break;
            case "Rate":
                sensor = MindsensorsAbsoluteIMURateSensor.getInstance(sensorPort, mode, interval);
                break;
        }
        postInit();
    }

    private void setIAxis() {
        switch (axis) {
            case "X":
                iAxis = BaseSensorMultiple.X;
                break;
            case "Y":
                iAxis = BaseSensorMultiple.Y;
                break;
            case "Z":
                iAxis = BaseSensorMultiple.Z;
                break;
        }
    }

    @Override
    public double compute() {
        if (threaded) {
            output = getStateAxis();
        } else {
            output = getSensorAxis();
        }

        return output;
    }

    private double getSensorAxis() {
        double rtn = 0.0;
        switch (iAxis) {
            case BaseSensorMultiple.X:
                rtn = sensor.getValue(0);
                break;
            case BaseSensorMultiple.Y:
                rtn = sensor.getValue(1);
                break;
            case BaseSensorMultiple.Z:
                rtn = sensor.getValue(2);
                break;
        }
        return rtn;
    }

    private double getStateAxis() {
        double rtn = 0.0;
        switch (iAxis) {
            case BaseSensorMultiple.X:
                rtn = sensorState.getValue(0);
                break;
            case BaseSensorMultiple.Y:
                rtn = sensorState.getValue(1);
                break;
            case BaseSensorMultiple.Z:
                rtn = sensorState.getValue(2);
                break;
        }
        return rtn;
    }

    @Override
    public void setParameter(String par) throws Exception {
        super.setParameter(par);
        if (sensor == null) {
            return;
        }
        String[] arr = par.split(":");
        if (arr[0].equals("Axis")) {
            axis = arr[1];
            setIAxis();
        }
    }
}
