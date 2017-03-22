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

//import uk.co.moons.control.links.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moonsit.utils.Environment;

public class SleepNeuralFunction extends NeuralFunction {

    public Long sleep = null;
    private long mark;

    public SleepNeuralFunction() {
    }

    public SleepNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Sleep")) {
                sleep = Long.parseLong(param.getValue());
                break;
            }
        }
        if (sleep == null) {
            throw new Exception("Sleep missing for SleepNeuralFunction");
        }
        mark = System.currentTimeMillis();

    }

    @Override
    public double compute() {
        long now = System.currentTimeMillis();
        long nm = (now - Environment.getInstance().getMark());
        //System.out.println(now);
        try {
            long sl = sleep - nm;
            if (sl >= 0) {
                //System.out.println(sl + " " + nm);
                Thread.sleep(sl);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(SleepNeuralFunction.class.getName()).log(Level.SEVERE, null, ex);
        }
        //mark = System.currentTimeMillis();
        //System.out.println(mark);
        return sleep;
    }
}
