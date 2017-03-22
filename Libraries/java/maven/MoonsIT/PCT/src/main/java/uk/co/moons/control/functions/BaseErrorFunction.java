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

//import uk.co.moons.logging.Logging;

public abstract class BaseErrorFunction implements ErrorInterface {
    
    BaseReferenceFunction  reference;
    //double reference=300;
    double perception=0; 
    double error;
    
    public void setReference(BaseReferenceFunction r){
        reference = r;
    }

    public double update(){
        /*
        Logging.clear();
        Logging.display("In Error ...", 2, 2);
        
        Logging.display((new Integer((int)reference)).toString(),  2, 4);
        Logging.display((new Integer((int)perception)).toString(),  2, 5);
        Logging.display((new Integer((int)error)).toString(), 2000, 2, 6);
        */
        
        error = reference.update() - perception;
        
        return error;
    }
    
    public double getError(){
        return error;
    }
    
    public void setPerception(double p){
        //Logging.display("In setPerception ...", 2, 2);
        
        perception = p;
    }
}
