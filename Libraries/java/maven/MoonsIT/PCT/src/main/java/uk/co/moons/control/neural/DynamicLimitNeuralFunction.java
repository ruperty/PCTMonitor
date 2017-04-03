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

public class DynamicLimitNeuralFunction extends NeuralFunction {

    public Double limit = null;
    public Double tolerance = null;

    private Integer limitIndex = null;
    private Integer dataIndex = null;

    public DynamicLimitNeuralFunction() {
        super();
    }

    public DynamicLimitNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Limit")) {
                limit = Double.parseDouble(param.getValue());
            }
            if (pname.equals("Tolerance")) {
                tolerance = Double.parseDouble(param.getValue());
            }
        }
    }

    @Override
    public void verifyConfiguration() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();

        if (limit != null) {
            dataIndex = 0;
            return;
        }

        for (int i = 0; i < controls.size(); i++) {
            String linkType = links.getType(i);
            if (linkType == null) {
                dataIndex = i;
                continue;
            }
            //LOG.log(Level.INFO, "LinkType {0}", linkType);
            if (linkType.equalsIgnoreCase("Limit")) {
                limitIndex = i;
            }
        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double a = controls.get(dataIndex).getValue();
        double b;
        if (limit != null) {
            b = limit;
        } else {
            b = controls.get(limitIndex).getValue();
        }

        if (tolerance != null && Math.abs(a - b) < tolerance) {
            output = Double.POSITIVE_INFINITY;
        } else {
            if (a > b) {
                output = b;
            } else {
                output = a;
            }
        }
        return output;
    }
}
