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
 * <b>Banded limit function</b>
 * 
 * <p>Output limited to configured values.
 *  
 * <p>Depending on whether the input is above or below a threshold, in a banded range, the output will be set to one of two provided values.
 * 
 * <p>The configuration parameters to the function are as follows:
 * <p>
 * <b>Threshold</b> - a threshold value (type Double). Mandatory. <br>
 * <b>Upper</b> - the output value if the input is > the threshold plus the bandwidth  (type Double). Mandatory. <br>
 * <b>Lower</b> - the output value if the input is < the threshold minus the bandwidth  (type Double). Mandatory. <br>
 * <b>BandWidth</b> - the width of the threshold range  (type Double). Mandatory. <br>
 * <b>BandValue</b> - the output value if the input is within the banded range  (type Double). Mandatory. <br>
 *
 * @author Rupert Young <rupert@moonsit.co.uk>
 * @version 1.0
 */

public class LimitBandedNeuralFunction extends NeuralFunction {

    public Double threshold = null;
    public Double upper = null;
    public Double lower = null;
    public Double bandwidth = null;
    public Double bandvalue = 0.0;
    public int counteridle = -1;
    private int ctr = 0;

    public LimitBandedNeuralFunction() {
        super();
    }

    public LimitBandedNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        for (Parameters param : ps) {
            if (param.getName().equals("Threshold")) {
                threshold = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("Upper")) {
                upper = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("Lower")) {
                lower = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("BandWidth")) {
                bandwidth = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("BandValue")) {
                bandvalue = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("CounterIdle")) {
                counteridle = Integer.valueOf(param.getValue());
            }
        }
        if (threshold == null) {
            throw new Exception("Threshold null for LimitBandedNeuralFunction");
        }
        if (upper == null) {
            throw new Exception("Upper null for LimitBandedNeuralFunction");
        }
        if (lower == null) {
            throw new Exception("Lower null for LimitBandedNeuralFunction");
        }
        if (bandwidth == null) {
            throw new Exception("BandWidth null for LimitBandedNeuralFunction");
        }

    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double value = controls.get(0).getValue();

        if (ctr++ > counteridle) {
            if (value > threshold + bandwidth) {
                output = upper;
            } else if (value < threshold - bandwidth) {
                output = lower;
            } else {
                output = bandvalue;
            }
        }

        return output;
    }
}
