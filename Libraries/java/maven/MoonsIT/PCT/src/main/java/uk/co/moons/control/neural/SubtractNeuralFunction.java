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

import uk.co.moons.control.functions.BaseControlFunction;
import java.util.List;
import pct.moons.co.uk.schema.layers.Parameters;

public class SubtractNeuralFunction extends NeuralFunction {

    public int sign = 1;
    public Double inactive = null;
    public Double offset = 0.0;

    public SubtractNeuralFunction() {
        super();
    }

    public SubtractNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Sign")) {
                sign = Integer.parseInt(param.getValue());
                continue;
            }
            if (pname.equals("Inactive")) {
                inactive = Double.parseDouble(param.getValue());
            }
            if (pname.equals("Offset")) {
                offset = Double.parseDouble(param.getValue());
            }
        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double a = controls.get(0).getValue();
        double b = controls.get(1).getValue();
        if (Math.abs(a) == Double.POSITIVE_INFINITY || Math.abs(b) == Double.POSITIVE_INFINITY) {
            output = Double.POSITIVE_INFINITY;
        } else if (inactive != null) {
            if (Math.abs(a) < inactive) {
                output = 0;
            } else {
                output = sign * (a - b + offset);
            }
        } else {
            output = sign * (a - b + offset);
        }
        return output;
    }
}
