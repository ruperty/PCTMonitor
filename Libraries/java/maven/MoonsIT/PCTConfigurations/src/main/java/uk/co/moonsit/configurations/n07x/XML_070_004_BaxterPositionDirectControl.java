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
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_070_004_BaxterPositionDirectControl {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        String file = Utils.getFileName("Robot", "ROS", "070-004-BaxterPositionDirectControl.xml");
        System.out.println(file);

        XML_070_004_BaxterPositionDirectControl xml = new XML_070_004_BaxterPositionDirectControl();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "50");

        String side = "Left";
        globals.put("ArmOne_Side", side);

        //globals.put(side + "VelocityControlOutput_Topic", "/robot/limb/" + side.toLowerCase() + "/joint_command");
        globals.put(side + "PositionControlOutput_Topic", "/robot/limb/" + side.toLowerCase() + "/joint_command");

        Utils.saveToXML(xml.run(), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    public XML_070_004_BaxterPositionDirectControl() {
    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Baxter arm control");

        List layer = layers.getLayer();

        String side = Globals.getInstance().get("ArmOne_Side");

        layer.add(configureLayer0("Baxter", side));
        //layer.add(Utils.emptyLayer("Position"));

        return layers;
    }

    private static Layers.Layer configureLayer0(String name, String side) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();

        Layers.Layer.Controller env = configureControllerEnv();

        String joint = "s1";
        ControlFunction function = Utils.configureControlFunction("ShoulderPublisherRef", "...", "Constant",
                new String[][]{{"Constant", "0", "String"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);
        function = Utils.configureControlFunction("ShoulderPublisher", "...", "ROSBaxterJointPublisher",
                new String[][]{
                    {"Joint", side.toLowerCase() + "_" + joint, "String"},
                    {"Topic", Globals.getInstance().get(side + "PositionControlOutput_Topic"), "String"},
                    {"Mode", "Position", "String"}
                },
                new String[][]{{"ShoulderPublisherRef"}});
        Utils.addTransferFunction(env, function, Utils.INPUT);

        function = Utils.configureControlFunction("ShoulderPosition", "...", "ROSSubscriber",
                new String[][]{
                    {"Index", side.toLowerCase() + "_" + joint, "String"},
                    {"Variable", "Position", "String"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);

        function = Utils.configureControlFunction("ShoulderVelocity", "...", "ROSSubscriber",
                new String[][]{
                    {"Index", side.toLowerCase() + "_" + joint, "String"},
                    {"Variable", "Velocity", "String"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);

        joint = "e1";
        function = Utils.configureControlFunction("ElbowPublisherRef", "...", "Constant",
                new String[][]{{"Constant", "0", "String"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);
        function = Utils.configureControlFunction("ElbowPublisher", "...", "ROSBaxterJointPublisher",
                new String[][]{
                    {"Joint", side.toLowerCase() + "_" + joint, "String"},
                    {"Topic", Globals.getInstance().get(side + "PositionControlOutput_Topic"), "String"},
                    {"Mode", "Position", "String"}
                },
                new String[][]{{"ElbowPublisherRef"}});
        Utils.addTransferFunction(env, function, Utils.INPUT);

        function = Utils.configureControlFunction("ElbowPosition", "...", "ROSSubscriber",
                new String[][]{
                    {"Index", side.toLowerCase() + "_" + joint, "String"},
                    {"Variable", "Position", "String"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);
        function = Utils.configureControlFunction("ElbowVelocity", "...", "ROSSubscriber",
                new String[][]{
                    {"Index", side.toLowerCase() + "_" + joint, "String"},
                    {"Variable", "Velocity", "String"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);

        joint = "w1";
        function = Utils.configureControlFunction("HandPublisherRef", "...", "Constant",
                new String[][]{{"Constant", "0", "String"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);
        function = Utils.configureControlFunction("HandPublisher", "...", "ROSBaxterJointPublisher",
                new String[][]{
                    {"Joint", side.toLowerCase() + "_" + joint, "String"},
                    {"Topic", Globals.getInstance().get(side + "PositionControlOutput_Topic"), "String"},
                    {"Mode", "Position", "String"}
                },
                new String[][]{{"HandPublisherRef"}});
        Utils.addTransferFunction(env, function, Utils.INPUT);

        function = Utils.configureControlFunction("HandPosition", "...", "ROSSubscriber",
                new String[][]{
                    {"Index", side.toLowerCase() + "_" + joint, "String"},
                    {"Variable", "Position", "String"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);

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
