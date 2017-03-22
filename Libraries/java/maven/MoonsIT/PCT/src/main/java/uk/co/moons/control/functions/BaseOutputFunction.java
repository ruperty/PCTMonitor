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
package uk.co.moons.control.functions;

public abstract class BaseOutputFunction implements OutputInterface {
    
    double gain;
    double output;
    double error;
    
    public double update(){
        output = gain * error;        
        return output;
    }
    
    public double getOutput(){
        return output;
    }
    
    public void setError(double e){
        //Logging.display("In setError...", 2, 2);
        
        error = e;
    }
    
}
