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
package uk.co.moonsit.configurations.n002;

import java.util.List;
import pct.moons.co.uk.schema.layers.ControlFunction;
import pct.moons.co.uk.schema.layers.Layers;
import pct.moons.co.uk.schema.layers.Layers.Layer;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller;
import uk.co.moonsit.config.functions.Globals;
import uk.co.moonsit.config.functions.Modules;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_002_001_ReorganisationSimulationProp {

    private final boolean test = false;

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        String file = Utils.getFileName("Models", "002-001-ReorganisationSimulationProp.xml");
        XML_002_001_ReorganisationSimulationProp xml = new XML_002_001_ReorganisationSimulationProp();

        Globals globals = Globals.getInstance();
        globals.put("SmoothRate_Smoothness", "0.999");
        globals.put("SmoothRate_Initial", "0");
        globals.put("MeanRate_Initial", "0");
        globals.put("Pause_Pause", "20");

        globals.put("Sine_Frequency", "0.5");
        globals.put("Sine_Amplitude", "5.0");
        globals.put("Sine_Step", "1");
        globals.put("Sine_YShift", "0");

        globals.put("Step_Period", "100");
        globals.put("Step_Upper", "10");
        globals.put("Step_Lower", "-10");

        globals.put("Random_Scale", "100");
        globals.put("Random_Slow", "0.05");
        globals.put("Random_Initial", "100");
        globals.put("Random_Max", "200");
        globals.put("Random_Min", "0");

        globals.put("ControlSimulationOutput_Gain", ".2");
        globals.put("ControlSimulationOutput_Min", "-5000");
        globals.put("ControlSimulationOutput_Max", "5000");

        globals.put("RMSErrorResponse_Period", "100");
        globals.put("RMSErrorResponse_Limit", "100");

        globals.put("GainReorganisation_LearningRate", "0.0");
        //globals.put("GainReorganisation_Percentage", "0.5");
                globals.put("GainReorganisation_Type", "HillClimb");

        globals.put("Disturbance_Weights", "0,1,0");

        globals.put("World_Gain", "1");

        Utils.saveToXML(xml.run(), file);
        Utils.verify(file);

    }

    public XML_002_001_ReorganisationSimulationProp() {

    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Model");
        layers.setDescription("Basic control of value of sonic reading for obstacle avoidance, with flipping of rotation direction");
        List layer = layers.getLayer();

        layer.add(configureLayer0("Interface"));
        //layer.add(Utils.emptyLayer("ControlSimulation"));

        modules(layers, test);
        return layers;
    }

    public static void modules(Layers layers, boolean test) throws Exception {
        Modules.moduleControlReorganisationSimulationProp(layers);
    }

    public static Controller configureEnv() throws Exception {
        Controller env = configureControllerEnv();

        //ControlFunction function = Utils.stepDisturbanceFunction();
        ControlFunction function = Utils.randomFunction();
        Utils.addTransferFunction(env, function, Utils.INPUT);
        function = Utils.stepFunction();
        Utils.addTransferFunction(env, function, Utils.INPUT);
        function = Utils.sineFunction();
        Utils.addTransferFunction(env, function, Utils.INPUT);

        return env;
    }

    private Layer configureLayer0(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        controllers.add(configureEnv());

        return layer;
    }

    static public Layers.Layer.Controller configureControllerEnv() throws Exception {
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

            inputFunctions.setTransfers(transfers);
        }
        functions.setInputFunctions(inputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

}
