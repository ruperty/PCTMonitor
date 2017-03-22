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
public class XML_050_003_LocationControlFullOffline {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
                String file = Utils.getFileName("Robot", "050-003-LocationControlFullOffline.xml");
        boolean test = true;

        XML_050_001_LocationControlFull xml = new XML_050_001_LocationControlFull();

        Globals globals = Globals.getInstance();
        globals.put("SmoothRate_Smoothness", "0.999");
        globals.put("SmoothRate_Initial", "0");
        globals.put("MeanRate_Initial", "0");
        globals.put("Pause_Pause", "250");

        globals.put("AccelerometerSensor_Type", "HiTechnicAccelerometerSensor");
        globals.put("AccelerometerSensor_Port", "S1");
        globals.put("Accelerometer_Threaded", "true");
        globals.put("Accelerometer_Interval", "5");

        globals.put("ProximitySensor_Port", "S2");
        globals.put("ProximityControlReference_Constant", "50");
        globals.put("ProximitySensor_Type", "EV3IRSensor");
        globals.put("ProximitySensor_Mode", "Distance");

        globals.put("ProximitySensor_Max", "100");

        globals.put("ProximityInputSmooth_Smoothness", "0.5");
        globals.put("ProximityRawSmooth_Smoothness", "0.75");

        globals.put("ProximityVelocity_NamePrefix", "ProximityVelocityWeight");

        globals.put("ProximityVelocityWeight_InScale", "0.15");
        globals.put("ProximityVelocityWeight_OutScale", "-0.5");
        globals.put("ProximityVelocityWeight_XShift", "-10");
        globals.put("ProximityVelocityWeight_YShift", "0.5");
        globals.put("ProximityVelocityWeight_InputTolerance", "1");
        globals.put("ProximityVelocityWeight_Link", "ProximityControlError");

        globals.put("ProximityVelocityDirectionReference_InScale", "0.1");
        globals.put("ProximityVelocityDirectionReference_OutScale", "100");
        globals.put("ProximityVelocityDirectionReference_XShift", "-30");
        globals.put("ProximityVelocityDirectionReference_YShift", "200");
        globals.put("ProximityVelocityDirectionReference_InputTolerance", "0");
        globals.put("ProximityVelocityDirectionReference_Link", "ProximityInputSmooth");

        globals.put("ProximityVelocityDirectionAdjustment_InScale", "0.1");
        globals.put("ProximityVelocityDirectionAdjustment_OutScale", "250");
        globals.put("ProximityVelocityDirectionAdjustment_XShift", "0");
        globals.put("ProximityVelocityDirectionAdjustment_YShift", "0");

        globals.put("ProximityPeriodReference_Constant", "600");

        globals.put("LocationDistanceControlWeight_InScale", "50000");
        globals.put("LocationDistanceControlWeight_OutScale", "1");//500
        globals.put("LocationDistanceControlWeight_XShift", "0");
        globals.put("LocationDistanceControlWeight_YShift", "0");

        globals.put("RotationWeightConsolidated_Constant", "1");

        globals.put("SpeedConsolidated_Weights", "1,1,1,1,1");
        globals.put("DirectionSpeedAdjustmentConsolidated_Weights", "1,1,1");

        globals.put("CompassSensor_Type", "HiTechnicCompassSensor");
        globals.put("CompassSensor_Port", "S4");
        globals.put("CompassSensor_AngleType", "AngleClockwise");
        globals.put("CompassVelocityReference_Constant", "0");
        globals.put("CompassBearingControlOutput_Gain", "-2");
        //globals.put("CompassBearingControlReference_Type", "Constant");

        globals.put("CompassBearingControlReference_Link", "LocationBearingReferenceWeighted");

        globals.put("LatitudeControlOutput_Gain", "1");
        globals.put("LongitudeControlOutput_Gain", "1");
        globals.put("LatitudeControlReference_Link", "LatitudeReferenceWeighted");
        globals.put("LongitudeControlReference_Link", "LongitudeReferenceWeighted");

        globals.put("LatitudeSensor_Constant", "44");
        globals.put("LongitudeSensor_Constant", "15");

        globals.put("LatitudeSensor_Port", "6668");
        globals.put("LongitudeSensor_Port", "6668");

        //globals.put("LatitudeReference_Constant", "44.000017");
        //globals.put("LongitudeReference_Constant", "15.000017");
        globals.put("LatitudePhoneReference_Initial", "44.112979");
        globals.put("LongitudePhoneReference_Initial", "15.228926");

        globals.put("LongitudePhoneReference_Port", "6670");
        globals.put("LatitudePhoneReference_Port", "6670");

        globals.put("B_Acceleration", "1000");
        globals.put("C_Acceleration", "1000");

        globals.put("PhoneAccelerometerSensor_Port", "6669");
        globals.put("PhoneAccelerometerDirectionControlInput_Smoothness", "0.7");
        globals.put("PhoneAccelerometerSpeedControlInput_Smoothness", "0.7");

        globals.put("PhoneAccelerometerSpeedControlOutput_InScale", "0.2");
        globals.put("PhoneAccelerometerSpeedControlOutput_OutScale", "1");
        globals.put("PhoneAccelerometerSpeedControlOutput_XShift", "0");
        globals.put("PhoneAccelerometerSpeedControlOutput_YShift", "0");
        globals.put("PhoneAccelerometerSpeedControlOutput_InputTolerance", "0.1");

        globals.put("PhoneAccelerometerDirectionControlOutput_InScale", "0.3");
        globals.put("PhoneAccelerometerDirectionControlOutput_OutScale", "200");
        globals.put("PhoneAccelerometerDirectionControlOutput_XShift", "-8");
        globals.put("PhoneAccelerometerDirectionControlOutput_YShift", "197");
        globals.put("PhoneAccelerometerDirectionControlOutput_InputTolerance", "0.1");

        globals.put("ShakingControlReference_Constant", "6000");
        globals.put("ShakingControlOutput_Gain", "1");
        globals.put("ShakingControlOutput_Slow", "50000");
        globals.put("ShakingChange_Period", "1000");
        globals.put("ShakingControlInput_Smooth", "0.99");
        globals.put("ShakingChangeRaw_Gain", "100");

        globals.put("VelocityTarget_Constant", "600");
        //globals.put("SpeedWeight_Constant", "0");

        globals.put("ProximityInputLimiting_Infinity", "true");
        Utils.saveToXML(xml.run(test), file);
        Utils.verify(file);

    }

    public XML_050_003_LocationControlFullOffline() {

    }

}
