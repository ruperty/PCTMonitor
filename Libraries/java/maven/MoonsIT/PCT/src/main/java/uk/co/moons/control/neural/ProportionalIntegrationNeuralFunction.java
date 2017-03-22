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
 * <b>A proportional integration function</b>
 * <p>
 * A function that adds a proportion of an input value to itself.
 * <p>
 * The basic function equation is output += gain * input;.
 * <p>
 * The configuration parameters to the function are as follows:
 * <br>
 * <b>Gain</b> - the proportion value, which may be -x to +x (type Double).
 * Mandatory.
 * <b>Min</b> - a lower limit of the output value (type Double). <br>
 * <b>Max</b> - an upper limit of the output value (type Double). <br>
 *
 * @author Rupert Young <rupert@moonsit.co.uk>
 * @version 1.0
 */
public class ProportionalIntegrationNeuralFunction extends NeuralFunction {

    /**
     *
     */
    public Double gain = null;
    public Double min = null;
    public Double max = null;
    public Boolean enableinfinity = false;

    private Integer resetIndex = null;
    private Integer resetValueIndex = null;

    /**
     *
     * @param g
     */
    public ProportionalIntegrationNeuralFunction(int g) {
        super();
        gain = new Double(g);
    }

    /**
     *
     * @param ps
     * @throws Exception
     */
    public ProportionalIntegrationNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("Gain")) {
                gain = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("Min")) {
                min = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("Max")) {
                max = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("EnableInfinity")) {
                enableinfinity = Boolean.valueOf(param.getValue());
            }
        }

        if (gain == null) {
            throw new Exception("Gain null for ProportionalIntegrationNeuralFunction");
        }

    }

    @Override
    public void init() throws Exception {
        super.init();
        List<BaseControlFunction> controls = links.getControlList();

        for (int i = 0; i < controls.size(); i++) {
            String linkType = links.getType(i);// controls.get(i).getLinkType();
            if (linkType == null) {
                continue;
            }
            //LOG.log(Level.INFO, "LinkType {0}", linkType);
            if (linkType.equals("Reset")) {
                resetIndex = i;
                continue;
            }
            if (linkType.equals("ResetValue")) {
                resetValueIndex = i;
            }
        }

        setResetValue(controls);
    }

    private void reset() {
        //if (Math.abs(input) == Double.POSITIVE_INFINITY) {
        //  output = 0;
        //}
        List<BaseControlFunction> controls = links.getControlList();

        Double resetLink = null;
        if (resetIndex != null) {
            resetLink = controls.get(resetIndex).getValue();
        }

        if (resetLink != null && resetLink == 1) {
            setResetValue(controls);
        }
    }

    private void setResetValue(List<BaseControlFunction> controls) {
        Double resetValue = null;
        if (resetValueIndex != null) {
            resetValue = controls.get(resetValueIndex).getNeural().getOutput();
        }
        if (resetValue != null) {
            output = resetValue;
        } else {
            output = 0;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        Double input = controls.get(0).getValue();

        reset();
        if (Math.abs(input) != Double.POSITIVE_INFINITY) {
            if (output == Double.POSITIVE_INFINITY) {
                setResetValue(controls);
            }
            output += gain * input;
            output = limits(output);
        } else if (enableinfinity) {
            output = Double.POSITIVE_INFINITY;
        } else {
            output = 0;
        }

        return output;
    }

    private double limits(double val) {

        if (min != null && val < min) {
            val = min;
        }

        if (max != null && val > max) {
            val = max;
        }
        return val;
    }

    @Override
    public double getParameter() {
        return gain;
    }

    @Override
    public void setParameter(double par) {
        gain = par;
    }
}
