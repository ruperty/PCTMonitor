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
 * <b>Weighted mean function</b>
 *
 * <p>
 * The mean of a weighted sum of inputs.
 *
 *
 * <p>
 * The configuration parameters to the function are as follows:
 * <br>
 * <b>Weights</b> - a comma-separated string of the weights (mandatory). <br>
 *
 * @author Rupert Young <rupert@moonsit.co.uk>
 * @version 1.0
 */

public class WeightedMeanNeuralFunction extends NeuralFunction {

    public String weights = null;
    public String absolute = "false";
    private List<Double> weightsArray = null;

    public WeightedMeanNeuralFunction() {
        super();
    }

    public WeightedMeanNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String name = param.getName();
            if (name.equals("Weights")) {
                weights = param.getValue();
                continue;
            }
            if (name.equals("Absolute")) {
                absolute = param.getValue();
            }
        }

        if (weights == null) {
            throw new Exception("Weights missing for WeightedMeanNeuralFunction");
        }
 weightsArray = getWeights();
    }
    
    
     @Override
    public void init() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();
        int size = controls.size();
        if (size != weightsArray.size()) {
            throw new Exception("Weights size incorrect for WeightedMeanNeuralFunction: " + getName());
        }

    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double sum = 0;
        int ctr = 0;
        for (BaseControlFunction control : controls) {
            double weight = weightsArray.get(ctr++);
            double value = control.getValue();
            if (absolute.equals("true")) {
                value = Math.abs(value);
            }
            sum += weight * value;
        }

        output = sum / controls.size();
        return output;
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
