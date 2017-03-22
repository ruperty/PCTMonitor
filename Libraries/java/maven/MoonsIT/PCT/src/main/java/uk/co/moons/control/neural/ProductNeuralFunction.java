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

public class ProductNeuralFunction extends NeuralFunction {

    public ProductNeuralFunction() {
        super();
    }

    public ProductNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double a = controls.get(0).getValue();
        double b = controls.get(1).getValue();
        if (Math.abs(a) == Double.POSITIVE_INFINITY) {
            output = a;
        } else if (Math.abs(b) == Double.POSITIVE_INFINITY) {
            output = b;
        } else {
            output = a * b;
        }
        return output;
    }
}
