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

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moonsit.outputs.SerialCSVWrite;

/**
 *
 * @author ReStart
 */
public class SerialCSVWriteNeuralFunction extends BaseSensorNeuralFunction {

    private static final Logger logger = Logger.getLogger(SerialCSVWriteNeuralFunction.class.getName());

    public int timeout = 2000;
    public int datarate = 9600;
    private SerialCSVWrite serial;

    public SerialCSVWriteNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("TimeOut")) {
                timeout = Integer.parseInt(param.getValue());
            }
            if (param.getName().equals("DataRate")) {
                datarate = Integer.parseInt(param.getValue());
            }
        }


    }

    @Override
    public void init() throws Exception {
        serial = new SerialCSVWrite(sensorPort, timeout, datarate);
        serial.init();
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        output = controls.get(0).getValue();
        try {
            serial.write(output);
        } catch (IOException ex) {
            Logger.getLogger(SerialCSVWriteNeuralFunction.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return output;
    }

   

}
