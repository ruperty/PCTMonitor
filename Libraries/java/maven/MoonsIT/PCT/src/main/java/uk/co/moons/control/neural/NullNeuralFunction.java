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

/**
 * <b>Null function</b>
 *
 * <P>
 * Returns zero.
 *
 *
 * @author Rupert Young
 * @version 1.0
 */
public class NullNeuralFunction extends NeuralFunction {

    public NullNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

    }

    @Override
    public double compute() {
        output = 0;

        return output;
    }
}
