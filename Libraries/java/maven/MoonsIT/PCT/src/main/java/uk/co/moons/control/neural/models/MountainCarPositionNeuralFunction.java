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

import uk.co.moons.control.functions.BaseControlFunction;
import java.util.List;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.neural.NeuralFunction;
import uk.co.moons.control.neural.models.mountaincar.MountainCarSingleton;

public class MountainCarPositionNeuralFunction extends NeuralFunction {

    private static final Logger LOG = Logger.getLogger(MountainCarPositionNeuralFunction.class.getName());

    public boolean random;
    public double factor;
    public long seed;
    private MountainCarSingleton singleton;

    public MountainCarPositionNeuralFunction() {
        super();
        singleton = MountainCarSingleton.getInstance(null, false, 0, 0);
    }

    public MountainCarPositionNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();

            if (pname.equals("Random")) {
                random = Boolean.parseBoolean(param.getValue());
            }
            if (pname.equals("Factor")) {
                factor = Double.parseDouble(param.getValue());
            }
            if (pname.equals("Seed")) {
                seed = Long.parseLong(param.getValue());
            }
        }

        singleton = MountainCarSingleton.getInstance(initial, random, factor, seed);

    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        int action = 1 + (int) controls.get(0).getValue();

        singleton.update(action);

        output = singleton.getPosition();

        return output;
    }

    @Override
    public void setParameter(String par) {
        super.setParameter(par);

        singleton.reset(initial, random, factor, seed);

    }

    @Override
    public void close() throws Exception {
        if (singleton != null) {
            singleton = null;
        }
        LOG.info("+++ close");
    }
}
