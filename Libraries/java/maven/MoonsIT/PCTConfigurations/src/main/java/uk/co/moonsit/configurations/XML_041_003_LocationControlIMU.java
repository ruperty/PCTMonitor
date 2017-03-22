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
import pct.moons.co.uk.schema.layers.Layers.Layer;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller;
import uk.co.moonsit.config.functions.Globals;
import uk.co.moonsit.config.functions.Modules;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_041_003_LocationControlIMU {

    private final boolean test = false;

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
                String file = Utils.getFileName("Robot", "041-003-LocationControlIMU.xml");
        XML_041_003_LocationControlIMU xml = new XML_041_003_LocationControlIMU();

        Globals globals = Globals.getInstance();
        globals.put("SmoothRate_Smoothness", "0.999");
        globals.put("SmoothRate_Initial", "0");
        globals.put("MeanRate_Initial", "0");
        globals.put("Pause_Pause", "10");
        
        globals.put("ProximitySensor_Port", "S2");
        globals.put("ProximityControlReference_Constant", "50");
        globals.put("ProximitySensor_Type", "EV3IRSensor");
        globals.put("ProximitySensor_Mode", "Distance");
        
        globals.put("ProximitySensor_Max", "250");

         globals.put("ProximityInputSmooth_Smoothness", "0.5");
        globals.put("ProximityRawSmooth_Smoothness", "0.95");
        /*
        globals.put("ProximityVelocityReference_InScale", "0.1");
        globals.put("ProximityVelocityReference_OutScale", "-250");
        globals.put("ProximityVelocityReference_XShift", "-25");
        globals.put("ProximityVelocityReference_YShift", "300");//247
        globals.put("ProximityVelocityReference_InputTolerance", "1");
        globals.put("ProximityVelocityReference_Link", "ProximityControlError");
*/
                  globals.put("ProximityVelocity_NamePrefix", "ProximityVelocityReference");

         globals.put("ProximityVelocityReference_InScale", "0.1");
         globals.put("ProximityVelocityReference_OutScale", "300");
         globals.put("ProximityVelocityReference_XShift", "-30");
         globals.put("ProximityVelocityReference_YShift", "300");
         globals.put("ProximityVelocityReference_InputTolerance", "1");
         globals.put("ProximityVelocityReference_Link", "ProximityInputSmooth");
         
        globals.put("ProximityVelocityDirectionReference_InScale", "0.1");
        globals.put("ProximityVelocityDirectionReference_OutScale", "100");
        globals.put("ProximityVelocityDirectionReference_XShift", "-30");
        globals.put("ProximityVelocityDirectionReference_YShift", "200");
        globals.put("ProximityVelocityDirectionReference_InputTolerance", "1");
        globals.put("ProximityVelocityDirectionReference_Link", "ProximityInputSmooth");

        
        globals.put("LocationDistanceControlWeight_InScale", "150000");
        globals.put("LocationDistanceControlWeight_OutScale", "500");//500
        globals.put("LocationDistanceControlWeight_XShift", "0");
        globals.put("LocationDistanceControlWeight_YShift", "0");

        globals.put("RotationWeightConsolidated_Constant", "1");
        globals.put("SpeedConsolidated_Weights", "1,1,1,0");
        globals.put("DirectionSpeedAdjustmentConsolidated_Weights", "1,1,0");

        globals.put("CompassSensor_Type", "MindsensorsAbsoluteIMUSensor");
        globals.put("CompassSensor_Port", "S4");
        globals.put("CompassSensor_AngleType", "AngleClockwise");
        globals.put("CompassVelocityReference_Constant", "0");
        globals.put("CompassBearingControlOutput_Gain", "-2");
        //globals.put("CompassBearingControlReference_Type", "Constant");
        globals.put("CompassBearingControlReference_Link", "LocationBearingReference");
                globals.put("CompassSensor_Interval", "5");

        globals.put("LatitudeControlOutput_Gain", "1");
        globals.put("LongitudeControlOutput_Gain", "1");
        globals.put("LatitudeControlReference_Link", "LatitudePhoneReference");
        globals.put("LongitudeControlReference_Link", "LongitudePhoneReference");

        globals.put("LatitudeSensor_Constant", "44");
        globals.put("LongitudeSensor_Constant", "15");

        globals.put("LatitudeSensor_Port", "6668");
        globals.put("LongitudeSensor_Port", "6668");

        globals.put("LatitudeReference_Constant", "44.000017");
        globals.put("LongitudeReference_Constant", "15.000017");

        globals.put("LatitudePhoneReference_Initial", "44.112979");
        globals.put("LongitudePhoneReference_Initial", "15.228926");

        globals.put("LongitudePhoneReference_Port", "6670");
        globals.put("LatitudePhoneReference_Port", "6670");

        globals.put("B_Acceleration", "1000");
        globals.put("C_Acceleration", "1000");
        globals.put("B_Sign", "-1");
        globals.put("C_Sign", "-1");

        globals.put("PhoneAccelerometerSensor_Port", "6669");
        globals.put("PhoneAccelerometerDirectionControlInput_Smoothness", "0.7");
        globals.put("PhoneAccelerometerSpeedControlInput_Smoothness", "0.7");

        globals.put("PhoneAccelerometerSpeedControlOutput_InScale", "0.2");
        globals.put("PhoneAccelerometerSpeedControlOutput_OutScale", "800");
        globals.put("PhoneAccelerometerSpeedControlOutput_XShift", "0");
        globals.put("PhoneAccelerometerSpeedControlOutput_YShift", "0");
        globals.put("PhoneAccelerometerSpeedControlOutput_InputTolerance", "0.1");

        globals.put("PhoneAccelerometerDirectionControlOutput_InScale", "0.3");
        globals.put("PhoneAccelerometerDirectionControlOutput_OutScale", "200");
        globals.put("PhoneAccelerometerDirectionControlOutput_XShift", "-8");
        globals.put("PhoneAccelerometerDirectionControlOutput_YShift", "197");
        globals.put("PhoneAccelerometerDirectionControlOutput_InputTolerance", "0.1");

        globals.put("ProximityPeriodReference_Constant", "500");

        //globals.put("SpeedWeight_Constant", "0");
                globals.put("VelocityTarget_Constant", "600");

        globals.put("ProximityInputLimiting_Infinity", "false");
        Utils.saveToXML(xml.run(), file);
        Utils.verify(file);

    }

    public XML_041_003_LocationControlIMU() {

    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Basic control of value of sonic reading for obstacle avoidance, with flipping of rotation direction");
        List layer = layers.getLayer();

        layer.add(configureLayer0("Interface"));
        layer.add(Utils.emptyLayer("LocationControl"));
        layer.add(Utils.emptyLayer("CoordinateControl"));
        layer.add(Utils.emptyLayer("AccelerometerControl"));

        modules(layers, test);

        return layers;
    }

    public static void modules(Layers layers, boolean test) throws Exception {
        boolean sensors = false;
        if (!test) {
            Modules.moduleDriveMotorsOutput(layers, test);
            Modules.moduleMotorsMovingSensors(layers);
        }
        Modules.moduleProximityAvoidanceDoubleTurning(layers, test, sensors, true);
        Modules.moduleCompassSystems(layers, test, sensors, XML_041_003_LocationControl.compassBearingReference());
        Modules.moduleLocationSystems(layers, test, sensors, 1);
        Modules.moduleCombinedSensors(layers, new String[]{"CompassSensor", "ProximitySensor", "LatitudeSensor", "LongitudeSensor"}, test);
        Modules.modulePhoneAccelerometerSystems(layers, test, 3, true);

    }


    private Layer configureLayer0(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        controllers.add(XML_041_003_LocationControl.configureEnv(test));
        controllers.add(XML_041_003_LocationControl.configureControllerMotorConsolidation());

        return layer;
    }

   
}
