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
 * <b>Percentage filter function</b>
 *
 * <p>
 * Filters a value that is within a percentage of a target value.
 *
 * <p>
 * If the input is within a configured percentage of a target value that percentage is output.
 * The percentage difference is scaled between 1 and 1-scale, e.g. if scale is 0.2 
 * then values are scaled between 1 and 0.8
 * <p>
 * The configuration parameters to the function are as follows:
 * <p>
 * <b>Target</b> - the target value (type Double). Mandatory. <br>
 * <b>Percentage</b> - the percentage, 0 - 100 (type Double). Mandatory. <br>
 * <b>Scale</b> - the scale (type Double). Default is 1 <br>
 *
 * @author Rupert Young <rupert@moonsit.co.uk>
 * @version 1.0
 */


public class PercentageFilterNeuralFunction extends NeuralFunction {

    public Double target = null;
    public Double percentage = null;
    public Double scale = 1d;

    public PercentageFilterNeuralFunction() {
        super();
    }

    public PercentageFilterNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        for (Parameters param : ps) {
            if (param.getName().equals("Target")) {
                target = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("Percentage")) {
                percentage = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("Scale")) {
                scale = Double.valueOf(param.getValue());
            }
        }
        if (target == null) {
            throw new Exception("Target null for PercentageFilterNeuralFunction");
        }
        if (percentage == null) {
            throw new Exception("Percentage null for PercentageFilterNeuralFunction");
        }

    }

    // scale - scales the percentage difference between 1 and 1-scale, e.g. if scale is 0.2 
    // then values are scaled between 1 and 0.8
    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double value = controls.get(0).getValue();

        double diff = target - value;
        if (Math.abs(diff) <= percentage) {
            output = 1 - (Math.abs(diff) / percentage) * scale;
        } else {
            output = 0;
        }

        return output;
    }
    /*
    double diff = target / value - 1;
    if (Math.abs(diff) <= percentage) {
    double sign = Math.signum(diff);
    output = sign * (1 - (Math.abs(diff) / percentage));
    } else {
    output = 0;
    }
     */
}
