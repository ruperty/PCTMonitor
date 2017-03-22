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

public class GreaterThanNeuralFunction extends NeuralFunction {

    public Double upper = null;
    public Double lower = null;
    public Double remainder = null;

    public GreaterThanNeuralFunction() {
        super();
    }

    public GreaterThanNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Upper")) {
                upper = Double.valueOf(param.getValue());
            }
            if (pname.equals("Lower")) {
                lower = Double.valueOf(param.getValue());
            }
            if (pname.equals("Remainder")) {
                remainder = Double.valueOf(param.getValue());
            }
        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();

        double a = controls.get(0).getValue();
        double b = controls.get(1).getValue();

        double diff = a - b;

        if (diff > 0) {
            output = upper;
            if (remainder != null) {
                if (Math.abs(diff) < remainder) {
                    output = lower;
                }
            }
        } else {
            output = lower;
        }

        return output;
    }
}
