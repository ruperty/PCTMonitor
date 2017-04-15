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
package uk.co.moonsit.learning.rate;

/**
 *
 * @author Rupert Young
 */
public interface LearningRateInterface {

    public double update(double response, double error);

    public void reset();

    public void setLearningRateParameters(String rateparameters);

}
