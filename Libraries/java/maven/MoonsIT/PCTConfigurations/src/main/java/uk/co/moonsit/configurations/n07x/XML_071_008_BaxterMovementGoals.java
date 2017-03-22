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
package uk.co.moonsit.configurations.n07x;

/*
 * This software is the property of Moon's Information Technology Ltd.
 * All rights reserved.
 * The software is only to be used for development and research purposes.
 * Commercial use is only permitted under license or agreement.
 */
import java.util.List;
import pct.moons.co.uk.schema.layers.ControlFunction;
import pct.moons.co.uk.schema.layers.Layers;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller;
import uk.co.moonsit.config.functions.Generic;
import uk.co.moonsit.config.functions.Globals;
import uk.co.moonsit.config.functions.Modules;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_071_008_BaxterMovementGoals {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        String file = Utils.getFileName("Robot", "ROS", "071-008-BaxterMovementGoals.xml");
        System.out.println(file);

        XML_071_008_BaxterMovementGoals xml = new XML_071_008_BaxterMovementGoals();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "50");
        globals.put("GlobalSpeedWeight", "1.5");
        globals.put("GoalGeneratorName", "GoalGenerator");
        globals.put("GoalGenerator_Initial", "1300");
        globals.put("Data_Directory", "Versioning/PCTSoftware/Libraries/java/netbeans/data");

        String system = "ArmReach";
        globals.put(system + "ControlManualReference_Constant", "0.9");
        system = "HandElevation";
        globals.put(system + "ControlManualReference_Constant", "-0.3");
        system = "HandPitch";
        globals.put(system + "ControlManualReference_Constant", "-0.92");
        system = "ShoulderYawS0";
        globals.put(system + "ControlManualReference_Constant", "0");

        String side = "Left";
        globals.put("ArmOne_Side", side);
        xml.armGlobals(globals, side);

        side = "Right";
        globals.put("ArmTwo_Side", side);
        xml.armGlobals(globals, side);

        Utils.saveToXML(xml.run(), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    public XML_071_008_BaxterMovementGoals() {
    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Baxter arm control");

        layers.getLayer().add(configureLayerEnv("Baxter"));
        layers.getLayer().add(Utils.emptyLayer("Hand"));
        layers.getLayer().add(Utils.emptyLayer("Memory"));

        //layers.getLayer().add(Utils.emptyLayer("RightMovement"));
        String[] systems = new String[]{"ArmReach", "HandElevation", "HandPitch", "ShoulderYawS0"};

        String[] leftlinks = getHandSystemInputLinks(Globals.getInstance().get("ArmOne_Side"), systems);
        String[] rightlinks = getHandSystemInputLinks(Globals.getInstance().get("ArmTwo_Side"), systems);
        Modules.baxterMemoryList(layers, 2, systems, leftlinks, rightlinks);

        String side = Globals.getInstance().get("ArmOne_Side");
        addArmControls(layers, side, 1, systems);

        side = Globals.getInstance().get("ArmTwo_Side");
        addArmControls(layers, side, 1, systems);

        return layers;
    }

    private void addArmControls(Layers layers, String side, int layerNum, String[] memorySystems) throws Exception {
        Utils.addController(layers, 0, Generic.configureSubscribers3D(side));

        Controller controller = Utils.emptyController(side + "Interface", true, false, false, true);
        Utils.addController(layers, 0, controller);

        String[] handSystemNames = new String[]{"ArmReachControl", "HandElevationControl", "HandPitchControl", "ShoulderYawS0Control"};

        addJointControlSystems(controller, side, handSystemNames);

        ControlFunction[] handReferences = getHandReferences(side, memorySystems);

        //Utils.addTransferFunction(layers, 0, side + "Interface", handReferences[3][0], Utils.OUTPUT, 0);
        Modules.addHandControlSystemsMemory(layers, layerNum, handSystemNames, handReferences, side);

    }

    private static ControlFunction[] getHandReferences(String side, String[] memorySystems) throws Exception {
        ControlFunction[] handReferences = new ControlFunction[memorySystems.length];
        for (int i = 0; i < handReferences.length; i++) {
            handReferences[i] = Utils.configureControlFunction(side + memorySystems[i] + "ControlReference", "...", "",
                    null, new String[][]{{side + memorySystems[i] + "MemoryPositionReference"}});
        }
        return handReferences;
    }

    private static String[] getHandSystemInputLinks(String side, String[] movementSystems) {
        String[] movementInputLinks = new String[movementSystems.length];
        for (int i = 0; i < movementSystems.length; i++) {
            movementInputLinks[i] = side + movementSystems[i] + "ControlInput";
        }

        return movementInputLinks;
    }

    /*
    private static ControlFunction[][] addMovementControlSystemsDirect(Layers layers, String side, int sidelayerNum, String[] handSystemNames, String[] movementSystems) throws Exception {
        ControlFunction[][] handReferences = new ControlFunction[4][];

        for (int i = 0; i < movementSystems.length; i++) {
            //ControlFunction positionWeight = Common..
            // Constant reference for movement system
            //ControlFunction positionReference = Utils.configureControlFunction(side + movementSystem + "MovementPositionControlReference", "...", "Constant", new String[][]{{"Constant", Globals.getInstance().get(side + movementSystem + "MovementPositionControlReference_Constant"), "Double"}}, null);

            handReferences[i] = Modules.armHandMovementControlsDirectNamed(layers, sidelayerNum, movementSystems[i], side, side + movementSystems[i] + "ControlInput");
        }

        return handReferences;
    }*/
    private static void addJointControlSystems(Controller controller, String side, String[] handSystems) throws Exception {
        String joint = "ElbowAngleE1";
        Generic.addPositionSubPub(controller, false, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,side + handSystems[0] + "OutputToElbow", false);
        joint = "ShoulderPitchS1";
        Generic.addPositionSubPub(controller, true, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,side + handSystems[0] + "OutputToShoulder", side + handSystems[1] + "Output");
        joint = "HandAngleW1";
        Generic.addPositionSubPub(controller, true, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,side + handSystems[2] + "Output", false);
        joint = "ShoulderYawS0Angle";//LeftShoulderYawS0MovementVelocityControlOutput side + joint + "MovementVelocityControlOutput"
        //String shoulderYawLink = side + joint + "MemoryPositionReference";

        Generic.addPositionSubPub(controller, true, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,side + handSystems[3] + "Output", false);
        joint = "ElbowRollE0";
        Generic.addPositionSubPub(controller, true, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,joint + "PositionControlManualReference", false);
        joint = "WristRollW0";
        Generic.addPositionSubPub(controller, true, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,joint + "PositionControlManualReference", false);
        joint = "HandRollW2";
        Generic.addPositionSubPub(controller, true, joint, side, true, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,joint + "PositionControlManualReference", false);

    }

    private static Layers.Layer configureLayerEnv(String name) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();
        controllers.add(configureControllerEnv());

        return layer;
    }

    private static Layers.Layer.Controller configureControllerEnv() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("Env");

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();
        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction("Rate", "Iteration rate", "Rate", null, null);
            inputFunctions.setInput(input);

            {
                Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer;

                transfer = Utils.configureControlFunction("Pause", "Function for pausing processing to maintain constant iteration rate", "Pause",
                        new String[][]{{"Pause", Globals.getInstance().get("Pause_Pause"), "Long"}, {"ConstantRate", "true", ""}}, null);
                transfersList.add(transfer);

                transfer = Utils.configureControlFunction("Zero", "...", "Constant",
                        new String[][]{{"Constant", "0", ""}}, null);
                transfersList.add(transfer);

                /*
                String name = "ArmReach";
                transfer = Utils.configureControlFunction(name + "ControlManualReference", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get(name + "ControlManualReference_Constant"), "Double"}}, null);
                transfersList.add(transfer);

                name = "HandElevation";
                transfer = Utils.configureControlFunction(name + "ControlManualReference", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get(name + "ControlManualReference_Constant"), "Double"}}, null);
                transfersList.add(transfer);
                name = "HandPitch";
                transfer = Utils.configureControlFunction(name + "ControlManualReference", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get(name + "ControlManualReference_Constant"), "Double"}}, null);
                transfersList.add(transfer);
                 */
                inputFunctions.setTransfers(transfers);
            }
            functions.setInputFunctions(inputFunctions);
        }

        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction("ElbowRollE0PositionControlManualReference", "...", "Constant",
                    new String[][]{{"Constant", Globals.getInstance().get("ElbowRollE0PositionControlManualReference_Constant"), "Double"}}, null);
            outputFunctions.setOutput(output);
            {
                Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer;

                //transfer = Utils.configureControlFunction("ShoulderYawS0PositionControlManualReference", "...", "Constant",
                //    new String[][]{{"Constant", Globals.getInstance().get("ShoulderYawS0PositionControlManualReference_Constant"), "Double"}}, null);           
                //transfersList.add(transfer);
                transfer = Utils.configureControlFunction("WristRollW0PositionControlManualReference", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get("WristRollW0PositionControlManualReference_Constant"), "Double"}}, null);
                transfersList.add(transfer);
                transfer = Utils.configureControlFunction("HandRollW2PositionControlManualReference", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get("HandRollW2PositionControlManualReference_Constant"), "Double"}}, null);
                transfersList.add(transfer);

                transfer = Utils.configureControlFunction("GlobalSpeedWeight", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get("GlobalSpeedWeight"), "Double"}}, null);
                transfersList.add(transfer);

                outputFunctions.setTransfers(transfers);
            }
            functions.setOutputFunctions(outputFunctions);

        }
        controller.setFunctions(functions);
        return controller;
    }

    public void armGlobals(Globals globals, String side) throws Exception {
        globals.put(side + "JointPublish_Topic", "/robot/limb/" + side.toLowerCase() + "/joint_command");

        String joint = "HandAngleW1";
        globals.put(joint + "_Joint", "w1");
        globals.put(side + joint + "PositionControl_Tolerance", "0.001");
        globals.put(side + joint + "PositionControl_Initial", "0.4");

        joint = "ElbowAngleE1";
        globals.put(joint + "_Joint", "e1");
        //globals.put(side + joint + "PubWeighted_Weights", "1");
        globals.put(side + joint + "PositionControl_Tolerance", "0.005");
        globals.put(side + joint + "PositionControl_Initial", "0.7");

        joint = "ShoulderPitchS1";
        globals.put(joint + "_Joint", "s1");
        globals.put(side + joint + "PubWeighted_Weights", "-1,1");
        globals.put(side + joint + "PositionControl_Tolerance", "0.005");
        globals.put(side + joint + "PositionControl_Initial", "0.3");

        joint = "ShoulderYawS0Angle";
        globals.put(joint + "_Joint", "s0");
        globals.put(joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControl_Tolerance", "0.005");
        globals.put(side + joint + "PositionControl_Initial", "0");
        //globals.put(side + joint + "ControlReference_Tolerance", "0.001");

        joint = "ElbowRollE0";
        globals.put(joint + "_Joint", "e0");
        globals.put(joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControl_Tolerance", "0.01");
        globals.put(side + joint + "PositionControl_Initial", "0");

        joint = "WristRollW0";
        globals.put(joint + "_Joint", "w0");
        globals.put(joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControl_Tolerance", "0.01");
        globals.put(side + joint + "PositionControl_Initial", "0");

        joint = "HandRollW2";
        globals.put(joint + "_Joint", "w2");
        globals.put(joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControl_Tolerance", "0.01");
        globals.put(side + joint + "PositionControl_Initial", "0");

        globals.put(side + "EndpointSub_Topic", "/robot/limb/" + side.toLowerCase() + "/endpoint_state");
        globals.put(side + "EndpointSubX_Variable", "/pose/position/x");
        globals.put(side + "EndpointSubX_Initial", "0.23");
        globals.put(side + "EndpointSubY_Variable", "/pose/position/y");
        globals.put(side + "EndpointSubY_Initial", "0.43");
        globals.put(side + "EndpointSubZ_Variable", "/pose/position/z");
        globals.put(side + "EndpointSubZ_Initial", globals.get("HandElevationControlManualReference_Constant"));

        globals.put(side + "EndpointSubQW_Variable", "/pose/orientation/w");
        globals.put(side + "EndpointSubQW_Initial", "0");

        globals.put(side + "EndpointSubQY_Variable", "/pose/orientation/y");
        globals.put(side + "EndpointSubQY_Initial", "0.9");

        String system = "ArmReach";
        globals.put(side + system + "ControlOutputToShoulder_Min", "-50");
        globals.put(side + system + "ControlOutputToShoulder_Max", "50");
        globals.put(side + system + "ControlOutputToShoulder_Gain", "-15000");
        globals.put(side + system + "ControlOutputToShoulder_Slow", "1000000");
        globals.put(side + system + "ControlOutputToShoulder_Initial", "0.57");
        globals.put(side + system + "ControlOutputToElbow_Min", "-0.05");
        globals.put(side + system + "ControlOutputToElbow_Max", "2.1");
        globals.put(side + system + "ControlOutputToElbow_Gain", "-75000");
        globals.put(side + system + "ControlOutputToElbow_Slow", "1000000");
        globals.put(side + system + "ControlOutputToElbow_Initial", "0.57");
        globals.put(side + system + "ControlInput_Smooth", "0.9");
        globals.put(side + system + "ControlPositionWeighted_Weights", "1,1,1");
        globals.put(side + system + "ControlErrorThreshold_Threshold", "0.2");
        globals.put(side + system + "MemoryPerception_Tolerance", "0.05");

        system = "HandElevation";
        globals.put(side + system + "ControlOutput_Min", "-50");
        globals.put(side + system + "ControlOutput_Max", "50");
        globals.put(side + system + "ControlOutput_Gain", "-80000");
        globals.put(side + system + "ControlOutput_Slow", "1000000");
        globals.put(side + system + "ControlInput_Smooth", "0.9");
        globals.put(side + system + "ControlOutput_Initial", "1.53");
        globals.put(side + system + "ControlPositionWeighted_Weights", "1,1,1");
        globals.put(side + system + "ControlErrorThreshold_Threshold", "0.2");
        globals.put(side + system + "MemoryPerception_Tolerance", "0.05");

        system = "HandPitch";
        globals.put(side + system + "ControlOutput_Min", "-1.6");
        globals.put(side + system + "ControlOutput_Max", "2.1");
        globals.put(side + system + "ControlOutput_Gain", "-50000");
        globals.put(side + system + "ControlOutput_Slow", "1000000");
        globals.put(side + system + "ControlInput_Smooth", "0.9");
        globals.put(side + system + "ControlOutput_Initial", "0");
        globals.put(side + system + "ControlPositionWeighted_Weights", "1,1,1");
        globals.put(side + system + "ControlErrorThreshold_Threshold", "0.2");
        globals.put(side + system + "MemoryPerception_Tolerance", "0.05");

        system = "ShoulderYawS0";
        globals.put(side + system + "ControlInput_Smooth", "0.9");
        globals.put(side + system + "ControlOutput_Initial", "0");
        globals.put(side + system + "ControlOutput_Min", "-1.7");
        globals.put(side + system + "ControlOutput_Max", "1.7");
        globals.put(side + system + "ControlOutput_Gain", "40000");
        globals.put(side + system + "ControlOutput_Slow", "1000000");
        globals.put(side + system + "ControlPositionWeighted_Weights", "1,1,1");
        globals.put(side + system + "ControlErrorThreshold_Threshold", "0.2");
        globals.put(side + system + "MemoryPerception_Tolerance", "0.05");

    }

}
