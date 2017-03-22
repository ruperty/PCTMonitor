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
package uk.co.moons.motors;

import java.util.logging.Logger;

/**
 *
 * @author ReStart
 */
public abstract class BaseMotorsImpl {

    private static final Logger logger = Logger.getLogger(BaseMotorsImpl.class.getName());

    public static final int MOTORA = 0;
    public static final int MOTORB = 1;
    public static final int MOTORC = 2;

    //private RemoteMotor motor = null;
    protected int motorIndex = -1;

    private int mi = 0;

    public void init() {

    }

    public void close() throws Exception {
    }

    public void stop() throws Exception {

    }

    protected int convertMotorIndex(String m) {
        if (m.equals("A")) {
            return BaseMotorsImpl.MOTORA;
        }
        if (m.equals("B")) {
            return BaseMotorsImpl.MOTORB;
        }
        if (m.equals("C")) {
            return BaseMotorsImpl.MOTORC;
        }

        return -1;
    }

    public void resetTacho() {

    }
    //public void stop() throws Exception {
    //}

    public void setSpeed(int output) {
    }

    public void setAcceleration(int acc) {
    }

    public float getMaxSpeed() {
        return 0;
    }

    public void rotateTo(int angle) {
    }

    public void rotate(int value) {

    }

    public int count() {

        return 0;
    }

    public int getSpeed() {
        return 0;
    }

    public void motorRegulation() {

    }

    public int getCount() {
        return 0;
    }

    public int getDirection() {
        return 0;
    }

    public void setPower(double output) {
    }

    public void setBackward() {

    }

    public void setForward() {

    }

    public int isMoving() {
        return 0;
    }

    public int isStalled() {
        return 0;
    }
}
