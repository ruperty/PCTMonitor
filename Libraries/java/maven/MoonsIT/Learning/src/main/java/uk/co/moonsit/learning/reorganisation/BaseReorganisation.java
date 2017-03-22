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

import uk.co.moons.math.RMath;

/**
 *
 * @author Rupert
 */
public abstract class BaseReorganisation implements ReorganisationInterface {
 
    protected double learningRate;
    protected double learningRateMax;
    protected double previousErrorResponse = 0;
    //private double errorResponseChange;
    protected Double adaptiveSmoothUpper = null;
    protected Double adaptiveSmoothLower = null;
    //private Double adaptiveLearningRate = null;
    private double shortMA = 0;
    private double longMA = 0;
    //private double adaptiveScalingFactor;
    protected double delta = 0.025;
    protected boolean continuous = false;

    public void reset() {
        shortMA = 0;
        longMA = 0;
        //adaptiveScalingFactor = Math.log(adaptiveSmoothLower) / Math.log(adaptiveSmoothUpper);
    }

    protected double adaptLearningRate(double response) {
        if (adaptiveSmoothUpper != 0) {
            shortMA = RMath.smooth(response, shortMA, adaptiveSmoothLower);
            longMA = RMath.smooth(response, longMA, adaptiveSmoothUpper);
            if (longMA != 0) {
                //learningRate = Math.abs(((lowerSmoothResponse / upperSmoothResponse) - 1) / adaptiveScalingFactor);
                learningRate = Math.min(learningRateMax, 10 * Math.abs((shortMA - longMA) /shortMA ));
            }
        }
        return learningRate;
    }

    public double getShortMA() {
        return shortMA;
    }

    public double getLongMA() {
        return longMA;
    }

    @Override
    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public void setAdaptiveSmoothUpper(Double adaptiveSmoothUpper) {
        this.adaptiveSmoothUpper = adaptiveSmoothUpper;
    }

    public void setAdaptiveSmoothLower(Double adaptiveSmoothLower) {
        this.adaptiveSmoothLower = adaptiveSmoothLower;
    }

    public void setAdaptiveFactor(Double delta) {
        this.delta = delta;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRateMax(double learningRateMax) {
        this.learningRateMax = learningRateMax;
    }

    public void setContinuous(boolean continuous) {
        this.continuous = continuous;
    }

}
