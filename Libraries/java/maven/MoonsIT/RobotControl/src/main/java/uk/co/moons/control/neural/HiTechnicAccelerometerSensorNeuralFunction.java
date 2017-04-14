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
import uk.co.moons.sensors.BaseSensorMultiple;
import uk.co.moons.sensors.HiTechnicAccelerometerSensor;

/**
 *
 * @author ReStart
 */
public class HiTechnicAccelerometerSensorNeuralFunction extends BaseSensorMultipleNeuralFunction {
    
    private static final Logger logger = Logger.getLogger(HiTechnicAccelerometerSensorNeuralFunction.class.getName());
    
    public String axis = null;
    public int offset = 0;
    private int iAxis = BaseSensorMultiple.X;
    
    public HiTechnicAccelerometerSensorNeuralFunction(List<Parameters> ps)
            throws Exception {
        super(ps);
        
        for (Parameters param : ps) {
            if (param.getName().equals("Axis")) {
                axis = param.getValue();
                setIAxis();
            }
            if (param.getName().equals("Offset")) {
                offset = Integer.parseInt(param.getValue());
            }
        }
    }
    
    public HiTechnicAccelerometerSensorNeuralFunction() {
        super();
    }
    
    @Override
    public void init() throws Exception {
        logger.log(Level.INFO, "Threaded is {0}", threaded);
        sensor = HiTechnicAccelerometerSensor.getInstance(sensorPort, interval);
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
