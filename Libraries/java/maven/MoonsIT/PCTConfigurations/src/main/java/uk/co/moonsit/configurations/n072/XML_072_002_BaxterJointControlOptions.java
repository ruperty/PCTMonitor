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
public class XML_072_002_BaxterJointControlOptions {

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

        String side = "Left";
        String file = Utils.getFileName("Robot", "ROS", "072-002-BaxterJointController" + side+suffix + ".xml");
        System.out.println(file);

        XML_072_002_BaxterJointControlOptions xml = new XML_072_002_BaxterJointControlOptions();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "25");

        globals.put("ArmOne_Side", side);

        //String[] joints = {"ShoulderPitchS1"};
        String[] joints = Generic.getAllJoints();
        String[] refLinks = new String[joints.length];
        int i = 0;
        for (String joint : joints) {
            refLinks[i] = joint + "PositionControlManualReference";
            i++;
        }
        xml.armGlobals(globals, side, joints, type);

        Utils.saveToXML(xml.run(type, joints, refLinks), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    public XML_072_002_BaxterJointControlOptions() {
    }

    public Layers run(int type, String joints[], String[] refLinks) throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Baxter arm control");

        List layer = layers.getLayer();

        String side = Globals.getInstance().get("ArmOne_Side");

        layer.add(configureLayer0("Baxter", side, joints, type));

        Modules.addJointControls(layers, side, joints, refLinks, null, type, false, false, false);

        return layers;
    }

    private static Layers.Layer configureLayer0(String name, String side, String joints[], int type) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();
        Controller env = configureControllerEnv();
        controllers.add(env);

        addReferences(env, side, joints, Utils.INPUT, type);

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

    public static void addReferences(Layers.Layer.Controller controller, String side, String joints[], int function, int type) throws Exception {
        for (String joint : joints) {

            {
                ControlFunction transfer = Utils.configureControlFunction(side + joint + "PositionControlManualReference", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get(side + joint + "PositionControlManualReference_Constant"), "Double"}}, null);
                Utils.addTransferFunction(controller, transfer, function);
            }
            switch (type) {
                case Generic.P: {
                    break;
                }
                case Generic.PV: {
                    ControlFunction transfer = Utils.configureControlFunction(side + joint + "VelocityReference", "...", "Constant",
                            new String[][]{{"Constant", Globals.getInstance().get(side + joint + "VelocityControlManualReference_Constant"), "Double"}}, null);
                    Utils.addTransferFunction(controller, transfer, function);
                }
                break;
                case Generic.PVA: {
                    ControlFunction transfer = Utils.configureControlFunction(side + joint + "VelocityReference", "...", "Constant",
                            new String[][]{{"Constant", Globals.getInstance().get(side + joint + "VelocityControlManualReference_Constant"), "Double"}}, null);
                    Utils.addTransferFunction(controller, transfer, function);

                }
                break;
            }

        }
    }

    public void armGlobals(Globals globals, String side, String[] joints, int type) throws Exception {

        globals.put(side + "JointPublish_Topic", "/robot/limb/" + side.toLowerCase() + "/joint_command");

        for (String joint : joints) {
            globals.put(joint + "_Joint", Generic.getJointCode(joint));
            globals.put(side + joint + "PositionControlManualReference_Constant", "0");
            //globals.put(side + joint + "PositionControl_Initial", "0");

            switch (type) {
                case Generic.P:
                    globals.put(side + joint + "PositionControl_Tolerance", "0.01");
                    globals.put(side + joint + "PositionControl_Initial", "0");
                    globals.put(side + joint + "PositionControl_Last", "0");
                    /*
                    globals.put(side + joint + "PositionControlInput_Smooth", "0.9");
                    globals.put(side + joint + "PositionControlReference_Weights", "1,0");
                    globals.put(side + joint + "PositionControlOutput_Gain", "20000");
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
                    globals.put(side + joint + "VelocityControlManualReference_Constant", "1");

                    break;
                case Generic.PVA:
                    globals.put(side + joint + "PositionControlInput_Smooth", "0.1");
                    globals.put(side + joint + "TorquePublish_Tolerance", "0.01");
                    globals.put(side + joint + "TorquePublish_Initial", "0");
                    globals.put(side + joint + "VelocityControlOutput_Gain", "0.1");
                    globals.put(side + joint + "PositionControlOutput_InScale", "10");
                    globals.put(side + joint + "PositionControlOutput_OutScale", "1");
                    globals.put(side + joint + "VelocityControlManualReference_Constant", "0");
                    globals.put(side + joint + "AccelerationControlOutput_Gain", "100000");
                    globals.put(side + joint + "AccelerationControlOutput_Slow", "1000000");
                    globals.put(side + joint + "AccelerationControlOutput_Min", "-5");
                    globals.put(side + joint + "AccelerationControlOutput_Max", "5");

                    break;
            }
        }

        String joint = "ShoulderPitchS1";
        switch (type) {
            case Generic.P:
                globals.put(side + joint + "PositionControlManualReference_Constant", "-0.4");
                globals.put(side + joint + "PositionControl_Initial", "-0.4");
                globals.put(side + joint + "PositionControl_Last", "0");
                break;
            case Generic.PV:
                globals.put(side + joint + "PositionControlManualReference_Constant", "-0.45");
                globals.put(side + joint + "PositionControl_Initial", "-0.9");
                globals.put(side + joint + "VelocityControlOutput_Gain", "15");
                globals.put(side + joint + "VelocityControlManualReference_Constant", "0.5");
                globals.put(side + joint + "PositionControlOutput_InScale", "5");
                break;
        }

        /*
        globals.put(side + joint + "PubWeighted_Weights", "1,1");
         */
        joint = "ElbowAngleE1";
        switch (type) {
            case Generic.P:
                globals.put(side + joint + "PositionControlManualReference_Constant", "1.8");
                globals.put(side + joint + "PositionControl_Initial", "1.8");
                globals.put(side + joint + "PositionControl_Last", "1.6");
                break;
            case Generic.PV:
                globals.put(side + joint + "PositionControlManualReference_Constant", "1.75");
                globals.put(side + joint + "PositionControl_Initial", "1");
                globals.put(side + joint + "TorquePublish_Initial", "1");
                globals.put(side + joint + "VelocityControlOutput_Gain", "1");
                globals.put(side + joint + "VelocityControlManualReference_Constant", "1");
                break;
        }

        joint = "WristRollW0";
        switch (type) {
            case Generic.P:
                globals.put(side + joint + "PositionControl_Last", "1.4");

                break;
            case Generic.PV:

                globals.put(side + joint + "VelocityControlOutput_Gain", "1");
                globals.put(side + joint + "VelocityControlManualReference_Constant", "4");
                globals.put(side + joint + "PositionControlOutput_InScale", "5");

                break;
        }

        joint = "HandRollW2";
        switch (type) {
            case Generic.P:

                break;
            case Generic.PV:
                globals.put(side + joint + "VelocityControlOutput_Gain", "0.75");
                globals.put(side + joint + "VelocityControlManualReference_Constant", "4");
                globals.put(side + joint + "PositionControlOutput_InScale", "5");
                break;
        }

        joint = "HandAngleW1";
        switch (type) {
            case Generic.P:
                globals.put(side + joint + "PositionControlManualReference_Constant", "-1.4");
                globals.put(side + joint + "PositionControl_Initial", "-1.4");
                globals.put(side + joint + "PositionControl_Last", "-1.5");
                break;
            case Generic.PV:
                globals.put(side + joint + "VelocityControlOutput_Gain", "1");
                globals.put(side + joint + "PositionControlManualReference_Constant", "-1.4");
                globals.put(side + joint + "PositionControl_Initial", "0");
                globals.put(side + joint + "VelocityControlManualReference_Constant", "4");
                globals.put(side + joint + "PositionControlOutput_InScale", "5");
                break;
        }

        joint = "ElbowRollE0";
        switch (type) {
            case Generic.P:
                break;
            case Generic.PV:
                globals.put(side + joint + "VelocityControlOutput_Gain", "5");
                globals.put(side + joint + "VelocityControlManualReference_Constant", "1");
                globals.put(side + joint + "PositionControlOutput_InScale", "5");
                break;
        }

        joint = "ShoulderYawS0";
        switch (type) {
            case Generic.P:
                globals.put(side + joint + "PositionControlManualReference_Constant", "-0.8");
                globals.put(side + joint + "PositionControl_Initial", "-0.8");
                globals.put(side + joint + "PositionControl_Last", "-0.5");
                break;
            case Generic.PV:
                break;
        }
    }
}
