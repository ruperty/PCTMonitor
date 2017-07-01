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

public class TestObject {

    public static String VALUE = "value";
    private int value = 0;
    private SwingPropertyChangeSupport propChangeSupport = new SwingPropertyChangeSupport(this);

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        int oldValue = this.value;
        int newValue = value;
        this.value = value;
        VALUE = "value";
        propChangeSupport.firePropertyChange(VALUE, oldValue, newValue);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propChangeSupport.removePropertyChangeListener(listener);
    }
}
