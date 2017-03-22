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
import uk.co.moons.math.RMath;

/**
 *
 * @author ReStart
 */
public class SmoothChangeNeuralFunction extends NeuralFunction {

    private double oldValue = 0;
    private double oldChange = 0;
    public Double smoothness;

    public SmoothChangeNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();

            if (pname.equals("Smoothness")) {
                smoothness = Double.parseDouble(param.getValue());
            }
            /*
            if (name.equals("OutLimit")) {
            outlimit = Double.parseDouble(param.getValue());
            }
            if (name.equals("TanScale")) {
            tanscale = Double.parseDouble(param.getValue());
            }
            if (name.equals("TanLimit")) {
            tanlimit = Double.parseDouble(param.getValue());
            }
             * */
        }

        if (smoothness == null) {
            throw new Exception("Smoothness missing for SmoothChangeNeuralFunction");
        }
    }

    @Override
    public void init() {

            RMath.smooth(0, 0, 1);
    }
    
    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double newValue = controls.get(0).getValue();
        double newChange = newValue - oldValue;
        oldValue = newValue;

        output = RMath.smooth(newChange, oldChange, smoothness);
        oldChange = output;
        return output;
    }
}
