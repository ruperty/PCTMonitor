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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ReStart
 */

@SuppressWarnings("unchecked")
public class ColorSchemeFactory {

    static final Logger logger = Logger.getLogger(ColorSchemeFactory.class.getName());

    public static BaseColorScheme getColorScheme(String scheme) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        BaseColorScheme cs;
        String className = "uk.co.moons.gui.controlpanel.display.color." + scheme;
        Class<BaseColorScheme> theClass;
        theClass = (Class<BaseColorScheme>)  Class.forName(className);

        Constructor<BaseColorScheme> constructor;
        constructor = theClass.getConstructor();
        try {
            cs =  constructor.newInstance();
        } catch (InvocationTargetException ex) {
            logger.log(Level.SEVERE, "+++ Cannot create empty constructor {0}", className);
            throw ex;
        }

        return cs;
    }
}
