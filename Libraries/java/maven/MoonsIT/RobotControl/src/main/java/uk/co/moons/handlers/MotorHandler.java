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
package uk.co.moons.handlers;



/**
 *
 * @author Rupert Young
 */

public class MotorHandler {

    public static final int FORWARD = 0,  BACKWARD = 1,  ROTATE = 2,  STOP = 3;
    private boolean motorsThreadFlag = true;
    private int count = 0;

    /** Creates a new instance of MotorHandler */
    public MotorHandler(boolean f) {
        motorsThreadFlag = f;
    }

    /*
    public MotorHandler() {
        Motor.A.setRegulationMode(Motor.A.REGULATION_MODE_IDLE);
    }*/

    public synchronized void setMotorsFlag(boolean m) {
        motorsThreadFlag = m;
    }

    public synchronized boolean getMotorsFlag() {
        return motorsThreadFlag;
    }

    public synchronized void setMotorsCount(int c) {
        count = c;
    }

    public synchronized int getMotorsCount() {
        return count;
    }
/*
    public void run(ArrayList motors) {
        ListIterator it = motors.listIterator();
        Vector v = (Vector) it.next();
        setMotorA(v);
    }
    */
/*
    public void setMotorA(Vector v) {
        System.out.println("+++ setMotorA ");

        CommandHandler.getSingleton().command(Motor.A, CommandHandler.MOTOR_SET_SPEED, (((Integer) v.elementAt(0)).intValue()));

        switch (((Integer) v.elementAt(1)).intValue()) {
            case STOP:

                CommandHandler.getSingleton().command(Motor.A, CommandHandler.MOTOR_STOP, 0);
                //System.out.println("+++ stop");
                break;
            case FORWARD:
                CommandHandler.getSingleton().command(Motor.A, CommandHandler.MOTOR_FORWARD, 0);
                //System.out.println("+++ forward");
                break;
            case BACKWARD:
                CommandHandler.getSingleton().command(Motor.A, CommandHandler.MOTOR_BACKWARD, 0);
                //System.out.println("+++ backward");
                break;
            case ROTATE:
                System.out.println("+++ rotating " + ((Integer) v.elementAt(2)).intValue());
                CommandHandler.getSingleton().command(Motor.A, CommandHandler.MOTOR_ROTATE, ((Integer) v.elementAt(2)).intValue());
                //CommandHandler.getSingleton().command( Motor.A, CommandHandler.MOTOR_STOP, 0);
                //System.out.println("+++ count " + Motor.A.getRotationCount());
                break;
        }
    }
    */
}
