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
import uk.co.moons.motors.MotorImpl;

/**
 *
 * @author ReStart
 */
public class BaseMotorNeuralFunction extends NeuralFunction {

    //private static final Logger logger = Logger.getLogger(BaseMotorNeuralFunction.class.getName());
    protected MotorImpl motor;
    protected String motorIndex = null;
    protected String motorType = null;

    public BaseMotorNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
    }

    @Override
    public void close() throws Exception {
        //logger.info("+++ close");
        stop();
        if (motor != null) {
            motor.close();
        }
    }

    public Double stop() throws Exception {
        //logger.info("+++ close");
        if (motor != null) {
            motor.stop();
        }
        return 0.0;
    }

    @Override
    public void pause() throws Exception {
        if (motor != null) {
            motor.stop();
        }
    }

}
