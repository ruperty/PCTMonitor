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
package uk.co.moons.control;

import uk.co.moons.control.functions.BaseErrorFunction;
import uk.co.moons.control.functions.BaseInputFunction;
import uk.co.moons.control.functions.BaseOutputFunction;
import uk.co.moons.control.functions.BaseReferenceFunction;


public interface ControllerInterface {

    public double iterate() throws Exception;

    public void stop() throws Exception;

    public BaseInputFunction getInputFunction();

    public BaseErrorFunction getErrorFunction();

    public BaseOutputFunction getOutputFunction();

    public BaseReferenceFunction getReferenceFunction();

    public ControlFunctionCollection getErrorFunction1();

    public ControlFunctionCollection getReferenceFunction1();

    public ControlFunctionCollection getInputFunction1();

    public ControlFunctionCollection getOutputFunction1();
}
