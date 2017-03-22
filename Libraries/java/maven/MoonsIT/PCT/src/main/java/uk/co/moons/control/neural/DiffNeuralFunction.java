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

import uk.co.moons.math.RMath;

public class DiffNeuralFunction extends NeuralFunction {

    private double newval;
    private double oldval;
    private double weight;

    public DiffNeuralFunction(double w) {
        weight = w;
    }

    @Override
    public double compute() {
        output = RMath.smooth(newval, oldval, 0.3);
        return output;
    }
}
