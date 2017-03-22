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
import java.io.File;
import java.util.List;
import pct.moons.co.uk.schema.layers.ControlFunction;
import pct.moons.co.uk.schema.layers.Layers;
import uk.co.moonsit.config.functions.Globals;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_070_001_BaxterJointRead {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

                        String file = Utils.getFileName("Robot", "ROS", "070-001-BaxterJointRead.xml");
        System.out.println(file);

        XML_070_001_BaxterJointRead xml = new XML_070_001_BaxterJointRead();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "20");

        globals.put("ROSJointSub_Index", "left_s1");
        globals.put("ROSJointSub_Variable", "Position");

        globals.put("ROSEndpointSub_Topic", "/robot/limb/left/endpoint_state");
        globals.put("ROSEndpointSub_Variable", "/pose/position/x");
                                        
        Utils.saveToXML(xml.run(), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    public XML_070_001_BaxterJointRead() {

    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Baxter joint read");

        List layer = layers.getLayer();

        layer.add(configureLayer0("Baxter"));

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

        controllers.add(configureControllerJoint());

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

    public static Layers.Layer.Controller configureControllerJoint() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("Joint");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction("ROSJointSub", "", "ROSSubscriber",
                    new String[][]{{"Index", Globals.getInstance().get("ROSJointSub_Index"), "String"},
                    {"Variable", Globals.getInstance().get("ROSJointSub_Variable"), "String"}}, null);

            inputFunctions.setInput(input);
            functions.setInputFunctions(inputFunctions);
        }

        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction("ROSEndpointSub", "...", "ROSSubscriber",
                    new String[][]{
                        {"Topic", Globals.getInstance().get("ROSEndpointSub_Topic"), "String"},
                        {"Variable", Globals.getInstance().get("ROSEndpointSub_Variable"), "String"}
                    }, null);

            outputFunctions.setOutput(output);
            functions.setOutputFunctions(outputFunctions);
        }

        controller.setFunctions(functions);
        return controller;
    }

}
