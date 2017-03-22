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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;

/**
 *
 * @author ReStart
 */
public class MemoryCounterIncrementNeuralFunction extends NeuralFunction {

    
    private int start;
    private int limit;
    private int counter;
    public String directory = null;

    public MemoryCounterIncrementNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Directory")) {
                directory = param.getValue();
            }

        }
        
       
        if (initial == null) {
            throw new Exception("Initial missing for " + this.getClass().getName());
        }
        if (directory == null) {
            throw new Exception("Directory missing for " + this.getClass().getName());
        }

    }

    @Override
    public void init() throws FileNotFoundException, IOException {

        start = (int) Math.round(initial);
        counter = start;
        try (BufferedReader in = new BufferedReader(new FileReader(System.getProperty("user.home") + File.separator+directory+File.separator+String.valueOf(start) + ".csv"))) {
            String line = in.readLine();
            limit = start + line.split(",").length - 4;
        }

    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double inputSum=0;
        for(BaseControlFunction control : controls){
            inputSum += control.getValue();
        }

        if (inputSum == nextValue(counter)*controls.size()) {
            counter = nextValue(counter);
        }

        output = counter;
        return output;
    }
    
    private int nextValue(int ctr){
        if (ctr >= limit) {
            ctr = start;
        }else{
            ctr++;
        }
        return ctr;
    }
    
}
