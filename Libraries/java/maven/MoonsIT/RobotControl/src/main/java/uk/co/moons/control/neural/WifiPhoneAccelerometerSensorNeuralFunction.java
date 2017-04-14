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
import uk.co.moons.sensors.PhoneAccelerometerSensor;

/**
 *
 * @author ReStart
 */
public class WifiPhoneAccelerometerSensorNeuralFunction extends BaseSensorServerNeuralFunction {

    private static final Logger logger = Logger.getLogger(WifiPhoneAccelerometerSensorNeuralFunction.class.getName());

    public String axis = null;
    public Integer port = null;
    public Integer timeout = 10000;
    private final int X = 0;
    private final int Y = 1;
    private final int Z = 2;
    private int iAxis;

    public WifiPhoneAccelerometerSensorNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("Axis")) {
                axis = param.getValue();
                setIAxis();
            }
            if (param.getName().equals("Port")) {
                port = Integer.parseInt(param.getValue());
            }
            if (param.getName().equals("Timeout")) {
                timeout = Integer.parseInt(param.getValue());
            }
        }
        if (port == null) {
            throw new Exception("Type null for port");
        }

    }

    @Override
    public void init() throws Exception {
        //super.init();
        sensor = PhoneAccelerometerSensor.getInstance(port, timeout, timeout, interval, initial);
        sensorState = sensor.getState();
        sensorState.setSensorActive(true);
        sensor.start();
    }

    private void setIAxis() {
        switch (axis) {
            case "X":
                iAxis = X;
                break;
            case "Y":
                iAxis = Y;
                break;
            case "Z":
                iAxis = Z;
                break;
        }
    }

    @Override
    public double compute() throws Exception {
        //logger.info("+++ compute");
        output = sensorState.getValue(iAxis);//getAxis();
        return output;
    }

    /*
     private double getAxis() throws Exception {
     double rtn = 0.0;
     //double[] data = sensorState.getData();
     switch (iAxis) {
     case X:
     rtn = sensorState.getValue(0);
     break;
     case Y:
     rtn = sensorState.getValue(1);
     break;
     case Z:
     rtn = sensorState.getValue(2);
     break;
     }
     return rtn;
     }*/
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
