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

public class BinaryPulseNeuralFunction extends NeuralFunction {

    public int pulse = 0;
    //public boolean sent = false;

    public BinaryPulseNeuralFunction() {
        super();
    }

    public BinaryPulseNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Pulse")) {
                pulse = Integer.parseInt(param.getValue());
            }
        }
    }

    @Override
    public double compute() {
        if (pulse == 0) {
            List<BaseControlFunction> controls = links.getControlList();
            if (!controls.isEmpty()) {
                pulse = (int) controls.get(0).getValue();
            }
        }

        output = pulse;
        if (pulse == 1) {
            pulse = 0;
        }

        return output;
    }
}
