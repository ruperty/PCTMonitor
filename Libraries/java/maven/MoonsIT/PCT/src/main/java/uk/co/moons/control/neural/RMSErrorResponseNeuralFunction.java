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

    public Integer period;
    public Double limit = 100.0;

    private int counter = 1;

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
                if (limit == 0) {
                    limit = null;
                }
            }
        }
        if (period == null) {
            throw new Exception("Period null for RMSErrorResponseNeuralFunction");
        }
    }

    @Override
    public void init() throws Exception {

        response = new RMSErrorResponse(limit, period);
        /*if (reset == null) {
            reset = 0.0;
        }*/
    }

    @Override
    public double compute() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();
        double error = controls.get(0).getValue();

        response.update(error);
        if (counter % period == 0) {
            output = response.getErrorResponse(period);
            //response.reset();
        }

        counter++;
        return output;
    }

    @Override
    public void setParameter(String par) {
        super.setParameter(par);
        String[] arr = par.split(":");
        if (arr[0].equals("Period") || arr[0].equals("Limit")) {
            if (limit == 0) {
                limit = null;
            }
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
