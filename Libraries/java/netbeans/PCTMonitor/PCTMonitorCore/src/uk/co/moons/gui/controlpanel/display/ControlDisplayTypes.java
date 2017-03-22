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
package uk.co.moons.gui.controlpanel.display;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.Properties;
import uk.co.moons.gui.controlpanel.display.color.BaseColorScheme;
import uk.co.moons.gui.controlpanel.display.color.ColorSchemeFactory;

/**
 *
 * @author ReStart
 */
public class ControlDisplayTypes {

    private final int MAXPLACES = 6;
    private Double min;
    private Double max;
    private int places = 3;
    private DecimalFormat df;
    private String format;
    private final String name;
    private BaseColorScheme colorScheme;

    public ControlDisplayTypes(String name) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        this.name = name;
        format = format(places);
        df = new DecimalFormat(format);
        colorScheme = ColorSchemeFactory.getColorScheme("StockColorScheme");
    }

    private String format(int places) {
        String rtn = null;
        switch (places) {
            case 0:
                rtn = "0";
                break;
            case 1:
                rtn = "0.0";
                break;
            case 2:
                rtn = "0.00";
                break;
            case 3:
                rtn = "0.000";
                break;
            case 4:
                rtn = "0.0000";
                break;
            case 5:
                rtn = "0.00000";
                break;
            case 6:
                rtn = "0.000000";
                break;
            default:
                rtn = "0.000";
                break;
        }

        return rtn;
    }

    public Color getColor(double value) {
        if (min == null) {
            return colorScheme.getColor(0, 0, value);
        }
        return colorScheme.getColor(min, max, value);
    }

    private int getPlaces(double d) {
        int i = 0;

        while (Math.abs(d) < 1) {
            if (i == MAXPLACES) {
                break;
            }
            d = d * 10;
            i++;
        }
        return i;
    }

    public String getText(double value) {

        if (places <= MAXPLACES) {
            int pl = getPlaces(value);
            if (pl > places) {
                places = pl;
                format = format(places);
                df = new DecimalFormat(format);
            }
        }
        if (value == 0) {
        } else if (min == null) {
            min = value;
            max = value;
        } else {

            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }

        return df.format(value);
    }

    public void setParameter(String val) throws ClassNotFoundException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String[] arr = val.split("=");
        if (arr[0].equals("min")) {
            min = Double.valueOf(arr[1]);
        }
        if (arr[0].equals("max")) {
            max = Double.valueOf(arr[1]);
        }
        if (arr[0].equals("color")) {
            colorScheme = ColorSchemeFactory.getColorScheme(arr[1]);
        }
        if (arr[0].equals("format")) {
            format = arr[1];
            places = format.length() - 2;
            df = new DecimalFormat(format);
        }

    }

    public void setProperties(Properties props) {

        props.setProperty(name + "_" + "min", String.valueOf(min == null ? 0 : min));
        props.setProperty(name + "_" + "max", String.valueOf(max == null ? 0 : max));
        props.setProperty(name + "_" + "color", colorScheme.getName());
        props.setProperty(name + "_" + "format", format);

    }
}
