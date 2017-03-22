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
 *
 * @author ReStart
 */
public class ChangeSignNeuralFunction extends NeuralFunction {

    public ChangeSignNeuralFunction() throws Exception {
        super();
    }

    public ChangeSignNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double input = controls.get(0).getValue();
        //double sign = Math.signum(input);

        //output = input * sign * -1;
        output = input *  -1;

        return output;
    }
}
