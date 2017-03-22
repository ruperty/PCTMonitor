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
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moons.motors.UnregulatedMotorImpl;

/**
 *
 * @author ReStart
 */
public class UnregulatedMotorCountReferenceNeuralFunction extends BaseUnregulatedMotorNeuralFunction {

    static final Logger logger = Logger.getLogger(UnregulatedMotorCountReferenceNeuralFunction.class.getName());
    private int reference = 0;


    public UnregulatedMotorCountReferenceNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        for (Parameters param : ps) {
            if (param.getName().equals("MotorIndex")) {
                motorIndex = param.getValue();
            }
           
        }
        if (motorIndex == null) {
            throw new Exception("Motor index null for MotorCountReferenceNeuralFunction");
        }
    }

    @Override
    public void init() {
        motor = new UnregulatedMotorImpl(motorIndex);
        motor.resetTacho();
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double input = controls.get(0).getValue();
        //logger.info("+++ " + getName() + " i " + input + " r " + reference + " o " + output);

        if (Math.abs(input) > 0 && reference == 0) {
            reference = (int) input;
            motor.resetTacho();
        }
        if (input == 0) {
            reference = 0;
            //CommandHandler.getSingleton().command(motor, CommandHandler.MOTOR_RESET_TACHO, 0);
        }

        output = reference;

        //logger.info("+++ " + getName() + " i " + input + " r " + reference + " o " + output);
        return output;
    }
}
