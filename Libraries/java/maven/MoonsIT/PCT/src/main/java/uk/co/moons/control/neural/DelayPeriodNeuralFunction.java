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

import java.util.ArrayList;
import java.util.List;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;

/**
 *
 * @author ReStart
 */
public class DelayPeriodNeuralFunction extends NeuralFunction {

    public Integer period = 1;
    private List<Double> history = null;

    public DelayPeriodNeuralFunction() throws Exception {
        super();
        history = new ArrayList<>();
    }

    public DelayPeriodNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        history = new ArrayList<>();

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Period")) {
                period = Integer.parseInt(param.getValue());
            }

        }

    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double newValue = controls.get(0).getValue();
        addValue(newValue);

        output = history.get(0);
        return output;
    }

    private void addValue(double value) {
        history.add(value);
        if (history.size() > period+1) {
            history.remove(0);
        }
    }
}
