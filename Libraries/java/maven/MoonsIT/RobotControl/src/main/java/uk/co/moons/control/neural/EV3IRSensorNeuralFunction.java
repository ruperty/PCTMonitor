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
import uk.co.moons.sensors.EV3IRSensor;

/**
 *
 * @author ReStart
 */
public class EV3IRSensorNeuralFunction extends BaseSensorNeuralFunction {

    public String mode = "Distance";
    public int channel = 0;
    public String type = "Distance";
    private int imode, itype;
    private final int DISTANCE_TYPE = 1;
    private final int DIRECTION_TYPE = 0;
    private final int SEEK_MODE = 0;
    private final int DISTANCE_MODE = 1;

    public EV3IRSensorNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        //sensorState = new BaseSensorState();
        for (Parameters param : ps) {
            if (param.getName().equals("Mode")) {
                mode = param.getValue();
            }
            if (param.getName().equals("Type")) {
                type = param.getValue();
            }
            if (param.getName().equals("Channel")) {
                channel = Integer.parseInt(param.getValue());
            }
        }
    }

    @Override
    public void init() throws Exception {
        switch (mode) {
            case "Seek":
                imode = SEEK_MODE;
                break;
            case "Distance":
                imode = DISTANCE_MODE;
                break;
        }

        switch (type) {
            case "Distance":
                itype = DISTANCE_TYPE;
                break;
            case "Direction":
                itype = DIRECTION_TYPE;
                break;
        }

        if (imode == SEEK_MODE) {
            super.init(8);
        } else {
            super.init();
        }
        sensor = new EV3IRSensor(sensorPort, sensorState, mode, type, channel, interval);
        postInit();
    }

    @Override
    public void setParameter(String par) throws Exception {
        super.setParameter(par);
        String[] arr = par.split(":");
        if (arr[0].equals("Mode")) {
            setMode(arr[1]);
        }
        if (arr[0].equals("Channel")) {
            setChannel(Integer.parseInt(arr[1]));
        }
        if (arr[0].equals("Type")) {
            setType(arr[1]);
        }
    }

    private void setMode(String mode) {
        if (sensor != null) {
            ((EV3IRSensor) sensor).setMode(mode);
        }
    }

    private void setType(String t) {
        if (sensor != null) {
            ((EV3IRSensor) sensor).setType(t);
        }
    }

    private void setChannel(int c) {
        if (sensor != null) {
            ((EV3IRSensor) sensor).setChannel(c);
        }
    }

    @Override
    public void close() {
        if (sensorState != null) {
            sensorState.setSensorActive(false);
        }
    }

    @Override
    public double compute() {
        if (imode == SEEK_MODE) {
            if (threaded) {
                output = sensorState.getValue((channel - 1) * 2 + itype);
            } else {
                output = sensor.getValue();
            }
        } else 
            if (threaded) {
            output = sensorState.getValue();
        } else {
            output = sensor.getValue();
        }

        return output;
    }

}
