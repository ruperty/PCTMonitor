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
public class XML_072_004_BaxterTargetControl {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        boolean robotsim = Boolean.getBoolean(System.getenv("ROBOT_SIM"));
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
        String file;
        if (sides.length == 1) {
            file = Utils.getFileName("Robot", "ROS", "072-004-BaxterTargetControl" + sides[0] + suffix + ".xml");
        } else {
            file = Utils.getFileName("Robot", "ROS", "072-004-BaxterTargetControlDouble" + suffix + ".xml");
        }
        System.out.println(file);

        XML_072_004_BaxterTargetControl xml = new XML_072_004_BaxterTargetControl();

        //String side = "Left";
        //globals.put("ArmOne_Side", side);
        String[] joints = Generic.getAllJoints();
        String[] jointRefLinks = new String[7];
        int i = 0;
        for (String joint : joints) {
            jointRefLinks[i] = joint + "PositionControlManualReference";
            i++;
        }
        String[] systems = {"TargetSystemX", "TargetSystemY", "ImageDepthZ",
            "ShoulderX", "HandPitchY", "IRDepthZ"};

        jointRefLinks[Generic.getJointIndex("WristRollW0", joints)] = systems[0] + "ControlOutput";
        jointRefLinks[Generic.getJointIndex("HandAngleW1", joints)] = systems[1] + "ControlOutput";
        //jointRefLinks[Common.getJointIndex("ShoulderPitchS1", joints)] = systems[2] + "ControlOutputToShoulder" + ":" + systems[4] + "ControlOutput" + ":" + systems[5] + "ControlOutputToShoulder";
        //jointRefLinks[Common.getJointIndex("ElbowAngleE1", joints)] = systems[2] + "ControlOutputToElbow" + ":" + systems[5] + "ControlOutputToElbow";
        jointRefLinks[Generic.getJointIndex("ShoulderPitchS1", joints)] = "DepthControlOutputToShoulder" + ":" + systems[4] + "ControlOutput";
        jointRefLinks[Generic.getJointIndex("ElbowAngleE1", joints)] = "DepthControlOutputToElbow";
        jointRefLinks[Generic.getJointIndex("ShoulderYawS0", joints)] = systems[3] + "ControlOutput";

        Utils.saveToXML(xml.run(type, joints, jointRefLinks, systems, sides, robotsim), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    public XML_072_004_BaxterTargetControl() {
    }

    public Layers run(int type, String joints[], String[] jointRefLinks, String[] systems, String[] sides, boolean robotsim) throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Baxter arm control");
        List layer = layers.getLayer();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "25");
        layer.add(configureLayer0("Baxter"));

        //String side = Globals.getInstance().get("ArmOne_Side");
        for (String side : sides) {
            armGlobals(globals, side, joints, type, systems, robotsim);
            Utils.addController(layers, 0, addReferences(side, systems, joints, type));

            String[] handInputLinks = new String[systems.length];
            handInputLinks[0] = side + "ROSTargetX";
            handInputLinks[1] = side + "ROSTargetY";
            handInputLinks[2] = side + "ImageDepthZControlInputWeighted";
            handInputLinks[3] = side + "WristRollW0ControlInputWeighted";
            handInputLinks[4] = side + "HandAngleW1ControlInputWeighted";
            handInputLinks[5] = side + "IRRangeWeighted";

            String[] handReferenceLinks = new String[systems.length];
            for (int i = 0; i < handReferenceLinks.length; i++) {
                handReferenceLinks[i] = side + systems[i] + "ControlManualReference";
            }

            int layerNum = Modules.addJointControls(layers, side, joints, jointRefLinks, systems[2], type, false, true, true);
            Modules.armRelativeHandControls(layers, layerNum, systems, side, handInputLinks, handReferenceLinks, false);

            insertDeactiveInputFunctions(layers, side);
            insertDepthReferenceFunctions(layers, side, systems);
            addGripperController(layers, side);
        }
        return layers;
    }

    private static void addGripperController(Layers layers, String side) throws Exception {
        String[][] referenceLinks = {{side + "TargetSystemXControlError"}, {side + "TargetSystemYControlError"}, {side + "IRDepthZControlError"}};
        Utils.addController(layers, 0, Generic.configureGripperController(side, referenceLinks));
    }

    private static void insertDepthReferenceFunctions(Layers layers, String side, String[] systems) throws Exception {

        String suffix = "OutputToElbow";
        String prefix = side + "DepthControl" + suffix;

        ControlFunction transfer = Utils.configureControlFunction(prefix + "Weight", "...", "WeightedSum",
                new String[][]{{"Weights", Globals.getInstance().get(prefix + "Weight_Weights"), ""}},
                new String[][]{{side + systems[2] + "Control" + suffix}, {side + systems[5] + "Control" + suffix}});

        Utils.addTransferFunction(layers, side + systems[2] + "Control", transfer, Utils.OUTPUT);

        transfer = Utils.configureControlFunction(prefix, "...", "Integration",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "_Gain"), "String"},
                    {"Slow", Globals.getInstance().get(prefix + "_Slow"), "String"},
                    {"Min", Globals.getInstance().get(prefix + "_Min"), "String"},
                    {"Max", Globals.getInstance().get(prefix + "_Max"), "String"},
                    {"Tolerance", Globals.getInstance().get(prefix + "_Tolerance"), "Double"},
                    {"Initial", Globals.getInstance().get(prefix + "_Initial"), ""}
                }, new String[][]{{prefix + "Weight"}});

        Utils.addTransferFunction(layers, side + systems[2] + "Control", transfer, Utils.OUTPUT);

        suffix = "OutputToShoulder";
        prefix = side + "DepthControl" + suffix;
        transfer = Utils.configureControlFunction(prefix + "Weight", "...", "WeightedSum",
                new String[][]{{"Weights", Globals.getInstance().get(prefix + "Weight_Weights"), ""}},
                new String[][]{{side + systems[2] + "Control" + suffix}, {side + systems[5] + "Control" + suffix}});

        Utils.addTransferFunction(layers, side + systems[5] + "Control", transfer, Utils.OUTPUT);

        transfer = Utils.configureControlFunction(prefix, "...", "Integration",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "_Gain"), "String"},
                    {"Slow", Globals.getInstance().get(prefix + "_Slow"), "String"},
                    {"Min", Globals.getInstance().get(prefix + "_Min"), "String"},
                    {"Max", Globals.getInstance().get(prefix + "_Max"), "String"},
                    {"Tolerance", Globals.getInstance().get(prefix + "_Tolerance"), "Double"},
                    {"Initial", Globals.getInstance().get(prefix + "_Initial"), ""}
                }, new String[][]{{prefix + "Weight"}});

        Utils.addTransferFunction(layers, side + systems[5] + "Control", transfer, Utils.OUTPUT);
    }

    private static void insertDeactiveInputFunctions(Layers layers, String side) throws Exception {

        {
            // to stop hand pitch control when no Y signal
            ControlFunction transfer = Utils.configureControlFunction(side + "TargetSystemYControlInputWeight",
                    "If value gt 1000 Inf, else 1. That is, if object is near then output is infinity, otherwise 1",
                    "DigitalLimit", new String[][]{{"Threshold", "1000", "Double"}, {"Upper", "Infinity", "Double"}, {"Lower", "1", "Double"}},
                    new String[][]{{side + "TargetSystemYControlInput"}});
            Utils.addTransferFunction(layers, side + "HandPitchYControl", transfer, Utils.INPUT);

            transfer = Utils.configureControlFunction(side + "HandAngleW1ControlInputWeighted", "...", "Product",
                    null, new String[][]{{side + "TargetSystemYControlInputWeight"}, {side + "HandAngleW1ControlInput"}});
            Utils.addTransferFunction(layers, side + "HandPitchYControl", transfer, Utils.INPUT);
        }
        {
            // to stop shoulder pan control when no X signal
            ControlFunction transfer = Utils.configureControlFunction(side + "TargetSystemXControlInputWeight",
                    "If value gt 1000 Inf, else 1. That is, if object is near then output is infinity, otherwise 1",
                    "DigitalLimit", new String[][]{{"Threshold", "1000", "Double"}, {"Upper", "Infinity", "Double"}, {"Lower", "1", "Double"}},
                    new String[][]{{side + "TargetSystemXControlInput"}});
            Utils.addTransferFunction(layers, side + "ShoulderXControl", transfer, Utils.INPUT);

            transfer = Utils.configureControlFunction(side + "WristRollW0ControlInputWeighted", "...", "Product",
                    null, new String[][]{{side + "TargetSystemXControlInputWeight"}, {side + "WristRollW0ControlInput"}});
            Utils.addTransferFunction(layers, side + "ShoulderXControl", transfer, Utils.INPUT);
        }

        {
            // to stop image depth control when no Z signal
            ControlFunction transfer = Utils.configureControlFunction(side + "TargetHypotenuseValue",
                    "Hypotenuse of X and Y",
                    "Hypotenuse", null,
                    new String[][]{{side + "TargetSystemXControlError"},
                    {side + "TargetSystemYControlError"}});
            Utils.addTransferFunction(layers, side + "ImageDepthZControl", transfer, Utils.INPUT);

            transfer = Utils.configureControlFunction(side + "TargetHypotenuseWeight",
                    "If value gt threshold Inf, else 1. That is, if object is near then output is infinity, otherwise 1",
                    "DigitalLimit",
                    new String[][]{{"Threshold", Globals.getInstance().get(side + "TargetHypotenuseWeight_Threshold"), "Double"}, {"Upper", "Infinity", "Double"}, {"Lower", "1", "Double"}},
                    new String[][]{{side + "TargetHypotenuseValue"}});
            Utils.addTransferFunction(layers, side + "ImageDepthZControl", transfer, Utils.INPUT);

            transfer = Utils.configureControlFunction(side + "ImageDepthZControlInputWeighted", "...", "Product",
                    null, new String[][]{{side + "TargetHypotenuseWeight"}, {side + "ROSTargetZ"}});
            Utils.addTransferFunction(layers, side + "ImageDepthZControl", transfer, Utils.INPUT);
        }

        {
            // to stop IR depth control when no IR signal

            ControlFunction transfer = Utils.configureControlFunction(side + "IRRangeWeighted", "...", "Product",
                    null, new String[][]{{side + "TargetHypotenuseWeight"}, {side + "IRRange"}});
            Utils.addTransferFunction(layers, side + "IRDepthZControl", transfer, Utils.INPUT);
        }

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

        ControlFunction reference = Utils.configureControlFunction(side + systems[0] + "ControlManualReference", "...", "Constant",
                new String[][]{{"Constant", Globals.getInstance().get(side + systems[0] + "ControlManualReference_Constant"), "Double"}}, null);
        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        for (int i = 1; i < systems.length; i++) {

            ControlFunction transfer = Utils.configureControlFunction(side + systems[i] + "ControlManualReference", "...", "Constant",
                    new String[][]{{"Constant", Globals.getInstance().get(side + systems[i] + "ControlManualReference_Constant"), "Double"}}, null);
            Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);

        }

        {
            //String joint = "ShoulderYawS0";
            //ControlFunction transfer = Utils.configureControlFunction(side + joint + "PositionControlManualReference", "...", "Constant", new String[][]{{"Constant", Globals.getInstance().get(side + joint + "PositionControlManualReference_Constant"), "Double"}}, null);
            //Utils.addTransferFunction(controller, transfer, function);
            String joint = "ElbowRollE0";
            ControlFunction transfer = Utils.configureControlFunction(side + joint + "PositionControlManualReference", "...", "Constant", new String[][]{{"Constant", Globals.getInstance().get(side + joint + "PositionControlManualReference_Constant"), "Double"}}, null);
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

    public void armGlobals(Globals globals, String side, String[] joints, int type, String[] systems, boolean robotsim) throws Exception {

        globals.put(side + "JointPublish_Topic", "/robot/limb/" + side.toLowerCase() + "/joint_command");
        globals.put(side + "ROSTarget_Topic", "/robotview/" + side.toLowerCase() + "/cartcoords");
        globals.put(side + "ROSIR_Topic", "/robot/range/" + side.toLowerCase() + "_hand_range/state");
        globals.put(side + "ROSGripper_Topic", "/robot/end_effector/" + side.toLowerCase() + "_gripper/command");

        for (String joint : joints) {
            globals.put(joint + "_Joint", Generic.getJointCode(joint));
            //globals.put(side + joint + "PositionControlManualReference_Constant", "0");

            switch (type) {
                case Generic.P:
                    globals.put(side + joint + "PositionControl_Last", "0");
                    globals.put(side + joint + "PositionControl_Tolerance", "0.01");
                    globals.put(side + joint + "PositionControl_Initial", "0");
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
                globals.put(side + joint + "PositionControl_Initial", "-0.5");
                globals.put(side + joint + "PositionControl_Last", "0");
                globals.put(side + joint + "PubWeighted_Weights", "1,1");
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
                globals.put(side + joint + "PositionControl_Initial", "1.75");
                globals.put(side + joint + "PositionControl_Last", "1.4");
                globals.put(side + joint + "PubWeighted_Weights", "1,1");

                break;
        }
        joint = "HandAngleW1";
        switch (type) {
            case Generic.P:
                globals.put(side + joint + "PositionControlManualReference_Constant", "-1.4");
                globals.put(side + joint + "PositionControl_Initial", "-1.4");
                break;
        }
        joint = "ShoulderYawS0";
        switch (type) {
            case Generic.P:
                if (side.equals("Left")) {
                    globals.put(side + joint + "PositionControlManualReference_Constant", "-0.8");
                    globals.put(side + joint + "PositionControl_Initial", "-0.8");
                } else {
                    globals.put(side + joint + "PositionControlManualReference_Constant", "0.8");
                    globals.put(side + joint + "PositionControl_Initial", "0.8");
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
        globals.put(side + system + "ControlManualReference_Constant", "10");
        if (robotsim) {
            globals.put(side + system + "ControlOutput_Gain", "-20");
        } else {
            globals.put(side + system + "ControlOutput_Gain", "-200");
        }
        globals.put(side + system + "ControlOutput_Slow", "1000000");
        globals.put(side + system + "ControlOutput_Min", "-3");
        globals.put(side + system + "ControlOutput_Max", "3");
        globals.put(side + system + "ControlOutput_Initial", "0");
        globals.put(side + system + "Control_Tolerance", "0.00001");

        system = systems[i++];
        // "TargetSystemY"
        globals.put(side + system + "ControlInput_Smooth", "0.1");
        if (robotsim) {
            globals.put(side + system + "ControlOutput_Gain", "20");
        } else {
            globals.put(side + system + "ControlOutput_Gain", "200");
        }
        globals.put(side + system + "ControlOutput_Slow", "1000000");
        globals.put(side + system + "ControlOutput_Min", "-1.57");
        globals.put(side + system + "ControlOutput_Max", "2.09");
        globals.put(side + system + "ControlManualInput_Constant", "0");
        globals.put(side + system + "ControlManualReference_Constant", "0");
        globals.put(side + system + "ControlOutput_Initial", "-1");
        globals.put(side + system + "Control_Tolerance", "0.00001");

        system = systems[i++];
        // "ImageDepthZ"
        globals.put(side + system + "ControlInput_Smooth", "0.99");
        globals.put(side + system + "ControlOutputToShoulder_Gain", "0");//100
        globals.put(side + system + "ControlOutputToElbow_Gain", "0");

        /*
        globals.put(side + system + "ControlOutputToShoulder_Slow", "1000000");
        globals.put(side + system + "ControlOutputToShoulder_Min", "-2.1");
        globals.put(side + system + "ControlOutputToShoulder_Max", "1.04");
        globals.put(side + system + "ControlOutputToShoulder_Initial", "-0.8");
        globals.put(side + system + "ControlOutputToElbow_Gain", "-0");//-100
        globals.put(side + system + "ControlOutputToElbow_Slow", "1000000");
        globals.put(side + system + "ControlOutputToElbow_Min", "0.5");
        globals.put(side + system + "ControlOutputToElbow_Max", "2.6");
        globals.put(side + system + "ControlOutputToElbow_Initial", "1.75");
         */
        globals.put(side + system + "ControlManualReference_Constant", "3");
        globals.put(side + "TargetHypotenuseWeight_Threshold", "100");

        system = systems[i++];
        // "ShoulderX"
        globals.put(side + system + "ControlManualReference_Constant", "0");
        globals.put(side + system + "ControlInput_Smooth", "0.1");
        if (robotsim) {
            globals.put(side + system + "ControlOutput_Gain", "5000");
        } else {
            globals.put(side + system + "ControlOutput_Gain", "10000");
        }
        globals.put(side + system + "ControlOutput_Slow", "1000000");
        globals.put(side + system + "ControlOutput_Min", "-1.7");
        globals.put(side + system + "ControlOutput_Max", "1.7");
        if (side.equals("Left")) {
            globals.put(side + system + "ControlOutput_Initial", "-0.8");
        } else {
            globals.put(side + system + "ControlOutput_Initial", "0.8");
        }

        if (robotsim) {
            globals.put(side + system + "Control_Tolerance", "0.001");
        } else {
            globals.put(side + system + "Control_Tolerance", "0.0001");
        }

        system = systems[i++];
        // "HandPitchY"
        globals.put(side + system + "ControlManualReference_Constant", "-1");
        globals.put(side + system + "ControlInput_Smooth", "0.1");
        if (robotsim) {
            globals.put(side + system + "ControlOutput_Gain", "-10000");
        } else {
            globals.put(side + system + "ControlOutput_Gain", "-20000");
        }
        globals.put(side + system + "ControlOutput_Slow", "1000000");
        globals.put(side + system + "ControlOutput_Min", "-3");
        globals.put(side + system + "ControlOutput_Max", "3");
        globals.put(side + system + "ControlOutput_Initial", "-0.5");
        globals.put(side + system + "Control_Tolerance", "0.001");

        system = systems[i++];
        // "IRDepthZ"
        globals.put(side + system + "ControlManualReference_Constant", "0.10");
        globals.put(side + system + "ControlInput_Smooth", "0.95");
        globals.put(side + system + "ControlOutputToShoulder_Gain", "0");//-100
        globals.put(side + system + "ControlOutputToElbow_Gain", "0");//-100

        /*
        globals.put(side + system + "ControlOutputToShoulder_Slow", "1000000");
        globals.put(side + system + "ControlOutputToShoulder_Min", "-2.1");
        globals.put(side + system + "ControlOutputToShoulder_Max", "1.04");
        globals.put(side + system + "ControlOutputToShoulder_Initial", "0");
        globals.put(side + system + "ControlOutputToElbow_Gain", "0");//5000
        globals.put(side + system + "ControlOutputToElbow_Slow", "1000000");
        globals.put(side + system + "ControlOutputToElbow_Min", "-2");
        globals.put(side + system + "ControlOutputToElbow_Max", "2.6");
        globals.put(side + system + "ControlOutputToElbow_Initial", "0");
        globals.put(side + system + "Control_Tolerance", "0.01");
         */
        String sys = "DepthControlOutputToElbow";
        globals.put(side + sys + "Weight_Weights", "1,1");
        globals.put(side + sys + "_Gain", "-100");//-100
        globals.put(side + sys + "_Slow", "1000000");
        globals.put(side + sys + "_Min", "0.5");
        globals.put(side + sys + "_Max", "2.6");
        globals.put(side + sys + "_Initial", "1.75");
        globals.put(side + sys + "_Tolerance", "0.0001");

        sys = "DepthControlOutputToShoulder";
        globals.put(side + sys + "Weight_Weights", "1,1");
        globals.put(side + sys + "_Gain", "100");//100
        globals.put(side + sys + "_Slow", "1000000");
        globals.put(side + sys + "_Min", "-5");
        globals.put(side + sys + "_Max", "5");
        globals.put(side + sys + "_Initial", "0");
        globals.put(side + sys + "_Tolerance", "0.0001");

        sys = "GripperControl";
        globals.put(side + sys + "Reference_Weights", "1,1,10");
        globals.put(side + sys + "Output_Rate", "0.99");
        globals.put(side + sys + "Output_Proportional", "true");
        globals.put(side + sys + "Reference_Smooth", "0.9");

    }
}
