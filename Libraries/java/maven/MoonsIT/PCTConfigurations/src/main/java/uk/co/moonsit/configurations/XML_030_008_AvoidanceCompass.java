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
public class XML_030_008_AvoidanceCompass {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
                String file = Utils.getFileName("Robot", "030-008-AvoidanceCompass.xml");
        boolean test = false;

        XML_030_008_AvoidanceCompass xml = new XML_030_008_AvoidanceCompass();

        Globals globals = Globals.getInstance();
        globals.put("SmoothRate_Smoothness", "0.99");
        globals.put("SmoothRate_Initial", "0");
        globals.put("MeanRate_Initial", "0");
        globals.put("Pause_Pause", "10");

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

        globals.put("RotationWeightConsolidated_Constant", "1");

        globals.put("SpeedConsolidated_Weights", "1,1,1");
        globals.put("DirectionSpeedAdjustmentConsolidated_Weights", "1,1,1");

        globals.put("CompassSensor_Type", "HiTechnicCompassSensor");
        globals.put("CompassSensor_Port", "S4");
        globals.put("CompassSensor_AngleType", "AngleClockwise");
        globals.put("CompassSensor_Interval", "5");

        globals.put("CompassVelocityReference_Constant", "0");
        globals.put("CompassBearingControlOutput_Gain", "-2");
        globals.put("LocationBearingReference_Constant", "90");

        globals.put("B_Acceleration", "1000");
        globals.put("C_Acceleration", "1000");
        globals.put("B_Sign", "1");
        globals.put("C_Sign", "1");

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

        globals.put("VelocityTarget_Constant", "100");
        //globals.put("SpeedWeight_Constant", "0");

        globals.put("ProximityInputLimiting_Infinity", "true");
        Utils.saveToXML(xml.run(test), file);
        Utils.verify(file);

    }

    public XML_030_008_AvoidanceCompass() {

    }

    public Layers run(boolean test) throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Control of value of proximity reading for obstacle avoidance, "
                + "with flipping of rotation direction. Also included is control from phone accelerometer. "
                + "Conflict control for direction from proximity overriding phone accelerometer and location bearing. "
                + "Compass memory.");

        List layer = layers.getLayer();

        layer.add(configureLayer0("Interfaces", test));
        layer.add(configureLayer1("LowerControl", test));
        layer.add(Utils.emptyLayer("UpperControl"));

        modules(layers, test);

        return layers;
    }

    public static void modules(Layers layers, boolean test) throws Exception {
        boolean sensors = false;
        Modules.moduleDriveMotorsOutput(layers, test);
        //if (!test) {            Modules.moduleMotorsMovingSensors(layers);        }

        Modules.moduleProximityAvoidanceDoubleTurning(layers, test, sensors, true);
        Modules.moduleCompassSystems(layers, test, sensors, compassBearingReference());
        int upperLayerNum = 2;
        addConflictControl(layers, upperLayerNum);

        Modules.moduleCombinedSensors(layers, new String[]{"CompassSensor", "ProximitySensor"}, test);
        Modules.modulePhoneAccelerometerSystems(layers, test, 1, false);
        //Modules.moduleBearingMemoryConflict(layers, upperLayerNum);
    }

    private static void addConflictControl(Layers layers, int conflictLayerNum) throws Exception {
        // Conflict between proximity direction and phone acc direction
        // switch off phone acc dir if proximal
        Utils.addController(layers, conflictLayerNum, Generic.configureControllerDirectionConflictControl());

        Utils.addController(layers, conflictLayerNum, Generic.configureControllerBearingMemoryConflict(), 1);

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

    public static Layers.Layer.Controller.Functions.ReferenceFunctions compassBearingReference() throws Exception {
        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction("CompassBearingControlReference",
                "Head direction reference, zero is centre", "Product", null,
                new String[][]{{"LocationBearingReference"}, {"DirectionConflictControlOutputWeight"}});

        {
            Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            // if proximal switch off location bearing
            ControlFunction transfer = Utils.configureControlFunction("LocationBearingConstant", "Constant",
                    "Constant", new String[][]{{"Constant", "90", "Double"}}, null);
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("LocationBearingMemoryInactive", "If input is infinity, memory inactive, this is 1 else 0",
                    "DigitalLimit",
                    new String[][]{{"Threshold", "180", "Double"}, {"Upper", "1", "Double"}, {"Lower", "0", "Double"}},
                    new String[][]{{"BearingMemoryConflictOutput"}});
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("LocationBearingConstantWeighted", "Weighted by 0 or 1 depending if the memory is active or inactive.",
                    "Product",
                    null,
                    new String[][]{{"LocationBearingMemoryInactive"}, {"LocationBearingConstant"}});
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("LocationBearingReference",
                    "Addition of memory and constant value. If former is valid latter will be 0. Otherwise latter will be constant value.",
                    "WeightedSum",
                    new String[][]{{"Weights", "1,1", "String"}},
                    new String[][]{{"BearingMemoryConflictOutput"}, {"LocationBearingConstantWeighted"}});
            transfersList.add(transfer);

            referenceFunctions.setTransfers(transfers);
        }

        referenceFunctions.setReference(reference);
        return referenceFunctions;
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
                new String[][]{{"VelocityTarget"}, {"ProximityVelocityWeight"}, {"PhoneAccelerometerSpeedControlOutput"}});

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
