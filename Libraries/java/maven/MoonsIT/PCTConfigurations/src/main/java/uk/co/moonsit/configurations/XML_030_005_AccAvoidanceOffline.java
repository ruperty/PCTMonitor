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

import java.util.List;
import pct.moons.co.uk.schema.layers.Layers;
import uk.co.moonsit.config.functions.Generic;
import uk.co.moonsit.config.functions.Globals;
import uk.co.moonsit.config.functions.Modules;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_030_005_AccAvoidanceOffline {

    private final boolean test = true;

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
                String file = Utils.getFileName("Robot", "030-005-AccAvoidanceOffline.xml");
        XML_030_005_AccAvoidanceOffline xml = new XML_030_005_AccAvoidanceOffline();

        Globals globals = Globals.getInstance();
        globals.put("SmoothRate_Smoothness", "0.999");
        globals.put("SmoothRate_Initial", "0");
        globals.put("MeanRate_Initial", "0");
        globals.put("Pause_Pause", "10");

        globals.put("ProximitySensor_Port", "S2");
        globals.put("ProximityControlReference_Constant", "50");
        globals.put("ProximitySensor_Type", "NXTUltrasonicSensor");
        globals.put("ProximitySensor_Max", "210");
        globals.put("ProximityInputSmooth_Smoothness", "0.5");
        globals.put("ProximityRawSmooth_Smoothness", "0.75");

        globals.put("ProximityVelocity_NamePrefix", "ProximityVelocityWeight");

        globals.put("ProximityVelocityWeight_InScale", "0.13");
        globals.put("ProximityVelocityWeight_OutScale", "-0.5");
        globals.put("ProximityVelocityWeight_XShift", "-25");
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

        globals.put("RotationWeightConsolidated_Constant", "1");

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

        globals.put("B_Acceleration", "1000");
        globals.put("C_Acceleration", "1000");

        globals.put("Consolidated_Weights", "1,1,1");
        globals.put("DirectionConsolidated_Weights", "1,1");

        globals.put("VelocityTarget_Constant", "600");

        globals.put("ProximityInputLimiting_Infinity", "true");

        Utils.saveToXML(xml.run(), file);
        Utils.verify(file);
    }

    public XML_030_005_AccAvoidanceOffline() {

    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Basic control of value of sonic reading for obstacle avoidance, "
                + "with flipping of rotation direction. Also included is control from phone accelerometer. "
                + "Both systems are activated according to presence, or absence, of signal (by infinity value. "
                + "When active their weight contribition to the velocity is less then 1.");

        List layer = layers.getLayer();

        layer.add(XML_030_006_AccAvoidance.configureLayer0("Interface"));
        
        layer.add(XML_030_006_AccAvoidance.configureLayer1("SensorControl"));
        layer.add(Utils.emptyLayer("Direction"));
        Modules.moduleDriveMotorsOutput(layers, test);
        Modules.moduleProximityAvoidanceDoubleTurning(layers, test, false, false);
        Modules.modulePhoneAccelerometerSystems(layers, test, 1, false);

        Utils.addController(layers, 2, Generic.configureControllerDirectionConflictControl());

        return layers;
    }

}
