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
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_070_002_BaxterJointPubSub {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        String file = Utils.getFileName("Robot", "ROS", "070-002-BaxterJointPubSub.xml");
        System.out.println(file);

        XML_070_002_BaxterJointPubSub xml = new XML_070_002_BaxterJointPubSub();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "20");

        /*globals.put("ROSJointSub_Index", "left_s1");
        globals.put("ROSJointSub_Variable", "Position");

        globals.put("ROSJointOutput_Topic", "/robot/limb/left/joint_command");
        globals.put("ROSJointOutput_Mode", "Position");
        globals.put("ROSJointOutput_Joint", "left_s1");
         */
        String side = "Right";
        globals.put(side + "JointPublish_Topic", "/robot/limb/" + side.toLowerCase() + "/joint_command");

        String joint = "HandAngle";
        globals.put(joint + "_Joint", "w1");
        globals.put(joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControl_Tolerance", "0.001");
        globals.put(side + joint + "PositionControl_Initial", "0");

        joint = "ElbowAngle";
        globals.put(joint + "_Joint", "e1");
        globals.put(joint + "PositionControlManualReference_Constant", "0.6");
        globals.put(side + joint + "PositionControl_Tolerance", "0.005");
        globals.put(side + joint + "PositionControl_Initial", "0.6");

        joint = "ShoulderPitch";
        globals.put(joint + "_Joint", "s1");
        globals.put(joint + "PositionControlManualReference_Constant", "1");
        globals.put(side + joint + "PositionControl_Tolerance", "0.005");
        globals.put(side + joint + "PositionControl_Initial", "1");

        joint = "ShoulderYawAngle";
        globals.put(joint + "_Joint", "s0");
        globals.put(joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControl_Tolerance", "0.005");
        globals.put(side + joint + "PositionControl_Initial", "0");

        joint = "ElbowRoll";
        globals.put(joint + "_Joint", "e0");
        globals.put(joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControl_Tolerance", "0.01");
        globals.put(side + joint + "PositionControl_Initial", "0");

        joint = "WristRoll";
        globals.put(joint + "_Joint", "w0");
        globals.put(joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControl_Tolerance", "0.01");
        globals.put(side + joint + "PositionControl_Initial", "0");

        joint = "HandRoll";
        globals.put(joint + "_Joint", "w2");
        globals.put(joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControl_Tolerance", "0.01");
        globals.put(side + joint + "PositionControl_Initial", "0");

        /*globals.put("Reference_Weights", "0,0,0,1");

        globals.put("Sine_Frequency", "0.5");
        globals.put("Sine_Amplitude", "75");
        globals.put("Sine_Step", "1");
        globals.put("Sine_YShift", "125");

        globals.put("Step_Period", "400");
        globals.put("Step_Upper", "200");
        globals.put("Step_Lower", "50");

        globals.put("Random_Scale", "100");
        globals.put("Random_Slow", "0.05");
        globals.put("Random_Initial", "100");
        globals.put("Random_Max", "220");
        globals.put("Random_Min", "30");
         */
        Utils.saveToXML(xml.run(), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    public XML_070_002_BaxterJointPubSub() {

    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Baxter joints pubsub test");

        List layer = layers.getLayer();

        layer.add(configureLayer0("Baxter"));

        String side = "Right";

        Layers.Layer.Controller controller = Utils.emptyController(side + "Interface", true, false, false, true);
        Utils.addController(layers, 0, controller);

        addJointControlSystems(controller, side);

        return layers;
    }

    public static Layers.Layer.Controller configureEnv() throws Exception {
        Layers.Layer.Controller env = configureControllerEnv();

        /*ControlFunction function = Utils.randomFunction();
        Utils.addTransferFunction(env, function, Utils.INPUT);
        function = Utils.stepFunction();
        Utils.addTransferFunction(env, function, Utils.INPUT);
        function = Utils.sineFunction();
        Utils.addTransferFunction(env, function, Utils.INPUT);
         */
        return env;
    }

    private static Layers.Layer configureLayer0(String name) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();
        controllers.add(configureEnv());

        //controllers.add(configureControllerJoints());
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
                    new String[][]{{"Pause", Globals.getInstance().get("Pause_Pause"), "Long"}, {"ConstantRate", "true", "Boolean"}}, null);
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("Zero", "...", "Constant",
                    new String[][]{{"Constant", "0", "Double"}}, null);
            transfersList.add(transfer);
            inputFunctions.setTransfers(transfers);
        }
        functions.setInputFunctions(inputFunctions);

        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction("ElbowRollPositionControlManualReference", "...", "Constant",
                    new String[][]{{"Constant", Globals.getInstance().get("ElbowRollPositionControlManualReference_Constant"), "Double"}}, null);
            outputFunctions.setOutput(output);
            {
                Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer;

                String joint = "WristRoll";
                transfer = Utils.configureControlFunction(joint + "PositionControlManualReference", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get(joint + "PositionControlManualReference_Constant"), "Double"}}, null);
                transfersList.add(transfer);
                joint = "HandRoll";
                transfer = Utils.configureControlFunction(joint + "PositionControlManualReference", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get(joint + "PositionControlManualReference_Constant"), "Double"}}, null);
                transfersList.add(transfer);

                joint = "ElbowAngle";
                transfer = Utils.configureControlFunction(joint + "PositionControlManualReference", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get(joint + "PositionControlManualReference_Constant"), "Double"}}, null);
                transfersList.add(transfer);

                joint = "HandAngle";
                transfer = Utils.configureControlFunction(joint + "PositionControlManualReference", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get(joint + "PositionControlManualReference_Constant"), "Double"}}, null);
                transfersList.add(transfer);

                joint = "ShoulderYawAngle";
                transfer = Utils.configureControlFunction(joint + "PositionControlManualReference", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get(joint + "PositionControlManualReference_Constant"), "Double"}}, null);
                transfersList.add(transfer);

                joint = "ShoulderPitch";
                transfer = Utils.configureControlFunction(joint + "PositionControlManualReference", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get(joint + "PositionControlManualReference_Constant"), "Double"}}, null);
                transfersList.add(transfer);
                outputFunctions.setTransfers(transfers);
            }
            functions.setOutputFunctions(outputFunctions);

        }

        controller.setFunctions(functions);
        return controller;
    }

    private static void addJointControlSystems(Layers.Layer.Controller controller, String side) throws Exception {
        String joint = "ElbowAngle";
        Generic.addPositionSubPub(controller, false, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"),null, joint + "PositionControlManualReference", false);
        joint = "ShoulderPitch";
        Generic.addPositionSubPub(controller, true, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"),null, joint + "PositionControlManualReference", false);
        joint = "HandAngle";
        Generic.addPositionSubPub(controller, true, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"),null, joint + "PositionControlManualReference", false);
        joint = "ShoulderYawAngle";//LeftShoulderYawMovementVelocityControlOutput side + joint + "MovementVelocityControlOutput"
        //String shoulderYawLink = side + joint + "MemoryPositionReference";

        Generic.addPositionSubPub(controller, true, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"),null, joint + "PositionControlManualReference", false);
        joint = "ElbowRoll";
        Generic.addPositionSubPub(controller, true, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"),null, joint + "PositionControlManualReference", false);
        joint = "WristRoll";
        Generic.addPositionSubPub(controller, true, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"),null, joint + "PositionControlManualReference", false);
        joint = "HandRoll";
        Generic.addPositionSubPub(controller, true, joint, side, true, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"),null, joint + "PositionControlManualReference", false);

    }

    public static Layers.Layer.Controller configureControllerJoints() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("Joint");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction("ROSJointOutput", "...", "ROSBaxterJointPublisher",
                    new String[][]{{"Joint", Globals.getInstance().get("ROSJointOutput_Joint"), "Double"},
                    {"Topic", Globals.getInstance().get("ROSJointOutput_Topic"), "Double"},
                    {"Mode", Globals.getInstance().get("ROSJointOutput_Mode"), "Double"}
                    },
                    new String[][]{{"ROSJointReference"}});
            inputFunctions.setInput(input);
            functions.setInputFunctions(inputFunctions);
        }

        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
            ControlFunction reference = Utils.configureControlFunction("ROSJointReference", "...", "Constant",
                    new String[][]{{"Constant", "0", "Double"}}, null);

            referenceFunctions.setReference(reference);
            functions.setReferenceFunctions(referenceFunctions);

        }

        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction("ROSJointSub", "", "ROSSubscriber",
                    new String[][]{{"Index", Globals.getInstance().get("ROSJointSub_Index"), "Double"},
                    {"Variable", Globals.getInstance().get("ROSJointSub_Variable"), "Double"}}, null);

            outputFunctions.setOutput(output);

            functions.setOutputFunctions(outputFunctions);
        }

        controller.setFunctions(functions);
        return controller;
    }

}
