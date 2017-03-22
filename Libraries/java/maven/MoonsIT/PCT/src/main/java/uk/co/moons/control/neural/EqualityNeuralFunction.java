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

import uk.co.moons.control.functions.BaseControlFunction;
import java.util.List;
import pct.moons.co.uk.schema.layers.Parameters;

public class EqualityNeuralFunction extends NeuralFunction {

    public Double equalvalue = null;
    public Double notequalvalue = null;
    public boolean absolute = false;

    public EqualityNeuralFunction() {
        super();
    }

    public EqualityNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("EqualValue")) {
                equalvalue = Double.parseDouble(param.getValue());
            }
            if (pname.equals("NotEqualValue")) {
                notequalvalue = Double.parseDouble(param.getValue());
            }
            if (pname.equals("Absolute")) {
                absolute = Boolean.parseBoolean(param.getValue());
            }
        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double a = controls.get(0).getValue();
        double b = controls.get(1).getValue();

        if (absolute) {
            a = Math.abs(a);
        }
        if (a == b) {
            output = equalvalue;
        } else {
            output = notequalvalue;
        }

        return output;
    }
}
