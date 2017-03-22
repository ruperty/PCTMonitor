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
public class XML_071_003_BaxterDoubleArmControl {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        String file = Utils.getFileName("Robot", "ROS", "071-003-BaxterDoubleArmControl.xml");
        System.out.println(file);

        XML_071_003_BaxterDoubleArmControl xml = new XML_071_003_BaxterDoubleArmControl();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "25");

        String system = "ArmReach";
        globals.put(system + "ControlManualReference_Constant", "0.9");
        system = "HandElevation";
        globals.put(system + "ControlManualReference_Constant", "-0.3");
        system = "HandPitch";
        globals.put(system + "ControlManualReference_Constant", "-0.92");

        String side = "Left";
        globals.put("ArmOne_Side", side);
        xml.armGlobals(globals, side);

        side = "Right";
        globals.put("ArmTwo_Side", side);
        xml.armGlobals(globals, side);

        Utils.saveToXML(xml.run(), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    public void armGlobals(Globals globals, String side) {
        globals.put(side + "JointPublish_Topic", "/robot/limb/" + side.toLowerCase() + "/joint_command");

        String joint = "HandAngleW1";
        globals.put(joint + "_Joint", "w1");
        globals.put(side + joint + "PositionControl_Tolerance", "0.001");
        globals.put(side + joint + "PositionControl_Initial", "0.5");

        joint = "ElbowAngleE1";
        globals.put(joint + "_Joint", "e1");
        globals.put(side + joint + "PubWeighted_Weights", "1");
        globals.put(side + joint + "PositionControl_Tolerance", "0.001");
        globals.put(side + joint + "PositionControl_Initial", "0.7");

        joint = "ShoulderPitchS1";
        globals.put(joint + "_Joint", "s1");
        globals.put(side + joint + "PubWeighted_Weights", "-1,1");
        globals.put(side + joint + "PositionControl_Tolerance", "0.001");
        globals.put(side + joint + "PositionControl_Initial", "0.3");

        joint = "ShoulderYawS0";
        globals.put(joint + "_Joint", "s0");
        globals.put(joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControl_Tolerance", "0.01");
        globals.put(side + joint + "PositionControl_Initial", "0");

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
        globals.put(side + "EndpointSubZ_Initial", "-0.7");

        globals.put(side + "EndpointSubQW_Variable", "/pose/orientation/w");
        globals.put(side + "EndpointSubQW_Initial", "0");

        globals.put(side + "EndpointSubQY_Variable", "/pose/orientation/y");
        globals.put(side + "EndpointSubQY_Initial", "0.9");

        String system = "ArmReach";
        globals.put(side + system + "ControlOutputToShoulder_Min", "-20");
        globals.put(side + system + "ControlOutputToShoulder_Max", "20");
        globals.put(side + system + "ControlOutputToShoulder_Gain", "-50000");
        globals.put(side + system + "ControlOutputToShoulder_Slow", "1000000");
        globals.put(side + system + "ControlOutputToShoulder_Initial", "0.57");
        globals.put(side + system + "ControlOutputToElbow_Min", "-0.05");
        globals.put(side + system + "ControlOutputToElbow_Max", "2.1");
        globals.put(side + system + "ControlOutputToElbow_Gain", "-75000");
        globals.put(side + system + "ControlOutputToElbow_Slow", "1000000");
        globals.put(side + system + "ControlOutputToElbow_Initial", "0.57");
        globals.put(side + system + "ControlInput_Smooth", "0.9");

        system = "HandElevation";
        globals.put(side + system + "ControlOutput_Min", "-20");
        globals.put(side + system + "ControlOutput_Max", "20");
        globals.put(side + system + "ControlOutput_Gain", "-30000");
        globals.put(side + system + "ControlOutput_Slow", "1000000");
        globals.put(side + system + "ControlInput_Smooth", "0.9");
        globals.put(side + system + "ControlOutput_Initial", "1.53");

        system = "HandPitch";
        globals.put(side + system + "ControlOutput_Min", "-1.6");
        globals.put(side + system + "ControlOutput_Max", "2.1");
        globals.put(side + system + "ControlOutput_Gain", "-30000");
        globals.put(side + system + "ControlOutput_Slow", "1000000");
        globals.put(side + system + "ControlInput_Smooth", "0.9");
        globals.put(side + system + "ControlOutput_Initial", "0");
    }

    public XML_071_003_BaxterDoubleArmControl() {
    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Baxter arm control");

        layers.getLayer().add(Utils.emptyLayer("Baxter"));
        layers.getLayer().add(Utils.emptyLayer("3D"));

        String side = Globals.getInstance().get("ArmOne_Side");
        addArmControls(layers, side, 2);

        side = Globals.getInstance().get("ArmTwo_Side");
        addArmControls(layers, side, 3);

        //layers.getLayer().add(Utils.emptyLayer("Env"));
        Utils.addController(layers, 0, configureControllerEnv());

        return layers;
    }

    private void addArmControls(Layers layers, String side, int layerNum) throws Exception {
        Utils.addController(layers, 1, Generic.configureSubscribers3D(side));
        layers.getLayer().add(Utils.emptyLayer("Top" + side));

        Controller controller = Utils.emptyController(side + "Interface", true, false, false, true);
        Utils.addController(layers, 0, controller);

        String reachName = "ArmReachControl";
        String handElevName = "HandElevationControl";
        String handPitchName = "HandPitchControl";
        String joint = "ElbowAngleE1";
        Generic.addPositionSubPub(controller, false, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,side + reachName + "OutputToElbow", true);
        joint = "ShoulderPitchS1";
        Generic.addPositionSubPub(controller, true, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,side + reachName + "OutputToShoulder", side + handElevName + "Output");
        joint = "HandAngleW1";
        Generic.addPositionSubPub(controller, true, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,side + handPitchName + "Output", false);
        joint = "ShoulderYawS0";
        Generic.addPositionSubPub(controller, true, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,joint + "PositionControlManualReference", false);
        joint = "ElbowRollE0";
        Generic.addPositionSubPub(controller, true, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,joint + "PositionControlManualReference", false);
        joint = "WristRollW0";
        Generic.addPositionSubPub(controller, true, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,joint + "PositionControlManualReference", false);
        joint = "HandRollW2";
        Generic.addPositionSubPub(controller, true, joint, side, true, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,joint + "PositionControlManualReference", false);
        Modules.armStaticHandControls(layers, layerNum, reachName, handElevName, handPitchName, side);
    }

    /*
    private static Layers.Layer configureLayer0(String name) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();
        controllers.add(configureControllerEnv());

        return layer;
    }*/
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
        }
        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction("ShoulderYawS0PositionControlManualReference", "...", "Constant",
                    new String[][]{{"Constant", Globals.getInstance().get("ShoulderYawS0PositionControlManualReference_Constant"), "Double"}}, null);
            outputFunctions.setOutput(output);
            {
                Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer;

                transfer = Utils.configureControlFunction("ElbowRollE0PositionControlManualReference", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get("ElbowRollE0PositionControlManualReference_Constant"), "Double"}}, null);
                transfersList.add(transfer);
                transfer = Utils.configureControlFunction("WristRollW0PositionControlManualReference", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get("WristRollW0PositionControlManualReference_Constant"), "Double"}}, null);
                transfersList.add(transfer);
                transfer = Utils.configureControlFunction("HandRollW2PositionControlManualReference", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get("HandRollW2PositionControlManualReference_Constant"), "Double"}}, null);
                transfersList.add(transfer);

                outputFunctions.setTransfers(transfers);
            }
            functions.setOutputFunctions(outputFunctions);

        }
        controller.setFunctions(functions);
        return controller;
    }

}
