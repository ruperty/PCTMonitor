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
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;

/**
 *
 * @author ReStart
 */
public class MemoryValueLookupNeuralFunction extends NeuralFunction {

    private static final Logger LOG = Logger.getLogger(MemoryValueLookupNeuralFunction.class.getName());

    public String directory = null;
    public String system = null;
    public String mode = null;
    public String parameter = null;
    private int start;
    //private int limit;
    //private int counter;
    private List<Double> list;
    //private ListIterator<Double> iter;

    public MemoryValueLookupNeuralFunction(List<Parameters> ps) throws Exception {
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
        start = (int) Math.round(initial);
        list = createList(directory, start, system, mode, parameter);
        output = list.get(0);
    }

    public static List<Double> createList(String directory, int start, String system, String mode, String parameter) throws IOException, Exception {
        List<Double> list = new ArrayList<>();
        String path = System.getProperty("user.home") + File.separator + directory + File.separator + String.valueOf(start) + ".csv";
        LOG.log(Level.INFO, "File path to data :{0}", path);
        try (BufferedReader in = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] arr = line.split(",");
                String prefix = arr[0] + arr[1] + arr[2];
                if (prefix.equalsIgnoreCase(system + mode + parameter)) {
                    for (int i = 3; i < arr.length; i++) {
                        list.add(Double.valueOf(arr[i]));
                    }
                    break;
                }
            }
        }
        if (list.isEmpty()) {
            throw new Exception("Memory list for " + system + mode + parameter + " not found in " + path);
        }

        return list;
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double input = controls.get(0).getValue();

        output = list.get((int) (input - start));

        return output;
    }
}
