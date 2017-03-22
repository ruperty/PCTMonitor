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
import uk.co.moonsit.config.functions.Globals;
import uk.co.moonsit.config.functions.Modules;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_070_003_BaxterPositionIntegrationControl {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        String file = Utils.getFileName("Robot", "ROS", "070-003-BaxterPositionIntegrationControl.xml");
        System.out.println(file);

        XML_070_003_BaxterPositionIntegrationControl xml = new XML_070_003_BaxterPositionIntegrationControl();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "20");

        String side = "Left";
        globals.put("ArmOne_Side", side);

        //globals.put(side + "VelocityControlOutput_Topic", "/robot/limb/" + side.toLowerCase() + "/joint_command");
        globals.put(side + "PositionControlOutput_Topic", "/robot/limb/" + side.toLowerCase() + "/joint_command");

        globals.put("HandAngle_Joint", "w1");
        globals.put("ShoulderPitch_Joint", "s1");
        globals.put("ElbowAngle_Joint", "e1");

        //globals.put(side + "HandAngleVelocityControlOutput_Gain", "2");
        globals.put(side + "HandAnglePositionControlManualReference_Constant", "0");
        globals.put(side + "HandAnglePositionControlOutput_Gain", "2600");
        globals.put(side + "HandAnglePositionControlOutput_Slow", "100000");
        globals.put(side + "HandAnglePositionControlReference_Weights", "1");

        globals.put(side + "ElbowAnglePositionControlManualReference_Constant", "1");
        globals.put(side + "ElbowAnglePositionControlReference_Weights", "1");
        globals.put(side + "ElbowAnglePositionControlOutput_Gain", "1000");
        globals.put(side + "ElbowAnglePositionControlOutput_Slow", "100000");

        globals.put(side + "ShoulderPitchPositionControlManualReference_Constant", "-1");
        globals.put(side + "ShoulderPitchPositionControlReference_Weights", "1");
        globals.put(side + "ShoulderPitchPositionControlOutput_Gain", "2000");
        globals.put(side + "ShoulderPitchPositionControlOutput_Slow", "100000");

        globals.put(side + "ShoulderPitchPositionControlInput_Smooth", "0.9");
        globals.put(side + "ElbowAnglePositionControlInput_Smooth", "0.75");
        globals.put(side + "HandAnglePositionControlInput_Smooth", "0.9");

        String elbowAngleJoint = "ElbowAngle";
        String shoulderPitchJoint = "ShoulderPitch";
        globals.put(side + "HandAnglePositionControlOutput_Min", "-1.5");
        globals.put(side + "HandAnglePositionControlOutput_Max", "2");
        globals.put(side + elbowAngleJoint + "PositionControlOutput_Min", "0");
        globals.put(side + elbowAngleJoint + "PositionControlOutput_Max", "2.5");
        globals.put(side + shoulderPitchJoint + "PositionControlOutput_Min", "-2.1");
        globals.put(side + shoulderPitchJoint + "PositionControlOutput_Max", "1.04");

        Utils.saveToXML(xml.run(), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    public XML_070_003_BaxterPositionIntegrationControl() {
    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Baxter arm control");

        List layer = layers.getLayer();

        String side = Globals.getInstance().get("ArmOne_Side");

        layer.add(configureLayer0("Baxter", side));
        layer.add(Utils.emptyLayer("Position"));
        //layer.add(Utils.emptyLayer("Top"));

        addJointModule(layers, 0, "HandAngle", side, side + "HandAnglePositionControlManualReference");
        addJointModule(layers, 0, "ElbowAngle", side, side + "ElbowAnglePositionControlManualReference");
        addJointModule(layers, 0, "ShoulderPitch", side, side + "ShoulderPitchPositionControlManualReference");

        return layers;
    }

    private static void addJointModule(Layers layers, int layer, String joint, String side, String link1) throws Exception {
        String[][] links = new String[1][1];
        links[0][0] = link1;
        Modules.armJointPositionController(layers, layer, joint, side, Globals.getInstance().get(joint + "_Joint"), links);
    }

    private static Layers.Layer configureLayer0(String name, String side) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();

        Layers.Layer.Controller env = configureControllerEnv();

        String joint = "s1";
        ControlFunction function = Utils.configureControlFunction("ShoulderVelocity", "...", "ROSSubscriber",
                new String[][]{
                    {"Index", side.toLowerCase() + "_" + joint, "String"},
                    {"Variable", "Velocity", "String"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);

        joint = "e1";

        function = Utils.configureControlFunction("ElbowVelocity", "...", "ROSSubscriber",
                new String[][]{
                    {"Index", side.toLowerCase() + "_" + joint, "String"},
                    {"Variable", "Velocity", "String"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);

        joint = "w1";

        function = Utils.configureControlFunction("HandVelocity", "...", "ROSSubscriber",
                new String[][]{
                    {"Index", side.toLowerCase() + "_" + joint, "String"},
                    {"Variable", "Velocity", "String"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);
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

}
