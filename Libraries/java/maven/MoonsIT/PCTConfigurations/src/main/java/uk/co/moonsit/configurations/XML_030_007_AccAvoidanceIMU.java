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
import pct.moons.co.uk.schema.layers.ControlFunction;
import pct.moons.co.uk.schema.layers.Layers;
import pct.moons.co.uk.schema.layers.Layers.Layer;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.OutputFunctions;
import uk.co.moonsit.config.functions.Generic;
import uk.co.moonsit.config.functions.Globals;
import uk.co.moonsit.config.functions.Modules;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_030_007_AccAvoidanceIMU {

    private final boolean test = false;

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
                String file = Utils.getFileName("Robot", "030-007-AccAvoidanceIMU.xml");
        XML_030_007_AccAvoidanceIMU xml = new XML_030_007_AccAvoidanceIMU();

        Globals globals = Globals.getInstance();
        globals.put("SmoothRate_Smoothness", "0.99");
        globals.put("SmoothRate_Initial", "0");
        globals.put("MeanRate_Initial", "0");
        globals.put("Pause_Pause", "10");

        globals.put("ProximitySensor_Port", "S2");
        globals.put("ProximityControlReference_Constant", "50");
        //globals.put("ProximitySensor_Type", "NXTUltrasonicSensor");
        //globals.put("ProximitySensor_Mode", "NULL");
        
        //globals.put("ProximitySensor_Type", "EV3UltrasonicSensor");
        //globals.put("ProximitySensor_Mode", "Distance");
        //globals.put("ProximitySensor_Mode", "Listen");
        
        globals.put("ProximitySensor_Type", "EV3IRSensor");
        globals.put("ProximitySensor_Mode", "Distance");
        
        globals.put("ProximitySensor_Max", "210");
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
        globals.put("B_Sign", "-1");
        globals.put("C_Sign", "-1");

        globals.put("Consolidated_Weights", "1,1,1");
        globals.put("DirectionConsolidated_Weights", "1,1");

        globals.put("VelocityTarget_Constant", "600");
        //globals.put("VelocityTarget_Constant", "0");
        
        globals.put("ProximityInputLimiting_Infinity", "true");

        Utils.saveToXML(xml.run(), file);
        Utils.verify(file);
    }

    public XML_030_007_AccAvoidanceIMU() {

    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Basic control of value of proximity reading for obstacle avoidance, "
                + "with flipping of rotation direction. Also included is control from phone accelerometer. "
                + "Both systems are activated according to presence, or absence, of signal (by infinity value. "
                + "When active their weight contribition to the velocity is less then 1.");
        
        List layer = layers.getLayer();

        layer.add(configureLayer0("Interface"));
        layer.add(configureLayer1("SensorControl"));
        layer.add(Utils.emptyLayer("Direction"));
        Modules.moduleDriveMotorsOutput(layers, test);
        Modules.moduleProximityAvoidanceDoubleTurning(layers, test, true, false);
        Modules.modulePhoneAccelerometerSystems(layers, test, 1, false);

        Utils.addController(layers, 2, Generic.configureControllerDirectionConflictControl());
        
        return layers;
    }

    public static Layer configureLayer1(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        controllers.add(configureControllerMotorConsolidation());
     return layer;
    }    
    
    public static Layer configureLayer0(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        Controller env = Generic.configureControllerEnv();
        controllers.add(env);
        ControlFunction function = Utils.configureControlFunction("RotationWeightConsolidated", "Rotation weight, 1 = no rotation", "Constant", new String[][]{{"Constant", Globals.getInstance().get("RotationWeightConsolidated_Constant"), "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);

        function = Utils.configureControlFunction("VelocityTarget", "Speed target", "Constant",
                new String[][]{{"Constant", Globals.getInstance().get("VelocityTarget_Constant"), "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);

        //controllers.add(Common.configureProximitySensor());
        return layer;
    }

    public static Controller configureControllerMotorConsolidation() throws Exception {
        Controller controller = new Controller();
        controller.setName("MotorConsolidation");
        Functions functions = new Functions();

        String prefix = Globals.getInstance().get("ProximityVelocity_NamePrefix");

        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction(prefix, "Velocity reference, as a sigmoid function of the sonic proximity", "Sigmoid",
                new String[][]{{"InScale", Globals.getInstance().get(prefix + "_InScale"), "Double"},
                {"OutScale", Globals.getInstance().get(prefix + "_OutScale"), "Double"},
                {"XShift", Globals.getInstance().get(prefix + "_XShift"), "Double"},
                {"YShift", Globals.getInstance().get(prefix + "_YShift"), "Double"}},
                new String[][]{{"ProximityControlOutput"}});

        referenceFunctions.setReference(reference);
        {
            Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer = Utils.configureControlFunction("ProximityVelocityDirectionAdjustment", "Velocity reference, as a sigmoid function of the sonic proximity", "Sigmoid",
                    new String[][]{{"InScale", Globals.getInstance().get("ProximityVelocityDirectionAdjustment_InScale"), "Double"},
                    {"OutScale", Globals.getInstance().get("ProximityVelocityDirectionAdjustment_OutScale"), "Double"},
                    {"XShift", Globals.getInstance().get("ProximityVelocityDirectionAdjustment_XShift"), "Double"},
                    {"YShift", Globals.getInstance().get("ProximityVelocityDirectionAdjustment_YShift"), "Double"}},
                    new String[][]{{"ProximityControlOutput"}});

            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("ProximityVelocityDirectionAdjustmentWeighted", "Product of sonic error ratio, velocity and direction", "Product",
                    null,
                    new String[][]{{"DirectionFlip"}, {"ProximityVelocityDirectionAdjustment"}});

            transfersList.add(transfer);

            referenceFunctions.setTransfers(transfers);

        }
        functions.setReferenceFunctions(referenceFunctions);

        OutputFunctions outputFunctions = new OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("SpeedConsolidated",
                "Exclusive function of speed input signals", "WeightedMultiply",
                new String[][]{{"Weights", Globals.getInstance().get("Consolidated_Weights"), "String"}},
                new String[][]{{"ProximityVelocityWeight"}, {"PhoneAccelerometerSpeedControlOutput"}, {"VelocityTarget"}});

        outputFunctions.setOutput(output);
        {
            Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer = Utils.configureControlFunction("DirectionSpeedAdjustmentConsolidated",
                    "Exclusive function of direction input signals", "WeightedSum",
                    new String[][]{{"Weights", Globals.getInstance().get("DirectionConsolidated_Weights"), "String"}},
                    new String[][]{{"ProximityVelocityDirectionAdjustmentWeighted"}, {"PhoneAccelerometerDirectionControlOutput"}});

            transfersList.add(transfer);
            outputFunctions.setTransfers(transfers);
        }
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

}
