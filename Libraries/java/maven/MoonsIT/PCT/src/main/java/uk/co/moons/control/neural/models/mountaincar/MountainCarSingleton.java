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
package uk.co.moons.control.neural.models.mountaincar;

//import org.rlcommunity.environments.mountaincar.MountainCarState;
/**
 *
 * @author ReStart
 */
public class MountainCarSingleton {
    
    protected MountainCarState mountainCarState;

    //private static final Logger logger = Logger.getLogger(MountainCarSingleton.class.getName());
    private static MountainCarSingleton instance = null;

    protected MountainCarSingleton() {
        mountainCarState = new MountainCarState(null, false, 0, 0);
    }

    
    protected MountainCarSingleton(Double initial, boolean paramBoolean, double paramDouble, long paramLong) {
        mountainCarState = new MountainCarState(initial, paramBoolean, paramDouble, paramLong);
    }

    
    public static MountainCarSingleton getInstance() {
        
        if (instance == null) {
            instance = new MountainCarSingleton();
        }
        
        return instance;
    }
    /**
     *
     * @param initial
     * @param paramBoolean random position and velocity on reset, if true
     * @param paramDouble random noise factor
     * @param paramLong random noise seed
     * @return
     *
     */
    public static MountainCarSingleton getInstance(Double initial, boolean paramBoolean, double paramDouble, long paramLong) {
        
        if (instance == null) {
            instance = new MountainCarSingleton(initial, paramBoolean, paramDouble, paramLong);
        }
        
        return instance;
    }
    
    public void update(int paramInt) {
        mountainCarState.update(paramInt);
    }
    
    public double getPosition() {
        return mountainCarState.getPosition();
    }
    
    public double getVelocity() {
        return mountainCarState.getVelocity();
    }
    
    
     public double getHeightAtPosition(double queryPosition) {
        return mountainCarState.getHeightAtPosition(queryPosition);
    }
    public void reset(Double initial, boolean paramBoolean, double paramDouble, long paramLong) {
        mountainCarState = new MountainCarState(initial, paramBoolean, paramDouble, paramLong);
    }
}
