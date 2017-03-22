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
public class XML_070_005_BaxterPositionVelocityControl {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        String file = Utils.getFileName("Robot", "ROS", "070-005-BaxterPositionVelocityControl.xml");
        System.out.println(file);

        XML_070_005_BaxterPositionVelocityControl xml = new XML_070_005_BaxterPositionVelocityControl();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "20");

        String side = "Left";
        globals.put("ArmOne_Side", side);

        //globals.put(side + "VelocityControlOutput_Topic", "/robot/limb/" + side.toLowerCase() + "/joint_command");
        globals.put(side + "JointPublish_Topic", "/robot/limb/" + side.toLowerCase() + "/joint_command");

        String joint = "HandAngle";
        globals.put(joint + "_Joint", "w1");

        globals.put(side + joint + "PositionControlManualReference_Constant", "0");
        globals.put(side + joint + "PositionControlOutput_InScale", "10");
        globals.put(side + joint + "PositionControlOutput_OutScale", "4");
        globals.put(side + joint + "PositionControlReferences_Weights", "1");
        globals.put(side + joint + "PositionControlInput_Smooth", "0.9");
        globals.put(side + joint + "PositionControlJointVelocityPublish_Disabled", "false");
        globals.put(side + joint + "PositionControlJointPositionPublish_Disabled", "false");
        globals.put(side + joint + "PositionControl_Tolerance", "0.1");
        globals.put(side + joint + "PositionControlErrorFiltered_Tolerance", "0.05");
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
        globals.put(side + joint + "PositionControl_Tolerance", "0.1");
        globals.put(side + joint + "PositionControlErrorFiltered_Tolerance", "0.05");
        globals.put(side + joint + "PositionControl_Initial", "0");

        joint = "ShoulderPitch";
        globals.put(joint + "_Joint", "s1");
        globals.put(side + joint + "PositionControlManualReference_Constant", "1");
        globals.put(side + joint + "PositionControlReferences_Weights", "1");
        globals.put(side + joint + "PositionControlOutput_InScale", "8");
        globals.put(side + joint + "PositionControlOutput_OutScale", "2");
        globals.put(side + joint + "PositionControlInput_Smooth", "0.9");
        globals.put(side + joint + "PositionControlJointVelocityPublish_Disabled", "false");
        globals.put(side + joint + "PositionControlJointPositionPublish_Disabled", "true");
        globals.put(side + joint + "PositionControl_Tolerance", "0.1");
        globals.put(side + joint + "PositionControlErrorFiltered_Tolerance", "0.05");
        globals.put(side + joint + "PositionControl_Initial", "0");

        Utils.saveToXML(xml.run(), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    public XML_070_005_BaxterPositionVelocityControl() {
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

        String joint = "HandAngle";

        addJointModule(layers, 1, "HandAngle", side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), side + joint + "PositionControlManualReference");
        joint = "ElbowAngle";
        addJointModule(layers, 1, "ElbowAngle", side, false, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), side + joint + "PositionControlManualReference");
        joint = "ShoulderPitch";
        addJointModule(layers, 1, "ShoulderPitch", side, true, Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"), Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"), side + joint + "PositionControlManualReference");

        return layers;
    }

    private static void addJointModule(Layers layers, int layer, String joint, String side, Boolean publish, String tolerance, String initial, String link1) throws Exception {
        String[][] links = new String[1][1];
        links[0][0] = link1;
        Modules.armJointPositionVelocityController(layers, layer, joint, side, Globals.getInstance().get(joint + "_Joint"), String.valueOf(publish), tolerance, initial, null,links, false);
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
