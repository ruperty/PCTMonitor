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
 * <b>Threshold function.</b>
 *
 * <p>
 * Output limited to by a threshold value.
 *
 * <p>
 * If the input is great than the threshold then input value is 
 * returned, otherwise the threshold value (with same sign as the input) 
 * is returned. If the <b>Alternate</b> parameter is set then the value of that is returned.
 * </p>
 * <p>
 * Or, if the <b>LessThan</b> parameter, is set then opposite logic is applied.
 * </p>
 * The configuration parameters to the function are as follows:
 * <p>
 * <b>Threshold</b> - the threshold value (type Double). Mandatory. <br>
 * <b>Alternate</b> - an alternate output value (type Double). <br>
 * <b>LessThan</b> - true or false (type Boolean).<br>
 * </p>
 * @author Rupert Young <rupert@moonsit.co.uk>
 * @version 1.0
 */



public class ThresholdNeuralFunction extends NeuralFunction {

    public Double threshold = null;
    public Double alternate = null;
    public boolean lessthan = false;

    public ThresholdNeuralFunction(double t) {
        super();
        threshold = t;
    }

    public ThresholdNeuralFunction(int t) {
        super();
        threshold = new Double(t);
    }

    public ThresholdNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Threshold")) {
                threshold = Double.parseDouble(param.getValue());
            }
            if (pname.equals("Alternate")) {
                alternate = Double.parseDouble(param.getValue());
            }
            if (pname.equals("LessThan")) {
                lessthan = Boolean.parseBoolean(param.getValue());
            }
        }

        if (threshold == null) {
            throw new Exception("Threshold missing for ThresholdNeuralFunction");
        }

    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double value = controls.get(0).getValue();
        double sign = Math.signum(value);

        if (lessthan) {
            if (Math.abs(value) < threshold) {
                output = value;
            }else {
                if (alternate == null) {
                    output = sign * threshold;
                } else {
                    output = alternate;
                }
            }
        } else {
            if (Math.abs(value) > threshold) {
                output = value;
            } else {
                if (alternate == null) {
                    output = sign * threshold;
                } else {
                    output = alternate;
                }
            }
        }

        return output;
    }
}
