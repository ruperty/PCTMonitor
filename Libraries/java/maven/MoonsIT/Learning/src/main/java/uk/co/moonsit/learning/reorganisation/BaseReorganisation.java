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
 
    protected double learningRate;
    protected double learningRateMax;
    protected double previousErrorResponse = 0;
    protected Double adaptiveSmoothUpper = null;
    protected Double adaptiveSmoothLower = null;
    private double shortMA = 0;
    private double longMA = 0;
    protected double delta = 0.025;
    protected boolean continuous = false;

    public void reset() {
        shortMA = 0;
        longMA = 0;
    }

    protected double adaptLearningRate(double response) {
        if (adaptiveSmoothUpper != 0) {
            shortMA = RMath.smooth(response, shortMA, adaptiveSmoothLower);
            longMA = RMath.smooth(response, longMA, adaptiveSmoothUpper);
            if (longMA != 0) {
                learningRate = Math.min(learningRateMax, /*10 * */ Math.abs((shortMA - longMA) /shortMA ));
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
