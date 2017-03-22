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
public class MotorReadNeuralFunction extends BaseMotorNeuralFunction {
    private static final Logger logger = Logger.getLogger(MotorReadNeuralFunction.class.getName());

    //private long timePrevious = 0;
    //private int countPrevious = 0;
    private MotorHandler mh = null;
    
   
    public MotorReadNeuralFunction(List<Parameters> ps) throws Exception {
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
        //logger.info("+++ dir "+motorIndex+" "+ motor.getDirection());
        output = motor.getSpeed() * motor.getDirection();
        return output;
    }

    /*
    private double getSpeedFromCount() {
        int countNow = motor.getCount();
        long timeNow = System.currentTimeMillis();
        long timeDiff = timeNow - timePrevious;
        double countDiff = countNow - countPrevious;
        double speed = 0;
        if (countPrevious == 0) {
            timePrevious = timeNow;
            countPrevious = countNow;
            return 0;
        }
        timePrevious = timeNow;
        countPrevious = countNow;

        if (timeDiff > 0) {
            speed = 1000 * countDiff / timeDiff;
        }

        if (speed > 900) {
            speed = 900;
        }
        if (speed < -900) {
            speed = -900;
        }

        return speed;
    }*/

    @Override
    public void close() throws Exception {
        logger.info("+++ close, doing nothing");
     }
}
