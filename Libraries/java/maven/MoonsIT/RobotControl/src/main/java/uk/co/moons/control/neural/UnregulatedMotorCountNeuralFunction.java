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
import uk.co.moons.motors.UnregulatedMotorImpl;

/**
 *
 * @author ReStart
 */
public class UnregulatedMotorCountNeuralFunction extends BaseUnregulatedMotorNeuralFunction {

    public double angleoffset = 0;

    public UnregulatedMotorCountNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        for (Parameters param : ps) {
            if (param.getName().equals("AngleOffset")) {
                angleoffset = Double.valueOf(param.getValue());
            }

            if (param.getName().equals("MotorIndex")) {
                motorIndex = param.getValue();
            }

        }
        if (motorIndex == null) {
            throw new Exception("Motor index null for MotorReadNeuralFunction");
        }

    }

    @Override
    public void init() throws Exception {
        super.init();
        motor = new UnregulatedMotorImpl(motorIndex);
        motor.flt();
        String os = System.getProperty("os.name");
        if (os.equalsIgnoreCase("linux")) {
            for (int i = 0; i < 20; i++) {
                motor.resetTacho();
                compute();
                //System.out.println("---> " + i + " count init");
            }
        }
    }

    @Override
    public double compute() {
        output = angleoffset + motor.count();
        return output;
    }

    @Override
    public void pause() throws Exception {
        motor.stop();
        motor.resetTacho();
    }
}
