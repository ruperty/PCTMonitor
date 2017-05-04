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
public abstract class BaseLearningRate implements LearningRateInterface {

    public final static int SMOOTHMAX = 0;
    public final static int SMOOTH = 1;
    public final static int SMOOTHFIXED = 2;
    public final static int ADDITIVE = 3;

    protected double learningRate;
    protected double learningRateMax = 0.5;

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public double getLearningRateMax() {
        return learningRateMax;
    }

    public void setLearningRateMax(double learningRateMax) {
        this.learningRateMax = learningRateMax;
    }

    public static int getLearningRateParameterIndex(String par) {
        switch (par) {
            case "Smooth":
                return 0;
            case "Max":
                return 1;
            case "Offset":
                return 1;
            case "AdditiveFactor":
                return 0;
            case "MulitplicativeFactor":
                return 1;
            case "SmoothUpper":
                return 0;
            case "SmoothLower":
                return 0;

        }
        return -1;
    }

    public static int getLearningRateType(String lt) {
        switch (lt) {
            case "Smooth":
                return SMOOTH;
            case "SmoothMax":
                return SMOOTHMAX;
            case "SmoothFixed":
                return SMOOTHFIXED;
            case "Additive":
                return ADDITIVE;

        }
        return 10;
    }

    public static String getLearningRateType(int lt) {
        switch (lt) {
            case SMOOTHMAX:
                return "SmoothMax";
            case SMOOTH:
                return "Smooth";
            case SMOOTHFIXED:
                return "SmoothFixed";
            case ADDITIVE:
                return "Additive";

        }
        return null;
    }

    public static String getRateParameters(String lrt) {
        switch (lrt) {
            case "Smooth":
                return "0.9^0.95";
            case "SmoothMax":
                return "0.1^0.8";
            case "SmoothFixed":
                return "0.5^0.0";
            case "Additive":
                return "0.025^0.5";

        }
        return null;
    }

}
