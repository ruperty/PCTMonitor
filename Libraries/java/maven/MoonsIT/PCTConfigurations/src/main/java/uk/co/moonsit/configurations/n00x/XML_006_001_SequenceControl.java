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
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_006_001_SequenceControl {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        String suffix = "006-001-SequenceControl";
        String type = "Models";
        String dir = "Sequences";
        String file = Utils.getFileName(type, dir, suffix + ".xml");
        System.out.println(file);

        XML_006_001_SequenceControl xml = new XML_006_001_SequenceControl();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "20");
        globals.put("Reset_Pulse", "0");

        //globals.put("OneControlInput_Smooth", "0.9");
        globals.put("OneControlOutput_Gain", "1000");
        globals.put("OneControlOutput_Slow", "10000");
        globals.put("OneControlOutput_Integrate", "true");
        globals.put("OneReferenceActive_Key", "1");
        globals.put("OneReference_Constant", "10");

        globals.put("TwoReference_Constant", "100");
        //globals.put("TwoControlInput_Smooth", "0.9");
        globals.put("TwoControlOutput_Gain", "100000");
        globals.put("TwoControlOutput_Slow", "1000000");
        globals.put("TwoControlOutput_Integrate", "true");
        globals.put("TwoReferenceActive_Key", "2");

        globals.put("ThreeReference_Constant", "1000");
        //globals.put("ThreeControlInput_Smooth", "0.9");
        globals.put("ThreeControlOutput_Gain", "9000");
        globals.put("ThreeControlOutput_Slow", "100000");
        globals.put("ThreeControlOutput_Integrate", "true");
        globals.put("ThreeReferenceActive_Key", "3");

        globals.put("SequenceControlInput_Tolerances", "0.01,0.001,0.112,");
        //globals.put("SequenceControlErrorFlipped_Offset", "1");
        globals.put("SequenceControlReference_Constant", "3");

        //globals.put("PointOne_Constant", "0.1");

        Utils.loadPars(Utils.getParametersFileName(type, dir, suffix));
        //globals.put("", "0.9");
        Utils.saveToXML(xml.run(), file, "../../Layers.xsd");
        Utils.verify(file).init();
    }

    public XML_006_001_SequenceControl() {
    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Model");
        layers.setDescription("Sequence test");
        Utils.setOrderedControllers(layers, new String[]{"Env", "SequenceControl", "OneControl", "TwoControl", "ThreeControl"});

        layers.getLayer().add(configureLayer0("Env"));
        layers.getLayer().add(configureLayer1("Sequence"));

        return layers;
    }

    private static Layers.Layer configureLayer0(String name) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();
        Controller env = configureControllerEnv();
        controllers.add(env);

        String prefix = "OneControl";
        Controller controller = Generic.configureIntegrationController(prefix, prefix + "Output", "OneReferenceActive");
        controllers.add(controller);

        prefix = "TwoControl";
        controller = Generic.configureIntegrationController(prefix, prefix + "Output", "TwoReferenceActive");
        controllers.add(controller);

        prefix = "ThreeControl";
        controller = Generic.configureIntegrationController(prefix, prefix + "Output", "ThreeReferenceActive");
        controllers.add(controller);

        return layer;
    }

    private static Layers.Layer.Controller configureControllerEnv() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("Env");

        ControlFunction input = Utils.configureControlFunction("Rate", "Iteration rate", "Rate", null, null);
        Utils.addFunction(controller, input, Utils.INPUT, false);

        {
            ControlFunction transfer = Utils.configureControlFunction("Pause", "Function for pausing processing to maintain constant iteration rate", "Pause",
                    new String[][]{{"Pause", Globals.getInstance().get("Pause_Pause"), "Long"}, {"ConstantRate", "true", ""}}, null);
            Utils.addTransferFunction(controller, transfer, Utils.INPUT);

            transfer = Utils.configureControlFunction("Zero", "...", "Constant",
                    new String[][]{{"Constant", "0", ""}}, null);
            Utils.addTransferFunction(controller, transfer, Utils.INPUT);

            transfer = Utils.configureControlFunction("Reset", "", "BinaryPulse",
                    new String[][]{{"Pulse", Globals.getInstance().get("Reset_Pulse"), "Integer"}}, null);
            Utils.addTransferFunction(controller, transfer, Utils.INPUT);
        }

        return controller;
    }

    private static Layers.Layer configureLayer1(String name) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();

        String prefix = "SequenceControl";
        String[][] inputLinks = new String[][]{{"OneControlError"}, {"TwoControlError"}, {"ThreeControlError"}, {"Reset", "Reset"}};
        ControlFunction reference = Utils.constantFunction(prefix + "Reference");

        String[] outputReferenceNames = new String[]{"OneReference", "TwoReference", "ThreeReference"};
        Controller sequence = Generic.configureSequenceController(prefix, inputLinks, reference, outputReferenceNames, new boolean[]{true, true, true});

        controllers.add(sequence);
        return layer;
    }

}
