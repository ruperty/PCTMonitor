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
public class HandDistanceNeuralFunction extends NeuralFunction {

    //public double angleoffset = 0;
    public double length = 1;

    public HandDistanceNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("Length")) {
                length = Double.valueOf(param.getValue());
            }
        }
    }

    // distance  = 2 * length * sin (beta/2) * cos(delta)
    // where distance equals horizontal distance from shoulder 
    // alpha = angle at shoulder
    // beta = angle at elbow
    // length = length of shoulder to elbow and elbow to hand = 1
    
    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        Double alpha = controls.get(0).getValue();
        Double beta = controls.get(1).getValue();

        //output = (2 * length * Math.sin(Math.toRadians(beta / 2))) * (Math.cos(Math.toRadians(90-(beta/2)+alpha)));
        output = (2 * length * Math.sin(Math.toRadians(beta / 2))) * (Math.sin(Math.toRadians(delta(alpha, beta))));

        return output;
        
    }
    
    // delta = 270+alpha-beta/2
    
    private double delta(double alpha, double beta){
        
        return 270+alpha-beta/2;
    
    }
}
