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
import uk.co.moonsit.utils.timing.Time;

public class ControlFunction extends BaseControlFunction {

    public ControlFunction() {
    }

    public ControlFunction(BaseNeuralFunction n) {
        neural = n;
        
        Double initial = neural.getInitial();
        if(initial!=null){
            value=initial;
        }
    }

    public Double getX() {
        String time = neural.getTime();
        if (time == null) {
            return null;
        } else {
            Time dtime = new Time(time);
            return new Double(dtime.getTime());
        }
    }
}
