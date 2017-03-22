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

import java.util.ArrayList;
import java.util.List;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;

/**
 * <b>Hypotenuse function</b>
 *
 * <p>
 * The square root of the sum of squares of a set of inputs.
 *
 *
 * <p>
 * The configuration parameters to the function are as follows:
 * <br>
 * <b>none</b>
 *
 * @author Rupert Young <rupert@moonsit.co.uk>
 * @version 1.0
 */
public class HypotenuseNeuralFunction extends NeuralFunction {

    public HypotenuseNeuralFunction() {
        super();
    }

    public HypotenuseNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
    }

    

    @Override
    public double compute() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();

        double sum = 0;
        int ctr = 0;
        for (BaseControlFunction control : controls) {
            double value = control.getValue();
            if (Math.abs(value) == Double.POSITIVE_INFINITY) {
                continue;
            }
            sum += value * value;
        }

        output = Math.sqrt(sum);
        return output;
    }

}
