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

/**
 *
 * @author ReStart
 */
public interface ReorganisationInterface {


    public double correct(double errorResponse, boolean applyCorrection, double parameter, double parameterMA) ;

    public void setLearningRate(double learningRate);

}
