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

import java.util.ArrayList;



public abstract class BaseInputFunction implements InputInterface {
    protected double input;
    private double high = 0;
    private double low = 0;
    ArrayList<String> arInputList = new ArrayList<String>();
    
    BaseInputFunction(){
    }
    
    public double update(){ 
        return input;
    }
    
    public double getInput(){
        return input;
    }
    
    public double getMax(){
        return high;
    }
    
    public double getMin(){
        return low;
    }
    
    public void recordInput(double input){        
        if(input > high)
            high = input;        
        if(input < low)
            low = input;        
    }
    
    public ArrayList<String> getInputList() {
        return arInputList;
    }
    
    public void setInput(double i){
        input = i;
    }
    
}
