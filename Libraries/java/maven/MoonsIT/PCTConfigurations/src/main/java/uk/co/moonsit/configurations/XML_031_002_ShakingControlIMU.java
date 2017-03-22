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
package uk.co.moonsit.configurations;

import uk.co.moonsit.config.functions.Globals;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_031_002_ShakingControlIMU {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        boolean test = false;
                String file = Utils.getFileName("Robot", "031-002-ShakingControlIMU.xml");
        XML_031_001_ShakingControl xml = new XML_031_001_ShakingControl();

        Globals globals = Globals.getInstance();
        globals.put("SmoothRate_Smoothness", "0.99");
        globals.put("SmoothRate_Initial", "10");
        globals.put("MeanRate_Initial", "10");
        globals.put("Pause_Pause", "10");

        globals.put("RotationWeightConsolidated_Constant", "1");

        globals.put("AccelerometerSensor_Type", "MindsensorsAbsoluteIMUMultipleSensor");
        globals.put("AccelerometerSensor_Port", "S4");
        globals.put("Accelerometer_Threaded", "true");
        globals.put("Accelerometer_Smooth", "0.9");
        globals.put("Accelerometer_Interval", "5");

        globals.put("VelocityReference_Constant", "100");
        globals.put("Zero_Constant", "0");

        globals.put("B_Acceleration", "1000");
        globals.put("C_Acceleration", "1000");
        globals.put("B_Sign", "-1");
        globals.put("C_Sign", "-1");

        globals.put("Consolidated_Weights", "1,1,1");

        globals.put("DirectionConsolidated_Weights", "1");

        globals.put("PhoneAccelerometerSensor_Port", "6669");
        globals.put("PhoneAccelerometerDirectionControlInput_Smoothness", "0.7");
        globals.put("PhoneAccelerometerSpeedControlInput_Smoothness", "0.7");

        globals.put("PhoneAccelerometerSpeedControlOutput_InScale", "0.2");
        globals.put("PhoneAccelerometerSpeedControlOutput_OutScale", "1");
        globals.put("PhoneAccelerometerSpeedControlOutput_XShift", "0");
        globals.put("PhoneAccelerometerSpeedControlOutput_YShift", "0");
        globals.put("PhoneAccelerometerSpeedControlOutput_InputTolerance", "0.3");

        globals.put("PhoneAccelerometerDirectionControlOutput_InScale", "0.3");
        globals.put("PhoneAccelerometerDirectionControlOutput_OutScale", "200");
        globals.put("PhoneAccelerometerDirectionControlOutput_XShift", "-8");
        globals.put("PhoneAccelerometerDirectionControlOutput_YShift", "197");
        globals.put("PhoneAccelerometerDirectionControlOutput_InputTolerance", "0.1");

        globals.put("ShakingControlReference_Constant", "42");
        globals.put("ShakingControlOutput_Gain", "1");
        globals.put("ShakingControlOutput_Slow", "100");
        globals.put("ShakingControlInput_Smooth", "0.99");
        //globals.put("ShakingChange_Period", "10");
        //globals.put("ShakingChangeRaw_Gain", "100");

        globals.put("VelocityTarget_Constant", "900");

        globals.put("CompassSensor_Type", "MindsensorsAbsoluteIMUSensor");
        globals.put("CompassSensor_Port", "S4");
        globals.put("CompassSensor_AngleType", "AngleClockwise");
        globals.put("CompassSensor_Interval", "5");

        Utils.saveToXML(xml.run(test), file);
        Utils.verify(file);
    }

    public XML_031_002_ShakingControlIMU() {

    }

}
