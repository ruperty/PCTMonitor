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
import uk.co.moonsit.config.functions.Generic;
import uk.co.moonsit.config.functions.Globals;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_072_001_ImageTargetRead {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        String file = Utils.getFileName("Robot", "ROS", "072-001-ImageTargetRead.xml");
        System.out.println(file);

        XML_072_001_ImageTargetRead xml = new XML_072_001_ImageTargetRead();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "25");

        String side = "Right";
        globals.put(side + "ROSTarget_Topic", "/robotview/" + side.toLowerCase() + "/cartcoords");

        Utils.saveToXML(xml.run(side), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    public XML_072_001_ImageTargetRead() {

    }

    public Layers run(String side) throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Target read");

        List layer = layers.getLayer();

        layer.add(configureLayer0("Target"));
        Generic.addBaxterVisionSensors(layers, side, false);

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

    /*
    public static Layers.Layer.Controller configureControllerTarget() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("Target");

        ControlFunction input = Utils.configureControlFunction("ROSTargetX", "", "ROSSubscriber",
                new String[][]{{"Topic", Globals.getInstance().get("ROSTarget_Topic"), "String"}, {"Variable", "X", "String"}}, null);
        Utils.addFunction(controller, input, Utils.INPUT, false);
        ControlFunction transfer = Utils.configureControlFunction("ROSTargetY", "", "ROSSubscriber",
                new String[][]{{"Topic", Globals.getInstance().get("ROSTarget_Topic"), "String"}, {"Variable", "Y", "String"}}, null);
        Utils.addTransferFunction(controller, transfer, Utils.INPUT);
        transfer = Utils.configureControlFunction("ROSTargetZ", "", "ROSSubscriber",
                new String[][]{{"Topic", Globals.getInstance().get("ROSTarget_Topic"), "String"}, {"Variable", "Z", "String"}}, null);
        Utils.addTransferFunction(controller, transfer, Utils.INPUT);

        return controller;
    }
     */
}
