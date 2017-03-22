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
import pct.moons.co.uk.schema.layers.Parameters;

public class ConstantNeuralFunction extends NeuralFunction {

    public Double constant;

    public ConstantNeuralFunction() {
    }

    public ConstantNeuralFunction(double o) {
        constant = o;
    }

    public ConstantNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Constant")) {
                constant = Double.parseDouble(param.getValue());
                break;
            }
        }
        if (constant == null) {
            throw new Exception("Constant missing for ConstantNeuralFunction");
        }
        output = constant;
    }

    @Override
    public double compute() {
        output = constant;
        return output;
    }

    @Override
    public String getDataString() {
        return Double.toString(constant);
    }

    
    
    @Override
    public String getParametersString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Constant").append(":");
        sb.append(constant).append("_");

        return sb.toString();
    }
}
