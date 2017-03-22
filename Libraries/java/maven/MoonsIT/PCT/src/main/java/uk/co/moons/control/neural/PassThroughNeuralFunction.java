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

public class PassThroughNeuralFunction extends NeuralFunction {

    public PassThroughNeuralFunction() {
        super();
    }

    public PassThroughNeuralFunction(List<Parameters> ps) {
        super(ps);
    }

    @Override
    public double compute() throws Exception {
        //List<BaseControlFunction> controls = links.get(0).getControlList();
        List<BaseControlFunction> controls = links.getControlList();
        output = controls.get(0).getValue();
        return output;
    }

    @Override
    public void close() throws Exception {
    }
}
