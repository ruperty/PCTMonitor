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
package uk.co.moonsit.learning.reorganisation;

import uk.co.moonsit.learning.rate.AdditiveLearningRate;
import uk.co.moonsit.learning.rate.BaseLearningRate;
import uk.co.moonsit.learning.rate.SmoothLearningRate;

/**
 * <b>Integration function.</b>
 *
 * <p>
 * Leaky integrator function.
 *
 * <p>
 * The amplified input is integrated and slowed by a slowing factor.
 *
 * If the input is infinity then zero is output. However, if IgnoreInfinity is
 * true, then the previous output is returned. However, if EnableInfinity is
 * true, then infinity is output.
 *
 * If EnableInfinity is true, output is infinity, and the input returns to a
 * real value then the output is set to the reset value, which comes from a
 * link, if configured.
 *
 * </p>
 * The configuration parameters to the function are as follows:
 * <p>
 * <b>Gain</b> - the amplification factor (type Double). Mandatory. <br>
 * <b>Slow</b> - the slowing factor (type Double). <br>
 * <b>EnableInfinity</b> - true or false (type Boolean).<br>
 * </p>
 *
 * @author Rupert Young <rupert@moonsit.co.uk>
 * @version 1.0
 */
public abstract class BaseReorganisation implements ReorganisationInterface {
    
    protected double delta = 0.025;
    protected boolean continuous = false;
    protected BaseLearningRate learningRate;
    protected double previousErrorResponse;
    
    public BaseReorganisation(double lr, String type) {
        setLRT(lr, type);
    }
    
    public void reset() {
        learningRate.reset();        
    }
    
    public void setAdaptiveFactor(Double delta) {
        this.delta = delta;
    }
    
    public void setContinuous(boolean continuous) {
        this.continuous = continuous;
    }
    
    @Override
    public void setLearningRate(double rate) {
        learningRate.setLearningRate(rate);
    }
    
    public double getLearningRate() {
        return learningRate.getLearningRate();
    }
    
    public void setLearningRateType(double lr, String type) {
        setLRT(lr, type);
    }
    
    private void setLRT(double lr, String type) {
        switch (type) {
            case "Smooth":
                learningRate = new SmoothLearningRate(lr);
                break;
            case "Additive":
                learningRate = new AdditiveLearningRate(lr);
                break;
        }
        
    }
    
    
    public void  setLearningRateParameters(String rateparameters){
        learningRate.setLearningRateParameters(rateparameters);
    }
            
    
}
