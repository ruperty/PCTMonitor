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
public class BlueColorScheme extends BaseColorScheme {

    @Override
    public Color getColor(double min, double max, double value) {
        double d = 155 + 100 * value / (max - min);
        int val = Math.round((float) d);
        if (val > 255) {
            val = 255;
        }
        if (val < 155) {
            val = 155;
        }
        return new Color(0, 0, val);
    }
}
