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

import uk.co.moons.config.Activation;
import uk.co.moons.control.functions.BaseErrorFunction;
import uk.co.moons.control.functions.BaseInputFunction;
import uk.co.moons.control.functions.BaseOutputFunction;
import uk.co.moons.control.functions.BaseReferenceFunction;
//import uk.co.moons.control.functions.*;

public abstract class BaseController implements ControllerInterface {

    protected Activation activation=null;
    protected BaseInputFunction inputFunction;
    protected BaseErrorFunction errorFunction;
    protected BaseOutputFunction outputFunction;
    protected BaseReferenceFunction referenceFunction;
    protected ControlFunctionCollection errorFunction1;
    protected ControlFunctionCollection referenceFunction1;
    protected ControlFunctionCollection inputFunction1;
    protected ControlFunctionCollection outputFunction1;

    @Override
    public BaseInputFunction getInputFunction() {
        return inputFunction;
    }

    @Override
    public BaseErrorFunction getErrorFunction() {
        return errorFunction;
    }

    @Override
    public BaseOutputFunction getOutputFunction() {
        return outputFunction;
    }

    @Override
    public BaseReferenceFunction getReferenceFunction() {
        return referenceFunction;
    }

    @Override
    public ControlFunctionCollection getErrorFunction1() {
        return errorFunction1;
    }

    @Override
    public ControlFunctionCollection getReferenceFunction1() {
        return referenceFunction1;
    }

    @Override
    public ControlFunctionCollection getInputFunction1() {
        return inputFunction1;
    }

    @Override
    public ControlFunctionCollection getOutputFunction1() {
        return outputFunction1;
    }
    
      
    @Override
    public void stop() throws Exception {
    
    }
}
