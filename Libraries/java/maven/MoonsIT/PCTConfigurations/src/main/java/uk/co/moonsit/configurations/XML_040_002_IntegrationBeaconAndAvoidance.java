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
public class XML_040_002_IntegrationBeaconAndAvoidance {

    private final boolean test = false;

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        String file = Utils.getFileName("Robot", "040-002-IntegrationBeaconAndAvoidance.xml");
        XML_040_002_IntegrationBeaconAndAvoidance xml = new XML_040_002_IntegrationBeaconAndAvoidance();

        Globals globals = Globals.getInstance();
        globals.put("SmoothRate_Smoothness", "0.999");
        globals.put("SmoothRate_Initial", "0");
        globals.put("MeanRate_Initial", "0");
        globals.put("Pause_Pause", "10");
        globals.put("IRSensor_Port", "S1");
        globals.put("IRSensor_Channel", "1");

        globals.put("ProximitySensor_Port", "S2");
        globals.put("ProximitySensor_Type", "NXTUltrasonicSensor");
        globals.put("ProximitySensor_Mode", "NULL");

        globals.put("ProximitySensor_Max", "210");

        globals.put("BeaconDistanceControl_Reference", "50");
        globals.put("RotationWeightConsolidated_Constant", "1");
        globals.put("ObjectVelocityReference_Constant", "200"); // base speed used when avoidance is active
        globals.put("ProximityControlReference_Constant", "30");
        globals.put("ProximityControlOutput_Scale", "40");
        globals.put("BeaconActiveSwitch_Upper", "-0.5");
        globals.put("BeaconDistanceControlOutput_OutScale", "600");
        globals.put("ProximityInput_Smoothness", "0.5");
        globals.put("HeadPositionControlOutput_Gain", "2.5");
        globals.put("RelativeHeadBodyOrientationOutput_Gain", "5");
        globals.put("MotorRotationalDifferenceControlOutput_Gain", "2");
        globals.put("B_Acceleration", "1000");
        globals.put("C_Acceleration", "1000");
        globals.put("B_Sign", "1");
        globals.put("C_Sign", "1");

        Utils.saveToXML(xml.run(), file);
        Utils.verify(file);
    }

    public XML_040_002_IntegrationBeaconAndAvoidance() {

    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Integration of beacon following and obstcale avoidance.");
        List layer = layers.getLayer();

        layer.add(configureLayer0("Interface"));
        layer.add(configureLayer1("Control"));
        layer.add(configureLayer2("UpperControl"));

        Modules.moduleAvoidanceProximityTurning(layers);
        Modules.moduleDriveMotorsOutput(layers, test);

        Utils.addToProximity(layers);

        return layers;
    }

    private Layer configureLayer0(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        Controller env = Generic.configureControllerEnv();
        ControlFunction function = Utils.configureControlFunction("RotationWeightConsolidated", "Rotation weight, 1 = no rotation", "Constant", new String[][]{{"Constant", Globals.getInstance().get("RotationWeightConsolidated_Constant"), "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);
        controllers.add(env);
        controllers.add(Generic.configureSensorIR());
        controllers.add(Generic.configureControllerMotorsInput());
        Utils.addLinks(controllers.get(controllers.size() - 1), Utils.INPUT, Utils.TRANSFER, "MotorBPosition", new String[][]{{"AvoidanceActive"}});
        Utils.addLinks(controllers.get(controllers.size() - 1), Utils.INPUT, Utils.TRANSFER, "MotorCPosition", new String[][]{{"AvoidanceActive"}});

        controllers.add(configureControllerMotorAHead());

        return layer;
    }

    private Layer configureLayer1(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        controllers.add(Generic.configureControllerBeaconHeadDirectionControl());
        controllers.add(Generic.configureControllerHeadPositionControl());

        Controller mrdc = Generic.configureControllerMotorRotationalDifferenceControl();
        mrdc.setActivation(Utils.configureActivation("AvoidanceActive", new String[]{"Input", "Reference"}));
        controllers.add(mrdc);

        Controller bdist = Generic.configureControllerBeaconDistanceControl();
        ControlFunction function = Utils.configureControlFunction("BeaconSpeed", "Smoothed value of the output speed", "Smooth",
                new String[][]{{"Smoothness", "0.95", "Double"}}, new String[][]{{"BeaconDistanceControlOutput"}});

        Utils.addTransferFunction(bdist, function, Utils.OUTPUT);
        controllers.add(bdist);

        controllers.add(configureControllerMotorConsolidation());

        return layer;
    }

    private Layer configureLayer2(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        controllers.add(Generic.configureControllerHeadBody());
        controllers.add(Generic.configureControllerBeaconActive());
        return layer;
    }

    private Controller configureControllerMotorAHead() throws Exception {
        Controller controller = new Controller();
        controller.setName("MotorAHead");
        Functions functions = new Functions();

        Functions.InputFunctions inputFunctions = new Functions.InputFunctions();
        ControlFunction input = Utils.configureControlFunction("HeadPositionConsolidated", "Exclusive function of speed input signals", "WeightedExclusive",
                new String[][]{{"Weights", "1,1", "String"}}, new String[][]{{"BeaconHeadDirectionControlOutput"}, {"HeadPositionControlOutput"}});
        inputFunctions.setInput(input);
        functions.setInputFunctions(inputFunctions);

        OutputFunctions outputFunctions = new OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("MotorAOutput", "Setting value of motor A speed", "MotorWrite",
                new String[][]{{"MotorIndex", "A", ""}, {"MotorType", "NXT", ""}, {"LowerPositionLimit", "-150", "Integer"}, {"UpperPositionLimit", "150", "Integer"}, {"Acceleration", "6000", "Integer"}},
                new String[][]{{"HeadPositionConsolidated"}, {"HeadPositionInput"}});

        outputFunctions.setOutput(output);

        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    private Controller configureControllerMotorConsolidation() throws Exception {
        Controller controller = new Controller();
        controller.setName("MotorConsolidation");
        Functions functions = new Functions();

        OutputFunctions outputFunctions = new OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("SpeedConsolidated", "Exclusive function of speed input signals", "WeightedExclusive",
                new String[][]{{"Weights", "1,1", "String"}}, new String[][]{{"ObjectSpeed"}, {"BeaconSpeed"}});

        outputFunctions.setOutput(output);
        {
            Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer = Utils.configureControlFunction("DirectionSpeedAdjustmentConsolidated", "Exclusive function of direction input signals", "WeightedExclusive",
                    new String[][]{{"Weights", "1,1", "String"}}, new String[][]{{"ProximityControlOutput"}, {"MotorRotationalDifferenceControlOutput"}});

            transfersList.add(transfer);
            outputFunctions.setTransfers(transfers);
        }
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

}
