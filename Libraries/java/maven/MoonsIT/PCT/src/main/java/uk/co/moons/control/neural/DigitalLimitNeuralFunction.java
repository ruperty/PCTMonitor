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
 * <b>Digital limit function</b>
 *
 * <p>
 * Output limited to one of two configured digital values.
 *
 * <p>
 * Depending on whether the input is above or below a threshold the output will
 * be set to one of two provided values.
 * <p>
 * The configuration parameters to the function are as follows:
 * <p>
 * <b>Threshold</b> - the threshold value (type Double). Mandatory. <br>
 * <b>Upper</b> - the output if the input is above the threshold (type Double).
 * Mandatory. <br>
 * <b>Lower</b> - the output if the input is less than (or equal to) the
 * threshold (type Double). Mandatory. <br>
 *
 * @author Rupert Young <rupert@moonsit.co.uk>
 * @version 1.0
 */
public class DigitalLimitNeuralFunction extends NeuralFunction {

    public Double threshold = null;
    public Double upper = null;
    public Double lower = null;
    public Double infinityvalue = null;
    public int counteridle = 0;
    private int ctr = 0;

    public DigitalLimitNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        for (Parameters param : ps) {
            if (param.getName().equals("InfinityValue")) {
                infinityvalue = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("Threshold")) {
                threshold = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("Upper")) {
                upper = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("Lower")) {
                lower = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("CounterIdle")) {
                counteridle = Integer.valueOf(param.getValue());
            }
        }
        if (threshold == null) {
            throw new Exception("Threshold null for DigitalLimitNeuralFunction");
        }
        if (upper == null) {
            throw new Exception("Upper null for DigitalLimitNeuralFunction");
        }
        if (lower == null) {
            throw new Exception("Lower null for DigitalLimitNeuralFunction");
        }

    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double value = controls.get(0).getValue();

        if (ctr++ > counteridle) {
            if (value > threshold) {
                output = upper;
            } else {
                output = lower;
            }

            if (Math.abs(value) == Double.POSITIVE_INFINITY) {
                if (infinityvalue != null) {
                    output = infinityvalue;
                }
            }

        }

        return output;
    }
}
