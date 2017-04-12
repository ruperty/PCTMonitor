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
package uk.co.moons.control;

import java.io.IOException;

/**
 *
 * @author ReStart
 */

public interface ControlHierarchyInterface {

    public boolean isRunning();

    public void setRunningFlag(boolean stopFlag);

    public void run() throws Exception;

    public void stop() throws Exception;

    public void pause() throws Exception;

    public void close() throws Exception;

    public void specificProcessing() throws IOException, Exception;

    public void init() throws Exception;

    public void post() throws Exception;

    public void print(String prefix);

    public String getTime();

    public void updateTime();
    
    public void setValues(String values);
    public void setValues(double[] values);

    public void setControllerParameter(String controller, String parameter, String value)throws  Exception;

    public void setControllerParameter(String controller, String pars)throws  Exception;

    public void orderedIterate() throws Exception;

    public void iterate() throws Exception;

    public void layeredIterate() throws Exception;
}
