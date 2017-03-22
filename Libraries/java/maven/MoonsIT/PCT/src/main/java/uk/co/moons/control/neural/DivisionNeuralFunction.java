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

public class DivisionNeuralFunction extends NeuralFunction {

    public int sign = 1;

    public DivisionNeuralFunction() {
        super();
    }

    public DivisionNeuralFunction(List<Parameters> ps) throws Exception {
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
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double a = controls.get(0).getValue();
        double b = controls.get(1).getValue();


        if (Math.abs(b) > 0) {
            output = a / b;
        } else {
            output = 0;
        }
        return output;
    }
}
