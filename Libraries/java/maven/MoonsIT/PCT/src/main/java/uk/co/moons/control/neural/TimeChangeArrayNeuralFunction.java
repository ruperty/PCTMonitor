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
import java.util.logging.Level;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;

public class TimeChangeArrayNeuralFunction extends NeuralFunction {

    private static final Logger logger = Logger.getLogger(TimeChangeArrayNeuralFunction.class.getName());
    private double oldValue = 0;
    private boolean first = true;
    public int size = 0;
    private List<Double> list = null;
    private int dtimeIndex = 1;
    private int valueIndex = 0;

    public TimeChangeArrayNeuralFunction() {
        super();
    }

    public TimeChangeArrayNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("Size")) {
                size = Integer.valueOf(param.getValue());
            }
        }

    }

    @Override
    public void init() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();
        if (controls.size() != 2) {
            throw new Exception("Links to value and to time must be configured");
        }
        list = new ArrayList<>();

        int ctr = 0;
        for (BaseControlFunction control : links.getControlList()) {
            if (control.getName().contains("Rate")) {
                dtimeIndex = ctr;
            }
            ctr++;
        }
        if (dtimeIndex == 0) {
            valueIndex = 1;
        }
        addToList(0.0);
        meanChange(10);
        list.remove(0);
    }

    private void addToList(Double f) {

        list.add(f);
        if (list.size() > size) {
            list.remove(0);
        }
    }

     @Override
    public void pause() throws Exception {     
        list.clear();
            logger.log(Level.INFO, "+++ cleared {0} ", new Object[]{getName()});
    }
    
    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        Double newValue = controls.get(valueIndex).getValue();
        Double dtime = controls.get(dtimeIndex).getValue() / 1000;

        if (first) {
            oldValue = newValue;
            first = false;
        }
        addToList(newValue - oldValue);

        if (dtime > 0) {
            output = meanChange(dtime);
        } else {
            logger.log(Level.WARNING, "dtime = {0} mSum = {1}", new Object[]{dtime, newValue});
        }

//if(Double.isNaN(output)){
        //  logger.warning("NaN dtime = "+dtime+ " mSum = "+newValue);
//}
        oldValue = newValue;

        return output;
    }

    private double meanChange(double dtime) {
        double sum = 0;
        for (Double d : list) {
            sum += d;
        }
        return sum / (size * dtime);
    }
}
