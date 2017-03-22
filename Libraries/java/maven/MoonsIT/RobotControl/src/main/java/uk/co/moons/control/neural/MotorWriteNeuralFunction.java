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
import java.util.logging.Level;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moons.motors.MotorImpl;

/**
 *
 * @author ReStart
 */
public class MotorWriteNeuralFunction extends BaseMotorNeuralFunction {

    private static final Logger logger = Logger.getLogger(MotorWriteNeuralFunction.class.getName());

    public final boolean batteryLimits = false;
    public Integer min = -850;
    public Integer max = 850;
    public Integer acceleration = null;
    public Integer upperpositionlimit = null;
    public Integer lowerpositionlimit = null;
    public Integer sign = 1;
    
    private int ctr = 1;
    //private final int mi = 0;

    public MotorWriteNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {

            if (param.getName().equals("MotorIndex")) {
                motorIndex = param.getValue();
            }
            if (param.getName().equals("Min")) {
                min = Integer.valueOf(param.getValue());
            }
            if (param.getName().equals("Max")) {
                max = Integer.valueOf(param.getValue());
            }
            if (param.getName().equals("MotorType")) {
                motorType = param.getValue();
            }
            if (param.getName().equals("Acceleration")) {
                acceleration = Integer.valueOf(param.getValue());
            }
            if (param.getName().equals("LowerPositionLimit")) {
                lowerpositionlimit = Integer.valueOf(param.getValue());
            }
            if (param.getName().equals("UpperPositionLimit")) {
                upperpositionlimit = Integer.valueOf(param.getValue());
            }
            if (param.getName().equals("Sign")) {
                sign = Integer.valueOf(param.getValue());
            }
        }

        if (motorIndex == null) {
            throw new Exception("Motor index null for MotorWriteNeuralFunction");
        }

    }

    @Override
    public void init() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();
        if (controls.size() > 1) {
            if (upperpositionlimit == null || lowerpositionlimit == null) {
                throw new Exception("For MotorWriteNeuralFunction both upperpositionlimit and lowerpositionlimit need to be defined");
            }
        }

        motor = new MotorImpl(motorIndex, motorType);
        motor.motorRegulation();
        if (acceleration != null) {
            motor.setAcceleration(acceleration);
        }
        if (batteryLimits) {
            setMax();
        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        Double input = sign*controls.get(0).getValue();
        Double pos = null;
        if (controls.size() > 1) {
            pos = controls.get(1).getValue();
        }
        output = input;

        if (output < 0) {
            output = Math.ceil(output);
            limits();
            if (pos != null) {
                if (pos > lowerpositionlimit) {
                } else {
                    output = 0;
                }
            }
            motor.setSpeed((int) Math.abs(output));
            motor.setBackward();
        } else {
            output = Math.floor(output);
            limits();
            if (pos != null) {
                if (pos < upperpositionlimit) {
                } else {
                    output = 0;
                }
            }
            motor.setSpeed((int) Math.abs(output));
            motor.setForward();
        }
        ctr++;
        return output;

    }

    private void setMax() {
        max = (int) motor.getMaxSpeed();
        min = -max;
    }

    @Override
    public void setParameter(String par) {
        logger.log(Level.INFO, "MotorWriteNeuralFunction{0}", par);
        super.setParameter(par);
        if (motor != null) {
            String[] arr = par.split(":");
            if (arr[0].equals("Acceleration")) {
                motor.setAcceleration(Integer.valueOf(arr[1]));
            }
        }
    }

    private void limits() {

        if (batteryLimits) {
            if (ctr % 1000 == 0) {
                setMax();
            }
        }

        if (output < min) {
            output = min;
        }
        if (output > max) {
            output = max;
        }
    }

}
