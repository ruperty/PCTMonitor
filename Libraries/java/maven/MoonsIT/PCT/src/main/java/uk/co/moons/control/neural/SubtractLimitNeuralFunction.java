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
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;

public class SubtractLimitNeuralFunction extends NeuralFunction {

    static final Logger logger = Logger.getLogger(SubtractLimitNeuralFunction.class.getName());
    public int sign = 1;
    public Double limit = null;

    public SubtractLimitNeuralFunction() {
        super();
    }

    public SubtractLimitNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Sign")) {
                sign = Integer.parseInt(param.getValue());
            }
            if (pname.equals("Limit")) {
                limit = Double.parseDouble(param.getValue());
            }
        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double reference = controls.get(0).getValue();
        double input = controls.get(1).getValue();

        double diff = reference - input;
        output = 0;

        if (sign > 0) {
            if (diff > limit) {
                output = limit;
            } else if (diff > 0) {
                output = diff;
            }
        } else {
            if (reference < 0) {
                if (diff < limit * sign) {
                    output = limit * sign;
                } else if (diff < 0) {
                    output = diff;
                }
            }
        }
        //logger.info("+++ " + getName() + " " + controls.get(0).getValue() + " " + controls.get(1).getValue() + " " + diff + " " + output);
        return output;
    }
}
