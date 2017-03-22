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

import java.util.List;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moonsit.utils.MoonsString;
import uk.co.moonsit.utils.timing.DateAndTime;

public class MaxSeriesNeuralFunction extends NeuralFunction {

    public String key = null;
    public String model = null;

    private int keyIndex = -1;
    private int resetIndex = -1;
    private int[] places;
    private double[] array;
    private double[] lastValues;
    private String[] names;
    private DateAndTime dt;

    public MaxSeriesNeuralFunction() throws Exception {
        super();
    }

    public MaxSeriesNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        ps.stream().forEach((param) -> {
            String pname = param.getName();
            if (pname.equals("Key")) {
                key = param.getValue();
            }
            if (pname.equals("Model")) {
                model = param.getValue();
            }
        });

        if (key == null) {
            throw new Exception("Key configuration value missing");
        }
    }

    private String extractName(String name) {
        if (name.contains(":")) {
            String[] arr = name.split(":");
            name = arr[0];
        }
        return name;
    }

    private int extractPlaces(String name) {
        int rtn = 0;
        if (name.contains(":")) {
            String[] arr = name.split(":");
            rtn = Integer.parseInt(arr[1]);
        }
        return rtn;
    }

    @Override
    public void init() throws Exception {
        dt = new DateAndTime();
        List<BaseControlFunction> controls = links.getControlList();
        array = new double[controls.size() - 1];
        names = new String[controls.size() - 1];
        lastValues = new double[controls.size() - 1];
        places = new int[controls.size() - 1];

        for (int i = 0; i < controls.size(); i++) {
            String lname = extractName(links.getType(i));
            int place = extractPlaces(links.getType(i));
            if (key.equalsIgnoreCase(lname)) {
                keyIndex = i;
            }
            if (i == controls.size() - 1) {
                if (links.getType(i).equalsIgnoreCase("Reset")) {
                    resetIndex = i;
                } else {
                    throw new Exception("Last link must be reset: " + this.getName());
                }

            } else {
                array[i] = controls.get(i).getValue();//.put(controls.get(i).getName(), controls.get(i).getValue());
                names[i] = lname;
                places[i] = place;
            }
        }

    }

    @Override
    public double compute() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();
        double value = controls.get(keyIndex).getNeural().getParameter(extractName(links.getType(keyIndex)).toLowerCase());
        double resetVal = controls.get(resetIndex).getValue();
        if (resetVal == 1) {
            reset();
        } else if (value > output) {
            output = value;
            for (int i = 0; i < array.length; i++) {
                array[i] = controls.get(i).getNeural().getParameter(extractName(links.getType(i)).toLowerCase());
            }
        }

        return output;
    }

    private void reset() {
        for (int i = 0; i < array.length; i++) {
            lastValues[i] = array[i];
            array[i] = 0;
        }
        output = 0;
    }

    @Override
    public String getDataString() {
        StringBuilder sb = new StringBuilder();
        dt.Now();
        sb.append("ID").append(":");
        sb.append(dt.YMD()).append("-").append(dt.HMSS("-")).append("_");
        sb.append("Model").append(":").append(model).append("_");
        for (int i = 0; i < lastValues.length; i++) {
            sb.append(names[i]).append(":");
            sb.append(MoonsString.formatStringPlaces(lastValues[i], places[i]));
            if (i < lastValues.length - 1) {
                sb.append("_");
            }
        }

        return sb.toString();
    }

    @Override
    public String getParameterName(int i) {
        return links.getType(i);
    }

    @Override
    public int getParametersSize() {
        return array.length;
    }

    @Override
    public double getParameter(int i) {
        return array[i];
    }

}
