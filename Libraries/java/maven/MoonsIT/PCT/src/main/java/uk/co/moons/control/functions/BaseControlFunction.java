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
package uk.co.moons.control.functions;

import uk.co.moons.control.neural.BaseNeuralFunction;

public abstract class BaseControlFunction implements InterfaceControlFunction {

    protected double value;
    protected BaseNeuralFunction neural;
    protected String name;
    protected boolean active = true;
    //protected String linkType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    BaseControlFunction() {
    }

    public BaseNeuralFunction getNeural() {
        return neural;
    }

    public void setActive(boolean active) throws Exception {
        if (!active) {
            neural.deactivate();
        }
        this.active = active;
    }

    /*
     public String getLinkType() {
     return linkType;
     }

     public void setLinkType(String linkType) {
     this.linkType = linkType;
     }
     */
    @Override
    public double update() throws Exception {
        try {
            if (active) {
                if (!neural.isDisabled()) {
                    value = neural.compute();
                }
            } else {
                value = 0;
            }
        } catch (Exception ex) {
            throw new Exception("Exception in control " + getName() + " " + ex.toString());
        }
        return value;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public void setValue(double i) {
        value = i;
    }
}
