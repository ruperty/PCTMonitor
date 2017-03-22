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

//import uk.co.moons.logging.Logging;
public class OutputFunction extends BaseControlFunction {

    protected double gain;
    
    public OutputFunction() {
    }

    public OutputFunction(double g, double out) {
        gain = g;
        value = out;
    }

    public OutputFunction(double g) {
        gain = g;
    }

    @Override
    public double update() throws Exception{
        return value;
    }

    public Double getX() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
 