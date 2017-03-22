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
import uk.co.moons.math.RMath;
//import uk.co.moons.utils.DebugTimes;

/**
 *
 * @author ReStart
 */
public class RateNeuralFunction extends NeuralFunction {

    private static final Logger logger = Logger.getLogger(RateNeuralFunction.class.getName());
    public boolean mean = false;
    private Long mark = null;
    private int ctr = 1;
    public Double smoothness = null;

    public RateNeuralFunction() throws Exception {
        super();
        initial = 0.0;
    }

    public RateNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        initial = 0.0;
        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Mean")) {
                mean = Boolean.valueOf(param.getValue());
            }
            if (pname.equals("Smoothness")) {
                smoothness = Double.valueOf(param.getValue());
            }
            if (pname.equals("Initial")) {
                initial = Double.valueOf(param.getValue());
            }
        }
        if (mean && (smoothness != null)) {
            throw new Exception("Only one of Mean and Smoothness should be configured");
        }

    }

    @Override
    public void init() {
        if (smoothness != null) {
            RMath.smooth(0, 0, 1);
        }
        output = initial;
        System.currentTimeMillis();
    }

    @Override
    public double compute() {

        if (mark == null) {
            mark = System.currentTimeMillis();
            //DebugTimes.getInstance().mark("1st rate");

            //if(getName().equals("MeanRate")) logger.info("1st " + output + " "+mark);
            return output;
        }
        //DebugTimes.getInstance().mark("rate ");
        long now = System.currentTimeMillis();

        if (smoothness != null) {
            output = RMath.smooth(now - mark, output, smoothness);
        } else {
            output = now - mark;
        }
        //if(getName().equals("MeanRate")) logger.info("n-m " + output+ " "+ctr+ " "+now+" "+mark);

        if (mean) {
            output = output / ctr++;
        } else {
            mark = now;
        }
        //if(getName().equals("MeanRate")) logger.info("nth " + output);
        return output;
    }

    @Override
    public void pause() throws Exception {
        //if (!mean) {
        mark = null;
        //}
        if (mean) {
            ctr = 1;
        }
    }
}
