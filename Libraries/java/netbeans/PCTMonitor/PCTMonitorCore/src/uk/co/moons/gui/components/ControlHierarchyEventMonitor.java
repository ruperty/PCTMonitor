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
package uk.co.moons.gui.components;

import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import javax.swing.event.SwingPropertyChangeSupport;
import uk.co.moons.control.ControlFunctionCollection;
import uk.co.moons.control.ControlHierarchy;
import uk.co.moons.control.ControlLayer;
import uk.co.moons.control.functions.BaseControlFunction;

/**
 *
 * @author Rupert Young
 * Moon's Information Technology
 *
 */
public class ControlHierarchyEventMonitor {

    public static String UPDATE = "update";
    public static String TIME = "time";
    private ControlHierarchy controlHierarchy = null;
    private final SwingPropertyChangeSupport propChangeSupport = new SwingPropertyChangeSupport(this);
    private final SwingPropertyChangeSupport propTimeChangeSupport = new SwingPropertyChangeSupport(this);
    private HashMap<String, Double> hmOldValues = null;
    private String oldTime = null;

    public ControlHierarchyEventMonitor() {
        hmOldValues = new HashMap<>();
    }

    public ControlHierarchy getControlHierarchy() {
        return controlHierarchy;
    }

    public void setControlHierarchy(ControlHierarchy controlHierarchy) {
        this.controlHierarchy = controlHierarchy;
    }

    public void update() throws Exception {
        String newTime = controlHierarchy.getTime();
        //System.out.println(System.currentTimeMillis()+" "+newTime);
        propTimeChangeSupport.firePropertyChange(TIME, oldTime, newTime);
        oldTime = newTime;
        
        ControlLayer[] layers = controlHierarchy.getLayers();

        for (int layer = 0; layer < layers.length; layer++) {
            int systems = layers[layer].size();
            for (int system = 0; system < systems; system++) {

                ControlFunctionCollection input = controlHierarchy.getController(layer, system).getInputFunction();
                if (input != null) {
                    fireEvent(input.getMainFunction());
                    List<BaseControlFunction> transfers = input.getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            fireEvent(transfer);
                        }
                    }
                }

                ControlFunctionCollection reference = controlHierarchy.getController(layer, system).getReferenceFunction();
                if (reference != null) {
                    fireEvent(reference.getMainFunction());
                    List<BaseControlFunction> transfers = reference.getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            fireEvent(transfer);
                        }
                    }
                }
                if (controlHierarchy.getController(layer, system).getErrorFunction() != null) {
                    fireEvent(controlHierarchy.getController(layer, system).getErrorFunction().getMainFunction());
                }


                ControlFunctionCollection output = controlHierarchy.getController(layer, system).getOutputFunction();
                if (output != null) {
                    fireEvent(output.getMainFunction());
                    List<BaseControlFunction> transfers = output.getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            fireEvent(transfer);
                        }
                    }
                }
            }
        }
    }

    private void fireEvent(BaseControlFunction function) {
        double newValue = function.getValue();

        Double oldValue = hmOldValues.get(function.getName());
        if (oldValue == null) {
            hmOldValues.put(function.getName(), 0.00001);
        } else {
            propChangeSupport.fireIndexedPropertyChange(UPDATE, function.getNeural().getPosindex(), oldValue, newValue);
            hmOldValues.put(function.getName(), newValue);
        }

    }

    public void clear(){
        hmOldValues.clear();
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propChangeSupport.addPropertyChangeListener(UPDATE, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propChangeSupport.removePropertyChangeListener(UPDATE, listener);
    }

    public void addTimePropertyChangeListener(PropertyChangeListener listener) {
        propTimeChangeSupport.addPropertyChangeListener(TIME, listener);
    }

    public void removeTimePropertyChangeListener(PropertyChangeListener listener) {
        propTimeChangeSupport.removePropertyChangeListener(TIME, listener);
    }
}
