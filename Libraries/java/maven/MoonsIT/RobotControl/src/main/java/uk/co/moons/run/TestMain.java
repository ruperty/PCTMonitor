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
package uk.co.moons.run;

import uk.co.moons.control.RobotControlHierarchy;

/**
 *
 * @author ReStart
 */
public class TestMain {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws Exception {

        String config = "C:\\Versioning\\Personal\\Controllers\\Robot\\012-002-TwoMotorRotateRefs.xml";
        //NXT.connect();
        RobotControlHierarchy ch = null;
        ch = new RobotControlHierarchy(config);
        ch.setOutputFile("C:\\tmp\\trading\\Versioning\\Personal\\Controllers\\Robot\\output\\out.csv");

        ch.init();//.initRobot();
        ch.run();


    }
}
