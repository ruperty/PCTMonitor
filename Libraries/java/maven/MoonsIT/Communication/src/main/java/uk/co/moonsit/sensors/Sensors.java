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
package uk.co.moonsit.sensors;

/**
 *
 * @author Rupert Young
 */
public class Sensors {

    //private double linAcc = 0;

    private boolean linAccThreadFlag = true;
 
    /** Creates a new instance of Sensors */
    public Sensors() {
    }

    /*
    public synchronized void setLinAcc(double s) {
        linAcc = s;
    }

    public synchronized double getLinAcc() {
        return linAcc;
    }*/

    
    
    public synchronized boolean getLinAccFlag() {
        return linAccThreadFlag;
    }

    public synchronized void setLinAccThreadFlag(boolean linAccThreadFlag) {
        this.linAccThreadFlag = linAccThreadFlag;
    }

}
