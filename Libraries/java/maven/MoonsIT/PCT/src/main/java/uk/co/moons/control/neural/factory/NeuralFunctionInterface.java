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

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 *
 * @author Rupert Young
 */
public interface NeuralFunctionInterface {

    public double compute() throws Exception;

    public double getOutput();

    public void setOutput(double o);

    public String getConfigParameter(String par);

    public double getParameter();

    public double getParameter(int i);

    public int getParameterInt(int index) throws Exception;

    public int getParametersSize();

    public String getParameterName(int index);

    public double getParameter(String field) throws NoSuchFieldException, SecurityException, IllegalAccessException;

    public Double getParameterDouble(String f) throws NoSuchFieldException, SecurityException, IllegalAccessException;

    public Boolean getParameterBoolean(String f) throws NoSuchFieldException, SecurityException, IllegalAccessException;

    public Object getParameterObject(String f) throws NoSuchFieldException, SecurityException, IllegalAccessException;

    public Type getParameterType(String f) throws NoSuchFieldException, SecurityException, IllegalAccessException;

    public String getDataString();

    public String getParametersString();

    //public int getParameter(int index, boolean b) throws Exception;
    public void setParameter(String par);

    public void setParameter(double par);

    public String getTime();

    public void setField(Field field, String value) throws IllegalArgumentException, IllegalAccessException;

    public void close() throws Exception;

    public Double stop() throws Exception;

    public void pause() throws Exception;

    public void deactivate() throws Exception;

    //public void init()throws Exception;
    //public void post()throws Exception;
}
