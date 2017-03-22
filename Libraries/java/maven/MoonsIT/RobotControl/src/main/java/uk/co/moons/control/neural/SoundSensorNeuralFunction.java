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
import uk.co.moons.sensors.SSensor;
import uk.co.moons.sensors.states.BaseSensorState;

/**
 *
 * @author ReStart
 */
public class SoundSensorNeuralFunction extends BaseSensorNeuralFunction {

    private String sSoundType;

    public SoundSensorNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);


        for (Parameters param : ps) {
            if (param.getName().equals("SensorPort")) {
                sensorPort = param.getValue();
            }
            if (param.getName().equals("SoundType")) {
                sSoundType = param.getValue();

            }
        }
        
    }

    @Override
    public void init() throws Exception {
        super.init();
        sensor = new SSensor(sensorPort,sensorState);
        if (sSoundType.equals("DB")) {
            ((SSensor)sensor).setDBA(true);
        } else {
            ((SSensor)sensor).setDBA(false);
        }
       
        postInit();
    }

  

    
}
