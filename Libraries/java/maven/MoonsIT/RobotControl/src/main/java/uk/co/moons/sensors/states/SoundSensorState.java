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
public class SoundSensorState {

    private int soundDB = 0;
    private int soundDBA = 0;
    private int soundType = 0;
    private boolean soundThreadFlag = true;

    /** Creates a new instance of Sensors */
    public SoundSensorState() {
    }


    public synchronized void setSoundType(int s) {
        soundType = s;
    }

    public synchronized int getSoundType() {
        return soundType;
    }

    public  synchronized  int getSoundDB() {
        return soundDB;
    }

    public  synchronized int getSoundDBA() {
        return soundDBA;
    }

    public  synchronized void setSoundDB(int soundDB) {
        this.soundDB = soundDB;
    }

    public  synchronized void setSoundDBA(int soundDBA) {
        this.soundDBA = soundDBA;
    }

    public synchronized void setSoundFlag(boolean s) {
        soundThreadFlag = s;
    }

    public synchronized boolean getSoundFlag() {
        return soundThreadFlag;
    }

}
