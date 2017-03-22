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
import uk.co.moons.sensors.MindsensorsAbsoluteIMUSensor;

/**
 *
 * @author ReStart
 */
public class MindsensorsAbsoluteIMUSensorNeuralFunction extends BaseSensorNeuralFunction {

    private static final Logger logger = Logger.getLogger(MindsensorsAbsoluteIMUSensorNeuralFunction.class.getName());

    private String mode = "Compass";
    public String angletype = "Compass";

    private int itype;
    private final int COMPASS = 0;
    private final int ANGLE_ANTICLOCKWISE = 1;
    private final int ANGLE_CLOCKWISE = 2;

    public MindsensorsAbsoluteIMUSensorNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("Mode")) {
                mode = param.getValue();
            }
            if (param.getName().equals("AngleType")) {
                angletype = param.getValue();
            }
        }
        setType(angletype);
    }

    @Override
    public void init() throws Exception {
        super.init();
        sensor = new MindsensorsAbsoluteIMUSensor(sensorPort, sensorState, mode, interval);
        postInit();
    }

    @Override
    public double compute() {
        if (threaded) {
            output = adjustReading(sensorState.getValue());
        } else {
            output = adjustReading(sensor.getValue());
        }

        return output;
    }

    // The raw value is 0 - 360 in an clockwise direction
    private double adjustReading(double value) {
        double reading = 0;

        switch (itype) {
            case ANGLE_CLOCKWISE:
                if (value <= 180) {
                    reading = value;
                } else {
                    reading = value - 360;
                }
                break;

            case ANGLE_ANTICLOCKWISE:
                if (value <= 180) {
                    reading = -1 * value;
                } else {
                    reading = 360 - value;
                }
                break;
            case COMPASS:
                reading = value;
                break;
        }

        return reading;
    }

    @Override
    public void setParameter(String par) {
        super.setParameter(par);
        if (sensor == null) {
            return;
        }
        String[] arr = par.split(":");
        if (arr[0].equalsIgnoreCase("angletype")) {
            setType(arr[1]);
        }

    }

    private void setType(String mode) {

        if (mode.equalsIgnoreCase("angleanticlockwise")) {
            itype = ANGLE_ANTICLOCKWISE;
        }
        if (mode.equalsIgnoreCase("angleclockwise")) {
            itype = ANGLE_CLOCKWISE;
        }
        if (mode.equalsIgnoreCase("compass")) {
            itype = COMPASS;
        }
    }
}
