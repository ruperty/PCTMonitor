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

public class FlipNonZeroNeuralFunction extends NeuralFunction {

    public Double valuea = null;
    public Double valueb = null;

    public FlipNonZeroNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        for (Parameters param : ps) {
            if (param.getName().equals("ValueA")) {
                valuea = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("ValueB")) {
                valueb = Double.valueOf(param.getValue());
            }
        }
        if (valuea == null) {
            throw new Exception("valuea null for FlipNonZeroNeuralFunction");
        }
        if (valueb == null) {
            throw new Exception("valueb null for FlipNonZeroNeuralFunction");
        }

    }

    /*
     * If the input is 0 then the output is 0
     * 
     * If the input is 1 then no change to output, unless it is 0 then change to valuea
     * 
     * If the input is -1 then change output to other value, unless it is 0 then change to valuea    
     * 
     */
    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double input = controls.get(0).getValue();

        if (input == 0) {
            output = 0;
        } else if (input < 0) {
            if (output == valuea) {
                output = valueb;
            } else {
                output = valuea;
            }
        } else {
            output = valuea;
        }

        return output;
    }
}
