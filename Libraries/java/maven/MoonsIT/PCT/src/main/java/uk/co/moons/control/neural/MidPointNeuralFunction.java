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

public class MidPointNeuralFunction extends NeuralFunction {

    public Double ratio = 0.5;
    private Integer startIndex = 0;
    private Integer targetIndex = 1;

    public MidPointNeuralFunction() {
        super();
    }

    public MidPointNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Ratio")) {
                ratio = Double.parseDouble(param.getValue());
            }
        }
    }

    @Override
    public void verifyConfiguration() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();
        if (controls.size() != 2) {
            throw new Exception(getName() + " requires two links, has " + controls.size());
        }
        
        for (int i = 0; i < controls.size(); i++) {
            String linkType = links.getType(i);
            if (linkType == null) {
                continue;
            }
            //LOG.log(Level.INFO, "LinkType {0}", linkType);
            if (linkType.equalsIgnoreCase("Start")) {
                startIndex = i;
                continue;
            }

            if (linkType.equals("Target")) {
                targetIndex = i;
            }
        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double start = controls.get(startIndex).getValue();
        double target = controls.get(targetIndex).getValue();

        output = ((start - target) / ratio) + target;

        return output;
    }
}
