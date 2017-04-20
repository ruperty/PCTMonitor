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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moonsit.learning.rate.BaseLearningRate;
import uk.co.moonsit.utils.MoonsString;

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
    
    private static final Logger LOG = Logger.getLogger(BaseReorganisation.class.getName());
    
    protected double correction = 0;
    protected double delta = 0.025;
    protected boolean continuous = false;
    protected BaseLearningRate learningRate;
    protected double previousErrorResponse;
    
    public BaseReorganisation(String type, double lr, String parameters) throws Exception {
        setLRT(type, lr, parameters);
    }
    
    public void reset() {
        learningRate.reset();
    }
    
    protected double computeCorrection(double lrate, double delta, double parameterMA, double error) {
        double correct = lrate * delta * parameterMA;
        
        System.out.println(MoonsString.formatPlaces( error,4) + " " +MoonsString.formatPlaces( lrate,4) + " " + MoonsString.formatPlaces( correct,4) + " " + MoonsString.formatPlaces( parameterMA, 4));
        //  LOG.log(Level.INFO, "{0} {1} {2}", new Object[]{MoonsString.formatPlaces( lrate,4), MoonsString.formatPlaces( correct,4), MoonsString.formatPlaces( parameterMA,4)});
      
        return correct;//* error;
    }
    
    public double getCorrection() {
        return correction;
    }
    
    public void setCorrection(double correction) {
        this.correction = correction;
    }
    
    public void setAdaptiveFactor(Double delta) {
        this.delta = delta;
    }
    
    public void setContinuous(boolean continuous) {
        this.continuous = continuous;
    }
    
    public void setLearningRateMax(double learningRateMax) {
        learningRate.setLearningRateMax(learningRateMax);
    }
    
    @Override
    public void setLearningRate(double rate) {
        learningRate.setLearningRate(rate);
    }
    
    public double getLearningRate() {
        return learningRate.getLearningRate();
    }
    
    public void setLearningRateType(String type, double lr, String parameters) throws Exception {
        setLRT(type, lr, parameters);
    }
    
    private void setLRT(String type, double lr, String parameters) throws Exception {
        
        String className = "uk.co.moonsit.learning.rate." + type + "LearningRate";
        Class theClass = Class.forName(className);
        
        try {
            Constructor constructor = theClass.getConstructor(Double.class, String.class);
            try {
                learningRate = (BaseLearningRate) constructor.newInstance(lr, parameters);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(BaseReorganisation.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
            
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(BaseReorganisation.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        /*
        switch (type) {
        case "Smooth":
        learningRate = new SmoothLearningRate(lr);
        break;
        case "Additive":
        learningRate = new AdditiveLearningRate(lr);
        break;
        }*/

 /*
        switch (type) {
            case "Smooth":
                learningRate = new SmoothLearningRate(lr);
                break;
            case "Additive":
                learningRate = new AdditiveLearningRate(lr);
                break;
        }*/
    }
    
    public void setLearningRateParameters(String rateparameters) {
        learningRate.setLearningRateParameters(rateparameters);
    }
    
}
