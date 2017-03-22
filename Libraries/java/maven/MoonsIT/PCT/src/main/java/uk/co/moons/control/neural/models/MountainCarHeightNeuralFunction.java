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
package uk.co.moons.control.neural.models;

import java.util.logging.Logger;
import uk.co.moons.control.neural.NeuralFunction;
import uk.co.moons.control.neural.models.mountaincar.MountainCarSingleton;

public class MountainCarHeightNeuralFunction extends NeuralFunction {

    private static final Logger LOG = Logger.getLogger(MountainCarHeightNeuralFunction.class.getName());

    private MountainCarSingleton singleton;

    public MountainCarHeightNeuralFunction() {
        super();
        singleton = MountainCarSingleton.getInstance();
    }

    @Override
    public double compute() {

        output = singleton.getHeightAtPosition(singleton.getPosition());

        return output;
    }
    @Override
    public void close() throws Exception {
        if (singleton != null) {
            singleton = null;
        }
        LOG.info("+++ close");
    }
}
