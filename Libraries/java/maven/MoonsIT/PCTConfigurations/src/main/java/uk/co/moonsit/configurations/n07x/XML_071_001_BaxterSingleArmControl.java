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
import uk.co.moonsit.config.functions.Generic;
import uk.co.moonsit.config.functions.Globals;
import uk.co.moonsit.config.functions.Modules;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_071_001_BaxterSingleArmControl {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        String file = Utils.getFileName("Robot", "ROS", "071-001-BaxterSingleArmControl.xml");
        System.out.println(file);

        XML_071_001_BaxterSingleArmControl xml = new XML_071_001_BaxterSingleArmControl();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "20");

        String side = "Left";
        globals.put("ArmOne_Side", side);

        globals.put(side + "JointPublish_Topic", "/robot/limb/" + side.toLowerCase() + "/joint_command");

        String joint = "HandAngle";
        globals.put(joint + "_Joint", "w1");

        globals.put(side + joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControlOutput_InScale", "3");
        globals.put(side + joint + "PositionControlOutput_OutScale", "4");
        globals.put(side + joint + "PositionControlReferences_Weights", "1");
        globals.put(side + joint + "PositionControlInput_Smooth", "0.9");
        globals.put(side + joint + "PositionControlJointVelocityPublish_Disabled", "false");
        globals.put(side + joint + "PositionControlJointPositionPublish_Disabled", "true");
        globals.put(side + joint + "PositionControlErrorFiltered_Tolerance", "0.05");
        globals.put(side + joint + "PositionControl_Tolerance", "0.01");
        globals.put(side + joint + "PositionControl_Initial", "0");

        joint = "ElbowAngle";
        globals.put(joint + "_Joint", "e1");
        globals.put(side + joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControlReferences_Weights", "1");
        globals.put(side + joint + "PositionControlOutput_InScale", "2");
        globals.put(side + joint + "PositionControlOutput_OutScale", "2");
        globals.put(side + joint + "PositionControlInput_Smooth", "0.9");
        globals.put(side + joint + "PositionControlJointVelocityPublish_Disabled", "false");
        globals.put(side + joint + "PositionControlJointPositionPublish_Disabled", "true");
        globals.put(side + joint + "PositionControlErrorFiltered_Tolerance", "0.05");
        globals.put(side + joint + "PositionControl_Tolerance", "0.01");
        globals.put(side + joint + "PositionControl_Initial", "0");

        joint = "ShoulderPitch";
        globals.put(joint + "_Joint", "s1");

        globals.put(side + joint + "PositionControlManualReference_Constant", "0    ");
        globals.put(side + joint + "PositionControlReferences_Weights", "-1,1");
        globals.put(side + joint + "PositionControlOutput_InScale", "2");
        globals.put(side + joint + "PositionControlOutput_OutScale", "2");
        globals.put(side + joint + "PositionControlInput_Smooth", "0.9");
        globals.put(side + joint + "PositionControlJointVelocityPublish_Disabled", "false");
        globals.put(side + joint + "PositionControlJointPositionPublish_Disabled", "true");
        globals.put(side + joint + "PositionControlErrorFiltered_Tolerance", "0.05");
        globals.put(side + joint + "PositionControl_Tolerance", "0.01");
        globals.put(side + joint + "PositionControl_Initial", "1");

        joint = "ShoulderYaw";
        globals.put(joint + "_Joint", "s0");
        globals.put(side + joint + "PositionControlManualReference_Constant", "-.5");
        globals.put(side + joint + "PositionControlOutput_InScale", "2");
        globals.put(side + joint + "PositionControlOutput_OutScale", "2");
        globals.put(side + joint + "PositionControlReferences_Weights", "1");
        globals.put(side + joint + "PositionControlInput_Smooth", "0.9");
        globals.put(side + joint + "PositionControlJointVelocityPublish_Disabled", "false");
        globals.put(side + joint + "PositionControlJointPositionPublish_Disabled", "true");
        globals.put(side + joint + "PositionControlErrorFiltered_Tolerance", "0.05");
        globals.put(side + joint + "PositionControl_Tolerance", "0.01");
        globals.put(side + joint + "PositionControl_Initial", "0");

        joint = "ElbowRoll";
        globals.put(joint + "_Joint", "e0");
        globals.put(side + joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControlOutput_InScale", "2");
        globals.put(side + joint + "PositionControlOutput_OutScale", "2");
        globals.put(side + joint + "PositionControlReferences_Weights", "1");
        globals.put(side + joint + "PositionControlInput_Smooth", "0.9");
        globals.put(side + joint + "PositionControlJointVelocityPublish_Disabled", "false");
        globals.put(side + joint + "PositionControlJointPositionPublish_Disabled", "true");
        globals.put(side + joint + "PositionControlErrorFiltered_Tolerance", "0.05");
        globals.put(side + joint + "PositionControl_Tolerance", "0.01");
        globals.put(side + joint + "PositionControl_Initial", "0");

        joint = "WristRoll";
        globals.put(joint + "_Joint", "w0");
        globals.put(side + joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControlOutput_InScale", "2");
        globals.put(side + joint + "PositionControlOutput_OutScale", "2");
        globals.put(side + joint + "PositionControlReferences_Weights", "1");
        globals.put(side + joint + "PositionControlInput_Smooth", "0.9");
        globals.put(side + joint + "PositionControlJointVelocityPublish_Disabled", "false");
        globals.put(side + joint + "PositionControlJointPositionPublish_Disabled", "true");
        globals.put(side + joint + "PositionControlErrorFiltered_Tolerance", "0.05");
        globals.put(side + joint + "PositionControl_Tolerance", "0.01");
        globals.put(side + joint + "PositionControl_Initial", "0");

        joint = "HandRoll";
        globals.put(joint + "_Joint", "w2");
        globals.put(side + joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControlOutput_InScale", "2");
        globals.put(side + joint + "PositionControlOutput_OutScale", "2");
        globals.put(side + joint + "PositionControlReferences_Weights", "1");
        globals.put(side + joint + "PositionControlInput_Smooth", "0.9");
        globals.put(side + joint + "PositionControlJointVelocityPublish_Disabled", "false");
        globals.put(side + joint + "PositionControlJointPositionPublish_Disabled", "true");
        globals.put(side + joint + "PositionControlErrorFiltered_Tolerance", "0.05");
        globals.put(side + joint + "PositionControl_Tolerance", "0.01");
        globals.put(side + joint + "PositionControl_Initial", "0");

        globals.put(side + "EndpointSub_Topic", "/robot/limb/" + side.toLowerCase() + "/endpoint_state");
        globals.put(side + "EndpointSubX_Variable", "/pose/position/x");
        globals.put(side + "EndpointSubX_Initial", "0.23");
        globals.put(side + "EndpointSubY_Variable", "/pose/position/y");
        globals.put(side + "EndpointSubY_Initial", "0.43");
        globals.put(side + "EndpointSubZ_Variable", "/pose/position/z");
        globals.put(side + "EndpointSubZ_Initial", "-0.7");
        globals.put(side + "EndpointSubQW_Variable", "/pose/orientation/w");
        globals.put(side + "EndpointSubQW_Initial", "0");
        globals.put(side + "EndpointSubQY_Variable", "/pose/orientation/y");
        globals.put(side + "EndpointSubQY_Initial", "0.9");

        String system = "ArmReach";
        globals.put(system + "ControlManualReference_Constant", "0.85");
        globals.put(side + system + "ControlOutputToShoulder_Min", "-6");
        globals.put(side + system + "ControlOutputToShoulder_Max", "6");
        globals.put(side + system + "ControlOutputToShoulder_Gain", "-20000");
        globals.put(side + system + "ControlOutputToShoulder_Slow", "1000000");
        globals.put(side + system + "ControlOutputToShoulder_Initial", "0.57");
        globals.put(side + system + "ControlOutputToElbow_Min", "-6");
        globals.put(side + system + "ControlOutputToElbow_Max", "6");
        globals.put(side + system + "ControlOutputToElbow_Gain", "-20000");
        globals.put(side + system + "ControlOutputToElbow_Slow", "1000000");
        globals.put(side + system + "ControlOutputToElbow_Initial", "0.57");
        globals.put(side + system + "ControlInput_Smooth", "0.9");

        system = "HandElevation";
        globals.put(system + "ControlManualReference_Constant", "-0.7");
        globals.put(side + system + "ControlOutput_Min", "-5");
        globals.put(side + system + "ControlOutput_Max", "5");
        globals.put(side + system + "ControlOutput_Gain", "0"/*"-10000"*/);
        globals.put(side + system + "ControlOutput_Slow", "1000000");
        globals.put(side + system + "ControlInput_Smooth", "0.9");
        globals.put(side + system + "ControlOutput_Initial", "1.53");

        system = "HandPitch";
        globals.put(system + "ControlManualReference_Constant", "-0.92");
        globals.put(side + system + "ControlOutput_Min", "-1.5");
        globals.put(side + system + "ControlOutput_Max", "2");
        globals.put(side + system + "ControlOutput_Gain", "0"/*"-10000"*/);
        globals.put(side + system + "ControlOutput_Slow", "100000");
        globals.put(side + system + "ControlInput_Smooth", "0.9");
        globals.put(side + system + "ControlOutput_Initial", "0");

        side = "Right";
        globals.put("ArmTwo_Side", side);
        globals.put("", "");
        globals.put("", "");

        Utils.saveToXML(xml.run(), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    public XML_071_001_BaxterSingleArmControl() {
    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Baxter arm control");

        List layer = layers.getLayer();

        String side = Globals.getInstance().get("ArmOne_Side");

        layer.add(configureLayer0("Baxter", side));
        layer.add(Utils.emptyLayer("Position"));
        layer.add(Utils.emptyLayer("Top"));

        String reachName = "ArmReachControl";
        String handElevName = "HandElevationControl";
        String handPitchName = "HandPitchControl";

        String joint = "ShoulderPitch";
        addJointModule(layers, 1, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), side + reachName + "OutputToShoulder", side + handElevName + "Output", false);
        joint = "ElbowAngle";
        addJointModule(layers, 1, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), side + reachName + "OutputToElbow", false);
        joint = "HandAngle";
        addJointModule(layers, 1, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), side + handPitchName + "Output", false);
        joint = "ShoulderYaw";
        addJointModule(layers, 0, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), side + joint + "PositionControlManualReference", false);
        joint = "ElbowRoll";
        addJointModule(layers, 0, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), side + joint + "PositionControlManualReference", false);
        joint = "WristRoll";
        addJointModule(layers, 0, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), side + joint + "PositionControlManualReference", false);
        joint = "HandRoll";
        addJointModule(layers, 0, joint, side, true, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), side + joint + "PositionControlManualReference", false);

        Modules.armStaticHandControls(layers, 2, reachName, handElevName, handPitchName, side);

        return layers;
    }

    private static void addJointModule(Layers layers, int layer, String joint, String side, Boolean publish, String tolerance, String initial, String link1, boolean pubPosition) throws Exception {
        String[][] links = new String[1][1];
        links[0][0] = link1;
        Modules.armJointPositionVelocityController(layers, layer, joint, side, Globals.getInstance().get(joint + "_Joint"), String.valueOf(publish), tolerance, initial, null,links, pubPosition);
    }

    private static void addJointModule(Layers layers, int layer, String joint, String side, Boolean publish, String tolerance, String initial, String link1, String link2, boolean pubPosition) throws Exception {
        String[][] links = new String[2][1];
        links[0][0] = link1;
        links[1][0] = link2;
        Modules.armJointPositionVelocityController(layers, layer, joint, side, Globals.getInstance().get(joint + "_Joint"), String.valueOf(publish), tolerance, initial,null, links, pubPosition
        );
    }

    private static Layers.Layer configureLayer0(String name, String side) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();
        controllers.add(configureControllerEnv());
        controllers.add(Generic.configureSubscribers3D(side));

        return layer;
    }

    private static Layers.Layer.Controller configureControllerEnv() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("Env");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

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

            inputFunctions.setTransfers(transfers);
        }
        functions.setInputFunctions(inputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

}
