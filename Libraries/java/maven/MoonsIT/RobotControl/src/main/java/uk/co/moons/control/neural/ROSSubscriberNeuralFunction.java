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
import uk.co.moons.sensors.ROSSubscriberSensor;

public class ROSSubscriberNeuralFunction extends
        BaseROSSensorNeuralFunction {

    private static final Logger logger = Logger
            .getLogger(ROSSubscriberNeuralFunction.class.getName());

    //public String joint;
    public String variable;
    public String topic = "/robot/joint_states";
    public String index = null;

    public ROSSubscriberNeuralFunction(List<Parameters> ps)
            throws Exception {
        super(ps);
        threaded = false;
        for (Parameters param : ps) {
            if (param.getName().equals("Topic")) {
                topic = param.getValue();
            }
            if (param.getName().equals("Variable")) {
                variable = param.getValue();
            }
            if (param.getName().equals("Index")) {
                index = param.getValue();
            }
        }
    }

    @Override
    public void init() throws Exception {
        super.init();
        sensor = new ROSSubscriberSensor(sensorState, topic, variable, index);
    }

    @Override
    public double compute() {
        try {
            output = super.compute();
        } catch (NullPointerException e) {
            logger.warning("ROS data is null - have you started ROS/baxter?");
        }
        return output;
    }

    @Override
    public void setParameter(String par) {
        super.setParameter(par);
        String[] arr = par.split(":");
        if (arr[0].equals("Index")) {
            if (sensor != null) {
                ((ROSSubscriberSensor) sensor).setIndex(arr[1]);
            }
        }
        if (arr[0].equals("Variable")) {
            if (sensor != null) {
                ((ROSSubscriberSensor) sensor).setVariable(arr[1]);
            }
        }

    }

    @Override
    public void close() {

        sensor.close();

    }

}
