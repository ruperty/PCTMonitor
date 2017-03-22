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
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moons.motors.UnregulatedMotorImpl;
//import uk.co.moons.utils.DebugTimes;

/**
 *
 * @author ReStart
 */
public class MotorPowerNeuralFunction extends BaseUnregulatedMotorNeuralFunction {

    public Integer min = -100;
    public Integer max = 100;

    public MotorPowerNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("MotorIndex")) {
                motorIndex = param.getValue();
            }

        }

        if (motorIndex == null) {
            throw new Exception("Motor index null for MotorPowerNeuralFunction");
        }

    }

    @Override
    public void init() throws Exception {
        super.init();
        motor = new UnregulatedMotorImpl(motorIndex);
        motor.flt();
        String os = System.getProperty("os.name");
        System.out.println(os);
        limits();
        if (os.equalsIgnoreCase("linux")) {
            output = 1;
            for (int i = 0; i < 20; i++) {
                compute();
                //System.out.println("---> " + i + " power init");
            }
            output = 0;
        }

    }

    @Override
    public double compute() {
        //DebugTimes.getInstance().mark("power");
        List<BaseControlFunction> controls = links.getControlList();
        Double input = controls.get(0).getValue();
        output = input;

        //DebugTimes.getInstance().mark("power1");
        limits();
        //DebugTimes.getInstance().mark("power2");

        motor.setPower(Math.abs(output));
        //DebugTimes.getInstance().mark("setPower");
        if (output < 0) {
            motor.setBackward();
        } else {
            motor.setForward();
        }
        //DebugTimes.getInstance().mark("forward");

        return output;
    }

    private void limits() {

        if (output < min) {
            output = min;
        }
        if (output > max) {
            output = max;
        }
    }

}
