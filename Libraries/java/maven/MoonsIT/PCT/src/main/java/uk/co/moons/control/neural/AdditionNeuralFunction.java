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

public class AdditionNeuralFunction extends NeuralFunction {

    public int sign = 1;

    public AdditionNeuralFunction() throws Exception {
        super();
    }

    public AdditionNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Sign")) {
                sign = Integer.parseInt(param.getValue());
                break;
            }
        }
    }

    
    @Override
    public void verifyConfiguration() throws Exception {
        if (links.getControlList().size() != 2) {
            throw new Exception("AdditionNeuralFunction requires two links, has " + links.getControlList().size());
        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        output = sign * (controls.get(0).getValue() + controls.get(1).getValue());
        return output;
    }
}
