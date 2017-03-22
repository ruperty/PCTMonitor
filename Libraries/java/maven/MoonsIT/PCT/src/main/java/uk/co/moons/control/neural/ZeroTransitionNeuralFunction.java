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

/**
 *
 * @author ReStart
 */
public class ZeroTransitionNeuralFunction extends NeuralFunction {

    private static final Logger logger = Logger.getLogger(ZeroTransitionNeuralFunction.class.getName());

    private double oldValue = 0;
    public String direction = null;
    public boolean positive = true;
    public boolean infinity = false;

    public ZeroTransitionNeuralFunction() throws Exception {
        super();
    }

    public ZeroTransitionNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Direction")) {
                setDirection(param.getValue());
            }
            if (pname.equals("Infinity")) {
                infinity = Boolean.parseBoolean(param.getValue());
            }
        }
    }

    private void setDirection(String direction) {
        positive = direction == null || !direction.equals("Negative");
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double input = controls.get(0).getValue();
        if (infinity) {
            output = Double.POSITIVE_INFINITY;
        } else {
            output = 0;
        }

        if (positive) {
            if (oldValue < 0 && input >= 0) {
                output = 1;
            }
        } else {
            if (oldValue != Double.POSITIVE_INFINITY) {
                if (oldValue > 0 && (input <= 0 || input == Double.POSITIVE_INFINITY)) {
                    output = 1;
                } 
            }
        }
        oldValue = input;

        return output;
    }

    @Override
    public void setParameter(String par) {
        super.setParameter(par);
        String[] arr = par.split(":");
        if (arr[0].equals("Direction")) {
            setDirection(arr[1]);
        }
    }
}
