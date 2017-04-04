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
package uk.co.moons.control.neural;

import java.util.ArrayList;
import java.util.List;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;

/**
 * <b>Weighted sum function</b>
 *
 * <p>
 * The weighted sum of a set of inputs.
 *
 * <p>
 * The sum of inputs multiplied by a weight value associated with each.
 *
 * <p>
 * The configuration parameters to the function are as follows:
 * <br>
 * <b>Weights</b> - a comma-separated string of the weights (mandatory). <br>
 * <b>Absolute</b> - a boolean flag defining if the absolute value of inputs are
 * taken. <br>
 * <b>Infinite</b> - a boolean flag indicating that the output should be
 * infinite if all the inputs are, otherwise the output will be zero. <br>
 * <b>Max</b> - a limit to the output value. <br>
 *
 * @author Rupert Young <rupert@moonsit.co.uk>
 * @version 1.0
 */
public class WeightedSumNeuralFunction extends NeuralFunction {

    public String weights = null;
    private List<Double> weightsArray = null;
    public Boolean absolute = false;
    public Boolean infinite = false;
    public Double max = null;

    public WeightedSumNeuralFunction() throws Exception {
        super();
        if (weights == null) {
            throw new Exception("Weights missing for WeightedSumNeuralFunction");
        }
    }

    public WeightedSumNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Weights")) {
                weights = param.getValue();
                continue;
            }
            if (pname.equals("Max")) {
                max = Double.parseDouble(param.getValue());
                continue;
            }
            if (pname.equals("Absolute")) {
                absolute = Boolean.parseBoolean(param.getValue());
            }
            if (pname.equals("Infinite")) {
                infinite = Boolean.parseBoolean(param.getValue());
            }
        }

        if (weights == null) {
            throw new Exception("Weights missing for WeightedSumNeuralFunction");
        }
        weightsArray = getWeights();
    }

    @Override
    public void init() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();
        getLinkSize(controls);
    }

    @Override
    public double compute() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();

        double sum = 0;
        int ctr = 0;
        int infinites = 0;

        for (BaseControlFunction control : controls) {
            double value = control.getValue();
            if (Math.abs(value) == Double.POSITIVE_INFINITY) {
                infinites++;
                ctr++;
                if (controls.size() == 1) {
                    sum = Double.POSITIVE_INFINITY;
                    break;
                }
                continue;
            }
            double weight = weightsArray.get(ctr++);
            if (absolute) {
                value = Math.abs(value);
            }
            sum += weight * value;
        }

        if (infinite) {
            if (infinites == controls.size()) {
                sum = Double.POSITIVE_INFINITY;
            }
        }
        output = sum;
        if (max != null && output > max) {
            output = max;
        }
        return output;
    }

    @Override
    public void setParameter(String par) {
        super.setParameter(par);
        String[] arr = par.split(":");
        if (arr[0].equals("Weights")) {
            weights = arr[1];
            weightsArray = getWeights();
        }
    }

    private List<Double> getWeights() {
        List<Double> w = new ArrayList<>();

        String[] arr = weights.split(",");
        for (String arr1 : arr) {
            w.add(Double.parseDouble(arr1));
        }

        return w;
    }

    private int getLinkSize(List<BaseControlFunction> bnflist) throws Exception {
        int size = bnflist.size();
        if (size != weightsArray.size()) {
            throw new Exception("Weights size incorrect for WeightedMultiplyNeuralFunction: " + getName());
        }
        for (Double weightsArray1 : weightsArray) {
            if (weightsArray1 == 0) {
                size--;
            }
        }

        return size;
    }
}
