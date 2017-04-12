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
 * <b>Weighted product function</b>
 *
 * <p>
 * The weighted product of a set of inputs.
 *
 * <p>
 * The square root of the product will be computed if configured.
 *
 * <p>
 * The configuration parameters to the function are as follows:
 * <br>
 * <b>Weights</b> - a comma-separated string of the weights (mandatory). <br>
 * <b>Root</b> - a boolean flag indicating to compute the square root, default
 * is true. <br>
 *
 * @author Rupert Young <rupert@moonsit.co.uk>
 * @version 1.0
 */
public class WeightedMultiplyNeuralFunction extends NeuralFunction {

    public String weights = null;
    public Boolean root = false;
    private List<Double> weightsArray = null;
    //private int size = 0;

    public WeightedMultiplyNeuralFunction() {
        super();
    }

    public WeightedMultiplyNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Weights")) {
                weights = param.getValue();
                continue;
            }

            if (param.getName().equals("Root")) {
                root = Boolean.parseBoolean(param.getValue());
            }
        }

        if (weights == null) {
            throw new Exception("Weights missing for WeightedMultiplyNeuralFunction");
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

        double sum = 1;
        int ctr = 0;
        for (BaseControlFunction control : controls) {
            double value = control.getValue();
            if (Math.abs(value) == Double.POSITIVE_INFINITY) {
                ctr++;
                continue;
            }
            double weight = weightsArray.get(ctr++);
            sum *= weight * value;
        }

        if (root) {
            double sign = Math.signum(sum);
            sum = Math.pow(Math.abs(sum), 1.0 / ctr);
            sum = sum * sign;
        }

        output = sum;
        return output;
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

    @Override
    public void setParameter(String par) throws Exception {
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
}
