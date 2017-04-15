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

import uk.co.moons.math.RMath;

/**
 *
 * @author Rupert Young
 *
 * Increase the rate if the gradient for the weight does not change sign, else
 * decrease Use small additive increasing +0.05 and multiplicative decreases 0.95
 *
 */
public class AdditiveLearningRate extends BaseLearningRate {

    protected Double adaptiveSmoothUpper = 0.95;
    protected Double adaptiveSmoothLower = 0.9;
    private double additiveFactor = 0.05;
    private double mulitplicativeFactor = 0.95;
    protected double learningRateMax = 0.5;

    public AdditiveLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    @Override
    public double update(double response) {
        if (adaptiveSmoothUpper != 0) {
            shortMA = RMath.smooth(response, shortMA, adaptiveSmoothLower);
            longMA = RMath.smooth(response, longMA, adaptiveSmoothUpper);
            if (longMA != 0) {
                learningRate = Math.min(learningRateMax, /*10 * */ Math.abs((shortMA - longMA) / shortMA));
            }
        }
        return learningRate;
    }

    @Override
    public void reset() {
        shortMA = 0;
        longMA = 0;
    }

}
