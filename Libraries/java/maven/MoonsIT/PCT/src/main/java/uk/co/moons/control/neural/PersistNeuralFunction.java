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
import uk.co.moons.control.functions.BaseControlFunction;

/**
 *
 * @author ReStart
 */
public class PersistNeuralFunction extends NeuralFunction {

    private static final Logger LOG = Logger.getLogger(PersistNeuralFunction.class.getName());

    private Integer resetIndex = null;
    private Integer inputIndex = null;

    public PersistNeuralFunction() throws Exception {
        super();
    }

    
    @Override
    public void init() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();

        for (int i = 0; i < controls.size(); i++) {
            String linkType = links.getType(i);// controls.get(i).getLinkType();
            if (linkType == null) {
                inputIndex = i;
                continue;
            }

            if (linkType.equals("Reset")) {
                resetIndex = i;
            }
        }
        output=0;
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double input = controls.get(inputIndex).getValue();

        if (resetIndex != null) {
            Double resetValue = controls.get(resetIndex).getValue();
            if (resetValue == 1) {
                output=0;
            }
        }

        if(output==0){
            if(input>0){
                output=input;
            }
        }
         
        return output;
    }
}
