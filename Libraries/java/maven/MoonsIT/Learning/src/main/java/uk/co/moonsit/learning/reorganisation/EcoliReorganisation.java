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
public class EcoliReorganisation extends BaseReorganisation {

    private double random = 0;

    public EcoliReorganisation(String lrType, Double lr, String parameters, Double delta, Boolean continuous) throws Exception {
        super(lrType, lr, parameters);
        this.delta = delta;
        this.continuous = continuous;
    }

    @Override
    public double correct(double errorResponse, boolean applyCorrection, double parameter, double parameterMA) {

        if (applyCorrection) {
            double lrate = learningRate.update(parameter, errorResponse);

            if (errorResponse >= previousErrorResponse) {
                random = (2 * (Math.random() - 0.5));
            }
            correction = lrate * delta * parameterMA * Math.abs(errorResponse) * random;
            if (!continuous) {
                parameter += correction;
            } else {
                correction = correction / 100;
            }

            previousErrorResponse = errorResponse;
        }

        if (continuous) {
            parameter += correction;
        }

        return parameter;
    }

}
