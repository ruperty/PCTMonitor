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

public class StepNeuralFunction extends NeuralFunction {

    private static final Logger logger = Logger.getLogger(StepNeuralFunction.class.getName());
    public Double lower = null;
    public Double upper = null;
    public Long period = null;
    private Long mark = null;
    public String periodtype = null;
    private boolean timePeriods = false;
    private int ctr = 1;

    public StepNeuralFunction() {
        super();
    }

    public StepNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("Period")) {
                period = Long.valueOf(param.getValue());
            }
            if (param.getName().equals("PeriodType")) {
                periodtype = param.getValue();
            }
            if (param.getName().equals("Lower")) {
                lower = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("Upper")) {
                upper = Double.valueOf(param.getValue());
            }
        }
        output = 0;
        if (periodtype != null && periodtype.equalsIgnoreCase("Time")) {
            timePeriods = true;
        }
    }

    @Override
    public double compute() {

        if (periodReached()) {
            if (output == lower) {
                output = upper;
            } else {
                output = lower;
            }
        }

        return output;
    }

    private boolean periodReached() {
        boolean rtn = false;
        if (timePeriods) {
            if (mark == null) {
                mark = System.currentTimeMillis();
            }
            Long time = System.currentTimeMillis();
            Long dtime = time - mark;

            if (dtime > period) {
                mark = time;
                rtn = true;
            }
        } else {
            if (ctr % period == 0) {
                rtn = true;
            }
            ctr++;
        }

        return rtn;
    }

}
