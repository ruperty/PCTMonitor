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
 */
public class SmoothMaxLearningRate extends BaseLearningRate {

    protected Double smooth = 0.95;
    private double shortMA = 0;
    private double max = 0;

    public SmoothMaxLearningRate(Double learningRate, String rateparameters) {
        this.learningRate = learningRate;
        setLearningRateParametersPrivate(rateparameters);
    }

    @Override
    public double update(double weight, double error) {
        shortMA = RMath.smooth(error, shortMA, smooth);
        learningRate = Math.min(learningRateMax, shortMA / max);
        return learningRate;
    }

    @Override
    public void reset() {
        shortMA = 0;
    }

    @Override
    public void setLearningRateParameters(String rateparameters) {
        setLearningRateParametersPrivate(rateparameters);
    }

    private void setLearningRateParametersPrivate(String rateparameters) {
        String[] arr = rateparameters.split("\\^");
        smooth = Double.parseDouble(arr[0]);
        max = Double.parseDouble(arr[1]);
    }

    @Override
    public String getParametersString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Smooth").append(":");
        sb.append(smooth).append("_");

        sb.append("Max").append(":");
        sb.append(max);

        return sb.toString();
    }
}
