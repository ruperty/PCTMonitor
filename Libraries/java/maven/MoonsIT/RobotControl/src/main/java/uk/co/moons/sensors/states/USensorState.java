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
package uk.co.moons.sensors.states;

/**
 *
 * @author Rupert Young
 */
public class USensorState {

    private int sonic = 255;
    private boolean sonicThreadFlag = true;

    /** Creates a new instance of Sensors */
    public USensorState() {
    }

    public synchronized void setSonic(int s) {
        sonic = s;
    }

    public synchronized int getSonic() {
        return sonic;
    }


    public synchronized void setSonicFlag(boolean s) {
        sonicThreadFlag = s;
    }

    public synchronized boolean getSonicFlag() {
        return sonicThreadFlag;
    }

}
