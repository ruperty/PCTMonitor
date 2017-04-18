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
package uk.co.moons.control.neural.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Link;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.neural.BaseNeuralFunction;

/**
 *
 * @author ReStart
 */
public class NeuralFunctionFactory {

    private static final Logger LOG = Logger.getLogger(NeuralFunctionFactory.class.getName());

    private static String getClassName(String name) {
        String className;
        if (name.contains(".")) {
            className = name;
        } else if (name.contains("Node")) {
            className = "uk.co.moons.control.neural.nodes." + name;
        } else {
            className = "uk.co.moons.control.neural." + name + "NeuralFunction";
        }

        return className;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static BaseNeuralFunction getNeuralFunction(pct.moons.co.uk.schema.layers.NeuralFunction nf) throws ClassNotFoundException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        BaseNeuralFunction bnf = null;

        String className = getClassName(nf.getType());
        Class theClass = Class.forName(className);

        List<Parameters> list = nf.getParameters();
        if (list.isEmpty()) {
            Constructor constructor = theClass.getConstructor();
            try {
                bnf = (BaseNeuralFunction) constructor.newInstance();
                //logger.info("+++ created " + className);
            } catch (InvocationTargetException ex) {
                LOG.log(Level.SEVERE, "+++ Cannot create empty constructor {0}", className);
                throw ex;
            }
        } else {
            Constructor constructor = theClass.getConstructor(List.class);
            try {
                bnf = (BaseNeuralFunction) constructor.newInstance(list);
                //logger.info("+++ created " + className);
            } catch (InvocationTargetException | java.lang.InstantiationException ex) {
                LOG.log(Level.SEVERE, "+++ Cannot create parameter constructor {0}", className);
                throw ex;
            }
        }
        bnf.setConfigLinks(nf.getLinks());

        return bnf;
    }

    private static List<Parameters> getParameters(String p) {
        List<Parameters> parameters = new ArrayList<>();
        //logger.info("---> " + p);
        String[] pars = p.split("~");
        if (pars.length > 0 && p.contains("~")) {
            for (int i = 0; i < pars.length; i++) {
                Parameters par = new Parameters();
                String[] vals = pars[i].split("\\^");
                par.setName(vals[0]);
                par.setValue(vals[1]);
                if (vals.length > 2) {
                    par.setDataType(vals[2]);
                }
                parameters.add(par);
            }
        }

        return parameters;
    }

    private static List<Link> getLinks(String p) {
        List<Link> links = new ArrayList<Link>();
        //logger.info("---> "+p);
        String[] lks = p.split("~");
        if (p.length() > 0 && lks.length > 0) {

            for (int i = 0; i < lks.length; i++) {
                Link link = new Link();
                String[] vals = lks[i].split("\\^");
                link.setName(vals[0]);
                link.setIndex(vals[1]);
                link.setType(vals[2]);
                links.add(link);
            }
        }

        return links;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static BaseNeuralFunction getNeuralFunction(String name, String type, String plinks) throws ClassNotFoundException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        BaseNeuralFunction bnf = null;

        String className = "uk.co.moons.control.neural." + type + "NeuralFunction";
        Class theClass = Class.forName(className);
        //logger.info("+++ " + plinks);
        String[] pl = plinks.split("&");
        List<Parameters> list = null;
        if (pl != null && pl.length > 0) {
            list = getParameters(pl[0]);
        }
        if (list == null || list.isEmpty()) {
            Constructor constructor = theClass.getConstructor();
            try {
                bnf = (BaseNeuralFunction) constructor.newInstance();
                //logger.info("+++ created " + className);
            } catch (InvocationTargetException ex) {
                LOG.severe("+++ Cannot create empty constructor " + className);
                throw ex;
            }
        } else {
            Constructor constructor = theClass.getConstructor(List.class);
            try {
                bnf = (BaseNeuralFunction) constructor.newInstance(list);
                //logger.info("+++ created " + className);
            } catch (InvocationTargetException ex) {
                LOG.severe("+++ Cannot create parameter constructor " + className);
                throw ex;
            } catch (java.lang.InstantiationException ex) {
                LOG.severe("+++ Cannot create parameter constructor " + className);
                throw ex;
            }
        }
        bnf.setConfigLinks(pl.length > 1 ? getLinks(pl[1]) : getLinks(""));

        return bnf;
    }
    // private pct.moons.co.uk.schema.layers.NeuralFunction createNeuralFunction(){
}
