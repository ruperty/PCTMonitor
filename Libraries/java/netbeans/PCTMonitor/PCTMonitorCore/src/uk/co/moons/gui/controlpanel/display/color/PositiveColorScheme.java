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
public class PositiveColorScheme extends BaseColorScheme {

    @Override
    public Color getColor(double min, double max, double value) {
        Color color = null;
        double d = 155 + 100 * value / (max - min);
        int val = Math.round((float) d);
        if (val > 255) {
            val = 255;
        }
        if (val <= 155) {
            color = new Color(240, 240, 240);
        } else {
            color = new Color(val, val, 0);
        }
        return color;
    }
}
