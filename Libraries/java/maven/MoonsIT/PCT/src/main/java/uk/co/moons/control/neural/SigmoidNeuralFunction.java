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

/**
 *
 * @author ReStart
 */
public class SigmoidNeuralFunction extends NeuralFunction {

    /*
     protected Double inlimit = null; // l(in)
     protected Double outlimit = null;// l(out)
     protected Double tanscale = null; // s(t)
     protected Double tanlimit = null; // l(t)
     * 
     */
    public Double inscale = null;
    public Double outscale = null;
    public Double xshift = 0.0;
    public Double yshift = null;
    public Integer sign = 1;
    public Double yshiftfactor = null;
    public Double inputtolerance = null;
    public boolean absolute = false;

    // implments x = l(out) . tanh( s(t) . y . l(t)/l(in))
    // inscale - scales the input value, determines steepness of slope
    // outscale - scales the output value, oprionally shifts values
    public SigmoidNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("XShift")) {
                xshift = Double.parseDouble(param.getValue());
            }
            if (pname.equals("YShift")) {
                yshift = Double.parseDouble(param.getValue());
            }
            if (pname.equals("InScale")) {
                inscale = Double.parseDouble(param.getValue());
            }
            if (pname.equals("OutScale")) {
                outscale = Double.parseDouble(param.getValue());
            }
            if (pname.equals("Sign")) {
                sign = Integer.parseInt(param.getValue());
            }
            if (pname.equals("YShiftFactor")) {
                yshiftfactor = Double.parseDouble(param.getValue());
            }
            if (pname.equals("InputTolerance")) {
                inputtolerance = Double.parseDouble(param.getValue());
            }
            if (pname.equals("Absolute")) {
                absolute = Boolean.parseBoolean(param.getValue());
            }
        }

        if (yshiftfactor != null && yshift != null) {
            throw new Exception("YShift and YShiftFactor cannot both be configured");
        }

        if (yshiftfactor == null) {
            yshiftfactor = 1.0;
        }
        if (yshift == null) {
            setYshift(yshiftfactor);
        }

        if (inscale == null) {
            throw new Exception("InScale missing for SigmoidNeuralFunction");
        }
        if (outscale == null) {
            throw new Exception("OutScale missing for SigmoidNeuralFunction");
        }

    }

    private void setYshift(Double yshiftfactor) {
        yshift = yshiftfactor * outscale;

    }

    private void setYshiftfactor(Double yshiftfactor) {
        this.yshiftfactor = yshiftfactor;
        setYshift(yshiftfactor);
    }

    @Override
    public void setParameter(String par) throws Exception {
        super.setParameter(par);
        String[] arr = par.split(":");
        if (arr[0].equals("YShiftFactor")) {
            setYshiftfactor(Double.valueOf(arr[1]));
        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double in = controls.get(0).getValue();
        if (Math.abs(in) == Double.POSITIVE_INFINITY) {
            output = in;
        } else {
            double outsign = 1;
            if (absolute) {
                outsign = Math.signum(in);
                in = Math.abs(in);
            }

            if (inputtolerance != null && Math.abs(in) < inputtolerance) {
                output = 0;
            } else {
                output = outsign * (outscale * Math.tanh(inscale * (in * sign + xshift)) + yshift);
            }
        }
        return output;
    }

    @Override
    public String getParametersString() {
        StringBuilder sb = new StringBuilder();

        sb.append("InScale").append(":");
        sb.append(inscale).append("_");

        sb.append("OutScale").append(":");
        sb.append(outscale).append("_");

        sb.append("XShift").append(":");
        sb.append(xshift).append("_");
        
        sb.append("YShift").append(":");
        sb.append(yshift).append("_");
        
        sb.append("InputTolerance").append(":");
        sb.append(inputtolerance);

        return sb.toString();
    }
    
     @Override
    public void setParameter(double par) {
        outscale = par;
    }

    @Override
    public double getParameter() {
        return outscale;
    }

}
