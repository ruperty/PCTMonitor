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
 * <b>Update filter function</b>
 *
 * <P>
 * If input is different from the existing, previous, value it is updated with
 * the new value.
 *
 *
 *
 * @author Rupert Young
 * @version 1.0
 */
public class UpdateFilterNeuralFunction extends NeuralFunction {

    public Double value = null;

    public UpdateFilterNeuralFunction() throws Exception {
        super();
    }

    public UpdateFilterNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double input = controls.get(0).getValue();
        
        if (Math.abs(input) != Double.POSITIVE_INFINITY && input != output) {
            output = input;
        }

        return output;
    }
}
