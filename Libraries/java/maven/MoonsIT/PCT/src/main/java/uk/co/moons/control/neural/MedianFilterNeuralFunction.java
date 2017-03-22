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
import java.util.Collections;
import java.util.List;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;

/**
 *
 * @author ReStart
 */
public class MedianFilterNeuralFunction extends NeuralFunction {

    public Integer size = 1;
    private List<Double> history = null;

    public MedianFilterNeuralFunction() throws Exception {
        super();
        history = new ArrayList<Double>();
    }

    public MedianFilterNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        history = new ArrayList<Double>();

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Size")) {
                size = Integer.parseInt(param.getValue());
            }
        }

    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double newValue = controls.get(0).getValue();
        addValue(newValue);
        //System.out.println(newValue);

        output = getValue();
        //print();
        return output;
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

        if (history.size() < size) {
            rtn = history.get(0);
        } else {
            List<Double> sorted = sorted();
            rtn = sorted.get(size / 2);
        }
        return rtn;
    }

    private List<Double> sorted() {
        List<Double> sorted = new ArrayList<>();
        for (double d : history) {
            sorted.add(d);
        }
        Collections.sort(sorted);
        return sorted;
    }

    private void addValue(double value) {
        history.add(value);
        if (history.size() > size) {
            history.remove(0);
        }
    }
}
