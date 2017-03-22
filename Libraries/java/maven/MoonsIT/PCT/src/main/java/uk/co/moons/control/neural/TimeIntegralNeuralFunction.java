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

public class TimeIntegralNeuralFunction extends NeuralFunction {

    private static final Logger LOG = Logger.getLogger(TimeIntegralNeuralFunction.class.getName());
    public Double tolerance = null;
    public Double gain = 1.0;
    public Boolean resetlink = false;
    private Long mark = null;
    private int dtimeIndex = -1;
    private int resetIndex = -1;

    public TimeIntegralNeuralFunction() {
        super();
    }

    public TimeIntegralNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("ResetLink")) {
                resetlink = Boolean.valueOf(param.getValue());
            }
            if (param.getName().equals("Tolerance")) {
                tolerance = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("Gain")) {
                gain = Double.valueOf(param.getValue());
            }
        }

        if (reset == null) {
            LOG.log(Level.INFO, "+++ reset is null");
        } else {
            LOG.log(Level.INFO, "+++ reset is {0} ", reset);
        }
        LOG.warning("Something wrong with resets here, ");
    }

    @Override
    public void init() throws Exception {
        int ctr = 0;
        for (BaseControlFunction control : links.getControlList()) {
            if (control.getName().contains("Rate")) {
                dtimeIndex = ctr;
            }
            ctr++;
        }

        List<BaseControlFunction> controls = links.getControlList();
        if (dtimeIndex < 0 && controls.size() == 2) {
            resetIndex = 1;
        }
        if (dtimeIndex == 1 && controls.size() == 3) {
            resetIndex = 2;
        }
        if (dtimeIndex == 2 && controls.size() == 3) {
            resetIndex = 1;
        }
        LOG.log(Level.INFO, "dtimeIndex {0} resetIndex{1}", new Object[]{dtimeIndex, resetIndex});
        System.currentTimeMillis();
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        Double input = controls.get(0).getValue();
        Double resetLink = null;
        if (resetIndex >= 0) {
            resetLink = controls.get(resetIndex).getValue();
        }

        double dtime;
        if (dtimeIndex >= 0) {
            dtime = controls.get(dtimeIndex).getValue() / 1000;
        } else {
            if (mark == null) {
                mark = System.currentTimeMillis();
            }
            Long time = System.currentTimeMillis();
            dtime = (time - mark) / 1000.0;
            mark = time;
        }
        if (resetLink != null && resetLink == 0) {
            output = 0;
        } else {
            //System.out.print("ba "+ output);
            output += gain * input * dtime;
            //System.out.println(" "+ input + " "+ dtime+ " " + output);

            if (resetlink) {
                if (Math.abs(input) < tolerance) {
                    output = 0.0;
                }
            }
            if (tolerance != null && Math.abs(output) < tolerance) {
                output = 0.0;
            }
        }

        return output;
    }
}
