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
//import uk.co.moons.utils.DebugTimes;

/**
 *
 * @author ReStart
 */
public class ParameterNeuralFunction extends NeuralFunction {

    private static final Logger LOG = Logger.getLogger(ParameterNeuralFunction.class.getName());

    public String parameter = null;
    private BaseNeuralFunction parameterNeuralFunction;

    public ParameterNeuralFunction() throws Exception {
        super();
    }

    public ParameterNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Parameter")) {
                parameter = param.getValue();
            }
        }
        if (parameter == null) {
            throw new Exception("Parameter name must be configured in ParameterNeuralFunction");
        }

    }

    @Override
    public void init() throws Exception  {
        List<BaseControlFunction> controls = links.getControlList();

        parameterNeuralFunction = controls.get(0).getNeural();
        try {
            output = parameterNeuralFunction.getParameter(parameter.toLowerCase());
        } catch (IllegalArgumentException|NoSuchFieldException | SecurityException | IllegalAccessException ex) {
            LOG.log(Level.INFO, "Parameter {0}", parameter);
            Logger.getLogger(ParameterNeuralFunction.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    public double compute() throws Exception {

        output = parameterNeuralFunction.getParameter(parameter.toLowerCase());
        return output;
    }

}
