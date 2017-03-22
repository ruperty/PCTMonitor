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

public class TimeChangeNeuralFunction extends NeuralFunction {

    private static final Logger LOG = Logger.getLogger(TimeChangeNeuralFunction.class.getName());
    private double oldValue = 0;
    private boolean first = true;
    //public int period = 1;

    public TimeChangeNeuralFunction() {
        super();
    }

    public TimeChangeNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        /*
        for (Parameters param : ps) {
            if (param.getName().equals("Period")) {
                period = Integer.valueOf(param.getValue());
            }

        }*/
    }

    @Override
    public void init() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();
        if (controls.size() != 2) {
            throw new Exception("Links to value and to time must be configured");
        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        Double dtime = null;
        //Double dtweight = null;
        Double newValue = controls.get(0).getValue();

        if (first) {
            oldValue = newValue;
            first = false;
        }

        if (controls.size() > 1) {
            dtime = controls.get(1).getValue();
            //dtweight = dtime / period;
        }

        if (dtime != null && dtime > 0) {
            //output = 1000 * dtweight * (newValue - oldValue) / dtime;
            output = 1000 * (newValue - oldValue) / dtime;
        } else {
            output = 0;
        }
        oldValue = newValue;

        return output;
    }
}
