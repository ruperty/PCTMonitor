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

public class SequenceOutputNeuralFunction extends NeuralFunction {

    public SequenceOutputNeuralFunction() throws Exception {
        super();
    }

    public SequenceOutputNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

    }

    @Override
    public void checkLinks() throws Exception {
        String tname = this.getName();
        String rname = tname.replace("Output", "Reference");

        BaseControlFunction rlink = getExternalControlFunction(rname);
        links.getControlList().add(rlink);

    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double error = controls.get(0).getValue();
        double ref = controls.get(1).getValue();

        double max = ref + 0.1;
        output = ref - error + 1;
        if (output > max) {
            output = max;
        }
        return output;
    }
}
