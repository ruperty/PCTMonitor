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

public class ExponentiaNeuralFunction extends NeuralFunction {

    public Double inscale = null;
    public Double outscale = null;
    public Double xshift = null;
    public Double yshift = null;

    public ExponentiaNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("InScale")) {
                inscale = Double.parseDouble(param.getValue());
            }
            if (pname.equals("OutScale")) {
                outscale = Double.parseDouble(param.getValue());
            }
            if (pname.equals("XShift")) {
                xshift = Double.parseDouble(param.getValue());
            }
            if (pname.equals("YShift")) {
                yshift = Double.parseDouble(param.getValue());
            }
        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double input = controls.get(0).getValue();

        double sign = Math.signum(input);

        output = sign * ((Math.exp(input*inscale) + xshift) * outscale + yshift);

        return output;
    }
}
