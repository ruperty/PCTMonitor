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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;
import static uk.co.moons.control.neural.MemoryValueLookupNeuralFunction.createList;

/**
 *
 * @author ReStart
 */
public class MemoryKeyIncrementNeuralFunction extends NeuralFunction {

    public String system = null;
    public String mode = null;
    public String parameter = null;
    public String directory = null;
    public Double tolerance = 0.01;

    private int start;
    private List<Double> list;
    private Double value;
    private int counter = 0;

    public MemoryKeyIncrementNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Directory")) {
                directory = param.getValue();
            }
            if (pname.equals("Parameter")) {
                parameter = param.getValue();
            }
            if (pname.equals("Mode")) {
                mode = param.getValue();
            }
            if (pname.equals("System")) {
                system = param.getValue();
            }
            if (pname.equals("Tolerance")) {
                tolerance = Double.valueOf(param.getValue());
            }
        }

        if (system == null) {
            throw new Exception("System missing for " + this.getClass().getName());
        }
        if (parameter == null) {
            throw new Exception("Parameter missing for " + this.getClass().getName());
        }
        if (mode == null) {
            throw new Exception("Mode missing for " + this.getClass().getName());
        }
        if (initial == null) {
            throw new Exception("Initial missing for " + this.getClass().getName());
        }
        if (directory == null) {
            throw new Exception("Directory missing for " + this.getClass().getName());
        }

    }

    @Override
    public void init() throws FileNotFoundException, IOException, Exception {
        if (links.getControlList().size() != 2) {
            throw new Exception(this.getClass().getName() + " links size is not correct, have you added the goal generator?");
        }
        start = (int) Math.round(initial);
        output = start;
        list = createList(directory, start, system, mode, parameter);
        //iter = list.listIterator();

        value = list.get(counter);
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double input = controls.get(0).getValue();
        int state = (int) (controls.get(1).getValue());

        if (Math.abs(value - input) < tolerance) {
            counter = nextValue(counter, state);
            output = start + counter;
            value = list.get(counter);
        }

        return output;
    }

    private int nextValue(int ctr, int state) {
        ctr = state + 1 - start;

        if (ctr >= list.size() ) {
            ctr = 0;
        }
        return ctr;
    }
}
