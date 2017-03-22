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
import uk.co.moonsit.config.functions.Globals;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_004_001_MountainCarModel {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        String file = Utils.getFileName("Models", "MountainCar", "004-001-MountainCarModel.xml");
        System.out.println(file);

        XML_004_001_MountainCarModel xml = new XML_004_001_MountainCarModel();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "20");
        globals.put("MountainCarModel_Initial", "-0.5");
        globals.put("MountainCarModel_Random", "0");
        globals.put("MountainCarModel_Factor", "0");
        globals.put("MountainCarModel_Seed", "1");

        globals.put("MountainCar_Action", "1");

        Utils.saveToXML(xml.run(), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    public XML_004_001_MountainCarModel() {
    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Model");
        layers.setDescription("MountainCar");

        layers.getLayer().add(configureLayer0("MountainCar"));

        return layers;
    }

    private static Layers.Layer configureLayer0(String name) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();
        controllers.add(configureControllerEnv());
        controllers.add(configureControllerModel());

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

                inputFunctions.setTransfers(transfers);
            }
            functions.setInputFunctions(inputFunctions);
        }

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerModel() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = "MountainCarModel";
        controller.setName(prefix);

        ControlFunction output = Utils.configureControlFunction("MountainCarPosition", "...", "uk.co.moons.control.neural.models.MountainCarPositionNeuralFunction",
                new String[][]{
                    {"Initial", Globals.getInstance().get(prefix + "_Initial"), "String"},
                    {"Random", Globals.getInstance().get(prefix + "_Random"), "String"},
                    {"Factor", Globals.getInstance().get(prefix + "_Factor"), "String"},
                    {"Seed", Globals.getInstance().get(prefix + "_Seed"), "String"}

                }, new String[][]{{"MountainCarAction"}});
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        ControlFunction transfer = Utils.configureControlFunction("MountainCarVelocity", "...", "uk.co.moons.control.neural.models.MountainCarVelocityNeuralFunction",
                null, null);
        Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);
        
        transfer = Utils.configureControlFunction("MountainCarHeight", "...", "uk.co.moons.control.neural.models.MountainCarHeightNeuralFunction",
                null, null);
        Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);

        transfer = Utils.configureControlFunction("MountainCarAction", "...", "Constant",
                new String[][]{{"Constant", Globals.getInstance().get("MountainCar_Action"), "Integer"}}, null);
        Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);

        return controller;
    }

}
