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
import uk.co.moons.handlers.MotorHandler;
import uk.co.moons.motors.MotorImpl;

/**
 *
 * @author ReStart
 */
public class MotorIsStalledNeuralFunction extends BaseMotorNeuralFunction {

    private static final Logger logger = Logger.getLogger(MotorIsStalledNeuralFunction.class.getName());

    private MotorHandler mh = null;

    public MotorIsStalledNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        for (Parameters param : ps) {
            if (param.getName().equals("MotorIndex")) {
                motorIndex = param.getValue();
            }
            if (param.getName().equals("MotorType")) {
                motorType = param.getValue();
            }
        }
        if (motorIndex == null) {
            throw new Exception("Motor index null for MotorReadNeuralFunction");
        }

        mh = new MotorHandler(true);
    }

    @Override
    public void init() {
        motor = new MotorImpl(motorIndex, motorType);
        motor.setSpeed(0);
    }

    @Override
    public double compute() {
        output = motor.isStalled();

        return output;
    }

    @Override
    public void close() throws Exception {
        logger.info("+++ close, doing nothing");
    }
}
