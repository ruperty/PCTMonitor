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
import uk.co.moons.sensors.PhoneGPSReference;

/**
 *
 * @author ReStart
 */
public class WifiPhoneGPSReferenceNeuralFunction extends BaseSensorServerNeuralFunction {

    private static final Logger logger = Logger.getLogger(WifiPhoneGPSReferenceNeuralFunction.class.getName());

    public String coordinate = null;
    public Integer port = null;
    public Integer timeout = 10000;

    public WifiPhoneGPSReferenceNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("Coordinate")) {
                coordinate = param.getValue();
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
        
        sensor = PhoneGPSReference.getInstance(port, timeout, timeout, interval, null);
        sensorState = sensor.getState();
        sensorState.setSensorActive(true);
        sensor.start();
        //if (initial != null) {
          //  output = initial;
        //}

    }

    
    @Override
    public double compute() throws Exception {
        //logger.info("+++ compute");
        output = getCoordinate(output);
        return output;
    }

    private double getCoordinate(double rtn) throws Exception {

        switch (coordinate) {
            case "Latitude": {
                double val = sensorState.getValue(0);
                if (val != 0) {
                    rtn = val;
                }
            }
            break;
            case "Longitude": {
                double val = sensorState.getValue(1);
                if (val != 0) {
                    rtn = val;
                }
            }
            break;
        }
        return rtn;
    }

}
