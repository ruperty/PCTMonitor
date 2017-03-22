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
public class BaseUnregulatedMotorsImpl {

    private static final Logger logger = Logger.getLogger(BaseUnregulatedMotorsImpl.class.getName());

    public static final int MOTORA = 0;
    public static final int MOTORB = 1;
    public static final int MOTORC = 2;

    //private RemoteMotor motor = null;
    protected int motorIndex = -1;

    private int mi = 0;

    public void init() {

       
    }

    protected int convertMotorIndex(String m) {
        if (m.equals("A")) {
            return BaseUnregulatedMotorsImpl.MOTORA;
        }
        if (m.equals("B")) {
            return BaseUnregulatedMotorsImpl.MOTORB;
        }
        if (m.equals("C")) {
            return BaseUnregulatedMotorsImpl.MOTORC;
        }

        return -1;
    }

    public void close() throws Exception {
    }

    public void stop() throws Exception {

    }
    /*
    public void stop() throws Exception {
        CommandHandler.getSingleton().command(motor, CommandHandler.MOTOR_STOP, 0);
        //CommandHandler.getSingleton().command(motor, CommandHandler.MOTOR_ROTATETO, 0);
    }
*/
    public void resetTacho() {
    }
/*
    public void setSpeed(int output) {
        CommandHandler.getSingleton().command(motor, CommandHandler.MOTOR_SET_SPEED, output);
    }

    public void setAcceleration(int acc) {
        CommandHandler.getSingleton().command(motor, CommandHandler.MOTOR_SET_ACCELERATION, acc);
    }

    public float getMaxSpeed() {
        return motor.getMaxSpeed();
    }

    public void rotateTo(int angle) {
        CommandHandler.getSingleton().command(motor, CommandHandler.MOTOR_ROTATETO, angle, true);
    }

    public void rotate(int value) {
        CommandHandler.getSingleton().command(motor, CommandHandler.MOTOR_ROTATE, value);

    }
*/
    public int count() {

        return 0;
    }
/*
    public int getSpeed() {
        return CommandHandler.getSingleton().command(motor, CommandHandler.MOTOR_GET_SPEED, 0);
    }

    public void motorRegulation() {

        CommandHandler.getSingleton().command(motor, CommandHandler.MOTOR_REGULATION, 0);
    }

    public int getCount() {
        return CommandHandler.getSingleton().command(motor, CommandHandler.MOTOR_COUNT, 0);
    }

    public int getDirection() {
        return CommandHandler.getSingleton().getDirection(mi);
    }
*/
    public void setPower(double output) {
    }

    public void setBackward() {

    }

    public void setForward() {

    }
    
    	public void flt() {
	}

  
}
