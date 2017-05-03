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
 *
 * Increase the rate if the gradient for the weight does not change sign, else
 * decrease Use small additive increasing +0.05 and multiplicative decreases
 * 0.95
 *
 */
public class AdditiveLearningRate extends BaseLearningRate {

    private double additiveFactor = 0.05;
    private double mulitplicativeFactor = 0.95;
    private double previousWeight = 0;
    private double previousGradient = 0;

    public AdditiveLearningRate(Double learningRate, String rateparameters) {
        this.learningRate = learningRate;
        setLearningRateParametersPrivate(rateparameters);
    }

    @Override
    public double update(double weight, double error) {
        double gradient = weight - previousWeight;
        if (Math.signum(gradient) == Math.signum(previousGradient)) {
            learningRate = Math.min(learningRateMax, learningRate + additiveFactor);
        } else {
            learningRate = Math.min(learningRateMax, learningRate * mulitplicativeFactor);
        }
        previousWeight = weight;
        previousGradient = gradient;
        return learningRate;
    }

    @Override
    public void reset() {
        previousWeight = 0;
        previousGradient = 0;
    }

    private void setLearningRateParametersPrivate(String rateparameters) {
        String[] arr = rateparameters.split("\\^");
        additiveFactor = Double.parseDouble(arr[0]);
        mulitplicativeFactor = Double.parseDouble(arr[1]);
    }

    @Override
    public void setLearningRateParameters(String rateparameters) {
        setLearningRateParametersPrivate(rateparameters);
    }

    @Override
    public String getParametersString() {
        StringBuilder sb = new StringBuilder();

        sb.append("AdditiveFactor").append(":");
        sb.append(additiveFactor).append("_");

        sb.append("MulitplicativeFactor").append(":");
        sb.append(mulitplicativeFactor);

        return sb.toString();
    }

}
