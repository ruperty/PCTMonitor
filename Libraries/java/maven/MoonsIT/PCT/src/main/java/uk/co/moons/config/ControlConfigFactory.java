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
public class ControlConfigFactory {

    private static final Logger LOG = Logger.getLogger(ControlConfigFactory.class.getName());

    @SuppressWarnings({"unchecked", "rawtypes", "LoggerStringConcat"})
    public static BaseControlConfig getControlConfigFunction(String config) throws ClassNotFoundException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        BaseControlConfig bcc = null;
        String type = "PipeConfig";
        if (config.contains(".xml") || config.startsWith("<")) {
            type = "XmlConfig";
        }
        String className = "uk.co.moons.config." + type;
        //LOG.info("Build type class " + className);
        Class theClass = Class.forName(className);

        Constructor constructor = theClass.getConstructor(String.class);
        try {
            long start = System.currentTimeMillis();
            bcc = (BaseControlConfig) constructor.newInstance(config);
            LOG.info("---> load " + (System.currentTimeMillis() - start));

        } catch (InvocationTargetException | java.lang.InstantiationException ex) {
            LOG.severe("+++ Cannot create string constructor " + className);
            throw ex;
        }

        return bcc;
    }
}
