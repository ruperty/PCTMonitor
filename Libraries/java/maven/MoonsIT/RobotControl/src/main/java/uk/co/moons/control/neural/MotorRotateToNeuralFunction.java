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
public class MotorRotateToNeuralFunction extends BaseMotorNeuralFunction {

    static final Logger logger = Logger.getLogger(MotorRotateToNeuralFunction.class.getName());
    
    protected String motorIndex = null;
    public Double speed = 200.0;
    public Double min = null;
    public Double max = null;
    private int sign = 1;
    private double previous = 0;

    public MotorRotateToNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {

            if (param.getName().equals("MotorIndex")) {
                motorIndex = param.getValue();
            }
            if (param.getName().equals("Speed")) {
                speed = Double.parseDouble(param.getValue());
            }
            if (param.getName().equals("Min")) {
                min = Double.parseDouble(param.getValue());
            }
            if (param.getName().equals("Max")) {
                max = Double.parseDouble(param.getValue());
            }
            if (param.getName().equals("Sign")) {
                sign = Integer.parseInt(param.getValue());
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

        output = limits(sign * input);

        if (Math.abs(output) > 0 && changed()) {
            motor.setSpeed(speed.intValue());
            motor.rotateTo((int) output);
            previous = output;
        }
        //logger.info("+++ " + getName() + " " + output);

        //System.out.println("count " + CommandHandler.getSingleton().command(motor, CommandHandler.MOTOR_COUNT, 0));

        return output;
    }

    private boolean changed() {
        //System.out.println(this.getName() + " " + output + " " + previous);
        if (Math.abs(Math.round(output - previous)) > 0) {
            return true;
        }
        return false;
    }

    private double limits(double val) {

        if (min != null && val < min) {
            val = min;
        }

        if (max != null && val > max) {
            val = max;
        }
        return val;
    }

    @Override
    public void close() throws Exception {
        if (motor != null) {
            motor.rotateTo(0);
            motor.close();
        }
    }
}
