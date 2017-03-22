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
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;

public class LocationBearingNeuralFunction extends NeuralFunction {

    static final Logger logger = Logger.getLogger(LocationBearingNeuralFunction.class.getName());

    public LocationBearingNeuralFunction() {
        super();
    }

    public LocationBearingNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double x = controls.get(0).getValue();
        double y = controls.get(1).getValue();

        if (Math.abs(x) == Double.POSITIVE_INFINITY || Math.abs(y) == Double.POSITIVE_INFINITY) {
            output = Double.POSITIVE_INFINITY;
        } else {
            output = Math.toDegrees(Math.atan2(x, y));
        }

        return output;
    }
}
