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

//import org.ros.node.ConnectedNode;
import pct.moons.co.uk.schema.layers.Parameters;

public abstract class BaseROSSensorNeuralFunction extends
        BaseSensorNeuralFunction {

    public BaseROSSensorNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
    }

    /*
	public void init(ConnectedNode node) throws Exception {
		super.init();

	}*/
}
