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
 * @author Rupert
 */
public class HillClimbReorganisation extends BaseReorganisation {



    public HillClimbReorganisation(String lrType, Double lr, String parameters, Double delta, Boolean continuous) throws Exception {
        super(lrType, lr, parameters);
        this.delta = delta;
        this.continuous = continuous;
    }

    @Override
    public double correct(double errorResponse, boolean applyCorrection, double parameter, double parameterMA) {

        if (applyCorrection) {
            double lrate = learningRate.update(parameter, errorResponse);

            double errorResponseChange = errorResponse - previousErrorResponse;

            if (errorResponseChange > 0) {
                delta = -delta;
            }
            correction = lrate * delta * parameterMA * Math.abs(errorResponse);
            if (!continuous) {
                parameter += correction;
            }
            previousErrorResponse = errorResponse;
        }

        if (continuous) {
            parameter += correction;
        }

        return parameter;
    }

}
