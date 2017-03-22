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
package uk.co.moons.math;

import java.util.List;

/**
 *
 * @author Rupert Young
 */
public class RMath {

    /** Creates a new instance of RMath */
    public RMath() {
    }

    static public double sigmoid(double x, double range, double scale) {
        return -range / 2 + range / (1 + Math.exp(-x * scale / range));
    }

    // Exponential smoothing - the higher the smoothing factor (weight) the smoother the result
    static public double smooth(double newVal, double oldVal, double weight) {
        return newVal * (1 - weight) + oldVal * weight;
    }

    static public float smooth(float newVal, float oldVal, float weight) {
        return newVal * (1 - weight) + oldVal * weight;
    }

    public static double mean(List<Double> list) {
        double sum = 0;
        for (Double s : list) {
            sum += s;
        }
        return sum / list.size();
    }

    public static double standardDeviation(List<Double> list, double mean) {
        return Math.sqrt(variance(list, mean));
    }

    public static double variance(List<Double> list, double mean) {
        double sum = 0;
        for (Double s : list) {
            double value = s - mean;
            sum += Math.pow(value, 2);
        }

        return sum / list.size();
    }
}
