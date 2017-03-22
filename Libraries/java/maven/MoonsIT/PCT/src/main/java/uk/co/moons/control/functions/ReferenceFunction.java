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

//import java.util.*;

public class ReferenceFunction extends BaseReferenceFunction {


    public ReferenceFunction(double r) {
	reference = r;
    }

    @Override
    public double getReference() {
        return reference;
    }
 
    @Override
    public void setReference(double r) {
        reference = r;
    }

    @Override
    public double update() {
        return reference;
    }

}
