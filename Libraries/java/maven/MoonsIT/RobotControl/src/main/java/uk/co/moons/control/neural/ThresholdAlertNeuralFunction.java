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

//import uk.co.moons.math.RMath;
import java.util.List;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moonsit.outputs.Alert;

public class ThresholdAlertNeuralFunction extends NeuralFunction {

    public Double threshold = null;
    public String type = "Sound";
    private boolean active = true;
    private Alert alert;

    public ThresholdAlertNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Threshold")) {
                threshold = Double.parseDouble(param.getValue());
            }
            if (pname.equals("Type")) {
                type = param.getValue();
            }
        }

        if (threshold == null) {
            throw new Exception("Threshold missing for ThresholdAlertNeuralFunction");
        }

    }

    @Override
    public void init() {
        alert = new Alert(type);
    }

    @Override
    public double compute() throws NoSuchFieldException, IllegalAccessException {
        List<BaseControlFunction> controls = links.getControlList();
        double value = controls.get(0).getValue();

        if (active) {
            if (value < threshold) {
                output = 1;
                alert.alert();
                active = false;
            }
        }

        return output;
    }

    @Override
    public void pause() throws Exception {
        active = true;
    }

    @Override
    public void close() {
        if(alert!=null)alert.close();
    }
}
