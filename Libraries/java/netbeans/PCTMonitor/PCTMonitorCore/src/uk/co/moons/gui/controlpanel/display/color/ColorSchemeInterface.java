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
package uk.co.moons.gui.controlpanel.display.color;

import java.awt.Color;

/**
 *
 * @author ReStart
 */
public interface ColorSchemeInterface {

    public Color getColor(double min, double max, double value);

    public String getName() ;

}
