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

public class NormalizeNeuralFunction extends NeuralFunction {

    public Double factor = null;

    public NormalizeNeuralFunction() throws Exception {
        super();
    }

    public NormalizeNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Factor")) {
                factor = Double.parseDouble(param.getValue());
            }
        }
        if (factor == null) {
            throw new Exception("NormalizeNeuralFunction requires non-null factor");
        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double value = controls.get(0).getValue();

        output = (factor-value) / factor;

        return output;
    }
}
