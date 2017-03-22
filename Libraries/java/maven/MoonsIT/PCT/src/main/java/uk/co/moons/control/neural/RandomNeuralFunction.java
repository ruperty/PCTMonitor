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

/**
 *
 * @author ReStart
 */
public class RandomNeuralFunction extends NeuralFunction {

    public Double slow = null;
    public Double scale = 1.0;
    public Double min = null;
    public Double max = null;
    //public Double offset = null;

    public RandomNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Scale")) {
                scale = Double.parseDouble(param.getValue());
                continue;
            }
            if (pname.equals("Min")) {
                min = Double.parseDouble(param.getValue());
                continue;
            }
            if (pname.equals("Max")) {
                max = Double.parseDouble(param.getValue());
                continue;
            }
            if (pname.equals("Slow")) {
                slow = Double.parseDouble(param.getValue());
                continue;
            }
            if (pname.equals("Initial")) {
                output = Double.parseDouble(param.getValue());
            }
            //if (pname.equals("Offset")) {
            //  offset = Double.parseDouble(param.getValue());
            //}
        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double input = 1;
        if (controls.size() > 0) {
            input = controls.get(0).getValue();
        }

        if (input > 0) {
            double rand = Math.random() * scale;

            if (slow != null) {
                output += slow * (rand - scale / 2);
            } else {
                output = rand;
            }

            if (min != null) {
                if (output < min) {
                    output = initial;
                }
            }

            if (max != null) {
                if (output > max) {
                    output = initial;
                }
            }

        } else {
            output = 0;
        }

        return output;
    }
}
