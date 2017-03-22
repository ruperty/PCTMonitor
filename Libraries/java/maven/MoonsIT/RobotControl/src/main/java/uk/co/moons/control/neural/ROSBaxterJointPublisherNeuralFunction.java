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
import uk.co.moons.actuators.impl.ROSBaxterJointPublisherActuatorImpl;
import uk.co.moons.control.functions.BaseControlFunction;

/**
 *
 * @author ReStart
 */
public class ROSBaxterJointPublisherNeuralFunction extends BaseActuatorNeuralFunction {

    private static final Logger LOG = Logger.getLogger(ROSBaxterJointPublisherNeuralFunction.class.getName());

    public String joint;
    public String topic;
    public String mode = "Position";
    public boolean publish = false;
    public double tolerance = 0.01;
    private boolean continuous = true;

    public ROSBaxterJointPublisherNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("Joint")) {
                joint = param.getValue();
            }
            if (param.getName().equals("Topic")) {
                topic = param.getValue();
            }
            if (param.getName().equals("Mode")) {
                mode = param.getValue();
            }
            if (param.getName().equals("Publish")) {
                publish = Boolean.valueOf(param.getValue());
            }
            if (param.getName().equals("Tolerance")) {
                tolerance = Double.valueOf(param.getValue());
            }
        }
        if (joint == null) {
            throw new Exception("Joint null for ROSBaxterJointPublisherNeuralFunction");
        }

        if (topic == null) {
            throw new Exception("Topic null for ROSBaxterJointPublisherNeuralFunction");
        }

    }

    public boolean isPublish() {
        return publish;
    }

    @Override
    public void init() throws Exception {
        super.init();
        actuator = new ROSBaxterJointPublisherActuatorImpl(topic, joint, mode);
        String prop = System.getenv("ROS_SIM");
        if (prop != null && prop.equals("true")) {
            continuous = false;
        }
        /*
        if (initial != null) {
            actuator.setValue(initial);
        }
        if (publish) {       // didn't this work for previous
            ((ROSBaxterJointPublisherActuatorImpl) actuator).publish();
            Thread.sleep(5000);
        }*/
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double newValue = controls.get(0).getValue();
        if (continuous) {
            //if (Math.abs(newValue - output) > tolerance) {
            output = newValue;
            actuator.setValue(output);
            //logger.info("+++ OUTPUT " + output);
            //}
        } else if (Math.abs(newValue - output) > tolerance) {
            output = newValue;
            actuator.setValue(output);
            LOG.info("+++ OUTPUT " + output);
        }

        if (publish) {
            ((ROSBaxterJointPublisherActuatorImpl) actuator).publish();
        }

        return output;
    }

    @Override
    public void close() throws Exception {

        if (publish) {
            actuator.close();
        }
    }

    @Override
    public Double stop() throws Exception {
        Double value = null;
        if (actuator != null) {
            if (last == null) {
                if (initial != null) {
                    value = initial;
                }
            } else {
                value = last;
            }
            if (value != null) {
                actuator.setValue(value);
                ((ROSBaxterJointPublisherActuatorImpl) actuator).publish("Position");
                //LOG.log(Level.INFO, "Stopping  {0} {1} to {2}", new Object[]{topic, joint, value});
            }
        }
        return value;
    }

    public double initial() throws Exception {
        if (actuator != null) {
            if (initial != null) {
                actuator.setValue(initial);
                ((ROSBaxterJointPublisherActuatorImpl) actuator).publish("Position");
                //LOG.log(Level.INFO, "Stopping  {0} {1} to {2}", new Object[]{topic, joint, value});
            }
        }
        return initial;
    }

    @Override
    public void setParameter(String par) {
        super.setParameter(par);
        String[] arr = par.split(":");
        if (arr[0].equals("Joint")) {
            if (actuator != null) {
                ((ROSBaxterJointPublisherActuatorImpl) actuator).setJoint(arr[1]);
            }
        }
        if (arr[0].equals("Mode")) {
            if (actuator != null) {
                ((ROSBaxterJointPublisherActuatorImpl) actuator).setMode(arr[1]);
            }
        }

    }
}
