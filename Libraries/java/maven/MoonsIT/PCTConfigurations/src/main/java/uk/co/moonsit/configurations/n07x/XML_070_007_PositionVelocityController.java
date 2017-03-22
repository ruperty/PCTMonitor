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
public class XML_070_007_PositionVelocityController {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        boolean integration = false;
        boolean robotsim = false;
        String file = Utils.getFileName("Robot", "ROS", "070-007-PositionVelocityController.xml");
        System.out.println(file);

        XML_070_007_PositionVelocityController xml = new XML_070_007_PositionVelocityController();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "25");

        String side = "Left";

        globals.put("ArmOne_Side", side);

        globals.put(side + "JointPublish_Topic", "/robot/limb/" + side.toLowerCase() + "/joint_command");

        String[] joints = {"ShoulderPitchS1"};
        String[] refLinks = {joints[0] + "PositionReference"};
        String joint = joints[0];
        globals.put(joint + "_Joint", "s1");

        globals.put(side + joint + "PositionControlInput_Smooth", "0.1");
        globals.put(side + joint + "VelocityControlInput_Smooth", "0.1");
        globals.put(side + joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "VelocityControlManualReference_Constant", "2");
        globals.put(side + joint + "TorquePublish_Tolerance", "0.01");
        globals.put(side + joint + "TorquePublish_Initial", "0");

        if (integration) {
            globals.put(side + joint + "VelocityControlOutput_Gain", "1000");
            globals.put(side + joint + "VelocityControlOutput_Slow", "1000000");
            globals.put(side + joint + "VelocityControlOutput_Min", "-1");
            globals.put(side + joint + "VelocityControlOutput_Max", "1");
        } else if (robotsim) {
            globals.put(side + joint + "VelocityControlOutput_Gain", "20");
        } else {
            globals.put(side + joint + "VelocityControlOutput_Gain", "1");
        }

        globals.put(side + joint + "PositionControlOutput_InScale", "5");
        globals.put(side + joint + "PositionControlOutput_OutScale", "1");

        Utils.saveToXML(xml.run(joints, refLinks, integration), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    public XML_070_007_PositionVelocityController() {
    }

    public Layers run(String joints[], String[] refLinks, boolean integration) throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Baxter arm control");

        List layer = layers.getLayer();

        String side = Globals.getInstance().get("ArmOne_Side");

        layer.add(configureLayer0("Interface", side, joints));
        Generic.addPVTSensorsActuators(layers, side, joints, "Velocity");

        Modules.addPVControls(layers, side, joints, refLinks, integration);

        return layers;
    }

    private static Layers.Layer configureLayer0(String name, String side, String joints[]) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();
        Layers.Layer.Controller env = configureControllerEnv();
        controllers.add(env);

        addReferences(env, side, joints, Utils.INPUT);
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

        controller.setFunctions(functions);
        return controller;
    }

    public static void addReferences(Controller controller, String side, String joints[], int function) throws Exception {
        for (String joint : joints) {
            ControlFunction transfer = Utils.configureControlFunction(side + joint + "PositionReference", "...", "Constant",
                    new String[][]{{"Constant", Globals.getInstance().get(side + joint + "PositionControlManualReference_Constant"), "Double"}}, null);
            Utils.addTransferFunction(controller, transfer, function);

            transfer = Utils.configureControlFunction(side + joint + "VelocityReference", "...", "Constant",
                    new String[][]{{"Constant", Globals.getInstance().get(side + joint + "VelocityControlManualReference_Constant"), "Double"}}, null);
            Utils.addTransferFunction(controller, transfer, function);
        }
    }

}
