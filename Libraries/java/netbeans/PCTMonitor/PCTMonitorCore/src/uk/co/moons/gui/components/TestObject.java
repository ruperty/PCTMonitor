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
package uk.co.moons.gui.components;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ReStart
 */
public class TestObject implements Runnable {

    private int value = 50;
//private ControlFunctionDisplay cfd;

    public TestObject() {
    }

    /*
    public void setCFD(ControlFunctionDisplay cfd){
    this.cfd=cfd;

    }
     */
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void run() {

        for (int i = 0; i < 100; i = i + 10) {
            value = value + 10;
            if (value >= 255) {
                value = 0;
            }
            System.out.println("+++ " + value);
            //cfd.revalidate();
            //cfd.repaint();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TestObject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }
}
