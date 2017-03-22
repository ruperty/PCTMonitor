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

import java.util.logging.Logger;
import uk.co.moonsit.utils.timing.Time;

/**
 *
 * @author ReStart
 */
public class ModelControlHierarchy extends ControlHierarchy {
    
    private static final Logger logger = Logger.getLogger(ModelControlHierarchy.class.getName());
    
    private double time=0;
    
    public ModelControlHierarchy(String config) throws Exception {
        super(config);
        timeRate = 0.01 / 3600; // default is 10ms, in hours
    
        //DateAndTime dt = new DateAndTime();        
        //dt.Now();        
        //time = dt.Time().getTime();
        
    }
    
    @Override
    public void updateTime(){
         time += timeRate;
    }
    
    @Override
    public String getTime() {     
        Time t = new Time(time );
        
        return t.HMSS();
    }

    
    
}
