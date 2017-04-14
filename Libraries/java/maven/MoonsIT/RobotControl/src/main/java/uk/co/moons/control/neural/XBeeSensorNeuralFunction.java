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
import uk.co.moons.sensors.XBeeSensor;
import uk.co.moons.sensors.states.BaseSensorState;

/**
 *
 * @author ReStart
 */
public class XBeeSensorNeuralFunction extends NeuralFunction {

    private XBeeSensor sensor;
    private final BaseSensorState ss;
    private String sensorPort = null;
    public long interval = 30;
    public String mode;
    public int limit = 100;

    public XBeeSensorNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        ss = new BaseSensorState();
        for (Parameters param : ps) {
            if (param.getName().equals("SensorPort")) {
                sensorPort = param.getValue();
            }
            if (param.getName().equals("Mode")) {
                mode = param.getValue();
            }
            if (param.getName().equals("Interval")) {
                interval = Long.parseLong(param.getValue());
            }
            if (param.getName().equals("Limit")) {
                limit = Integer.parseInt(param.getValue());
            }
        }
        if (sensorPort == null) {
            throw new Exception("SensorPort null for XBeeSensorNeuralFunction");
        }
    }

    @Override
    public void init() {

        sensor = new XBeeSensor(sensorPort, mode, ss, interval, limit);
        Thread ust = new Thread(sensor);

        ss.setSensorActive(true);
        ust.start();
    }

    @Override
    public void close() {
      if(ss!=null)  ss.setSensorActive(false);
    }

    @Override
    public double compute() {
        output = ss.getValue();
        return output;
    }

    @Override
    public void setParameter(String par) throws Exception {
        super.setParameter(par);
        if (sensor == null) {
            return;
        }
        String[] arr = par.split(":");
        if (arr[0].equals("Interval")) {
            sensor.setInterval(Long.valueOf(arr[1]));
        }
    }

}
