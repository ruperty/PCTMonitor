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

//import uk.co.moons.math.RMath;
import java.util.List;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;

public class MaximumNeuralFunction extends NeuralFunction {

    public Double attenutationrate = null;

    public MaximumNeuralFunction() throws Exception {
        super();
    }

    public MaximumNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("AttenutationRate")) {
                attenutationrate = Double.parseDouble(param.getValue());
            }

        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double value = controls.get(0).getValue();

        if (value > output) {
            output = value;
        } else if (attenutationrate != null) {
            output = output * attenutationrate;
        }

        return output;
    }
}
