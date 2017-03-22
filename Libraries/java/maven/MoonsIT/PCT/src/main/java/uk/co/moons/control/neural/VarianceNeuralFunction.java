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
 *
 * @author ReStart
 */
public class VarianceNeuralFunction extends NeuralFunction {

    public Integer size = 1;
    private List<Double> history = null;
    private Double removed = 0.0;
    private double sum = 0;
    private double sumofsquares = 0;
    private int resetIndex = -1;

    public VarianceNeuralFunction() throws Exception {
        super();
        history = new ArrayList<>();
    }

    public VarianceNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        history = new ArrayList<>();

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Size")) {
                size = Integer.parseInt(param.getValue());
            }
        }

    }

    @Override
    public void init() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();

        for (int i = 0; i < controls.size(); i++) {
            String type = links.getType(i);
            if (type != null && type.equalsIgnoreCase("Reset")) {
                resetIndex = i;
            }
        }

    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double resetVal = controls.get(resetIndex).getValue();
        if (resetVal == 1) {
            reset();
        }
        double newValue = controls.get(0).getValue();
        addValue(newValue);
        //System.out.println(newValue);

        output = getValue();
        //print();
        return output;
    }

    private void reset() {
        history.clear();
        sumofsquares = 0;
        sum = 0;
        output = 0;
        removed = 0.0;
    }

    private void print() {
        System.out.print("[");
        for (double d : history) {
            System.out.print(d + " ");
        }
        System.out.println("]");
    }

    private double getValue() {
        Double rtn;

        double end = history.get(history.size() - 1);
        sum += end - removed;
        sumofsquares += end * end - removed * removed;
        // root sum of squares
        rtn = Math.sqrt(sumofsquares - (sum * sum) / history.size());

        return rtn;
    }

    private void addValue(double value) {
        history.add(value);
        if (history.size() > size) {
            removed = history.remove(0);
        }
    }
}
