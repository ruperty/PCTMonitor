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

import java.util.*;

public abstract class BaseReferenceFunction implements ReferenceInterface {

    double reference = 0;
    HashMap<String, BaseOutputFunction> outputs = null;

    BaseReferenceFunction() {
        outputs = new HashMap<String, BaseOutputFunction>();
    }

    public void addOutput(String name, BaseOutputFunction out) {
        outputs.put(name, out);
    }

    public double getReference() {
        return reference;
    }

    public void setReference(double r) {
        reference = r;
    }

    public double update() {
        return reference;
    }
}
