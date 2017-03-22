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
public abstract class BaseColorScheme implements ColorSchemeInterface {

    public Color getColor(double min, double max, double value) {
        double d = 255 * (value-min) / (max - min);
        int val = Math.round((float) d);
        if (val > 255) {
            val = 255;
        }
        if (val < 0) {
            val = 0;
        }
        return new Color(val, val, val);
    }

    public Color getColor() {
        return Color.red;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }
}
