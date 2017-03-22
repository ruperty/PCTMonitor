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

import javax.swing.JTextField;

/**
 *
 * @author ReStart
 */
public class ControlTextField extends JTextField{

    private final String name;

    public ControlTextField(String name, int columns) {
        super(columns);
        this.name=name;
    }

    public String getString(){
        return name + ":"+getText();
    }

}
