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
package uk.co.moons.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

/**
 *
 * @author ReStart
 */
public class ControlBuildFactory {

    private static final Logger LOG = Logger.getLogger(ControlBuildFactory.class.getName());

    @SuppressWarnings({"unchecked", "rawtypes", "LoggerStringConcat"})
    public static BaseControlBuild getControlBuildFunction(String config) throws ClassNotFoundException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        BaseControlBuild bcc = null;
        String type = "PipeControlBuild";
        if (config.contains(".xml") || config.startsWith("<")) {
            type = "XmlControlBuild";
        }
        String className = "uk.co.moons.config." + type;
        //LOG.info("Build type class " + className);

        Class theClass = Class.forName(className);

        Constructor constructor = theClass.getConstructor(String.class);
        try {
            bcc = (BaseControlBuild) constructor.newInstance(config);
        } catch (InvocationTargetException | java.lang.InstantiationException ex) {
            LOG.severe("+++ Cannot create string constructor " + className);
            throw ex;
        }

        return bcc;
    }

    @SuppressWarnings({"unchecked", "rawtypes", "LoggerStringConcat"})

    public static BaseControlBuild getControlBuildFunction(int num) throws ClassNotFoundException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        BaseControlBuild bcc = null;
        String className = "uk.co.moons.xml.layers.BaseControlBuild";
        Class theClass = Class.forName(className);

        Constructor constructor = theClass.getConstructor(String.class);
        try {
            bcc = (BaseControlBuild) constructor.newInstance(num);
        } catch (InvocationTargetException | java.lang.InstantiationException ex) {
            LOG.severe("+++ Cannot create string constructor " + className);
            throw ex;
        }

        return bcc;
    }
}
