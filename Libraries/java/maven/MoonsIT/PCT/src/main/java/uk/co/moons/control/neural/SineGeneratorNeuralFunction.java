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
public class SineGeneratorNeuralFunction extends NeuralFunction {

    public Double amplitude = 1.0;
    public Double frequency = 1.0;
    public Double phase = 0.0;
    public Double xshift = 0.0;
    public Double yshift = 0.0;
    public Double step = 0.0;
    private Double angle = 0.0;
    private Integer resetIndex = null;

    public SineGeneratorNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Amplitude")) {
                amplitude = Double.parseDouble(param.getValue());
            }
            if (pname.equals("Frequency")) {
                frequency = Double.parseDouble(param.getValue());
            }
            if (pname.equals("Phase")) {
                phase = Double.parseDouble(param.getValue());
            }
            if (pname.equals("XShift")) {
                xshift = Double.parseDouble(param.getValue());
            }
            if (pname.equals("YShift")) {
                yshift = Double.parseDouble(param.getValue());
            }
            if (pname.equals("Step")) {
                step = Double.parseDouble(param.getValue());
            }
        }
    }

    @Override
    public void init() throws Exception {
        super.init();
        List<BaseControlFunction> controls = links.getControlList();

        for (int i = 0; i < controls.size(); i++) {
            String linkType = links.getType(i);// controls.get(i).getLinkType();

            if (linkType.equals("Reset")) {
                resetIndex = i;
            }

        }

        reset();
    }

    private void reset() {

        List<BaseControlFunction> controls = links.getControlList();
        Double resetLink;
        if (resetIndex != null) {
            resetLink = controls.get(resetIndex).getValue();
            if (resetLink == 1) {
                angle = 0.0;
            }
        }

    }

    @Override
    public double compute() {
        reset();
        output = yshift + amplitude * Math.sin(Math.toRadians((angle + phase) * frequency + xshift));
        angle += step;

        return output;
    }
    
      @Override
    public String getParametersString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Amplitude").append(":");
        sb.append(amplitude).append("_");

        sb.append("Frequency").append(":");
        sb.append(frequency).append("_");

        sb.append("Phase").append(":");
        sb.append(phase).append("_");

        sb.append("Xshift").append(":");
        sb.append(xshift).append("_");

        sb.append("Yshift").append(":");
        sb.append(yshift).append("_");

        sb.append("Step").append(":");
        sb.append(step);

        return sb.toString();
    }
}

