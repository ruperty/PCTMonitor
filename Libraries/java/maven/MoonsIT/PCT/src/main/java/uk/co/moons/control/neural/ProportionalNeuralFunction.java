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

//import uk.co.moons.math.RMath;
import java.util.List;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;

public class ProportionalNeuralFunction extends NeuralFunction {

    private static final Logger LOG = Logger.getLogger(ProportionalNeuralFunction.class.getName());

    public Double gain = null;

    public Boolean enableinfinity = false;
    //public Boolean tuning = false;
    //public Double reorganizationgain = 1.0;
    //public Integer reorgdelay = 10;
    //public Double maximumerr = 10.0;
    //public AutoTuneFilter autoTune;

    public ProportionalNeuralFunction() throws Exception {
        super();
        if (gain == null) {
            throw new Exception("Gain null for ProportionalNeuralFunction");
        }
    }

    public ProportionalNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("Gain")) {
                gain = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("EnableInfinity")) {
                enableinfinity = Boolean.valueOf(param.getValue());
            }
            /*
            if (param.getName().equals("Tuning")) {
                tuning = Boolean.valueOf(param.getValue());
            }
            if (param.getName().equals("ReorgDelay")) {
                reorgdelay = Integer.valueOf(param.getValue());
            }
            if (param.getName().equals("ReorganizationGain")) {
                reorganizationgain = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("MaximumErr")) {
                maximumerr = Double.valueOf(param.getValue());
            }*/

        }

        //autoTune = new AutoTuneFilter();
        if (gain == null) {
            throw new Exception("Gain null for ProportionalNeuralFunction");
        }

    }

    @Override
    public double compute() {
        //DebugTimes.getInstance().mark("prop");
        List<BaseControlFunction> controls = links.getControlList();
        double input = controls.get(0).getValue();

        if (Math.abs(input) != Double.POSITIVE_INFINITY) {
            if (Math.abs(gain) > 0) {
                output = input * gain;
            }
        } else if (enableinfinity) {
            output = Double.POSITIVE_INFINITY;
        } else {
            output = 0;
        }
        return output;
    }

    @Override
    public double getParameter() {
        return gain;
    }

    @Override
    public void setParameter(double par) {
        gain = par;
    }

    @Override
    public String getParametersString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Gain").append(":");
        sb.append(gain);

        return sb.toString();
    }
}
