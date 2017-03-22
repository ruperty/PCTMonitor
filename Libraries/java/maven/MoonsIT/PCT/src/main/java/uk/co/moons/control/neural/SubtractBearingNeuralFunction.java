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

public class SubtractBearingNeuralFunction extends NeuralFunction {

    static final Logger logger = Logger.getLogger(SubtractBearingNeuralFunction.class.getName());

    public SubtractBearingNeuralFunction() {
        super();
    }

    public SubtractBearingNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double reference = controls.get(0).getValue();
        double input = controls.get(1).getValue();

        double diff = reference - input;
        double sign = Math.signum(diff);
        double abs = Math.abs(diff);
       
        if (abs > 180) {
            output = -1 * sign * (360 - abs);
        } else {
            output = diff;
        }

        return output;
    }
}
