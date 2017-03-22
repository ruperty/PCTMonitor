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
package uk.co.moonsit.configurations.n00x;

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
public class XML_001_018_Memory {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        String file = Utils.getFileName("Robot", "001-018-Memory.xml");
        System.out.println(file);

        XML_001_018_Memory xml = new XML_001_018_Memory();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "20");

        globals.put("GoalGenerator_Initial", "1000");
        globals.put("Data_Directory", "../data");

        String[] systems = new String[]{"HandElevation", "ArmReach", "HandPitch", "ShoulderYaw"};
        //String[] systems = new String[]{"HandElevation"};

        int gain = 10000;
        String side = "Left";
        for (String system : systems) {
            globals.put(side + system + "TestIntegratorOutput_Gain", String.valueOf(gain));
            globals.put(side + system + "TestIntegratorOutput_Slow", "100000");
            // globals.put(side + system + "MemoryPerceptionSmoothInput_Smooth", "0.9");
            globals.put(side + system + "MemoryPerception_Tolerance", "0.05");

            gain = gain + 5000;
        }
        gain = 10000;
        side = "Right";
        for (String system : systems) {
            globals.put(side + system + "TestIntegratorOutput_Gain", String.valueOf(gain));
            globals.put(side + system + "TestIntegratorOutput_Slow", "100000");
            //globals.put(side + system + "MemoryPerceptionSmoothInput_Smooth", "0.9");
            globals.put(side + system + "MemoryPerception_Tolerance", "0.05");

            gain = gain + 5000;
        }
        globals.put("GoalGeneratorName", "GoalGenerator");

        Utils.saveToXML(xml.run(systems), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    public XML_001_018_Memory() {
    }

    public Layers run(String[] systems) throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Memory test");

        layers.getLayer().add(configureLayer0("Env", systems));
        layers.getLayer().add(Utils.emptyLayer("MemoryLeft"));
        layers.getLayer().add(Utils.emptyLayer("MemoryRight"));

        String[] leftlinks = new String[systems.length];
        for (int i = 0; i < systems.length; i++) {
            leftlinks[i] = "Left" + systems[i] + "TestIntegratorInput";
        }
        String[] rightlinks = new String[systems.length];
        for (int i = 0; i < systems.length; i++) {
            rightlinks[i] = "Right" + systems[i] + "TestIntegratorInput";
        }
        Modules.baxterMemoryList(layers, 1, systems, leftlinks, rightlinks);

        return layers;
    }

    private static Layers.Layer configureLayer0(String name, String[] systems) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();
        Controller env = configureControllerEnv();
        controllers.add(env);

        String side = "Left";

        for (String system : systems) {
            controllers.add(Generic.configureTestIntegrator(side + system + "TestIntegrator", side + system + "MemoryPositionReference"));
        }
        side = "Right";
        for (String system : systems) {
            controllers.add(Generic.configureTestIntegrator(side + system + "TestIntegrator", side + system + "MemoryPositionReference"));
        }

        return layer;
    }

    private static Layers.Layer.Controller configureControllerEnv() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("Env");

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();
        {
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

                inputFunctions.setTransfers(transfers);
            }
            functions.setInputFunctions(inputFunctions);
        }

        controller.setFunctions(functions);
        return controller;
    }

}
