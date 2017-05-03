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
public class SmoothNeuralFunction extends NeuralFunction {

    public Double smoothness = null;
    public Double old = null;

    private Integer resetIndex = null;
    private Integer activeIndex = null;
    private Integer dataIndex = null;
    

    public SmoothNeuralFunction() throws Exception {
        super();
        if (smoothness == null) {
            throw new Exception("Smoothness parameter is null in SmoothNeuralFunction");
        }

    }

    public SmoothNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Smoothness")) {
                smoothness = Double.parseDouble(param.getValue());
            }
            if (pname.equals("Old")) {
                old = Double.parseDouble(param.getValue());
            }
        }
        if (smoothness == null) {
            throw new Exception("Smoothness parameter is null in SmoothNeuralFunction");
        }
    }

    @Override
    public void init() throws Exception {
        super.init();
        List<BaseControlFunction> controls = links.getControlList();

        for (int i = 0; i < controls.size(); i++) {
            String linkType = links.getType(i);// controls.get(i).getLinkType();
            if (linkType == null) {
                dataIndex=i;
                continue;
            }
            //LOG.log(Level.INFO, "LinkType {0}", linkType);
            if (linkType.equals("Reset")) {
                resetIndex = i;
            }
            if (linkType.equals("Active")) {
                activeIndex = i;
            }
        }
    }

    private void reset() {
        List<BaseControlFunction> controls = links.getControlList();

        Double resetLink = null;
        if (resetIndex != null) {
            resetLink = controls.get(resetIndex).getValue();
        }

        if (resetLink != null && resetLink == 1) {
            old = null;
        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double newVal = controls.get(dataIndex).getValue();
        reset();

        if (activeIndex != null) {
            Double activeLink = controls.get(activeIndex).getValue();
            if (Math.abs(activeLink) == Double.POSITIVE_INFINITY) {
                newVal = Double.POSITIVE_INFINITY;
            }
        }

        if (Math.abs(newVal) == Double.POSITIVE_INFINITY) {
            old = null;
            output = Double.POSITIVE_INFINITY;
        } else {
            if (old == null) {
                output = newVal;
            } else {
                output = RMath.smooth(newVal, old, smoothness);
            }
            old = output;
        }
        return output;
    }

    @Override
    public String getParametersString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Smoothness").append(":");
        sb.append(smoothness).append("_");

        return sb.toString();
    }

     @Override
    public double getParameter() {
        return smoothness;
    }

    @Override
    public void setParameter(double par) {
        smoothness = par;
    }
    
    @Override
    public void pause() throws Exception {
        old = null;
    }
}
