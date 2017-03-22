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
public class InitialActivationNeuralFunction extends NeuralFunction {

    static final Logger logger = Logger.getLogger(InitialActivationNeuralFunction.class.getName());
    //private int reference = 0;
    //protected String motorIndex = null;

    public InitialActivationNeuralFunction() {
        super();
    }

    public InitialActivationNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        for (Parameters param : ps) {
            if (param.getName().equals("")) {
                //motorIndex = param.getValue();
            }
        }


    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double input = controls.get(0).getValue();
        double fwdInput = controls.get(1).getValue();

        //logger.info("+++ " + getName() + " i " + input + " r " + output + " o " + output);
        if (output > 0 && fwdInput == 0) {
            output = 0;
        }

        if (Math.abs(input) > 0 && output == 0) {
            output = input;
        }
        if (input == 0) {
            output = 0;
        }

        //logger.info("+++ " + getName() + " i " + input + " r " + output + " o " + output);

        return output;
    }
}
