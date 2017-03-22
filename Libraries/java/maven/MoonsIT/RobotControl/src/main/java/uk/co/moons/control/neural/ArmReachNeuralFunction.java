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
public class ArmReachNeuralFunction extends NeuralFunction {


    public double length = 1;

    public ArmReachNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("Length")) {
                length = Double.valueOf(param.getValue());
            }
        }
    }

    // reach = 2 * length * sin(beta / 2)
    // where reach equals shoulder to hand
    // beta = angle at elbow
    // length = length of shoulder to elbow and elbow to hand
    
    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        Double input = controls.get(0).getValue();
        output = 2 * length * Math.sin(Math.toRadians(input / 2));

        return output;
    }
}
