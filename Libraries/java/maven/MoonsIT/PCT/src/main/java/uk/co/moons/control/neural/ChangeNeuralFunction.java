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
import java.util.logging.Level;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;

/**
 *
 * @author ReStart
 */
public class ChangeNeuralFunction extends NeuralFunction {

    private static final Logger LOG = Logger.getLogger(ChangeNeuralFunction.class.getName());

    private double oldValue = 0;
    public Boolean absolute = false;
    private Boolean linkinitial = false;
    public double gain = 1;
    private Integer resetIndex = null;
    private Integer inputIndex = null;
    private Integer activeIndex = null;
    private boolean zeroreset = false;

    public ChangeNeuralFunction() throws Exception {
        super();
    }

    public ChangeNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("ZeroReset")) {
                zeroreset = Boolean.parseBoolean(param.getValue());
            }
            if (pname.equals("Absolute")) {
                absolute = Boolean.parseBoolean(param.getValue());
            }
            if (pname.equals("LinkInitial")) {
                linkinitial = Boolean.parseBoolean(param.getValue());
            }
            if (param.getName().equals("Gain")) {
                gain = Double.valueOf(param.getValue());
            }
        }
        if(zeroreset && linkinitial)
            throw new Exception("In ChangeNeuralFunction both zeroreset and linkinitial cannot be true");
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
            //LOG.log(Level.INFO, "LinkType {0}", linkType);
            if (linkinitial && linkType.equals("Initial")) {
                inputIndex = i;
                setInitial(controls);
                continue;
            }

            if (linkType.equals("Reset")) {
                resetIndex = i;
            }
            if (linkType.equals("Active")) {
                activeIndex = i;
            }
        }
    }

    private void setInitial(List<BaseControlFunction> controls) {
        if (linkinitial) {
            oldValue = controls.get(inputIndex).getNeural().getOutput();
        } else {
            oldValue = 0;
        }
    }

    private double reset(double newValue, List<BaseControlFunction> controls) {
        if (resetIndex != null) {
            Double resetValue = controls.get(resetIndex).getValue();
            if (resetValue == 1) {
                if (zeroreset) {
                    oldValue = newValue = 0;
                } else {
                    oldValue = newValue;
                }
            }
        }
        return newValue;
    }

    private boolean isActive(List<BaseControlFunction> controls) {

        if (activeIndex != null) {
            Double activeLink = controls.get(activeIndex).getValue();
            if (Math.abs(activeLink) == Double.POSITIVE_INFINITY) {
                return false;
            }
        }
        return true;
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double newValue = controls.get(inputIndex).getValue();

        newValue = reset(newValue, controls);

        if (Math.abs(newValue) == Double.POSITIVE_INFINITY || !isActive(controls)) {
            setInitial(controls);
            output = 0;
        } else {
            output = gain * (newValue - oldValue);
            oldValue = newValue;
            if (absolute) {
                output = Math.abs(output);
            }
        }
        return output;
    }
}
