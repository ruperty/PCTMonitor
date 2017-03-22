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
public class ValueActivateNeuralFunction extends NeuralFunction {

    private static final Logger LOG = Logger.getLogger(ValueActivateNeuralFunction.class.getName());

    private Integer keyIndex = null;
    private Integer valueIndex = null;
    public Double key = null;

    public ValueActivateNeuralFunction() throws Exception {
        super();
    }

    public ValueActivateNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();

            if (pname.equals("Key")) {
                key = Double.parseDouble(param.getValue());
            }
        }

        if (key == null) {
            throw new Exception("Key missing for ValueActivateNeuralFunction");
        }
    }

    @Override
    public void checkLinks() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();

        for (int i = 0; i < controls.size(); i++) {
            String linkType = links.getType(i);// controls.get(i).getLinkType();
            if (linkType == null) {
                valueIndex = i;
                continue;
            }

            if (linkType.equals("Key")) {
                keyIndex = i;
            }
        }
        if (keyIndex == null) {
            throw new Exception("Key link type missing for ValueActivateNeuralFunction");
        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double value = controls.get(valueIndex).getValue();

        if (key == controls.get(keyIndex).getValue()) {
            output = value;
        } else {
            output = Double.POSITIVE_INFINITY;
        }

        return output;
    }
}
