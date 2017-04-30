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
import uk.co.moons.control.functions.BaseControlFunction;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moonsit.learning.error.RMSErrorResponse;

public class RMSErrorResponseNeuralFunction extends NeuralFunction {

    public Integer period = null;
    public Double limit = 0.0;
    public Double scale = 1.0;

    private int counter = 1;
    private Integer updateIndex = null;
    private Integer errorIndex = null;

    private RMSErrorResponse response;

    public RMSErrorResponseNeuralFunction() {
        super();
    }

    public RMSErrorResponseNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Period")) {
                period = Integer.parseInt(param.getValue());
            }
            if (pname.equals("Limit")) {
                limit = Double.parseDouble(param.getValue());
            }
            if (pname.equals("Scale")) {
                scale = Double.parseDouble(param.getValue());
            }
        }
    }

    @Override
    public void init() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();

        for (int i = 0; i < controls.size(); i++) {
            String linkType = links.getType(i);
            if (linkType == null) {
                errorIndex = i;
                continue;
            }
            if (linkType.equals("Update")) {
                updateIndex = i;
            }
        }

        if (updateIndex != null) {
            response = new RMSErrorResponse(scale);
        } else {
            response = new RMSErrorResponse(limit, period);
        }
        /*if (reset == null) {
            reset = 0.0;
        }*/
    }

    @Override
    public double compute() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();
        double error = controls.get(errorIndex).getValue();

        response.update(error);
        if (applyCorrection()) {
            if (updateIndex != null) {
                output = response.getErrorResponse(counter);
                counter = 1;
            } else {
                output = response.getErrorResponse(period);
            }

            //response.reset();
        }

        counter++;
        return output;
    }

    private boolean applyCorrection() throws Exception {
        if (updateIndex != null) {
            if (links.getControlList().get(updateIndex).getValue() == 1) {
                return true;
            }

        } else {
            if (counter % period == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setParameter(String par) throws Exception {
        super.setParameter(par);
        String[] arr = par.split(":");
        if (arr[0].equals("Period")) {
            period = Integer.parseInt(arr[1]);
        }
        if (arr[0].equals("Limit")) {
            limit = Double.parseDouble(arr[1]);
        }
        if (period != null && response != null) {
            response.setLimit(limit, period);
        }
    }

    @Override
    public int getParameterInt(int index) throws Exception {
        Integer rtn = null;
        switch (index) {
            case RMSErrorResponse.PERIOD:
                rtn = period;
                break;
            case RMSErrorResponse.COUNTER:
                rtn = counter;
                break;
            default:
                throw new Exception("Incorrect parameter index (" + index + ") in RMSErrorResponseNeuralFunction.getParameter");
        }

        return rtn;
    }

}
