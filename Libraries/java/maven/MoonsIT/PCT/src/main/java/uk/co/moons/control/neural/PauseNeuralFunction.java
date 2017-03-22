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
//import uk.co.moons.utils.DebugTimes;

/**
 *
 * @author ReStart
 */
public class PauseNeuralFunction extends NeuralFunction {

    public boolean constantrate = false;
    private Long mark = null;
    private int ctr = 1;
    public Long pause = null;

    public PauseNeuralFunction() throws Exception {
        super();
    }

    public PauseNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("ConstantRate")) {
                constantrate = Boolean.valueOf(param.getValue());
            }
            if (pname.equals("Pause")) {
                pause = Long.valueOf(param.getValue());
            }
        }
        if (pause == null) {
            throw new Exception("Pause must be configured");
        }

    }

    @Override
    public double compute() {

        if (mark == null) {
            mark = System.currentTimeMillis();
            //DebugTimes.getInstance().mark("1st pause");
            //output = 0;
            //return output;
        }
        //DebugTimes.getInstance().mark("pause");
        try {
            if (constantrate) {
                output = computePause();
            } else {
                Thread.sleep(pause);
                output = pause;
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(PauseNeuralFunction.class.getName()).log(Level.SEVERE, null, ex);
        }

        return output;
    }

    private long computePause() throws InterruptedException {
        long sleep = 0;
        long elapsed = System.currentTimeMillis() - mark;

        if (pause > elapsed) {
            sleep = (int) (pause - elapsed);
            Thread.sleep(sleep);
        }
        //logger.info(rate + " " + elapsed+ " " +pause);
        mark = System.currentTimeMillis();
        return sleep;
    }

    
    @Override
    public void pause() throws Exception {
            mark = null;
    }
}
