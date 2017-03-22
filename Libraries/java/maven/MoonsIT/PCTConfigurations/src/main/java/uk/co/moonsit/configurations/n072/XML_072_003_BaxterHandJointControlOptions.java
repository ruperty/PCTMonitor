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
package uk.co.moonsit.configurations.n072;

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
public class XML_072_003_BaxterHandJointControlOptions {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        int type = Generic.P;
        String suffix = null;
        switch (type) {
            case Generic.P:
                suffix = "P";
                break;
            case Generic.PV:
                suffix = "PV";
                break;
            case Generic.PVA:
                suffix = "PVA";
                break;
        }
        //String[] sides = {"Left", "Right"};
        String[] sides = {"Right"};
        //String[] sides = {"Left"};
        String file = null;
        if (sides.length == 1) {
            file = Utils.getFileName("Robot", "ROS", "072-003-BaxterHandJointController" + sides[0] + suffix + ".xml");
        } else {
            file = Utils.getFileName("Robot", "ROS", "072-003-BaxterHandJointControllerDouble" + suffix + ".xml");
        }

        System.out.println(file);

        XML_072_003_BaxterHandJointControlOptions xml = new XML_072_003_BaxterHandJointControlOptions();

        //String side = "Left";
        //String side = "Right";
        //globals.put("ArmOne_Side", side);
        String[] joints = Generic.getAllJoints();
        String[] jointRefLinks = new String[7];
        int i = 0;
        for (String joint : joints) {
            jointRefLinks[i] = joint + "PositionControlManualReference";
            i++;
        }
        String[] systems = {"TargetSystemX", "TargetSystemY", "ImageDepthZ"};

        jointRefLinks[Generic.getJointIndex("WristRollW0", joints)] = systems[0] + "ControlOutput";
        jointRefLinks[Generic.getJointIndex("HandAngleW1", joints)] = systems[1] + "ControlOutput";
        jointRefLinks[Generic.getJointIndex("ShoulderPitchS1", joints)] = systems[2] + "ControlOutputToShoulder";
        jointRefLinks[Generic.getJointIndex("ElbowAngleE1", joints)] = systems[2] + "ControlOutputToElbow";

        Utils.saveToXML(xml.run(type, joints, jointRefLinks, systems, sides), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    public XML_072_003_BaxterHandJointControlOptions() {
    }

    public Layers run(int type, String joints[], String[] jointRefLinks, String[] systems, String[] sides) throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Baxter arm control");

        List layer = layers.getLayer();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "25");
        layer.add(configureLayer0("Baxter"));

        //String side = Globals.getInstance().get("ArmOne_Side");
        for (String side : sides) {
            armGlobals(globals, side, joints, type, systems);
            Utils.addController(layers, 0, addReferences(side, systems, joints, type));

            String[] handInputLinks = new String[3];
            for (int i = 0; i < handInputLinks.length; i++) {
                handInputLinks[i] = side + systems[i] + "ControlManualInput";
            }

            String[] handReferenceLinks = new String[3];
            for (int i = 0; i < handReferenceLinks.length; i++) {
                handReferenceLinks[i] = side + systems[i] + "ControlManualReference";
            }

            int layerNum = Modules.addJointControls(layers, side, joints, jointRefLinks, null, type, false, false, false);
            Modules.armRelativeHandControls(layers, layerNum, systems, side, handInputLinks, handReferenceLinks, true);
        }
        return layers;
    }

    private static Layers.Layer configureLayer0(String name) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();
        Controller env = configureControllerEnv();
        controllers.add(env);

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

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller addReferences(String side, String[] systems, String joints[], int type) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(side + "Constants");

        ControlFunction input = Utils.configureControlFunction(side + systems[0] + "ControlManualInput", "...", "Constant",
                new String[][]{{"Constant", Globals.getInstance().get(side + systems[0] + "ControlManualInput_Constant"), "Double"}}, null);
        Utils.addFunction(controller, input, Utils.INPUT, false);

        for (int i = 1; i < systems.length; i++) {
            ControlFunction transfer = Utils.configureControlFunction(side + systems[i] + "ControlManualInput", "...", "Constant",
                    new String[][]{{"Constant", Globals.getInstance().get(side + systems[i] + "ControlManualInput_Constant"), "Double"}}, null);
            Utils.addTransferFunction(controller, transfer, Utils.INPUT);
        }

        ControlFunction reference = Utils.configureControlFunction(side + systems[0] + "ControlManualReference", "...", "Constant",
                new String[][]{{"Constant", Globals.getInstance().get(side + systems[0] + "ControlManualReference_Constant"), "Double"}}, null);
        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        for (int i = 1; i < systems.length; i++) {

            ControlFunction transfer = Utils.configureControlFunction(side + systems[i] + "ControlManualReference", "...", "Constant",
                    new String[][]{{"Constant", Globals.getInstance().get(side + systems[i] + "ControlManualReference_Constant"), "Double"}}, null);
            Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);

        }

        {
            String joint = "ShoulderYawS0";
            ControlFunction transfer = Utils.configureControlFunction(side + joint + "PositionControlManualReference", "...", "Constant", new String[][]{{"Constant", Globals.getInstance().get(side + joint + "PositionControlManualReference_Constant"), "Double"}}, null);
            Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);
            joint = "ElbowRollE0";
            transfer = Utils.configureControlFunction(side + joint + "PositionControlManualReference", "...", "Constant", new String[][]{{"Constant", Globals.getInstance().get(side + joint + "PositionControlManualReference_Constant"), "Double"}}, null);
            Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);
            joint = "HandRollW2";
            transfer = Utils.configureControlFunction(side + joint + "PositionControlManualReference", "...", "Constant", new String[][]{{"Constant", Globals.getInstance().get(side + joint + "PositionControlManualReference_Constant"), "Double"}}, null);
            Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);
        }

        for (String joint : joints) {

            switch (type) {
                case Generic.P: {
                    break;
                }
                case Generic.PV: {
                    ControlFunction transfer = Utils.configureControlFunction(side + joint + "VelocityReference", "...", "Constant",
                            new String[][]{{"Constant", Globals.getInstance().get(side + joint + "VelocityControlManualReference_Constant"), "Double"}}, null);
                    Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);
                }
                break;
                case Generic.PVA: {
                    ControlFunction transfer = Utils.configureControlFunction(side + joint + "VelocityReference", "...", "Constant",
                            new String[][]{{"Constant", Globals.getInstance().get(side + joint + "VelocityControlManualReference_Constant"), "Double"}}, null);
                    Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);

                }
                break;
            }

        }
        return controller;
    }

    public void armGlobals(Globals globals, String side, String[] joints, int type, String[] systems) throws Exception {
        boolean left = side.equals("Left");

        globals.put(side + "JointPublish_Topic", "/robot/limb/" + side.toLowerCase() + "/joint_command");

        for (String joint : joints) {
            globals.put(joint + "_Joint", Generic.getJointCode(joint));
            //    globals.put(side + joint + "PositionControlManualReference_Constant", "0");

            switch (type) {
                case Generic.P:
                    globals.put(side + joint + "PositionControl_Last", "0");
                    globals.put(side + joint + "PositionControl_Tolerance", "0.01");
                    globals.put(side + joint + "PositionControl_Initial", "0");
                    /*
                    globals.put(side + joint + "PositionControlInput_Smooth", "0.9");
                    globals.put(side + joint + "PositionControlReference_Weights", "1,0");
                    globals.put(side + joint + "PositionControlOutput_Gain", "1000");
                    globals.put(side + joint + "PositionControlOutput_Slow", "1000000");
                    globals.put(side + joint + "PositionControlOutput_Min", "-5");
                    globals.put(side + joint + "PositionControlOutput_Max", "5");
                     */
                    break;
                case Generic.PV:
                    globals.put(side + joint + "VelocityControlInput_Smooth", "0.1");
                    globals.put(side + joint + "PositionControlInput_Smooth", "0.1");
                    globals.put(side + joint + "TorquePublish_Tolerance", "0.01");
                    globals.put(side + joint + "TorquePublish_Initial", "0");
                    globals.put(side + joint + "VelocityControlOutput_Gain", "20");
                    globals.put(side + joint + "PositionControlOutput_InScale", "10");
                    globals.put(side + joint + "PositionControlOutput_OutScale", "1");
                    globals.put(side + joint + "VelocityControlManualReference_Constant", "0");

                    break;
                case Generic.PVA:
                    globals.put(side + joint + "TorquePublish_Tolerance", "0.01");
                    globals.put(side + joint + "TorquePublish_Initial", "0");
                    globals.put(side + joint + "VelocityControlOutput_Gain", "20");
                    globals.put(side + joint + "PositionControlOutput_InScale", "10");
                    globals.put(side + joint + "PositionControlOutput_OutScale", "1");
                    globals.put(side + joint + "VelocityControlManualReference_Constant", "0");
                    globals.put(side + joint + "AccelerationControlOutput_Gain", "1000");
                    globals.put(side + joint + "AccelerationControlOutput_Slow", "1000000");
                    globals.put(side + joint + "AccelerationControlOutput_Min", "-5");
                    globals.put(side + joint + "AccelerationControlOutput_Max", "5");

                    break;
            }
        }

        String joint = "ShoulderPitchS1";
        switch (type) {
            case Generic.P:
                if (side.equals("Left")) {
                    globals.put(side + joint + "PositionControl_Initial", "-0.45");
                    globals.put(side + joint + "PositionControl_Last", "0");
                } else {
                    globals.put(side + joint + "PositionControl_Initial", "-0.11");
                    globals.put(side + joint + "PositionControl_Last", "-0.11");
                }
                break;
        }

        joint = "ElbowRollE0";
        switch (type) {
            case Generic.P:
                globals.put(side + joint + "PositionControlManualReference_Constant", "0");
                break;
        }

        joint = "ElbowAngleE1";
        switch (type) {
            case Generic.P:
                if (side.equals("Left")) {
                    globals.put(side + joint + "PositionControl_Initial", "1.75");
                    globals.put(side + joint + "PositionControl_Last", "1.4");

                } else {
                    globals.put(side + joint + "PositionControl_Initial", "1.05");
                    globals.put(side + joint + "PositionControl_Last", "1.05");

                }
                break;
        }
        joint = "HandAngleW1";
        switch (type) {
            case Generic.P:
                if (side.equals("Left")) {
                    globals.put(side + joint + "PositionControlManualReference_Constant", "-1.4");
                    globals.put(side + joint + "PositionControl_Initial", "-1.4");
                } else {
                    globals.put(side + joint + "PositionControlManualReference_Constant", "-0.9");
                    globals.put(side + joint + "PositionControl_Initial", "-0.9");
                    globals.put(side + joint + "PositionControl_Last", "-0.9");
                }
                break;
        }
        joint = "ShoulderYawS0";
        switch (type) {
            case Generic.P:
                if (side.equals("Left")) {
                    globals.put(side + joint + "PositionControlManualReference_Constant", "-0.8");
                    globals.put(side + joint + "PositionControl_Initial", "-0.8");
                } else {
                    globals.put(side + joint + "PositionControlManualReference_Constant", "0.6");
                    globals.put(side + joint + "PositionControl_Initial", "0.6");
                    globals.put(side + joint + "PositionControl_Last", "0.6");
                }
                break;
        }

        joint = "WristRollW0";
        switch (type) {
            case Generic.P:
                break;
        }

        joint = "HandRollW2";
        switch (type) {
            case Generic.P:
                globals.put(side + joint + "PositionControlManualReference_Constant", "0");
                break;
        }

        int i = 0;
        String system = systems[i++];
        // "TargetSystemX"
        globals.put(side + system + "ControlInput_Smooth", "0.1");
        globals.put(side + system + "ControlManualInput_Constant", "0");
        globals.put(side + system + "ControlManualReference_Constant", "0");
        globals.put(side + system + "ControlOutput_Gain", "10000");
        globals.put(side + system + "ControlOutput_Slow", "1000000");
        globals.put(side + system + "ControlOutput_Min", "-5");
        globals.put(side + system + "ControlOutput_Max", "5");
        globals.put(side + system + "ControlOutput_Initial", "0");
        globals.put(side + system + "Control_Tolerance", "0.00001");

        system = systems[i++];
        // "TargetSystemY"
        globals.put(side + system + "ControlInput_Smooth", "0.1");
        globals.put(side + system + "ControlOutput_Gain", "10000");
        globals.put(side + system + "ControlOutput_Slow", "1000000");
        globals.put(side + system + "ControlOutput_Min", "-5");
        globals.put(side + system + "ControlOutput_Max", "5");
        globals.put(side + system + "ControlManualInput_Constant", "0");
        globals.put(side + system + "ControlManualReference_Constant", "0");
        globals.put(side + system + "ControlOutput_Initial", left?"-1.4":"-0.9");
        globals.put(side + system + "Control_Tolerance", "0.00001");
        system = systems[i++];
        // "ImageDepthZ"
        globals.put(side + system + "ControlInput_Smooth", "0.1");
        globals.put(side + system + "ControlOutputToShoulder_Gain", "5000");
        globals.put(side + system + "ControlOutputToShoulder_Slow", "1000000");
        globals.put(side + system + "ControlOutputToShoulder_Min", "-2.1");
        globals.put(side + system + "ControlOutputToShoulder_Max", "1.04");

        globals.put(side + system + "ControlOutputToShoulder_Initial", left ? "-0.45" : "-0.11");

        globals.put(side + system + "ControlOutputToElbow_Gain", "-10000");
        globals.put(side + system + "ControlOutputToElbow_Slow", "1000000");
        globals.put(side + system + "ControlOutputToElbow_Min", "-0.05");
        globals.put(side + system + "ControlOutputToElbow_Max", "2.6");
        globals.put(side + system + "ControlOutputToElbow_Initial", left?"1.75":"1.05");

        globals.put(side + system + "ControlManualInput_Constant", "0");
        globals.put(side + system + "ControlManualReference_Constant", "0");

    }
}
