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
package uk.co.moons.control.neural.models;

import uk.co.moons.control.functions.BaseControlFunction;
import java.util.List;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.neural.NeuralFunction;

public class ForceModelNeuralFunction extends NeuralFunction {

    //public Double mass = null;
    //public Double time = null;
    public Double velocity = 0.0;

    public ForceModelNeuralFunction() {
        super();
    }

    public ForceModelNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            
            if (pname.equals("Velocity")) {
                velocity = Double.parseDouble(param.getValue());
            }
        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double time = controls.get(0).getValue();
        double force = controls.get(1).getValue();
        double mass = controls.get(2).getValue();

        output = velocity + time * force / mass;
        velocity = output;

        return output;
    }
}
