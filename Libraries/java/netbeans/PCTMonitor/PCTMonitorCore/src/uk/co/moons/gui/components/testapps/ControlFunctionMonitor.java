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
package uk.co.moons.gui.components.testapps;

import java.beans.PropertyChangeListener;
import javax.swing.event.SwingPropertyChangeSupport;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moons.control.functions.ControlFunction;
import uk.co.moons.control.neural.BaseNeuralFunction;
import uk.co.moons.control.neural.RateNeuralFunction;

/**
 *
 * @author Rupert Young
 * Moon's Information Technology
 *
 */
public class ControlFunctionMonitor {

    public static String SYSTEM = "output";
    private BaseControlFunction inputControl = null;
    private BaseNeuralFunction binf = null;
    private SwingPropertyChangeSupport propChangeSupport = new SwingPropertyChangeSupport(this);

    ControlFunctionMonitor() {
        try {
            binf = new RateNeuralFunction();
        } catch (Exception e) {
            e.printStackTrace();
        }
        inputControl = new ControlFunction(binf);
        inputControl.setName("Test");
        binf.setName("Test");
    }

    public void update() throws Exception {
        double oldValue = inputControl.getValue();
        double newValue = inputControl.update();
        SYSTEM = inputControl.getName();
        propChangeSupport.firePropertyChange(SYSTEM, oldValue, newValue);
    }

    public BaseControlFunction getInputControl() {
        return inputControl;
    }

    public void setInputControl(BaseControlFunction inputControl) {
        this.inputControl = inputControl;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propChangeSupport.removePropertyChangeListener(listener);
    }
}
