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

//import uk.co.moons.control.neural.*;


public interface InterfaceControlFunction {

    public void setValue(double e);
    public double getValue();
    public double update()throws Exception;
    public Double getX();
    
}
