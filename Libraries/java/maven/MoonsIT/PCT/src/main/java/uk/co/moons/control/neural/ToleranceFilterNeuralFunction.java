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

public class ToleranceFilterNeuralFunction extends NeuralFunction {

    public Double tolerance = null;

    public ToleranceFilterNeuralFunction() {
        super();
    }

    public ToleranceFilterNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Tolerance")) {
                tolerance = Double.parseDouble(param.getValue());
            }
        }

        if (tolerance == null) {
            throw new Exception("Tolerance missing for ToleranceFilterNeuralFunction");
        }

    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double input = controls.get(0).getValue();

        if (Math.abs(input) == Double.POSITIVE_INFINITY) {
            output = Double.POSITIVE_INFINITY;
        } else if (Math.abs(input) < tolerance) {
            output = 0;
        } else {
            output = input;
        }

        return output;
    }
}
