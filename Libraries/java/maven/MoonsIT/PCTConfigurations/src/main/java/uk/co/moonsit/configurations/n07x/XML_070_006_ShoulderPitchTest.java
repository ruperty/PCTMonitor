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
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_070_006_ShoulderPitchTest {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        String file = Utils.getFileName("Robot", "ROS", "070-006-ShoulderPitchTest.xml");
        System.out.println(file);
        boolean leaky = true;

        XML_070_006_ShoulderPitchTest xml = new XML_070_006_ShoulderPitchTest();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "20");

        String side = "Left";
        if (leaky) {
            globals.put(side + "ShoulderIntegratorOutput_Gain", "1000");
            globals.put(side + "ShoulderIntegratorOutput_Slow", "10000");
        } else {
            globals.put(side + "ShoulderIntegratorOutput_Gain", "0.5");
            globals.put(side + "ShoulderIntegratorOutput_Slow", "N/A");
        }
        globals.put("ArmOne_Side", side);

        globals.put(side + "JointPublish_Topic", "/robot/limb/" + side.toLowerCase() + "/joint_command");

        String joint = "HandAngle";
        globals.put(joint + "_Joint", "w1");
        globals.put(side + joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControl_Initial", "0");

        joint = "ElbowAngle";
        globals.put(joint + "_Joint", "e1");
        globals.put(side + joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControl_Initial", "0");

        joint = "ShoulderPitch";
        globals.put(joint + "_Joint", "s1");
        globals.put(side + joint + "PositionControl_Initial", "1");
        globals.put(side + joint + "PositionControl_Tolerance", "0.01");

        joint = "ShoulderYaw";
        globals.put(joint + "_Joint", "s0");
        globals.put(side + joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControl_Tolerance", "0.01");
        globals.put(side + joint + "PositionControl_Initial", "0");

        joint = "ElbowRoll";
        globals.put(joint + "_Joint", "e0");
        globals.put(side + joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControl_Tolerance", "0.01");
        globals.put(side + joint + "PositionControl_Initial", "0");

        joint = "WristRoll";
        globals.put(joint + "_Joint", "w0");
        globals.put(side + joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControl_Tolerance", "0.01");
        globals.put(side + joint + "PositionControl_Initial", "0");

        joint = "HandRoll";
        globals.put(joint + "_Joint", "w2");
        globals.put(side + joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControl_Tolerance", "0.01");
        globals.put(side + joint + "PositionControl_Initial", "0");

        Utils.saveToXML(xml.run(leaky), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    public XML_070_006_ShoulderPitchTest() {
    }

    public Layers run(boolean leaky) throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Baxter arm control");

        List layer = layers.getLayer();

        String side = Globals.getInstance().get("ArmOne_Side");

        layer.add(configureLayer0("Baxter", side));
        //layer.add(Utils.emptyLayer("Position"));
        layer.add(Utils.emptyLayer("Top"));

        Controller controller = Utils.emptyController("Interface", true, false, false, true);
        Utils.addController(layers, 0, controller);

        String joint = "ElbowAngle";
        Generic.addPositionSubPub(controller, false, joint, side, false, null, Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,side + joint + "PositionControlManualReference", false);
        joint = "ShoulderPitch";
        Generic.addPositionSubPub(controller, true, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,side + "ShoulderIntegratorOutput", false);
        joint = "HandAngle";
        Generic.addPositionSubPub(controller, true, joint, side, false, null, Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,side + joint + "PositionControlManualReference", false);
        joint = "ShoulderYaw";
        Generic.addPositionSubPub(controller, true, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,side + joint + "PositionControlManualReference", false);
        joint = "ElbowRoll";
        Generic.addPositionSubPub(controller, true, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,side + joint + "PositionControlManualReference", false);
        joint = "WristRoll";
        Generic.addPositionSubPub(controller, true, joint, side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,side + joint + "PositionControlManualReference", false);
        joint = "HandRoll";
        Generic.addPositionSubPub(controller, true, joint, side, true, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), null,side + joint + "PositionControlManualReference", false);

        Utils.addController(layers, 1, configureShoulderIntegrator(leaky));

        return layers;
    }

    /*
    private static void addPositionSubPub(Controller controller, boolean tr, String joint, String side, String link1, boolean weight, String publish, String tolerance, String initial) throws Exception {
        String[][] links = new String[1][1];
        links[0][0] = link1;

        ControlFunction function = Utils.configureControlFunction(side + joint + "Sub", "...", "ROSSubscriber",
                new String[][]{
                    {"Index", side.toLowerCase() + "_" + Globals.getInstance().get(joint + "_Joint"), "String"},
                    {"Variable", "Position", "String"}}, null);

        Utils.addFunction(controller, function, Utils.INPUT, tr);

        if (weight) {
            function = Utils.configureControlFunction(side + joint + "PubWeighted", "...", "WeightedSum",
                    new String[][]{
                        {"Weights", Globals.getInstance().get(side + joint + "PubWeighted_Weights"), "String"}
                    },
                    links);
            Utils.addFunction(controller, function, Utils.OUTPUT, tr);
        }

        function = Utils.configureBaxterPublisher(side + joint + "Pub", "...",
                side.toLowerCase() + "_" + Globals.getInstance().get(joint + "_Joint"),
                Globals.getInstance().get(side + "JointPublish_Topic"),
                "Position", publish, tolerance, null, initial,
                (weight ? new String[][]{{side + joint + "PubWeighted"}} : links));

        Utils.addFunction(controller, function, Utils.OUTPUT, tr);

        if (weight) {
            function = Utils.configureControlFunction(side + joint + "PubWeighted", "...", "WeightedSum",
                    new String[][]{
                        {"Weights", Globals.getInstance().get(side + joint + "PubWeighted_Weights"), "String"}
                    },
                    links);
            Utils.addFunction(controller, function, Utils.OUTPUT, true);
        }

    }

    private static void addPositionSubPub(Controller controller, boolean tr, String joint, String side, String link1, String link2) throws Exception {
        String[][] links = new String[2][1];
        links[0][0] = link1;
        links[1][0] = link2;
        ControlFunction function = Utils.configureControlFunction(side + joint + "Sub", "...", "ROSSubscriber",
                new String[][]{
                    {"Index", side.toLowerCase() + "_" + Globals.getInstance().get(joint + "_Joint"), "String"},
                    {"Variable", "Position", "String"}}, null);

        Utils.addFunction(controller, function, Utils.INPUT, tr);

        function = Utils.configureControlFunction(side + joint + "PubWeighted", "...", "WeightedSum",
                new String[][]{
                    {"Weights", Globals.getInstance().get(side + joint + "PubWeighted_Weights"), "String"}
                },
                links);
        Utils.addFunction(controller, function, Utils.OUTPUT, tr);

        function = Utils.configureControlFunction(side + joint + "Pub", "...", "ROSBaxterJointPublisher",
                new String[][]{
                    {"Joint", side.toLowerCase() + "_" + Globals.getInstance().get(joint + "_Joint"), "String"},
                    {"Topic", Globals.getInstance().get(side + "JointPublish_Topic"), "String"},
                    {"Mode", "Position", "String"}
                },
                new String[][]{{side + joint + "PubWeighted"}});
        Utils.addFunction(controller, function, Utils.OUTPUT, tr);

    }
     */
    private static Layers.Layer configureLayer0(String name, String side) throws Exception {
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

            transfer = Utils.configureControlFunction("LeftElbowAnglePositionControlManualReference", "...", "Constant",
                    new String[][]{{"Constant", Globals.getInstance().get("LeftElbowAnglePositionControlManualReference_Constant"), "Double"}}, null);
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("LeftShoulderYawPositionControlManualReference", "...", "Constant",
                    new String[][]{{"Constant", Globals.getInstance().get("LeftShoulderYawPositionControlManualReference_Constant"), "Double"}}, null);
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("LeftElbowRollPositionControlManualReference", "...", "Constant",
                    new String[][]{{"Constant", Globals.getInstance().get("LeftElbowRollPositionControlManualReference_Constant"), "Double"}}, null);
            transfersList.add(transfer);
            transfer = Utils.configureControlFunction("LeftWristRollPositionControlManualReference", "...", "Constant",
                    new String[][]{{"Constant", Globals.getInstance().get("LeftWristRollPositionControlManualReference_Constant"), "Double"}}, null);
            transfersList.add(transfer);
            transfer = Utils.configureControlFunction("LeftHandRollPositionControlManualReference", "...", "Constant",
                    new String[][]{{"Constant", Globals.getInstance().get("LeftHandRollPositionControlManualReference_Constant"), "Double"}}, null);
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("LeftHandAnglePositionControlManualReference", "...", "Constant",
                    new String[][]{{"Constant", Globals.getInstance().get("LeftHandAnglePositionControlManualReference_Constant"), "Double"}}, null);
            transfersList.add(transfer);

            inputFunctions.setTransfers(transfers);
        }
        functions.setInputFunctions(inputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureShoulderIntegrator(boolean leaky) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String name = "LeftShoulderIntegrator";
        controller.setName(name);
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction(name + "Input", "...", "",
                    null, new String[][]{{name + "Output"}});
            inputFunctions.setInput(input);

            functions.setInputFunctions(inputFunctions);
        }

        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
            ControlFunction reference = Utils.configureControlFunction(name + "Reference", "Distance constant", "Constant",
                    new String[][]{{"Constant", "0", "Double"}}, null);
            referenceFunctions.setReference(reference);

            functions.setReferenceFunctions(referenceFunctions);
        }

        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction(name + "Error", "...", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            functions.setErrorFunctions(errorFunctions);
        }
        {
            String[][] pars;
            if (leaky) {
                pars = new String[][]{
                    {"Gain", Globals.getInstance().get(name + "Output_Gain"), "Double"},
                    {"Slow", Globals.getInstance().get(name + "Output_Slow"), "Double"}};

            } else {
                pars = new String[][]{
                    {"Gain", Globals.getInstance().get(name + "Output_Gain"), "Double"}};

            }
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction(name + "Output", "output", leaky ? "Integration" : "ProportionalIntegration",
                    pars, null);
            outputFunctions.setOutput(output);
            functions.setOutputFunctions(outputFunctions);
        }
        controller.setFunctions(functions);
        return controller;
    }

}
