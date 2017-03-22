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
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_041_002_LocationControlTest {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    private final boolean test = true;

    public static void main(String[] args) throws Exception {
                String file = Utils.getFileName("Robot", "041-002-LocationControlTest.xml");
        XML_041_002_LocationControlTest xml = new XML_041_002_LocationControlTest();

        Globals globals = Globals.getInstance();

        globals.put("SmoothRate_Smoothness", "0.999");
        globals.put("SmoothRate_Initial", "0");
        globals.put("MeanRate_Initial", "0");
        globals.put("Pause_Pause", "10");

        globals.put("LatitudeSensor_Constant", "44");
        globals.put("LongitudeSensor_Constant", "15");

        globals.put("LatitudeSensor_Port", "6668");
        globals.put("LongitudeSensor_Port", "6668");
        //globals.put("LatitudeReference_Constant", "45");
        //globals.put("LongitudeReference_Constant", "15.5");

        globals.put("LatitudePhoneReference_Initial", "44.112979");
        globals.put("LongitudePhoneReference_Initial", "15.228926");

        globals.put("LatitudeControlOutput_Gain", "1");
        globals.put("LongitudeControlOutput_Gain", "1");

        globals.put("LongitudePhoneReference_Port", "6670");
        globals.put("LatitudePhoneReference_Port", "6670");

        globals.put("LatitudeControlReference_Link", "LatitudePhoneReference");
        globals.put("LongitudeControlReference_Link", "LongitudePhoneReference");

        globals.put("CompassBearingControlReference_Type", "LocationBearing");
        globals.put("CompassBearingControlOutput_Gain", "-3.5");
        globals.put("CompassBearingControlReference_Link", "LocationBearingReference");

        globals.put("LocationDistanceControlWeight_InScale", "20000");
        globals.put("LocationDistanceControlWeight_OutScale", "500");//500
        globals.put("LocationDistanceControlWeight_XShift", "0");
        globals.put("LocationDistanceControlWeight_YShift", "0");

        globals.put("LocationDistanceControlReference_InScale", "20000");
        globals.put("LocationDistanceControlReference_OutScale", "500");
        globals.put("LocationDistanceControlReference_XShift", "0");
        globals.put("LocationDistanceControlReference_YShift", "0");

        globals.put("ProximityControlReference_Constant", "50");

        globals.put("ProximityVelocity_NamePrefix", "ProximityVelocityReference");

        globals.put("ProximityVelocityReference_InScale", "0.1");
        globals.put("ProximityVelocityReference_OutScale", "-250");
        globals.put("ProximityVelocityReference_XShift", "-25");
        globals.put("ProximityVelocityReference_YShift", "247");
        globals.put("ProximityVelocityReference_InputTolerance", "1");

        globals.put("ProximityVelocityDirectionReference_InScale", "0.1");
        globals.put("ProximityVelocityDirectionReference_OutScale", "250");
        globals.put("ProximityVelocityDirectionReference_XShift", "-30");
        globals.put("ProximityVelocityDirectionReference_YShift", "300");
        globals.put("ProximityVelocityDirectionReference_InputTolerance", "1");
        globals.put("ProximityVelocityDirectionReference_Link", "ProximityInputSmooth");
        globals.put("RotationWeightConsolidated_Constant", "1");
        globals.put("SpeedConsolidated_Weights", "1,1,1,0");
        globals.put("DirectionSpeedAdjustmentConsolidated_Weights", "1,1,0");

        globals.put("ProximityVelocityReference_Link", "ProximityControlError");

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

        globals.put("B_Acceleration", "1000");
        globals.put("C_Acceleration", "1000");

        globals.put("ProximitySensor_Port", "S2");
        globals.put("ProximityInputSmooth_Smoothness", "0.5");
        globals.put("ProximityRawSmooth_Smoothness", "0.9995");

        //globals.put("SpeedWeight_Constant", "1");
        globals.put("VelocityTarget_Constant", "600");

        globals.put("ProximityPeriodReference_Constant", "500");

        globals.put("ProximityInputLimiting_Infinity", "false");

        Utils.saveToXML(xml.run(), file);
        Utils.verify(file);
    }

    public XML_041_002_LocationControlTest() {
    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Integration of beacon following and obstcale avoidance.");
        List layer = layers.getLayer();

        layer.add(configureLayer0("Interface"));
        layer.add(Utils.emptyLayer("LocationControl"));
        layer.add(Utils.emptyLayer("CoordinateControl"));

        XML_041_003_LocationControl.modules(layers, test);

        return layers;
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
