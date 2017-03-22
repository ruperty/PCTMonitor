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
public class ScalingNeuralFunction extends NeuralFunction {

    public Double offset = 0.0;
    public Double scale = null;

    public ScalingNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Scale")) {
                scale = Double.parseDouble(param.getValue());
            }
            if (pname.equals("Offset")) {
                offset = Double.parseDouble(param.getValue());
            }
        }
        if (scale == null) {
            throw new Exception("Scale missing for ScalingNeuralFunction");
        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double input =controls.get(0).getValue();
        double sign = Math.signum(input);
        output = sign * offset + input * scale;

        return output;
    }
}
