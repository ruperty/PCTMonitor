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
public class StockColorScheme extends BaseColorScheme {

    @Override
    public Color getColor(double min, double max, double value) {
        int val = 0;
        Color color = null;

        if (value > 0) {
            double d = 155 + 100 * value / max;
            val = Math.round((float) d);
            if (val > 255) {
                val = 255;
            }
            color = new Color(0, val, 0);
        }

        if (value < 0) {
            double d = 155 + 100 * value / min;
            val = Math.round((float) d);
            if (val < 0) {
                val = 0;
            }
            color = new Color(val, 0, 0);
        }
        return color;
    }
}
