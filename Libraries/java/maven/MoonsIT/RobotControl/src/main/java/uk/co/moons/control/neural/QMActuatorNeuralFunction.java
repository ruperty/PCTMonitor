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
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.actuators.QMActuator;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moonsit.messaging.QMState;

/**
 *
 * @author Rupert Young
 */
public class QMActuatorNeuralFunction extends NeuralFunction {

    private static final Logger LOG = Logger.getLogger(QMActuatorNeuralFunction.class.getName());

    public String variable = null;
    public boolean publish = false;
    private int ivariable;
    private QMActuator actuator;

    public QMActuatorNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("Variable")) {
                variable = param.getValue();
                setIvariable();
            }
            if (param.getName().equals("Publish")) {
                publish = Boolean.parseBoolean(param.getValue());
            }
        }
        if (variable == null) {
            throw new Exception("Null configuration value for variable");
        }

    }

   

    
    

    @Override
    public void init() throws Exception {
        actuator = new QMActuator();
        reset();
    }

    private void reset() {
        switch (variable) {
            case "LaserOutputX":
                output = actuator.getValue(QMState.STARTX);
                break;
            case "LaserOutputY":
                output = actuator.getValue(QMState.STARTY);
                break;
        }

    }

    private void setIvariable() {
        switch (variable) {
            case "LaserOutputX":
                ivariable = QMState.LASEROUTPUTX;
                break;
            case "LaserOutputY":
                ivariable = QMState.LASEROUTPUTY;
                break;
        }
    }

    @Override
    public double compute() throws IOException {
        if (actuator.isReset()) {
            reset();
        }
        List<BaseControlFunction> controls = links.getControlList();
        output = controls.get(0).getValue();

        actuator.setValue(ivariable, output);
        if (publish) {
            actuator.publish();
            if (actuator.isReset()) {
                actuator.unset();
            }
        }
        
        return output;
    }

    @Override
    public void setParameter(String par) {
        super.setParameter(par);
        if (actuator == null) {
            return;
        }
        String[] arr = par.split(":");
        if (arr[0].equals("Variable")) {
            variable = arr[1];
            setIvariable();
        }
    }
}
