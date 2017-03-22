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
import java.util.logging.Level;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;

/**
 * <b>Limit function</b>
 *
 * <p>
 * Output limited to between configured values.
 *
 * <p>
 * The output is limited to be between minimum and maximum values.
 * <p>
 * The configuration parameters to the function are as follows:
 * <p>
 * <b>Min</b> - a lower limit of the output value (type Double). Mandatory. <br>
 * <b>Max</b> - an upper limit of the output value (type Double). Mandatory.
 * <br>
 *
 * @author Rupert Young <rupert@moonsit.co.uk>
 * @version 1.0
 */
public class LimitNeuralFunction extends NeuralFunction {

    private static final Logger LOG = Logger.getLogger(LimitNeuralFunction.class.getName());

    public Double max = null;
    public Double min = null;
    public boolean infinity = false;
    private Integer maxIndex = null;
    private Integer minIndex = null;

    public LimitNeuralFunction() throws Exception {
        super();
    }

    public LimitNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        for (Parameters param : ps) {
            if (param.getName().equals("Max")) {
                max = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("Min")) {
                min = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("Infinity")) {
                infinity = Boolean.valueOf(param.getValue());
            }
        }
    }

    @Override
    public void init() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();

        for (int i = 0; i < controls.size(); i++) {
            String linkType = links.getType(i);// controls.get(i).getLinkType();
            if (linkType == null) {
                continue;
            }
            //LOG.log(Level.INFO, "LinkType {0}", linkType);
            if (linkType.equals("Max")) {
                maxIndex = i;
                continue;
            }
            if (linkType.equals("Min")) {
                minIndex = i;
            }

        }

    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double value = controls.get(0).getValue();
        if (maxIndex != null) {
            max = controls.get(maxIndex).getValue();
        }
        if (minIndex != null) {
            min = controls.get(minIndex).getValue();
        }

        if (max != null && value > max) {
            if (infinity) {
                output = Double.POSITIVE_INFINITY;
            } else {
                output = max;
            }
        } else if (min != null && value < min) {
            output = min;
        } else {
            output = value;
        }

        return output;
    }
}
