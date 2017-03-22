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
public class HabituateNeuralFunction extends NeuralFunction {

    public Double rate = null;
    public Double threshold = 0.1;
    public boolean proportional = true;

    public HabituateNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();

            if (pname.equals("Rate")) {
                rate = Double.parseDouble(param.getValue());
            }
            if (pname.equals("Threshold")) {
                threshold = Double.parseDouble(param.getValue());
            }
            if (pname.equals("Proportional")) {
                proportional = Boolean.parseBoolean(param.getValue());
            }
        }

        if (rate == null) {
            throw new Exception("Rate missing for HabituateNeuralFunction");
        }
    }

    /*
     * if the input is non-zero and output is zero then output = input
     *      
     * if the input is non-zero or output is non-zero then output is habituated
     * 
     * if output is below a threshold then output is set to zero
     * 
     */
    
    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double input = controls.get(0).getValue();

        if (input == 0 && output == 0) {
            output = 0;
        } else if (output == 0) {
            output = input;
        } else {
            if (proportional) {
                output = output * rate;
            } else {
                output = output - rate;
            }
        }
        if (output < threshold) {
            output = 0;
        }
        return output;
    }
}
