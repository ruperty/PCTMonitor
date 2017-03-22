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
import uk.co.moons.actuators.impl.ROSGripperPublisherActuatorImpl;

import uk.co.moons.control.functions.BaseControlFunction;

/**
 *
 * @author ReStart
 */
public class ROSGripperPublisherNeuralFunction extends BaseActuatorNeuralFunction {

    private static final Logger LOG = Logger.getLogger(ROSGripperPublisherNeuralFunction.class.getName());

    public String topic;
    private double oldValue=1;

    public ROSGripperPublisherNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("Topic")) {
                topic = param.getValue();
            }
        }

        if (topic == null) {
            throw new Exception("Topic null for ROSGripperPublisherNeuralFunction");
        }

    }

  

    @Override
    public void init() throws Exception {
        super.init();
        actuator = new ROSGripperPublisherActuatorImpl(topic);
        output=1;
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double input = controls.get(0).getValue();
        if (input!=oldValue) {      
            oldValue=input;
            actuator.setValue(input);
            actuator.publish();
            output=input;
        }

        return output;
    }

    @Override
    public void close() throws Exception {
            actuator.close();
    }

    

   

    
}
