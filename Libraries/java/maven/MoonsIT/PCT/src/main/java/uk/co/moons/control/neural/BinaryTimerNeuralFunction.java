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
import uk.co.moons.control.functions.BaseControlFunction;

/**
 *
 * @author ReStart
 */
public class BinaryTimerNeuralFunction extends NeuralFunction {

    private Long start = 0l;
    private Long timer = 0l;
    public Long period;
    public Double timeron = 0.0;
    public Double timeroff = 1.0;

    public BinaryTimerNeuralFunction() throws Exception {
        super();
    }

    public BinaryTimerNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Period")) {
                period = Long.parseLong(param.getValue());
            }
            if (param.getName().equals("TimerOn")) {
                timeron = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("TimerOff")) {
                timeroff = Double.valueOf(param.getValue());
            }
        }

    }

    @Override
    public void init() {
        output = timeroff;
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        int input = (int) controls.get(0).getValue();

        if (start > 0) {
            timer = System.currentTimeMillis() - start;
            if (timer > period) {
                output = timeroff;
                start = 0l;
            }
        } else if (input == 1) {
            start = System.currentTimeMillis();
            output = timeron;
        }

        return output;
    }

}
