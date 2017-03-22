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
public class XML_050_002_LocationControlFullIMU {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
                String file = Utils.getFileName("Robot", "050-002-LocationControlFullIMU.xml");
        boolean test = false;

        XML_050_002_LocationControlFullIMU xml = new XML_050_002_LocationControlFullIMU();

        Globals globals = Globals.getInstance();
        globals.put("SmoothRate_Smoothness", "0.99");
        globals.put("SmoothRate_Initial", "10");
        globals.put("MeanRate_Initial", "10");
        globals.put("Pause_Pause", "10");

        globals.put("AccelerometerSensor_Type", "MindsensorsAbsoluteIMUMultipleSensor");
        globals.put("AccelerometerSensor_Port", "S4");
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

        globals.put("CompassSensor_Type", "MindsensorsAbsoluteIMUSensor");
        globals.put("CompassSensor_Port", "S4");
        globals.put("CompassSensor_AngleType", "AngleClockwise");
        globals.put("CompassSensor_Interval", "5");

        globals.put("CompassVelocityReference_Constant", "0");
        globals.put("CompassBearingControlOutput_Gain", "-2");


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
        globals.put("LatitudePhoneReference_Initial", "44.11024120");
        globals.put("LongitudePhoneReference_Initial", "15.29344350");

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
        globals.put("PhoneAccelerometerSpeedControlOutput_OutScale", "1");
        globals.put("PhoneAccelerometerSpeedControlOutput_XShift", "0");
        globals.put("PhoneAccelerometerSpeedControlOutput_YShift", "0");
        globals.put("PhoneAccelerometerSpeedControlOutput_InputTolerance", "0.3");

        globals.put("PhoneAccelerometerDirectionControlOutput_InScale", "0.3");
        globals.put("PhoneAccelerometerDirectionControlOutput_OutScale", "200");
        globals.put("PhoneAccelerometerDirectionControlOutput_XShift", "-8");
        globals.put("PhoneAccelerometerDirectionControlOutput_YShift", "197");
        globals.put("PhoneAccelerometerDirectionControlOutput_InputTolerance", "0.1");

        globals.put("ShakingControlReference_Constant", "20");
        globals.put("ShakingControlOutput_Gain", "1");
        globals.put("ShakingControlOutput_Slow", "75");
        globals.put("ShakingControlInput_Smooth", "0.9");
        //globals.put("ShakingChangeRaw_Gain", "100");

        /*
         globals.put("ShakingControlReference_Constant", "1.2");
         globals.put("ShakingControlOutput_Gain", "1500");
         globals.put("ShakingControlOutput_Slow", "10000");
         globals.put("ShakingControlInput_Smooth", "0.99");*/
        globals.put("VelocityTarget_Constant", "500");
        //globals.put("SpeedWeight_Constant", "0");

        globals.put("ProximityInputLimiting_Infinity", "true");
        Utils.saveToXML(xml.run(test), file);
        Utils.verify(file);

    }

    public XML_050_002_LocationControlFullIMU() {

    }

    public Layers run(boolean test) throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Control of value of proximity reading for obstacle avoidance, "
                + "with flipping of rotation direction. Also included is control from phone accelerometer. "
                + "Plus GPS location control."
                + "Plus shaking control."
                + "Conflict control for direction from proximity overriding phone accelerometer and location bearing."
                + "Conflict control for active phone accelerometer overriding location systems.");

        List layer = layers.getLayer();

        layer.add(configureLayer0("Interfaces", test));
        layer.add(configureLayer1("LowerControl", test));
        layer.add(Utils.emptyLayer("UpperControl"));
        layer.add(Utils.emptyLayer("GPS"));
        layer.add(Utils.emptyLayer("Conflict"));

        modules(layers, test);

        return layers;
    }

    public static void modules(Layers layers, boolean test) throws Exception {
        boolean sensors = false;
        Modules.moduleDriveMotorsOutput(layers, test);
        //if (!test) {            Modules.moduleMotorsMovingSensors(layers);        }

        Modules.moduleProximityAvoidanceDoubleTurning(layers, test, sensors, true);
        Modules.moduleCompassSystems(layers, test, sensors, XML_050_001_LocationControlFull.compassBearingReference());
        int shakingLayerNum = 1, locationLowerlayerNum = 2, conflictLayerNum = 4, locationRefConflictLayerNum = 3, bearingConflictLayerNum = 2;
        Modules.moduleLocationSystems(layers, test, sensors, locationLowerlayerNum);
        addConflictControl(layers, conflictLayerNum, locationRefConflictLayerNum, bearingConflictLayerNum);

        Modules.moduleShakingControlSystems(layers, false, shakingLayerNum, false);
        Modules.moduleCombinedSensors(layers, new String[]{"CompassSensor", "ProximitySensor", "LatitudeSensor", "LongitudeSensor"}, test);
        Modules.modulePhoneAccelerometerSystems(layers, test, 1, false);

    }

    private static void addConflictControl(Layers layers, int conflictLayerNum, int locationRefConflictLayerNum, int bearingConflictLayerNum) throws Exception {
        // Conflict between proximity direction and phone acc direction
        // switch off phone acc dir if proximal
        Utils.addController(layers, conflictLayerNum, Generic.configureControllerDirectionConflictControl());
        // Conflict between phone acc and location
        Utils.addController(layers, conflictLayerNum, Generic.configureControllerPhoneAccLocationConflictControl());
        // if phone acc is active switch off location systems
        ControlFunction function = Utils.configureControlFunction("LatitudeReferenceWeighted", "",
                "Product", null,
                new String[][]{{"LatitudePhoneReference"}, {"PhoneAccLocationConflictControlOutput"}});
        Utils.addTransferFunction(layers, locationRefConflictLayerNum, "LatitudeControl", function, Utils.REFERENCE);
        function = Utils.configureControlFunction("LongitudeReferenceWeighted", "",
                "Product", null,
                new String[][]{{"LongitudePhoneReference"}, {"PhoneAccLocationConflictControlOutput"}});
        Utils.addTransferFunction(layers, locationRefConflictLayerNum, "LongitudeControl", function, Utils.REFERENCE);


    }

    public static Controller configureEnv(boolean test) throws Exception {
        Controller env = Generic.configureControllerEnv();
        ControlFunction function = Utils.configureControlFunction("RotationWeightConsolidated",
                "Rotation weight, 1 = no rotation", "Constant",
                new String[][]{{"Constant", Globals.getInstance().get("RotationWeightConsolidated_Constant"), "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);

        function = Utils.configureControlFunction("VelocityTarget", "Speed target", "Constant",
                new String[][]{{"Constant", Globals.getInstance().get("VelocityTarget_Constant"), "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);
        function = Utils.configureControlFunction("Zero", "Speed target", "Constant",
                new String[][]{{"Constant", "0", "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);

        return env;
    }

    private static Layer configureLayer0(String name, boolean test) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();
        controllers.add(configureEnv(test));

        return layer;
    }

    private static Layer configureLayer1(String name, boolean test) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        controllers.add(configureControllerMotorConsolidation());

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
        ControlFunction output = Utils.configureControlFunction("SpeedConsolidated", "Exclusive function of speed input signals",
                "WeightedMultiply",
                new String[][]{{"Weights", Globals.getInstance().get("SpeedConsolidated_Weights"), "String"}},
                //new String[][]{{"PhoneAccelerometerSpeedControlOutput"}, {"ProximityVelocityReference"}, {"LocationDistanceControlReference"}});
                new String[][]{{"VelocityTarget"}, {"ShakingControlOutput"}, {"ProximityVelocityWeight"}, {"PhoneAccelerometerSpeedControlOutput"}, {"LocationDistanceControlWeight"}});

        outputFunctions.setOutput(output);
        {
            Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer = Utils.configureControlFunction("DirectionSpeedAdjustmentConsolidated",
                    "Sum of direction input signals", "WeightedSum",
                    new String[][]{{"Weights", Globals.getInstance().get("DirectionSpeedAdjustmentConsolidated_Weights"), "String"}},
                    //new String[][]{{"PhoneAccelerometerDirectionControlOutput"}, {"ProximityControlOutputWeighted"}, {"CompassBearingControlOutput"}});
                    new String[][]{{"ProximityVelocityDirectionAdjustmentWeighted"}, {"PhoneAccelerometerDirectionControlOutput"}, {"CompassBearingControlOutput"}});

            transfersList.add(transfer);
            outputFunctions.setTransfers(transfers);
        }
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

}
