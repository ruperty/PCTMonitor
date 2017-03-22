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
import uk.co.moons.motors.MotorImpl;

/**
 *
 * @author ReStart
 */
public class MotorRotateNeuralFunction extends BaseMotorNeuralFunction {

    static final Logger logger = Logger.getLogger(MotorRotateNeuralFunction.class.getName());
    public Double speed = 200.0;

    public MotorRotateNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {

            if (param.getName().equals("MotorIndex")) {
                motorIndex = param.getValue();
            }
            if (param.getName().equals("Speed")) {
                speed = Double.parseDouble(param.getValue());
            }
            if (param.getName().equals("MotorType")) {
                motorType = param.getValue();
            }
        }

        if (motorIndex == null) {
            throw new Exception("Motor index null for MotorRotateNeuralFunction");
        }

    }

    @Override
    public void init() {
        motor = new MotorImpl(motorIndex, motorType);

        motor.motorRegulation();
        motor.resetTacho();
        motor.setSpeed(speed.intValue());

    }

    @Override
    public double compute() {
        //init();

        List<BaseControlFunction> controls = links.getControlList();
        Double input = controls.get(0).getValue();

        output = input;

        if (Math.abs(output) > 0) {
            motor.setSpeed(speed.intValue());
            motor.rotate((int) output);
        }
        //logger.info("+++ " + getName() + " " + output);

        //System.out.println("count " + CommandHandler.getSingleton().command(motor, CommandHandler.MOTOR_COUNT, 0));
        return output;

    }

    @Override
    public void close() throws Exception {
        if (motor != null) {
            motor.rotateTo(0);
            motor.close();
        }
    }
}
