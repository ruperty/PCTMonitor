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
import java.util.ListIterator;
import java.util.Vector;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;

/**
 *
 * @author ReStart
 */
public class FilterSubtractNeuralFunction extends NeuralFunction {

    private static final Logger logger = Logger.getLogger(FilterSubtractNeuralFunction.class.getName());
    public int sign = 1;
    public double weight = 1;
    public String operator = "LessThan";

    public FilterSubtractNeuralFunction() {
        super();
    }

    public FilterSubtractNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        for (Parameters param : ps) {
            String name = param.getName();
            if (name.equals("Sign")) {
                sign = Integer.parseInt(param.getValue());
            }
            if (name.equals("Weight")) {
                weight = Double.parseDouble(param.getValue());
            }
            if (name.equals("Operator")) {
                operator = param.getValue();
            }

        }
    }

    @Override
    public double compute() {

        double value = 0;
        List<BaseControlFunction> controls = links.getControlList();

        Double a = controls.get(0).getValue();
        Double b = controls.get(1).getValue();

        if (operator.equals("LessThan")) {
            if (b < a) {
                value = weight * sign * (a - b);
            } else {
                value = 0;
            }
        } else {
            if (b > a) {
                value = weight * sign * (b - a);
            } else {
                value = 0;
            }
        }

        return value;
    }

}
