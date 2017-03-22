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
package uk.co.moons.control.filter;

/**
 *
 * @author adam
 */
public class AutoTuneFilter {
    
    public Double direction = null; // random direction from -1 to +1
    public Double pastErr = null;   // needed to see if error is reducing or not
    public Integer counter = null;
    public Double avgErr = null;  
    
    public AutoTuneFilter () {
        direction = 1.0;
        pastErr = 0.0;
        counter = 0;
        avgErr = 0.0;
    }

    public double compute (double gain, double error, int delay, double maxErr, double reorgGain) {
        error = Math.abs(error);
        avgErr += (error - avgErr)/(double)delay;
        
        if (counter >= delay) { // every delay iterations, check the error
            if (avgErr > pastErr) direction = -direction/1.1; //2.0*(0.5 - Math.random()); // tumble
            System.out.println("                         pastErr " + pastErr +
                    " avgErr:"  + avgErr);
            counter = 0;
            pastErr = avgErr;
            //avgErr = 0.0;
                  
        }
        counter++;
        
        if (error > maxErr) error = maxErr;
        double change  = error * reorgGain * direction;
        System.out.println("reorgGain " + reorgGain +  " direction: " + direction +
                " change:" + change);
        
        return (gain+change);
    }
}