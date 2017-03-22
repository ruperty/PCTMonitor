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

/**
 *
 * @author ReStart
 */
public class MemoryNeuralFunction extends NeuralFunction {

    public int rate = 0;
    private int ctr = 1;
    private final int FREE = 0;
    private final int USED = 1;
    public int type = FREE;

    public MemoryNeuralFunction() throws Exception {
        super();
    }

    public MemoryNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Rate")) {
                rate = Integer.valueOf(param.getValue());
            }
            if (pname.equals("Type")) {
                if (param.getValue().equalsIgnoreCase("used")) {
                    type = USED;
                }
            }
        }

    }

    @Override
    public void init() {
        getMemory();
    }

    @Override
    public double compute() {

        //DebugTimes.getInstance().mark("memory");
        if (rate == 0) {
            output = getMemory();
        } else {
            if (ctr % rate == 0) {
                output = getMemory();
            }

        }
        ctr++;
        return output;
    }

    private long getMemory() {
        switch (type) {
            case FREE:
                return Runtime.getRuntime().freeMemory();
            case USED:
                return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        }
        return 0;
    }
}
