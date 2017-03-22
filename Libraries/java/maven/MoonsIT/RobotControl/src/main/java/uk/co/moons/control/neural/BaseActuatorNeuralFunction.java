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
import uk.co.moons.actuators.impl.BaseActuatorImpl;

/**
 *
 * @author ReStart
 */
public class BaseActuatorNeuralFunction extends NeuralFunction {

    private static final Logger LOG = Logger.getLogger(BaseActuatorNeuralFunction.class.getName());

    protected BaseActuatorImpl actuator;

    public BaseActuatorNeuralFunction() {
    }

    public BaseActuatorNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
    }

    public BaseActuatorImpl getActuator() {
        return actuator;
    }

    
}
