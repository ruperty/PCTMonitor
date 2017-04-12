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
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;


/**
 * <b>Weighted sum function</b>
 *
 * <p>
 * Exclusive selection of a value (weighted) from a set of inputs.
 *
 * <p>
 * The first non-zero value in a list of inputs is selected as the output, and weighted. 
 *
 * <p>
 * The configuration parameters to the function are as follows:
 * <br>
 * <b>Weights</b> - a comma-separated string of the weights (mandatory). <br>
 * <b>Absolute</b> - a boolean defining if the absolute value of inputs are taken. <br>
 *
 * @author Rupert Young <rupert@moonsit.co.uk>
 * @version 1.0
 */

public class WeightedExclusiveNeuralFunction extends NeuralFunction {

    private static final Logger logger = Logger.getLogger(WeightedExclusiveNeuralFunction.class.getName());
    
    public String weights = null;
    private List<Double> weightsArray = null;

    public WeightedExclusiveNeuralFunction() {
        super();
    }

    public WeightedExclusiveNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Weights")) {
                weights = param.getValue();
            }
        }

        if (weights == null) {
            throw new Exception("Weights missing for WeightedExclusiveNeuralFunction");
        }
        weightsArray = getWeights();
    }

    @Override
    public void init() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();
        int size = controls.size();
        if (size != weightsArray.size()) {
            throw new Exception("Weights size incorrect for WeightedExclusiveNeuralFunction: " + getName());
        }

    }

    @Override
    @SuppressWarnings("LoggerStringConcat")
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        int ctr = 0;

        output = 0;
        for (BaseControlFunction control : controls) {
            double weight = weightsArray.get(ctr++);
            double value = control.getValue();
            double wvalue = weight * value;
            //logger.info(control.getName() + " " + wvalue);
            if (Math.abs(wvalue) > 0 && Math.abs(wvalue)!= Double.POSITIVE_INFINITY) {
                output = wvalue;
                break;
            }
        }

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
