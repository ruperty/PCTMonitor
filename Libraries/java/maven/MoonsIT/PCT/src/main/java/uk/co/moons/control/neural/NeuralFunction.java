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
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;

public class NeuralFunction extends BaseNeuralFunction {

    private static final Logger LOG = Logger.getLogger(NeuralFunction.class.getName());

    public NeuralFunction() {
        super();
    }

    public NeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
    }

    @Override
    public double compute() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();
        output = controls.get(0).getValue();
        return output;
    }

    

   



    

}
