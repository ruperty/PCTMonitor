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
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;

public class RoundNeuralFunction extends NeuralFunction {

    public int itype = 0;
    public String type = null;
    
    public RoundNeuralFunction() throws Exception {
        super();
    }

    public RoundNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Type")) {
                type = param.getValue();
                if(type.equalsIgnoreCase("floor"))
                    itype=1;
                if(type.equalsIgnoreCase("ceil"))
                    itype=2;
            }

        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double value = controls.get(0).getValue();

        switch(itype){
            case 0:
                output = Math.round(value);
                break;
            case 1:
                output = Math.floor(value);
                break;
            case 2:
                output = Math.ceil(value);
                break;
        }

        return output;
    }
}
