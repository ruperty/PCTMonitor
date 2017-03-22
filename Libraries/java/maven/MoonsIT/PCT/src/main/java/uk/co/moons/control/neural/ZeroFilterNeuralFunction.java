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
 * <b>Zero filter function
 *
 * <P>
 * If input is zero the output is set to the configured values, otherwise the
 * output equals the input.
 *
 * <p>
 * The configuration parameters to the function are as follows:
 * <br>
 * <b>Value</b> - the value (type Double). Mandatory.
 *
 * @author Rupert Young
 * @version 1.0
 */
public class ZeroFilterNeuralFunction extends NeuralFunction {

    public Double value = null;

    public ZeroFilterNeuralFunction() throws Exception {
        super();
    }

    public ZeroFilterNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        for (Parameters param : ps) {
            if (param.getName().equals("Value")) {
                value = Double.valueOf(param.getValue());
            }
        }
        if (value == null) {
            throw new Exception("Value null for LimitNeuralFunction");
        }

    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double input = controls.get(0).getValue();

        if (input == 0) {
            output = value;
        } else {
            output = input;
        }

        return output;
    }
}
