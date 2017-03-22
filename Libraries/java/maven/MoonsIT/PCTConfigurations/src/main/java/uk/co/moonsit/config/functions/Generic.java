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
package uk.co.moonsit.config.functions;

import uk.co.moonsit.config.functions.Utils;
import java.util.List;
import pct.moons.co.uk.schema.layers.ControlFunction;
import pct.moons.co.uk.schema.layers.Layers;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller;

/**
 *
 * @author Rupert Young
 */
public class Generic {

    public static final int P = 0;
    public static final int PV = 1;
    public static final int PVA = 2;

    public static Layers.Layer.Controller configureSequenceController(String prefix, String[][] inputLinks, ControlFunction reference, String[] outputTransfers, boolean[] createConstant) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(prefix);

        ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Sequence",
                new String[][]{{"Tolerances", Globals.getInstance().get(prefix + "Input_Tolerances"), "String"}},
                inputLinks);
        Utils.addFunction(controller, input, Utils.INPUT, false);

        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "SequenceOutput",
                null, null);
        //new String[][]{{prefix + "Reference"}, {prefix + "Error"}});
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        /*
        ControlFunction output = Utils.configureControlFunction(prefix + "ErrorFlipped", "...", "Subtract",
                new String[][]{{"Offset", Globals.getInstance().get(prefix + "ErrorFlipped_Offset"), ""}},
                new String[][]{{prefix + "Reference"}, {prefix + "Error"}});
        Utils.addFunction(controller, output, Utils.OUTPUT, false);
        {
            ControlFunction transfer = Utils.constantFunction("PointOne", false);
            Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);

            transfer = Utils.configureControlFunction(prefix + "ReferenceMax", "...", "Addition",
                    null, new String[][]{{"PointOne"}, {prefix + "Reference", "Max"}});
            Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);

            transfer = Utils.configureControlFunction(prefix + "Output", "...", "Limit",
                    null, new String[][]{{prefix + "ErrorFlipped"}, {prefix + "ReferenceMax", "Max"}});
            Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);


        }*/
        ControlFunction transfer;
        int ctr = 0;
        for (String tname : outputTransfers) {
            String[][] links = new String[][]{{tname}, {prefix + "Output", "Key"}};
            if (createConstant[ctr]) {
                transfer = Utils.constantFunction(tname);
                Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);
            }
            transfer = Utils.valueActivateFunction(tname + "Active", links);
            Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);
            ctr++;
        }

        return controller;
    }

    public static Layers.Layer.Controller configureProportionalController(String prefix, ControlFunction input, ControlFunction reference) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(prefix);

        Utils.addFunction(controller, input, Utils.INPUT, false);

        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Proportional",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"}
                }, null);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    public static Layers.Layer.Controller configureProportionalController(String prefix, String inputLink, ControlFunction reference) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(prefix);

        ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "", null,
                new String[][]{{inputLink}});
        Utils.addFunction(controller, input, Utils.INPUT, false);

        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Proportional",
                new String[][]{{"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"}}, null);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    public static Layers.Layer.Controller configureProportionalController(String prefix, String[][] inputLinks, String referenceLink) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(prefix);

        ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smoothness"), "String"}},
                inputLinks);
        Utils.addFunction(controller, input, Utils.INPUT, false);

        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "", null,
                new String[][]{{referenceLink}});
        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Proportional",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"}
                }, null);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    public static Layers.Layer.Controller configureProportionalController(String prefix, String inputLink, String referenceLink) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(prefix);

        ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "String"}},
                new String[][]{{inputLink}});
        Utils.addFunction(controller, input, Utils.INPUT, false);

        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "", null,
                new String[][]{{referenceLink}});
        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Proportional",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"}
                }, null);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    public static Layers.Layer.Controller configureProportionalController(String prefix, ControlFunction input, String referenceLink) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(prefix);

        Utils.addFunction(controller, input, Utils.INPUT, false);

        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "", null,
                new String[][]{{referenceLink}});
        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Proportional",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"}
                }, null);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    public static Layers.Layer.Controller configureSigmoidController(String prefix, String inputLink, ControlFunction reference, boolean display) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(prefix);

        ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "", null,
                new String[][]{{inputLink}});
        Utils.addFunction(controller, input, Utils.INPUT, false);

        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.sigmoidFunction(prefix + "Output", prefix + "Error", "false", display);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    public static Layers.Layer.Controller configureSigmoidController(String prefix, String inputLink, String referenceLink, boolean display) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(prefix);

        ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "", null,
                new String[][]{{inputLink}});
        Utils.addFunction(controller, input, Utils.INPUT, false);

        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "", null,
                new String[][]{{referenceLink}});
        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.sigmoidFunction(prefix + "Output", prefix + "Error", "false", display);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    public static Layers.Layer.Controller configureSigmoidController(String prefix, ControlFunction input, ControlFunction reference, boolean display) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(prefix);

        Utils.addFunction(controller, input, Utils.INPUT, false);

        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.sigmoidFunction(prefix + "Output", prefix + "Error", "false", display);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    public static Layers.Layer.Controller configureIntegrationController(String prefix, ControlFunction input, ControlFunction reference, String[][] outputLinks) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(prefix);

        Utils.addFunction(controller, input, Utils.INPUT, false);

        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract", null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Integration",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"},
                    {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "String"},
                    {"Tolerance", Globals.getInstance().get(prefix + "Output_Tolerance"), "String"},
                    {"Integrate", Globals.getInstance().get(prefix + "Output_Integrate"), ""}
                }, outputLinks);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    public static Layers.Layer.Controller configureIntegrationController(String prefix, ControlFunction input, String[][] referenceLinks) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(prefix);

        Utils.addFunction(controller, input, Utils.INPUT, false);

        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "", null, referenceLinks);
        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract", null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Integration",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"},
                    {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "String"},
                    {"Tolerance", Globals.getInstance().get(prefix + "Output_Tolerance"), "String"},
                    {"Integrate", Globals.getInstance().get(prefix + "Output_Integrate"), "Boolean"}
                }, null);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    public static Layers.Layer.Controller configureIntegrationController(String prefix, String inputLink, ControlFunction reference, String[][] outputLinks) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(prefix);

        ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "Double"}},
                new String[][]{{inputLink}});
        Utils.addFunction(controller, input, Utils.INPUT, false);

        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Integration",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"},
                    {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "String"},
                    {"Tolerance", Globals.getInstance().get(prefix + "Output_Tolerance"), "String"},
                    {"Integrate", Globals.getInstance().get(prefix + "Output_Integrate"), "Boolean"}
                }, outputLinks);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    public static Layers.Layer.Controller configureIntegrationController(String prefix, String[][] inputLinks, String referenceLink, String[][] outputLinks, boolean infinity) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(prefix);

        ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "Double"}},
                inputLinks);
        Utils.addFunction(controller, input, Utils.INPUT, false);

        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "", null,
                new String[][]{{referenceLink}});
        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        String[][] pars;
        if (infinity) {
            pars = new String[][]{
                {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"},
                {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "String"},
                {"Tolerance", Globals.getInstance().get(prefix + "Output_Tolerance"), "String"},
                {"Integrate", Globals.getInstance().get(prefix + "Output_Integrate"), "Boolean"},
                {"EnableInfinity", "true", "Boolean"}};
        } else {
            pars = new String[][]{
                {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"},
                {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "String"},
                {"Tolerance", Globals.getInstance().get(prefix + "Output_Tolerance"), "String"},
                {"Integrate", Globals.getInstance().get(prefix + "Output_Integrate"), "Boolean"}};
        }

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Integration",
                pars, outputLinks);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    public static Layers.Layer.Controller configureIntegrationController(String prefix, String[][] inputLinks, String referenceLink, String[][] outputLinks) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(prefix);

        ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smoothness"), "Double"}},
                inputLinks);
        Utils.addFunction(controller, input, Utils.INPUT, false);

        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "", null,
                new String[][]{{referenceLink}});
        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Integration",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"},
                    {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "String"},
                    {"Tolerance", Globals.getInstance().get(prefix + "Output_Tolerance"), "String"},
                    {"Integrate", Globals.getInstance().get(prefix + "Output_Integrate"), "Boolean"}
                }, outputLinks);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    public static Layers.Layer.Controller configureIntegrationController(String prefix, String inputLink, String referenceLink, String[][] outputLinks) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(prefix);

        ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "Double"}},
                new String[][]{{inputLink}});
        Utils.addFunction(controller, input, Utils.INPUT, false);

        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "", null,
                new String[][]{{referenceLink}});
        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Integration",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"},
                    {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "String"},
                    {"Tolerance", Globals.getInstance().get(prefix + "Output_Tolerance"), "String"},
                    {"Integrate", Globals.getInstance().get(prefix + "Output_Integrate"), "Boolean"}
                }, outputLinks);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    public static Layers.Layer.Controller configureIntegrationController(String prefix, ControlFunction input, String referenceLink, String[][] outputLinks) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(prefix);

        Utils.addFunction(controller, input, Utils.INPUT, false);

        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "", null,
                new String[][]{{referenceLink}});
        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Integration",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"},
                    {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "String"},
                    {"Tolerance", Globals.getInstance().get(prefix + "Output_Tolerance"), "String"},
                    {"Integrate", Globals.getInstance().get(prefix + "Output_Integrate"), "Boolean"}
                }, outputLinks);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    public static Layers.Layer.Controller configureIntegrationController(String prefix, ControlFunction input, String referenceLink) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(prefix);

        Utils.addFunction(controller, input, Utils.INPUT, false);

        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "", null,
                new String[][]{{referenceLink}});
        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Integration",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"},
                    {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "String"},
                    {"Integrate", Globals.getInstance().get(prefix + "Output_Integrate"), "Boolean"}
                }, null);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    public static Layers.Layer.Controller configureIntegrationController(String prefix, String inputLink, String referenceLink) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(prefix);

        ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "", null,
                new String[][]{{inputLink}});
        Utils.addFunction(controller, input, Utils.INPUT, false);

        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "", null,
                new String[][]{{referenceLink}});
        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Integration",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"},
                    {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "String"},
                    {"Integrate", Globals.getInstance().get(prefix + "Output_Integrate"), "Boolean"}
                }, null);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    public static Layers.Layer.Controller configureIntegrationController(String prefix, String inputLink, ControlFunction reference) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(prefix);

        ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "", null,
                new String[][]{{inputLink}});
        Utils.addFunction(controller, input, Utils.INPUT, false);

        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Integration",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"},
                    {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "String"},
                    {"Integrate", Globals.getInstance().get(prefix + "Output_Integrate"), "Boolean"}
                }, null);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    public static Layers.Layer.Controller configureGripperController(String side, String[][] referenceLinks) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + "GripperControl";
        controller.setName(prefix);

        {
            ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "",
                    null,
                    new String[][]{{prefix + "OutputLimited"}});
            Utils.addFunction(controller, input, Utils.INPUT, false);

        }

        {
            ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "DigitalLimit",
                    new String[][]{{"Threshold", "5", "Double"}, {"Upper", "1", "Double"}, {"Lower", "0", "Double"}},
                    new String[][]{{prefix + "ReferenceSmooth"}});
            Utils.addFunction(controller, reference, Utils.REFERENCE, false);

            ControlFunction transfer = Utils.configureControlFunction(prefix + "ReferenceWeighted", "...", "WeightedSum",
                    new String[][]{{"Absolute", "true", "Boolean"}, {"Weights", Globals.getInstance().get(prefix + "Reference_Weights"), ""}},
                    referenceLinks);
            Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);

            transfer = Utils.configureControlFunction(prefix + "ReferenceSmooth", "...", "Smooth",
                    new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Reference_Smooth"), "Double"}},
                    new String[][]{{prefix + "ReferenceWeighted"}});
            Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);
        }

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                new String[][]{{"Sign", "1", "Integer"}}, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);
        {
            ControlFunction output = Utils.configureControlFunction(prefix + "Habituate", "...", "Habituate",
                    //ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Proportional",
                    new String[][]{
                        {"Rate", Globals.getInstance().get(prefix + "Output_Rate"), "Double"},
                        {"Proportional", Globals.getInstance().get(prefix + "Output_Proportional"), "Boolean"}}, null);
            Utils.addFunction(controller, output, Utils.OUTPUT, false);

            ControlFunction transfer = Utils.configureControlFunction(prefix + "OutputLimited", "...", "DigitalLimit",
                    new String[][]{{"Threshold", "0.11", "Double"}, {"Upper", "1", "Double"},
                    {"Lower", "0", "Double"}, {"Initial", "1", "Double"}},
                    new String[][]{{prefix + "Habituate"}});
            Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);

            transfer = Utils.configureControlFunction(prefix + "Output", "...", "ROSGripperPublisher",
                    new String[][]{{"Topic", Globals.getInstance().get(side + "ROSGripper_Topic"), ""}},
                    new String[][]{{prefix + "OutputLimited"}});
            Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);

        }
        return controller;
    }

    public static Layers.Layer.Controller configureArmReachRelativeController(String side, String system, String inputLink, String referenceLink, boolean integration) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + system + "Control";
        controller.setName(prefix);

        ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "Double"}},
                new String[][]{{inputLink}});
        Utils.addFunction(controller, input, Utils.INPUT, false);

        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "",
                null, new String[][]{{referenceLink}});
        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        String[][] parsShoulder = null;
        String[][] parsElbow = null;
        String type = null;
        if (integration) {
            type = "Integration";
            parsShoulder = new String[][]{
                {"Gain", Globals.getInstance().get(prefix + "OutputToShoulder_Gain"), "Double"},
                {"Slow", Globals.getInstance().get(prefix + "OutputToShoulder_Slow"), "Double"},
                {"Min", Globals.getInstance().get(prefix + "OutputToShoulder_Min"), "String"},
                {"Max", Globals.getInstance().get(prefix + "OutputToShoulder_Max"), "String"},
                {"Initial", Globals.getInstance().get(prefix + "OutputToShoulder_Initial"), ""}};
            parsElbow = new String[][]{{"Gain", Globals.getInstance().get(prefix + "OutputToElbow_Gain"), "Double"},
            {"Slow", Globals.getInstance().get(prefix + "OutputToElbow_Slow"), "Double"},
            {"Min", Globals.getInstance().get(prefix + "OutputToElbow_Min"), "String"},
            {"Max", Globals.getInstance().get(prefix + "OutputToElbow_Max"), "String"},
            {"Initial", Globals.getInstance().get(prefix + "OutputToElbow_Initial"), ""}};
        } else {
            type = "Proportional";
            parsShoulder = new String[][]{
                {"Gain", Globals.getInstance().get(prefix + "OutputToShoulder_Gain"), "Double"}};
            parsElbow = new String[][]{
                {"Gain", Globals.getInstance().get(prefix + "OutputToElbow_Gain"), "Double"}};
        }
        ControlFunction output = Utils.configureControlFunction(prefix + "OutputToShoulder", "...", type,
                parsShoulder, null);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        ControlFunction transfer = Utils.configureControlFunction(prefix + "OutputToElbow", "...", type,
                parsElbow, new String[][]{{prefix + "Error"}});

        Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);
        return controller;
    }

    public static Layers.Layer.Controller configureRelativeHandController(String side, String system, String inputLink, String referenceLink) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + system + "Control";
        controller.setName(prefix);

        ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "Double"}},
                new String[][]{{inputLink}});
        Utils.addFunction(controller, input, Utils.INPUT, false);

        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "",
                null, new String[][]{{referenceLink}});
        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Integration",
                //ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Proportional",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"},
                    {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "String"},
                    {"Min", Globals.getInstance().get(prefix + "Output_Min"), "String"},
                    {"Max", Globals.getInstance().get(prefix + "Output_Max"), "String"},
                    {"Tolerance", Globals.getInstance().get(prefix + "_Tolerance"), "Double"},
                    {"Initial", Globals.getInstance().get(prefix + "Output_Initial"), ""}

                }, null);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    public static String[] getAllJoints() {
        String[] joints = {"ShoulderYawS0", "ShoulderPitchS1", "ElbowRollE0", "ElbowAngleE1", "WristRollW0", "HandAngleW1", "HandRollW2"};
        return joints;
    }

    public static int getJointIndex(String joint, String[] joints) {
        int index = -1;
        for (int i = 0; i < joints.length; i++) {
            if (joints[i].equals(joint)) {
                index = i;
                break;
            }
        }
        return index;

    }

    public static String getJointCode(String joint) {
        String jcode = null;

        switch (joint) {
            case "ShoulderYawS0":
                jcode = "s0";
                break;
            case "ShoulderPitchS1":
                jcode = "s1";
                break;
            case "ElbowRollE0":
                jcode = "e0";
                break;
            case "ElbowAngleE1":
                jcode = "e1";
                break;
            case "WristRollW0":
                jcode = "w0";
                break;
            case "HandAngleW1":
                jcode = "w1";
                break;
            case "HandRollW2":
                jcode = "w2";
                break;
        }
        return jcode;
    }

    public static void addBaxterVisionSensors(Layers layers, String side, boolean ir) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(side + "Vision");
        Utils.addController(layers, 0, controller);

        ControlFunction input = Utils.configureControlFunction(side + "ROSTargetX", "", "ROSSubscriber",
                new String[][]{{"Topic", Globals.getInstance().get(side + "ROSTarget_Topic"), ""}, {"Variable", "X", ""}}, null);
        Utils.addFunction(controller, input, Utils.INPUT, false);
        ControlFunction transfer = Utils.configureControlFunction(side + "ROSTargetY", "", "ROSSubscriber",
                new String[][]{{"Topic", Globals.getInstance().get(side + "ROSTarget_Topic"), ""}, {"Variable", "Y", ""}}, null);
        Utils.addTransferFunction(controller, transfer, Utils.INPUT);
        transfer = Utils.configureControlFunction(side + "ROSTargetZ", "", "ROSSubscriber",
                new String[][]{{"Topic", Globals.getInstance().get(side + "ROSTarget_Topic"), ""}, {"Variable", "Z", ""}}, null);
        Utils.addTransferFunction(controller, transfer, Utils.INPUT);
        if (ir) {
            transfer = Utils.configureControlFunction(side + "IRRange", "", "ROSSubscriber",
                    new String[][]{{"Topic", Globals.getInstance().get(side + "ROSIR_Topic"), ""}}, null);
            Utils.addTransferFunction(controller, transfer, Utils.INPUT);
        }
    }

    public static void addPVTSensorsActuators(Layers layers, String side, String joints[], String referenceType) throws Exception {

        Controller controller = Utils.emptyController("SensorsActuators", true, false, false, true);
        Utils.addController(layers, 0, controller);
        int i = 0;
        for (String joint : joints) {
            String variable = "Position";

            ControlFunction function = Utils.configureControlFunction(side + joint + variable + "Input", "...", "ROSSubscriber",
                    new String[][]{
                        {"Index", side.toLowerCase() + "_" + Globals.getInstance().get(joint + "_Joint"), ""},
                        {"Variable", variable, ""}}, null);
            Utils.addFunction(controller, function, Utils.INPUT, i > 0);

            variable = "Velocity";
            function = Utils.configureControlFunction(side + joint + variable + "Input", "...", "ROSSubscriber",
                    new String[][]{
                        {"Index", side.toLowerCase() + "_" + Globals.getInstance().get(joint + "_Joint"), ""},
                        {"Variable", variable, ""}}, null);

            Utils.addFunction(controller, function, Utils.INPUT, true);

            variable = "Effort";
            function = Utils.configureControlFunction(side + joint + variable + "Input", "...", "ROSSubscriber",
                    new String[][]{
                        {"Index", side.toLowerCase() + "_" + Globals.getInstance().get(joint + "_Joint"), ""},
                        {"Variable", variable, ""}}, null);

            Utils.addFunction(controller, function, Utils.INPUT, true);

            function = Utils.configureBaxterPublisher(side + joint + "TorqueOutput",
                    "...",
                    side.toLowerCase() + "_" + Globals.getInstance().get(joint + "_Joint"),
                    Globals.getInstance().get(side + "JointPublish_Topic"),
                    "Torque",
                    i + 1 == joints.length ? "true" : "false",
                    Globals.getInstance().get(side + joint + "TorquePublish_Tolerance"),
                    null,
                    Globals.getInstance().get(side + joint + "TorquePublish_Initial"),
                    null,
                    new String[][]{{side + joint + referenceType + "ControlOutput"}});
            Utils.addFunction(controller, function, Utils.OUTPUT, i > 0);

            i++;
        }

    }

    public static Layers.Layer.Controller configureMemoryPerception(String side, String[] systems, String[] links) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("MemoryPerception" + side);

        {

            {
                int i = 0;
                String name = side + systems[i] + "MemoryPerception";
                ControlFunction reference = Utils.configureControlFunction(name, "...", "MemoryKeyIncrement",
                        new String[][]{
                            {"System", side, ""},
                            {"Mode", systems[i], ""},
                            {"Parameter", "Position", ""},
                            {"Tolerance", Globals.getInstance().get(name + "_Tolerance"), "Double"},
                            {"Initial", Globals.getInstance().get("GoalGenerator_Initial"), ""},
                            {"Directory", Globals.getInstance().get("Data_Directory"), ""}
                        }, new String[][]{{links[i]}, {Globals.getInstance().get("GoalGeneratorName")}});
                Utils.addFunction(controller, reference, Utils.REFERENCE, false);

                for (i = 1; i < systems.length; i++) {
                    name = side + systems[i] + "MemoryPerception";
                    ControlFunction transfer = Utils.configureControlFunction(name, "...", "MemoryKeyIncrement",
                            new String[][]{
                                {"System", side, ""},
                                {"Mode", systems[i], ""},
                                {"Parameter", "Position", ""},
                                {"Tolerance", Globals.getInstance().get(name + "_Tolerance"), "Double"},
                                {"Initial", Globals.getInstance().get("GoalGenerator_Initial"), ""},
                                {"Directory", Globals.getInstance().get("Data_Directory"), ""}
                            }, new String[][]{{links[i]}, {Globals.getInstance().get("GoalGeneratorName")}});
                    Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);
                }
            }
        }

        return controller;
    }

    public static Layers.Layer.Controller configureMemoryReference(String gname, String side, String[] systems, String type) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("MemoryReference" + type + side);

        {
            int i = 0;
            String name = side + systems[i] + "Memory" + type + "Reference";
            ControlFunction output = Utils.configureControlFunction(name, "...", "MemoryValueLookup",
                    new String[][]{
                        {"System", side, ""},
                        {"Mode", systems[i], ""},
                        {"Parameter", type, ""},
                        {"Initial", Globals.getInstance().get("GoalGenerator_Initial"), ""},
                        {"Directory", Globals.getInstance().get("Data_Directory"), ""}
                    }, new String[][]{{gname}});
            Utils.addFunction(controller, output, Utils.REFERENCE, false);

            for (i = 1; i < systems.length; i++) {

                name = side + systems[i] + "Memory" + type + "Reference";
                ControlFunction transfer = Utils.configureControlFunction(name, "...", "MemoryValueLookup",
                        new String[][]{
                            {"System", side, ""},
                            {"Mode", systems[i], ""},
                            {"Parameter", type, ""},
                            {"Initial", Globals.getInstance().get("GoalGenerator_Initial"), ""},
                            {"Directory", Globals.getInstance().get("Data_Directory"), ""}
                        }, new String[][]{{gname}});
                Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);
            }
        }

        return controller;
    }

    static public Layers.Layer.Controller configurePVPositionController(String side, String joint, String referenceLink) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + joint + "PositionControl";
        controller.setName(prefix);

        ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "Double"}},
                new String[][]{{side + joint + "PositionInput"}});

        Utils.addFunction(controller, input, Utils.INPUT, false);

        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "",
                null, new String[][]{{referenceLink}});
        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Sigmoid",
                new String[][]{
                    {"YShift", "0", ""},
                    {"InScale", Globals.getInstance().get(prefix + "Output_InScale"), "Double"},
                    {"OutScale", Globals.getInstance().get(prefix + "Output_OutScale"), "Double"}}, null);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    static public Layers.Layer.Controller configurePVVelocityController(String side, String joint, String[][] referenceLinks, boolean integration) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + joint + "VelocityControl";
        controller.setName(prefix);

        ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "Double"}},
                new String[][]{{side + joint + "VelocityInput"}});
        Utils.addFunction(controller, input, Utils.INPUT, false);

        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "Product",
                null, referenceLinks);
        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        String type = null;
        String[][] pars = null;
        if (integration) {
            type = "Integration";
            pars = new String[][]{
                {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"},
                {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "String"},
                {"Min", Globals.getInstance().get(prefix + "Output_Min"), "String"},
                {"Max", Globals.getInstance().get(prefix + "Output_Max"), "String"}
            };
        } else {
            type = "Proportional";
            pars = new String[][]{
                {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"}};
        }

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", type,
                pars, null);

        Utils.addFunction(controller, output, Utils.OUTPUT,
                false);

        return controller;
    }

    static public Layers.Layer.Controller configurePVAVelocityController(String side, String joint, String[][] referenceLinks) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + joint + "VelocityControl";
        controller.setName(prefix);

        ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "", null,
                new String[][]{{side + joint + "VelocityInput"}});
        Utils.addFunction(controller, input, Utils.INPUT, false);

        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "Product",
                null, referenceLinks);
        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Proportional",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"}}, null);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    static public Layers.Layer.Controller configurePVAAccelerationController(String side, String joint, String referenceLink) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + joint + "AccelerationControl";
        controller.setName(prefix);

        ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "TimeChange", null,
                new String[][]{{side + joint + "VelocityControlInput"}, {"Rate"}});
        Utils.addFunction(controller, input, Utils.INPUT, false);

        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "",
                null, new String[][]{{referenceLink}});
        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        //ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Proportional",
        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Integration",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"},
                    {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "String"},
                    {"Min", Globals.getInstance().get(prefix + "Output_Min"), "String"},
                    {"Max", Globals.getInstance().get(prefix + "Output_Max"), "String"}
                }, null);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    static public Layers.Layer.Controller configureTestIntegrator(String prefix, String referenceLink) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(prefix);

        ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "", null,
                new String[][]{{prefix + "Output"}});
        Utils.addFunction(controller, input, Utils.INPUT, false);

        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "",
                null, new String[][]{{referenceLink}});
        Utils.addFunction(controller, reference, Utils.REFERENCE, false);

        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        Utils.addFunction(controller, error, Utils.ERROR, false);

        ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Integration",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"},
                    {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "String"}},
                null);
        Utils.addFunction(controller, output, Utils.OUTPUT, false);

        return controller;
    }

    static public Layers.Layer.Controller configureArmPositionVelocityController(String name, String side, String joint,
            String publish, String tolerance, String initial, String last, String[][] referenceLinks, boolean pubPosition) throws Exception {

        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(side + name + "PositionControl");
        String prefix = side + name + "PositionControl";

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                    new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "Double"}},
                    new String[][]{{prefix + "Subscriber"}});

            inputFunctions.setInput(input);

            {
                Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction(prefix + "Subscriber", "", "ROSSubscriber",
                        new String[][]{
                            {"Index", side.toLowerCase() + "_" + joint, ""},
                            {"Variable", "Position", ""}}, null);
                transfersList.add(transfer);
                inputFunctions.setTransfers(transfers);

            }
            functions.setInputFunctions(inputFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
            ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "", "WeightedSum",
                    new String[][]{{"Weights", "0,1", "String"}},
                    new String[][]{{prefix + "ManualReference"}, {prefix + "References"}});
            referenceFunctions.setReference(reference);
            {
                Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction(prefix + "ManualReference", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get(prefix + "ManualReference_Constant"), "Double"}}, null);
                transfersList.add(transfer);
                transfer = Utils.configureControlFunction(prefix + "References", "...", "WeightedSum",
                        new String[][]{{"Weights", Globals.getInstance().get(prefix + "References_Weights"), "String"}},
                        referenceLinks);
                transfersList.add(transfer);

                referenceFunctions.setTransfers(transfers);

            }
            functions.setReferenceFunctions(referenceFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            {
                Layers.Layer.Controller.Functions.ErrorFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ErrorFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer;

                transfer = Utils.configureControlFunction(prefix + "ErrorFiltered", "error", "ToleranceFilter",
                        new String[][]{{"Tolerance", Globals.getInstance().get(prefix + "ErrorFiltered_Tolerance"), "Double"}},
                        new String[][]{{prefix + "Error"}});

                transfersList.add(transfer);
                errorFunctions.setTransfers(transfers);

            }

            functions.setErrorFunctions(errorFunctions);
        }
        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Sigmoid",
                    new String[][]{
                        {"YShift", "0", "Double"},
                        {"InScale", Globals.getInstance().get(prefix + "Output_InScale"), "Double"},
                        {"OutScale", Globals.getInstance().get(prefix + "Output_OutScale"), "Double"}},
                    new String[][]{{prefix + "ErrorFiltered"}});

            {
                Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer;
                if (!pubPosition) {
                    transfer = Utils.configureBaxterPublisher(prefix + "VelocityPublisher",
                            "...",
                            side.toLowerCase() + "_" + joint,
                            Globals.getInstance().get(side + "JointPublish_Topic"),
                            "Velocity", publish,
                            tolerance,
                            Globals.getInstance().get(prefix + "JointVelocityPublish_Disabled"),
                            initial,
                            null,
                            new String[][]{{prefix + "Output"}});

                    /*transfer = Utils.configureControlFunction(prefix + "VelocityPublisher", "...", "ROSBaxterJointPublisher",
                            new String[][]{
                                {"Joint", side.toLowerCase() + "_" + joint, "String"},
                                {"Topic", Globals.getInstance().get(side + "JointPublish_Topic"), "String"},
                                {"Mode", "Velocity", "String"},
                                {"Disabled", Globals.getInstance().get(prefix + "JointVelocityPublish_Disabled"), "String"}
                            },
                            new String[][]{{prefix + "Output"}});
                     */
                    transfersList.add(transfer);
                } else {
                    transfer = Utils.configureBaxterPublisher(prefix + "PositionPublisher",
                            "...",
                            side.toLowerCase() + "_" + joint,
                            Globals.getInstance().get(side + "JointPublish_Topic"),
                            "Position",
                            publish,
                            tolerance,
                            Globals.getInstance().get(prefix + "JointVelocityPublish_Disabled"),
                            initial,
                            last,
                            new String[][]{{prefix + "Reference"}});

                    /*transfer = Utils.configureControlFunction(prefix + "PositionPublisher", "...", "ROSBaxterJointPublisher",
                            new String[][]{
                                {"Joint", side.toLowerCase() + "_" + joint, "String"},
                                {"Topic", Globals.getInstance().get(side + "JointPublish_Topic"), "String"},
                                {"Mode", "Position", "String"},
                                {"Disabled", Globals.getInstance().get(prefix + "JointPositionPublish_Disabled"), "String"}
                            },
                            new String[][]{{prefix + "Reference"}});
                     */
                    transfersList.add(transfer);
                }
                outputFunctions.setTransfers(transfers);

            }
            outputFunctions.setOutput(output);
            functions.setOutputFunctions(outputFunctions);
        }
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureMovementPositionControllerDirectNamed(String name, String side, String inputLink, String referenceLink) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + name + "MovementPositionControl";
        controller.setName(prefix);

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "", null, new String[][]{{inputLink}});
            functions.setInputFunctions(Utils.inputFunctions(input));
        }
        {
            ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "", null, new String[][]{{referenceLink}});
            functions.setReferenceFunctions(Utils.referenceFunctions(reference));
        }
        functions.setErrorFunctions(Utils.errorFunctions(prefix));

        {
            ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "",
                    null, null);
            functions.setOutputFunctions(Utils.outputFunctions(output));
        }
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureMovementPositionControllerDirect(String name, String side, String inputLink, ControlFunction reference) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + name + "MovementPositionControl";
        controller.setName(prefix);

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "", null, new String[][]{{inputLink}});
            functions.setInputFunctions(Utils.inputFunctions(input));
        }

        functions.setReferenceFunctions(Utils.referenceFunctions(reference));
        functions.setErrorFunctions(Utils.errorFunctions(prefix));

        {
            ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "WeightedMultiply",
                    new String[][]{{"Weights", Globals.getInstance().get(side + name + "MovementPositionControlOutput_Weights"), ""}},
                    new String[][]{{side + name + "MovementPositionControlError"}, {side + name + "MovementPositionWeight"}, {"GlobalSpeedWeight"}});
            functions.setOutputFunctions(Utils.outputFunctions(output));
        }
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureMovementPositionController(String name, String side, String inputLink, ControlFunction reference) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + name + "MovementPositionControl";
        controller.setName(prefix);

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "", null, new String[][]{{inputLink}});
            functions.setInputFunctions(Utils.inputFunctions(input));
        }

        functions.setReferenceFunctions(Utils.referenceFunctions(reference));
        functions.setErrorFunctions(Utils.errorFunctions(prefix));

        {
            ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Sigmoid",
                    new String[][]{
                        {"YShift", "0", ""},
                        {"InScale", Globals.getInstance().get(prefix + "Output_InScale"), "Double"},
                        {"OutScale", Globals.getInstance().get(prefix + "Output_OutScale"), "Double"}}, null);
            functions.setOutputFunctions(Utils.outputFunctions(output));
        }
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureMovementVelocityController(String name, String side, String inputLink, ControlFunction reference) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + name + "MovementVelocityControl";
        controller.setName(prefix);

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "TimeChange",
                    null, new String[][]{{inputLink}, {"Rate"}});
            functions.setInputFunctions(Utils.inputFunctions(input));
        }

        functions.setReferenceFunctions(Utils.referenceFunctions(reference));
        functions.setErrorFunctions(Utils.errorFunctions(prefix));

        {
            ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "",
                    null, null);
            functions.setOutputFunctions(Utils.outputFunctions(output));
        }
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configurePController(String side, String joint, String referenceLink) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + joint + "PositionControl";
        controller.setName(prefix);
        {
            ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                    new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "Double"}},
                    new String[][]{{side + joint + "ControlInput"}});
            Utils.addFunction(controller, input, Utils.INPUT, false);
        }
        {
            ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "", "WeightedSum",
                    new String[][]{{"Weights", "0,1", "String"}},
                    new String[][]{{prefix + "ManualReference"}, {prefix + "References"}});
            Utils.addFunction(controller, reference, Utils.REFERENCE, false);

            ControlFunction transfer = Utils.configureControlFunction(prefix + "References", "...", "WeightedSum",
                    new String[][]{{"Weights", Globals.getInstance().get(prefix + "Reference_Weights"), "String"}},
                    new String[][]{{referenceLink}, {prefix + "ManualReference"}});

            Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);
        }
        {
            ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                    null, null);
            Utils.addFunction(controller, error, Utils.ERROR, false);
        }
        {
            //ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Integration",
            ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Proportional",
                    new String[][]{
                        {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"}/*,
                        {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "String"},
                        {"Min", Globals.getInstance().get(prefix + "Output_Min"), "String"},
                        {"Max", Globals.getInstance().get(prefix + "Output_Max"), "String"}*/
                    },
                    null);

            Utils.addFunction(controller, output, Utils.OUTPUT, false);
            /*
            ControlFunction transfer = Utils.configureControlFunction(prefix + "Publisher", "...", "ROSBaxterJointPublisher",
                    new String[][]{
                        {"Joint", side.toLowerCase() + "_" + joint, "String"},
                        {"Topic", Globals.getInstance().get(side + "PositionControlOutput_Topic"), "String"},
                        {"Mode", "Position", "String"}
                    },
                    new String[][]{{prefix + "Output"}});
            Utils.addTransferFunction(controller, transfer, Utils.INPUT);
             */
        }
        return controller;
    }

    static public Layers.Layer.Controller configureArmPositionController(String name, String side, String joint, String[][] referenceLinks) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(side + name + "PositionControl");
        String prefix = side + name + "PositionControl";

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                    new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "Double"}},
                    new String[][]{{prefix + "Subscriber"}});

            inputFunctions.setInput(input);

            {
                Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction(prefix + "Subscriber", "", "ROSSubscriber",
                        new String[][]{
                            {"Index", side.toLowerCase() + "_" + joint, ""},
                            {"Variable", "Position", ""}}, null);
                transfersList.add(transfer);
                inputFunctions.setTransfers(transfers);

            }
            functions.setInputFunctions(inputFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
            ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "", "WeightedSum",
                    new String[][]{{"Weights", "0,1", "String"}},
                    new String[][]{{prefix + "ManualReference"}, {prefix + "References"}});
            referenceFunctions.setReference(reference);
            {
                Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction(prefix + "ManualReference", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get(prefix + "ManualReference_Constant"), "Double"}}, null);
                transfersList.add(transfer);
                transfer = Utils.configureControlFunction(prefix + "References", "...", "WeightedSum",
                        new String[][]{{"Weights", Globals.getInstance().get(prefix + "Reference_Weights"), "String"}},
                        referenceLinks);
                transfersList.add(transfer);

                referenceFunctions.setTransfers(transfers);

            }
            functions.setReferenceFunctions(referenceFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            functions.setErrorFunctions(errorFunctions);
        }
        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Integration",
                    new String[][]{
                        {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"},
                        {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "String"},
                        {"Min", Globals.getInstance().get(prefix + "Output_Min"), "String"},
                        {"Max", Globals.getInstance().get(prefix + "Output_Max"), "String"}},
                    null);

            {
                Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction(prefix + "Publisher", "...", "ROSBaxterJointPublisher",
                        new String[][]{
                            {"Joint", side.toLowerCase() + "_" + joint, "String"},
                            {"Topic", Globals.getInstance().get(side + "PositionControlOutput_Topic"), "String"},
                            {"Mode", "Position", "String"}
                        },
                        new String[][]{{prefix + "Output"}});

                transfersList.add(transfer);

                outputFunctions.setTransfers(transfers);

            }

            outputFunctions.setOutput(output);

            functions.setOutputFunctions(outputFunctions);
        }
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureHandPitchController(String name, String side, ControlFunction[] references) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + name;

        controller.setName(prefix);

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                    new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "Double"}},
                    new String[][]{{prefix + "Diff"}});

            inputFunctions.setInput(input);

            {
                Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction(prefix + "Diff", "", "Subtract",
                        null, new String[][]{{side + "EndpointSubQW"}, {side + "EndpointSubQY"}});

                transfersList.add(transfer);
                inputFunctions.setTransfers(transfers);
            }

            functions.setInputFunctions(inputFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();

            referenceFunctions.setReference(references[0]);
            {
                Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                transfersList.add(references[1]);
                referenceFunctions.setTransfers(transfers);
            }

            functions.setReferenceFunctions(referenceFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            functions.setErrorFunctions(errorFunctions);
        }
        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Integration",
                    new String[][]{
                        {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "Double"},
                        {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "Double"},
                        {"Min", Globals.getInstance().get(prefix + "Output_Min"), "String"},
                        {"Max", Globals.getInstance().get(prefix + "Output_Max"), "String"},
                        {"Initial", Globals.getInstance().get(prefix + "Output_Initial"), "Double"}

                    }, null);
            outputFunctions.setOutput(output);

            functions.setOutputFunctions(outputFunctions);
        }
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureHandPitchControllerMemory(String name, String side, ControlFunction reference) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + name;

        controller.setName(prefix);

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                    new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "Double"}},
                    new String[][]{{prefix + "Diff"}});

            inputFunctions.setInput(input);

            {
                Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction(prefix + "Diff", "", "Subtract",
                        null, new String[][]{{side + "EndpointSubQW"}, {side + "EndpointSubQY"}});

                transfersList.add(transfer);
                inputFunctions.setTransfers(transfers);
            }

            functions.setInputFunctions(inputFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();

            referenceFunctions.setReference(reference);

            functions.setReferenceFunctions(referenceFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            {
                Layers.Layer.Controller.Functions.ErrorFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ErrorFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction(prefix + "ErrorThreshold", "...", "Threshold",
                        new String[][]{{"LessThan", "true", "Boolean"},
                        {"Threshold", Globals.getInstance().get(prefix + "ErrorThreshold_Threshold"), "Double"}

                        }, new String[][]{{prefix + "Error"}});

                transfersList.add(transfer);
                errorFunctions.setTransfers(transfers);
            }
            functions.setErrorFunctions(errorFunctions);
        }
        {
            functions.setOutputFunctions(getMemoryOutputFunction(prefix));

        }
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureShoulderYawS0ControllerMemory(String name, String side, ControlFunction reference) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + name;

        controller.setName(prefix);

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                    new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "Double"}},
                    new String[][]{{side + "ShoulderYawS0AngleControlInput"}});

            inputFunctions.setInput(input);
            functions.setInputFunctions(inputFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();

            referenceFunctions.setReference(reference);

            functions.setReferenceFunctions(referenceFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            {
                Layers.Layer.Controller.Functions.ErrorFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ErrorFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction(prefix + "ErrorThreshold", "...", "Threshold",
                        new String[][]{{"LessThan", "true", "Boolean"},
                        {"Threshold", Globals.getInstance().get(prefix + "ErrorThreshold_Threshold"), "Double"}

                        }, new String[][]{{prefix + "Error"}});

                transfersList.add(transfer);
                errorFunctions.setTransfers(transfers);
            }
            functions.setErrorFunctions(errorFunctions);
        }
        {
            functions.setOutputFunctions(getMemoryOutputFunction(prefix));
        }
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller.Functions.OutputFunctions getMemoryOutputFunction(String prefix) throws Exception {

        String link = prefix.substring(0, prefix.indexOf("Control")) + "MemoryWeightReference";

        Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
        ControlFunction output = Utils.configureControlFunction(prefix + "PositionWeighted", "...", "WeightedMultiply",
                new String[][]{{"Weights", Globals.getInstance().get(prefix + "PositionWeighted_Weights"), "Double"}},
                new String[][]{{prefix + "ErrorThreshold"}, {link}, {"GlobalSpeedWeight"}});
        outputFunctions.setOutput(output);

        {
            Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer = Utils.configureControlFunction(prefix + "Output", "...", "Integration",
                    new String[][]{
                        {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "Double"},
                        {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "Double"},
                        {"Min", Globals.getInstance().get(prefix + "Output_Min"), "String"},
                        {"Max", Globals.getInstance().get(prefix + "Output_Max"), "String"},
                        {"Initial", Globals.getInstance().get(prefix + "Output_Initial"), "Double"}

                    }, new String[][]{{prefix + "PositionWeighted"}});

            transfersList.add(transfer);
            outputFunctions.setTransfers(transfers);

        }

        return outputFunctions;
    }

    static public Layers.Layer.Controller configureHandPitchController(String name, String side, ControlFunction reference) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + name;

        controller.setName(prefix);

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                    new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "Double"}},
                    new String[][]{{prefix + "Diff"}});

            inputFunctions.setInput(input);

            {
                Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction(prefix + "Diff", "", "Subtract",
                        null, new String[][]{{side + "EndpointSubQW"}, {side + "EndpointSubQY"}});

                transfersList.add(transfer);
                inputFunctions.setTransfers(transfers);
            }

            functions.setInputFunctions(inputFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();

            referenceFunctions.setReference(reference);

            functions.setReferenceFunctions(referenceFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            functions.setErrorFunctions(errorFunctions);
        }
        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Integration",
                    new String[][]{
                        {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "Double"},
                        {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "Double"},
                        {"Min", Globals.getInstance().get(prefix + "Output_Min"), "String"},
                        {"Max", Globals.getInstance().get(prefix + "Output_Max"), "String"},
                        {"Initial", Globals.getInstance().get(prefix + "Output_Initial"), "Double"}

                    }, null);
            outputFunctions.setOutput(output);

            functions.setOutputFunctions(outputFunctions);
        }
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureHandElevationController(String name, String side, ControlFunction[] references) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + name;
        controller.setName(prefix);

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                    new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "Double"}},
                    new String[][]{{side + "EndpointSubZ"}});

            inputFunctions.setInput(input);

            functions.setInputFunctions(inputFunctions);
        }

        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();

            referenceFunctions.setReference(references[0]);
            {
                Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                transfersList.add(references[1]);
                referenceFunctions.setTransfers(transfers);
            }

            functions.setReferenceFunctions(referenceFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            functions.setErrorFunctions(errorFunctions);
        }
        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Integration",
                    new String[][]{
                        {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "Double"},
                        {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "Double"},
                        {"Min", Globals.getInstance().get(prefix + "Output_Min"), "Double"},
                        {"Max", Globals.getInstance().get(prefix + "Output_Max"), "Double"},
                        {"Initial", Globals.getInstance().get(prefix + "Output_Initial"), ""}
                    }, null);
            outputFunctions.setOutput(output);

            functions.setOutputFunctions(outputFunctions);
        }
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureHandElevationControllerMemory(String name, String side, ControlFunction reference) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + name;
        controller.setName(prefix);

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                    new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "Double"}},
                    new String[][]{{side + "EndpointSubZ"}});

            inputFunctions.setInput(input);

            functions.setInputFunctions(inputFunctions);
        }

        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();

            referenceFunctions.setReference(reference);

            functions.setReferenceFunctions(referenceFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            {
                Layers.Layer.Controller.Functions.ErrorFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ErrorFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction(prefix + "ErrorThreshold", "...", "Threshold",
                        new String[][]{{"LessThan", "true", "Boolean"},
                        {"Threshold", Globals.getInstance().get(prefix + "ErrorThreshold_Threshold"), "Double"}

                        }, new String[][]{{prefix + "Error"}});

                transfersList.add(transfer);
                errorFunctions.setTransfers(transfers);
            }
            functions.setErrorFunctions(errorFunctions);
        }
        {
            functions.setOutputFunctions(getMemoryOutputFunction(prefix));

        }
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureHandElevationController(String name, String side, ControlFunction reference) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + name;
        controller.setName(prefix);

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                    new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "Double"}},
                    new String[][]{{side + "EndpointSubZ"}});

            inputFunctions.setInput(input);

            functions.setInputFunctions(inputFunctions);
        }

        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();

            referenceFunctions.setReference(reference);

            functions.setReferenceFunctions(referenceFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            functions.setErrorFunctions(errorFunctions);
        }
        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Integration",
                    new String[][]{
                        {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "Double"},
                        {"Slow", Globals.getInstance().get(prefix + "Output_Slow"), "Double"},
                        {"Min", Globals.getInstance().get(prefix + "Output_Min"), "Double"},
                        {"Max", Globals.getInstance().get(prefix + "Output_Max"), "Double"},
                        {"Initial", Globals.getInstance().get(prefix + "Output_Initial"), ""}
                    }, null);
            outputFunctions.setOutput(output);

            functions.setOutputFunctions(outputFunctions);
        }
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureArmReachHypotenuseController(String name, String side, ControlFunction references[]) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + name;
        controller.setName(prefix);
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                    new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "Double"}},
                    new String[][]{{prefix + "Hypotenuse"}});

            {
                Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction(prefix + "Hypotenuse", "", "Hypotenuse",
                        null, new String[][]{{side + "EndpointSubX"}, {side + "EndpointSubY"}, {side + "EndpointSubZ"}});

                transfersList.add(transfer);
                inputFunctions.setTransfers(transfers);
            }

            inputFunctions.setInput(input);
            functions.setInputFunctions(inputFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();

            referenceFunctions.setReference(references[0]);
            {
                Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                transfersList.add(references[1]);
                referenceFunctions.setTransfers(transfers);
            }
            functions.setReferenceFunctions(referenceFunctions);
            //Utils.addTransferFunction(controller, references[1], Utils.REFERENCE);

        }
        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            functions.setErrorFunctions(errorFunctions);
        }
        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction(prefix + "OutputToShoulder", "...", "Integration",
                    new String[][]{{"Gain", Globals.getInstance().get(prefix + "OutputToShoulder_Gain"), "Double"},
                    {"Slow", Globals.getInstance().get(prefix + "OutputToShoulder_Slow"), "Double"},
                    {"Min", Globals.getInstance().get(prefix + "OutputToShoulder_Min"), "String"},
                    {"Max", Globals.getInstance().get(prefix + "OutputToShoulder_Max"), "String"},
                    {"Initial", Globals.getInstance().get(prefix + "OutputToShoulder_Initial"), ""}
                    }, null);
            outputFunctions.setOutput(output);
            {
                Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction(prefix + "OutputToElbow", "...", "Integration",
                        new String[][]{{"Gain", Globals.getInstance().get(prefix + "OutputToElbow_Gain"), "Double"},
                        {"Slow", Globals.getInstance().get(prefix + "OutputToElbow_Slow"), "Double"},
                        {"Min", Globals.getInstance().get(prefix + "OutputToElbow_Min"), ""},
                        {"Max", Globals.getInstance().get(prefix + "OutputToElbow_Max"), ""},
                        {"Initial", Globals.getInstance().get(prefix + "OutputToElbow_Initial"), ""}
                        }, new String[][]{{prefix + "Error"}});

                transfersList.add(transfer);

                outputFunctions.setTransfers(transfers);

            }
            functions.setOutputFunctions(outputFunctions);
        }
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureArmReachHypotenuseControllerMemory(String name, String side, ControlFunction reference) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + name;
        controller.setName(prefix);
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                    new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "Double"}},
                    new String[][]{{prefix + "Hypotenuse"}});

            {
                Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction(prefix + "Hypotenuse", "", "Hypotenuse",
                        null, new String[][]{{side + "EndpointSubX"}, {side + "EndpointSubY"}, {side + "EndpointSubZ"}});

                transfersList.add(transfer);
                inputFunctions.setTransfers(transfers);
            }

            inputFunctions.setInput(input);
            functions.setInputFunctions(inputFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();

            referenceFunctions.setReference(reference);

            functions.setReferenceFunctions(referenceFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            {
                Layers.Layer.Controller.Functions.ErrorFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ErrorFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction(prefix + "ErrorThreshold", "...", "Threshold",
                        new String[][]{{"LessThan", "true", "Boolean"},
                        {"Threshold", Globals.getInstance().get(prefix + "ErrorThreshold_Threshold"), "Double"}

                        }, new String[][]{{prefix + "Error"}});

                transfersList.add(transfer);
                errorFunctions.setTransfers(transfers);
            }

            functions.setErrorFunctions(errorFunctions);
        }
        {
            String link = prefix.substring(0, prefix.indexOf("Control")) + "MemoryWeightReference";

            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction(prefix + "PositionWeighted", "...", "WeightedMultiply",
                    new String[][]{{"Weights", Globals.getInstance().get(prefix + "PositionWeighted_Weights"), "Double"}},
                    new String[][]{{prefix + "ErrorThreshold"}, {link}, {"GlobalSpeedWeight"}});
            outputFunctions.setOutput(output);
            {
                Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction(prefix + "OutputToElbow", "...", "Integration",
                        new String[][]{{"Gain", Globals.getInstance().get(prefix + "OutputToElbow_Gain"), "Double"},
                        {"Slow", Globals.getInstance().get(prefix + "OutputToElbow_Slow"), "Double"},
                        {"Min", Globals.getInstance().get(prefix + "OutputToElbow_Min"), "Double"},
                        {"Max", Globals.getInstance().get(prefix + "OutputToElbow_Max"), "Double"},
                        {"Initial", Globals.getInstance().get(prefix + "OutputToElbow_Initial"), ""}
                        }, new String[][]{{prefix + "PositionWeighted"}});

                transfersList.add(transfer);

                transfer = Utils.configureControlFunction(prefix + "OutputToShoulder", "...", "Integration",
                        new String[][]{{"Gain", Globals.getInstance().get(prefix + "OutputToShoulder_Gain"), "Double"},
                        {"Slow", Globals.getInstance().get(prefix + "OutputToShoulder_Slow"), "Double"},
                        {"Min", Globals.getInstance().get(prefix + "OutputToShoulder_Min"), "String"},
                        {"Max", Globals.getInstance().get(prefix + "OutputToShoulder_Max"), "String"},
                        {"Initial", Globals.getInstance().get(prefix + "OutputToShoulder_Initial"), ""}
                        }, new String[][]{{prefix + "PositionWeighted"}});
                transfersList.add(transfer);
                outputFunctions.setTransfers(transfers);

            }
            functions.setOutputFunctions(outputFunctions);
        }
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureArmReachHypotenuseController(String name, String side, ControlFunction reference) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = side + name;
        controller.setName(prefix);
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction(prefix + "Input", "...", "Smooth",
                    new String[][]{{"Smoothness", Globals.getInstance().get(prefix + "Input_Smooth"), "Double"}},
                    new String[][]{{prefix + "Hypotenuse"}});

            {
                Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction(prefix + "Hypotenuse", "", "Hypotenuse",
                        null, new String[][]{{side + "EndpointSubX"}, {side + "EndpointSubY"}, {side + "EndpointSubZ"}});

                transfersList.add(transfer);
                inputFunctions.setTransfers(transfers);
            }

            inputFunctions.setInput(input);
            functions.setInputFunctions(inputFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();

            referenceFunctions.setReference(reference);

            functions.setReferenceFunctions(referenceFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            functions.setErrorFunctions(errorFunctions);
        }
        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction(prefix + "OutputToShoulder", "...", "Integration",
                    new String[][]{{"Gain", Globals.getInstance().get(prefix + "OutputToShoulder_Gain"), "Double"},
                    {"Slow", Globals.getInstance().get(prefix + "OutputToShoulder_Slow"), "Double"},
                    {"Min", Globals.getInstance().get(prefix + "OutputToShoulder_Min"), "String"},
                    {"Max", Globals.getInstance().get(prefix + "OutputToShoulder_Max"), "String"},
                    {"Initial", Globals.getInstance().get(prefix + "OutputToShoulder_Initial"), ""}
                    }, null);
            outputFunctions.setOutput(output);
            {
                Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction(prefix + "OutputToElbow", "...", "Integration",
                        new String[][]{{"Gain", Globals.getInstance().get(prefix + "OutputToElbow_Gain"), "Double"},
                        {"Slow", Globals.getInstance().get(prefix + "OutputToElbow_Slow"), "Double"},
                        {"Min", Globals.getInstance().get(prefix + "OutputToElbow_Min"), "Double"},
                        {"Max", Globals.getInstance().get(prefix + "OutputToElbow_Max"), "Double"},
                        {"Initial", Globals.getInstance().get(prefix + "OutputToElbow_Initial"), ""}
                        }, new String[][]{{prefix + "Error"}});

                transfersList.add(transfer);

                outputFunctions.setTransfers(transfers);

            }
            functions.setOutputFunctions(outputFunctions);
        }
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureArmPositionWithVelocityController(String name, String side, String joint, String[][] referenceLinks) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(side + name + "PositionControl");
        String prefix = side + name + "PositionControl";

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction(prefix + "Input", "", "ROSSubscriber",
                    new String[][]{
                        {"Index", side.toLowerCase() + "_" + joint, ""},
                        {"Variable", "Position", ""}}, null);

            inputFunctions.setInput(input);
            functions.setInputFunctions(inputFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
            ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "", "WeightedSum",
                    new String[][]{{"Weights", "0,1", "String"}},
                    new String[][]{{prefix + "ManualReference"}, {prefix + "References"}});
            referenceFunctions.setReference(reference);
            {
                Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction(prefix + "ManualReference", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get(prefix + "ManualReference_Constant"), "Double"}}, null);
                transfersList.add(transfer);
                transfer = Utils.configureControlFunction(prefix + "References", "...", "WeightedSum",
                        new String[][]{{"Weights", Globals.getInstance().get(prefix + "Reference_Weights"), "String"}},
                        referenceLinks);
                transfersList.add(transfer);

                referenceFunctions.setTransfers(transfers);

            }
            functions.setReferenceFunctions(referenceFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            functions.setErrorFunctions(errorFunctions);
        }
        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "Proportional",
                    new String[][]{
                        {"Gain", Globals.getInstance().get(prefix + "Output_Gain"), "String"},
                        {"Min", Globals.getInstance().get(prefix + "Output_Min"), "String"},
                        {"Max", Globals.getInstance().get(prefix + "Output_Max"), "String"}
                    },
                    null);
            outputFunctions.setOutput(output);

            functions.setOutputFunctions(outputFunctions);
        }
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureArmVelocityController(String name, String side, String joint) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(side + name + "VelocityControl");
        String prefix = side + name + "VelocityControl";

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction(prefix + "Input", "", "ROSSubscriber",
                    new String[][]{
                        {"Index", side.toLowerCase() + "_" + joint, ""},
                        {"Variable", "Velocity", ""}}, null);

            inputFunctions.setInput(input);
            functions.setInputFunctions(inputFunctions);
        }

        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
            ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "",
                    null, new String[][]{{side + name + "PositionControlOutput"}});
            referenceFunctions.setReference(reference);
            functions.setReferenceFunctions(referenceFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            functions.setErrorFunctions(errorFunctions);
        }
        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction(prefix + "Output", "...", "LimitBanded",
                    new String[][]{
                        {"Threshold", "0", "Double"},
                        {"Upper", Globals.getInstance().get(prefix + "Output_Upper"), "Double"},
                        {"Lower", Globals.getInstance().get(prefix + "Output_Lower"), "Double"},
                        {"BandWidth", Globals.getInstance().get(prefix + "Output_BandWidth"), "Double"},
                        {"BandValue", Globals.getInstance().get(prefix + "Output_BandValue"), "Double"}},
                    null);
            outputFunctions.setOutput(output);
            {
                Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction(prefix + "Publisher", "...", "ROSBaxterJointPublisher",
                        new String[][]{
                            {"Joint", side.toLowerCase() + "_" + joint, "String"},
                            {"Topic", Globals.getInstance().get(side + "JointPublish_Topic"), "String"},
                            {"Mode", "Velocity", "String"}
                        },
                        new String[][]{{prefix + "Output"}});

                transfersList.add(transfer);

                outputFunctions.setTransfers(transfers);

            }
            functions.setOutputFunctions(outputFunctions);
        }

        controller.setFunctions(functions);
        return controller;
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

            //transfer = Utils.configureControlFunction("Memory", "Used RAM", "Memory",                    new String[][]{{"Type", "USED", ""}}, null);
            //transfersList.add(transfer);
            transfer = Utils.configureControlFunction("MeanRate", "Mean of iteration rate", "Rate",
                    new String[][]{{"Mean", "true", "Boolean"}, {"Initial", Globals.getInstance().get("MeanRate_Initial"), "Double"}}, null);
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("SmoothRate", "Moving average of iteration rate", "Rate",
                    new String[][]{{"Smoothness", Globals.getInstance().get("SmoothRate_Smoothness"), "Double"},
                    {"Initial", Globals.getInstance().get("SmoothRate_Initial"), "Double"}}, null);
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("Pause", "Function for pausing processing to maintain constant iteration rate", "Pause",
                    new String[][]{{"Pause", Globals.getInstance().get("Pause_Pause"), "Long"}, {"ConstantRate", "true", "Boolean"}}, null);
            transfersList.add(transfer);

            inputFunctions.setTransfers(transfers);
        }
        functions.setInputFunctions(inputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller getPhoneAccelerometerSensor(boolean test) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("PhoneAccelerometerSensor");
        String type = "WifiPhoneAccelerometerSensor";
        //if (test) {            type = "";        }
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = Utils.configureControlFunction("PhoneAccelerometerX", "Value of X Accelerometer sensor ", type,
                new String[][]{{"Port", Globals.getInstance().get("PhoneAccelerometerSensor_Port"), "Integer"}, {"Axis", "X", "String"},
                {"Initial", "Infinity", "Double"}}, null);
        inputFunctions.setInput(input);

        Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
        List<ControlFunction> transfersList = transfers.getTransfer();

        ControlFunction transfer = Utils.configureControlFunction("PhoneAccelerometerY", "Value of Y Accelerometer sensor ", type,
                new String[][]{{"Port", Globals.getInstance().get("PhoneAccelerometerSensor_Port"), ""}, {"Axis", "Y", "String"},
                {"Initial", "Infinity", "Double"}}, null);
        transfersList.add(transfer);
        transfer = Utils.configureControlFunction("PhoneAccelerometerZ", "Value of Z Accelerometer sensor ", type,
                new String[][]{{"Port", Globals.getInstance().get("PhoneAccelerometerSensor_Port"), ""}, {"Axis", "Z", "String"},
                {"Initial", "Infinity", "Double"}}, null);
        transfersList.add(transfer);
        inputFunctions.setTransfers(transfers);

        functions.setInputFunctions(inputFunctions);
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller getAccelerometerSensor(boolean test, boolean smooth) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("AccelerometerSensor");
        String type = Globals.getInstance().get("AccelerometerSensor_Type");
        if (test) {
            type = "Constant";
        }
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = Utils.configureControlFunction("AccelerometerX", "Value of X Accelerometer sensor ", type,
                new String[][]{{"SensorPort", Globals.getInstance().get("AccelerometerSensor_Port"), "Integer"},
                {"Mode", "Acceleration", "String"},
                {"Axis", "X", "String"},
                {"Interval", Globals.getInstance().get("Accelerometer_Interval"), "Double"},
                {"Threaded", Globals.getInstance().get("Accelerometer_Threaded"), "Boolean"},
                {"Constant", "0", "Double"}},
                null);
        inputFunctions.setInput(input);
        {
            Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();

            ControlFunction transfer = Utils.configureControlFunction("AccelerometerY", "Value of Y Accelerometer sensor ", type,
                    new String[][]{{"SensorPort", Globals.getInstance().get("AccelerometerSensor_Port"), ""},
                    {"Mode", "Acceleration", "String"},
                    {"Axis", "Y", "String"},
                    {"Threaded", Globals.getInstance().get("Accelerometer_Threaded"), "Boolean"},
                    {"Constant", "0", "Double"}}, null);
            transfersList.add(transfer);
            transfer = Utils.configureControlFunction("AccelerometerZ", "Value of Z Accelerometer sensor ", type,
                    new String[][]{{"SensorPort", Globals.getInstance().get("AccelerometerSensor_Port"), ""},
                    {"Mode", "Acceleration", "String"},
                    {"Axis", "Z", "String"},
                    {"Threaded", Globals.getInstance().get("Accelerometer_Threaded"), "Boolean"},
                    {"Constant", "0", "Double"}}, null);
            transfersList.add(transfer);

            if (smooth) {
                transfer = Utils.configureControlFunction("AccelerometerXSmooth", "...", "Smooth",
                        new String[][]{{"Smoothness", Globals.getInstance().get("Accelerometer_Smooth"), "Double"}}, new String[][]{{"AccelerometerX"}});
                transfersList.add(transfer);
                transfer = Utils.configureControlFunction("AccelerometerYSmooth", "...", "Smooth",
                        new String[][]{{"Smoothness", "0.9", "Double"}}, new String[][]{{"AccelerometerY"}});
                transfersList.add(transfer);
                transfer = Utils.configureControlFunction("AccelerometerZSmooth", "...", "Smooth",
                        new String[][]{{"Smoothness", "0.9", "Double"}}, new String[][]{{"AccelerometerZ"}});
                transfersList.add(transfer);
            }
            inputFunctions.setTransfers(transfers);
        }
        functions.setInputFunctions(inputFunctions);
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureSensorIR() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("IRSensor");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = Utils.configureControlFunction("IRSensorDistance", "Value of IR sensor distance", "EV3IRSensor",
                new String[][]{{"SensorPort", Globals.getInstance().get("IRSensor_Port"), ""}, {"Mode", "Seek", "String"}, {"Type", "Distance", "String"}, {"Channel", Globals.getInstance().get("IRSensor_Channel"), "Integer"}}, null);
        inputFunctions.setInput(input);

        Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
        List<ControlFunction> transfersList = transfers.getTransfer();

        ControlFunction transfer = Utils.configureControlFunction("IRSensorDirection", "Value of IR sensor direction", "EV3IRSensor",
                new String[][]{{"SensorPort", Globals.getInstance().get("IRSensor_Port"), ""}, {"Mode", "Seek", "String"}, {"Type", "Direction", "String"}, {"Channel", Globals.getInstance().get("IRSensor_Channel"), "Integer"}}, null);
        transfersList.add(transfer);
        inputFunctions.setTransfers(transfers);

        functions.setInputFunctions(inputFunctions);
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureSensors(boolean test, String[] sensors) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("Sensors");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = getSensorType(test, sensors[0]);
        inputFunctions.setInput(input);
        if (sensors.length > 1) {
            Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();

            for (int i = 1; i < sensors.length; i++) {
                ControlFunction transfer = getSensorType(test, sensors[i]);
                transfersList.add(transfer);
            }
            inputFunctions.setTransfers(transfers);
        }

        functions.setInputFunctions(inputFunctions);
        controller.setFunctions(functions);
        return controller;
    }

    static public ControlFunction getSensorType(boolean test, String type) throws Exception {
        ControlFunction sensor = null;
        if (type.contains("CompassSensor")) {
            sensor = getCompassSensor(test);
        }
        if (type.contains("LatitudeSensor")) {
            sensor = getLatitudeSensor();
        }
        if (type.contains("LongitudeSensor")) {
            sensor = getLongitudeSensor();
        }
        if (!test) {
            if (type.contains("ProximitySensor")) {
                sensor = getSensorProximityRaw();
            }
        }

        return sensor;
    }

    static public Layers.Layer.Controller configureControllerBearingMemoryConflict() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("BearingMemoryConflict");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = Functions.configureInputFunction(Functions.bearingMemoryFunctions());
        functions.setInputFunctions(inputFunctions);

        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction("BearingMemoryConflictReference",
                "Zero reference", "Constant",
                new String[][]{{"Constant", "0", "Double"}}, null);
        referenceFunctions.setReference(reference);
        functions.setReferenceFunctions(referenceFunctions);

        Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction("BearingMemoryConflictError", "error",
                "Subtract", new String[][]{{"Sign", "-1", "Double"}}, null);
        errorFunctions.setError(error);
        functions.setErrorFunctions(errorFunctions);

        Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("BearingMemoryConflictOutput", "Memory value or infinity", "",
                null, null);

        outputFunctions.setOutput(output);
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureSensorXBee(boolean test) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("XBeeSensor");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = Utils.configureControlFunction("XBeeZeroTransition", "...", "ZeroTransition",
                null, new String[][]{{"XBeeSensorTimeChange"}});

        {
            Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer = getXBeeSensor(test);

            transfersList.add(transfer);
            transfer = Utils.configureControlFunction("XBeeSensorSmooth", "Smooth value of xbee", "Smooth",
                    new String[][]{{"Smoothness", Globals.getInstance().get("XBee_Smoothness"), "Double"}}, new String[][]{{"XBeeRead"}});
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("XBeeSensorTimeChange", "...", "Change",
                    null, new String[][]{{"XBeeSensorSmooth"}, {"SmoothRate"}});
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("XBeeSensorTimeChangeSmooth", "Smooth of Xbee signal change", "Smooth",
                    new String[][]{{"Smoothness", Globals.getInstance().get("XBeeSensorTimeChangeSmooth_Smoothness"), "Double"}},
                    new String[][]{{"XBeeSensorTimeChange"}});
            transfersList.add(transfer);

            inputFunctions.setTransfers(transfers);
        }
        inputFunctions.setInput(input);

        functions.setInputFunctions(inputFunctions);
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureSensorCompass(boolean test) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("CompassSensor");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = getCompassSensor(test);

        inputFunctions.setInput(input);

        functions.setInputFunctions(inputFunctions);
        controller.setFunctions(functions);
        return controller;
    }

    static public ControlFunction getXBeeSensor(boolean test) throws Exception {
        ControlFunction function;
        if (test) {
            function = Utils.configureControlFunction("XBeeRead", "Value of xbee bearing", "Constant",
                    new String[][]{{"Constant", "0", "Double"}}, null);
        } else {
            function = Utils.configureControlFunction("XBeeRead", "Value of compass bearing", "XBeeSensor",
                    new String[][]{{"SensorPort", Globals.getInstance().get("XBeeSensor_Port"), ""}, {"Interval", Globals.getInstance().get("XBeeSensor_Interval"), "Long"}}, null);
        }
        return function;
    }

    static public ControlFunction getCompassSensor(boolean test) throws Exception {
        ControlFunction function;
        if (test) {
            function = Utils.configureControlFunction("CompassRead", "Value of compass bearing", "Constant",
                    new String[][]{{"Constant", "0", "Double"}}, null);
        } else {
            function = Utils.configureControlFunction("CompassRead", "Value of compass bearing", Globals.getInstance().get("CompassSensor_Type"),
                    new String[][]{{"SensorPort", Globals.getInstance().get("CompassSensor_Port"), ""},
                    {"AngleType", Globals.getInstance().get("CompassSensor_AngleType"), "String"},
                    {"Interval", Globals.getInstance().get("CompassSensor_Interval"), "Integer"}}, null);
        }
        return function;
    }

    static public Layers.Layer.Controller configureSensorLatitude() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("LatitudeSensor");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = getLatitudeSensor();
        inputFunctions.setInput(input);

        functions.setInputFunctions(inputFunctions);
        controller.setFunctions(functions);
        return controller;
    }

    static public ControlFunction getLatitudeSensor() throws Exception {
        ControlFunction function;
        function = Utils.configureControlFunction("LatitudeSensor", "Value of current latitude", "WifiPhoneGPSSensor",
                new String[][]{{"Port", Globals.getInstance().get("LatitudeSensor_Port"), "Integer"}, {"Coordinate", "Latitude", "String"},}, null);
        return function;
    }

    static public ControlFunction getLatitudeReference() throws Exception {
        ControlFunction function;
        function = Utils.configureControlFunction("LatitudePhoneReference", "Value of reference latitude", "WifiPhoneGPSReference",
                new String[][]{{"Port", Globals.getInstance().get("LatitudePhoneReference_Port"), "Integer"},
                {"Coordinate", "Latitude", "String"},
                {"Initial", Globals.getInstance().get("LatitudePhoneReference_Initial"), "Double"}}, null);
        return function;
    }

    static public Layers.Layer.Controller configureSensorLongitude() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("LongitudeSensor");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = getLongitudeSensor();
        inputFunctions.setInput(input);

        functions.setInputFunctions(inputFunctions);
        controller.setFunctions(functions);
        return controller;
    }

    static public ControlFunction getLongitudeSensor() throws Exception {
        ControlFunction function;
        function = Utils.configureControlFunction("LongitudeSensor", "Value of current latitude", "WifiPhoneGPSSensor",
                new String[][]{{"Port", Globals.getInstance().get("LongitudeSensor_Port"), "Integer"}, {"Coordinate", "Longitude", "String"},}, null);
        return function;
    }

    static public ControlFunction getLongitudeReference() throws Exception {
        ControlFunction function;
        function = Utils.configureControlFunction("LongitudePhoneReference", "Value of current latitude", "WifiPhoneGPSReference",
                new String[][]{{"Port", Globals.getInstance().get("LongitudePhoneReference_Port"), "Integer"},
                {"Coordinate", "Longitude", "String"},
                {"Initial", Globals.getInstance().get("LongitudePhoneReference_Initial"), "Double"}}, null);
        return function;
    }

    public static Layers.Layer.Controller configureControllerHeadBody() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("RelativeHeadBodyOrientation");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = Utils.configureControlFunction("RelativeHeadBodyOrientationInput", "Control of relationship of head to body", "",
                null, new String[][]{{"HeadPositionInput"}});
        inputFunctions.setInput(input);
        functions.setInputFunctions(inputFunctions);

        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction("RelativeHeadBodyOrientationReference", "Desired relationship, of zero", "Constant",
                new String[][]{{"Constant", "0", "Double"}}, null);
        referenceFunctions.setReference(reference);
        functions.setReferenceFunctions(referenceFunctions);

        Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction("RelativeHeadBodyOrientationError", "Relationship error", "Subtract",
                null, null);
        errorFunctions.setError(error);
        functions.setErrorFunctions(errorFunctions);

        Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("RelativeHeadBodyOrientationOutput", "Proportional output", "Proportional",
                new String[][]{{"Gain", Globals.getInstance().get("RelativeHeadBodyOrientationOutput_Gain"), "Double"}}, null);
        outputFunctions.setOutput(output);
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerMotorRotationalDifferenceControl() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("MotorRotationalDifferenceControl");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = Utils.configureControlFunction("MotorRotationalDifferenceControlInput", "Difference bewtween the two wheels", "Subtract",
                null, new String[][]{{"MotorCPosition"}, {"MotorBPosition"}});
        inputFunctions.setInput(input);
        functions.setInputFunctions(inputFunctions);

        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction("MotorRotationalDifferenceControlReference", "Desired difference", "TimeIntegral",
                new String[][]{{"Gain", "1", "Double"}}, new String[][]{{"RelativeHeadBodyOrientationOutput"}, {"Rate"}});
        referenceFunctions.setReference(reference);
        functions.setReferenceFunctions(referenceFunctions);

        Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction("MotorRotationalDifferenceControlError", "Difference error", "Subtract",
                null, null);
        errorFunctions.setError(error);
        functions.setErrorFunctions(errorFunctions);

        Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("MotorRotationalDifferenceControlOutput", "Proportional output", "Proportional",
                new String[][]{{"Gain", Globals.getInstance().get("MotorRotationalDifferenceControlOutput_Gain"), "Double"}}, null);
        outputFunctions.setOutput(output);
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerMotorsMovingSensors() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("MotorInput");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();

        ControlFunction input = Utils.configureControlFunction("MotorBMoving", "Indicates if motor B is moving", "MotorIsMoving",
                new String[][]{{"MotorIndex", "B", ""}, {"MotorType", "NXT", ""}}, null);
        inputFunctions.setInput(input);

        pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions.Transfers();
        List<ControlFunction> transfersList = transfers.getTransfer();

        ControlFunction transfer;
        transfer = Utils.configureControlFunction("MotorCMoving", "Indicates if motor C is moving", "MotorIsMoving",
                new String[][]{{"MotorIndex", "C", ""}, {"MotorType", "NXT", ""}}, null);
        transfersList.add(transfer);
        transfer = Utils.configureControlFunction("MotorBStalled", "Indicates if motor B is stalled", "MotorIsStalled",
                new String[][]{{"MotorIndex", "B", ""}, {"MotorType", "NXT", ""}}, null);
        transfersList.add(transfer);
        transfer = Utils.configureControlFunction("MotorCStalled", "Indicates if motor C is stalled", "MotorIsStalled",
                new String[][]{{"MotorIndex", "C", ""}, {"MotorType", "NXT", ""}}, null);
        transfersList.add(transfer);

        inputFunctions.setTransfers(transfers);

        functions.setInputFunctions(inputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerMotorsInput() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("MotorInput");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();

        ControlFunction input = Utils.configureControlFunction("HeadPositionInput", "Tacho count of Motor A - Head", "MotorCount",
                new String[][]{{"MotorIndex", "A", ""}, {"MotorType", "NXT", ""}}, null);
        inputFunctions.setInput(input);
        {
            pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();

            ControlFunction transfer;
            transfer = Utils.configureControlFunction("MotorBPosition", "Tacho count of Motor B", "MotorCount",
                    new String[][]{{"MotorIndex", "B", ""}}, null);
            transfersList.add(transfer);
            transfer = Utils.configureControlFunction("MotorCPosition", "Tacho count of Motor C", "MotorCount",
                    new String[][]{{"MotorIndex", "C", ""}}, null);
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("MotorSum", "Sum of tacho counts of motors B and C", "WeightedSum",
                    new String[][]{{"Weights", "1,1", "String"}}, new String[][]{{"MotorBPosition"}, {"MotorCPosition"}});
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("MotorsVelocity", "Mean of previous 4 tachocount sums", "TimeChangeArray",
                    new String[][]{{"Size", "4", "Integer"}}, new String[][]{{"MotorSum"}, {"Rate"}}
            );
            transfersList.add(transfer);
            inputFunctions.setTransfers(transfers);
        }
        /*
         transfer = Utils.configureControlFunction("MotorBVelocity", "Mean of previous 4 B tachocounts", "TimeChangeArray",
         new String[][]{{"Size", "4", "Integer"}}, new String[][]{{"MotorBPosition"}, {"Rate"}});
         transfersList.add(transfer);

         transfer = Utils.configureControlFunction("MotorCVelocity", "Mean of previous 4 C tachocounts", "TimeChangeArray",
         new String[][]{{"Size", "4", "Integer"}}, new String[][]{{"MotorCPosition"}, {"Rate"}});
         transfersList.add(transfer);
         */

        functions.setInputFunctions(inputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerBeaconDistanceControl() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("BeaconDistanceControl");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction("BeaconDistanceControlInput", "Distance to beacon", "",
                    null, new String[][]{{"DistanceFilter"}});
            inputFunctions.setInput(input);

            Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer = Utils.configureControlFunction("DistanceFilter", "Distance or zero if beacon inactive", "Threshold",
                    new String[][]{{"Threshold", "128", "Double"}, {"LessThan", "true", "Boolean"}, {"Alternate", "0", "Double"}},
                    new String[][]{{"IRSensorDistance"}});
            transfersList.add(transfer);
            inputFunctions.setTransfers(transfers);

            functions.setInputFunctions(inputFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
            ControlFunction reference = Utils.configureControlFunction("BeaconDistanceControlReference", "Distance reference multiplied by beacon active weight", "WeightedMultiply",
                    new String[][]{{"Weights", "1,1", "String"}, {"Root", "false", "Boolean"}}, new String[][]{{"DistanceReference"}, {"BeaconActiveOutputSwitch"}});
            referenceFunctions.setReference(reference);
            Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer = Utils.configureControlFunction("DistanceReference", "Distance constant", "Constant",
                    new String[][]{{"Constant", Globals.getInstance().get("BeaconDistanceControl_Reference"), "Double"}}, null);
            transfersList.add(transfer);
            referenceFunctions.setTransfers(transfers);
            functions.setReferenceFunctions(referenceFunctions);
        }

        Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction("BeaconDistanceControlError", "Distance error", "Subtract",
                null, null);
        errorFunctions.setError(error);
        functions.setErrorFunctions(errorFunctions);

        Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("BeaconDistanceControlOutput", "Sigmoid output", "Sigmoid",
                new String[][]{{"InScale", "-0.03", "Double"}, {"OutScale", Globals.getInstance().get("BeaconDistanceControlOutput_OutScale"), "Double"}, {"XShift", "0", "Double"}, {"YShift", "0", "Double"}}, null);
        outputFunctions.setOutput(output);
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerPhoneAccelerometerSpeedControl() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("PhoneAccelerometerSpeedControl");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction("PhoneAccelerometerSpeedControlInput", "Smoothed value of Y reading", "Smooth",
                    new String[][]{{"Smoothness", Globals.getInstance().get("PhoneAccelerometerSpeedControlInput_Smoothness"), "Double"}},
                    new String[][]{{"PhoneAccelerometerY"}});
            inputFunctions.setInput(input);
            functions.setInputFunctions(inputFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
            ControlFunction reference = Utils.configureControlFunction("PhoneAccelerometerSpeedControlReference", "...", "Constant",
                    new String[][]{{"Constant", "0", "Double"}}, null);
            referenceFunctions.setReference(reference);

            functions.setReferenceFunctions(referenceFunctions);
        }

        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction("PhoneAccelerometerSpeedControlError", "Error", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            functions.setErrorFunctions(errorFunctions);
        }
        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction("PhoneAccelerometerSpeedControlOutput", "Sigmoid output", "Sigmoid",
                    new String[][]{
                        {"InScale", Globals.getInstance().get("PhoneAccelerometerSpeedControlOutput_InScale"), "Double"},
                        {"OutScale", Globals.getInstance().get("PhoneAccelerometerSpeedControlOutput_OutScale"), "Double"},
                        {"XShift", Globals.getInstance().get("PhoneAccelerometerSpeedControlOutput_XShift"), "Double"},
                        {"YShift", Globals.getInstance().get("PhoneAccelerometerSpeedControlOutput_YShift"), "Double"},
                        {"InputTolerance", Globals.getInstance().get("PhoneAccelerometerSpeedControlOutput_InputTolerance"), "Double"},
                        {"Absolute", "true", "Boolean"}
                    }, null);
            outputFunctions.setOutput(output);

            functions.setOutputFunctions(outputFunctions);
        }
        controller.setFunctions(functions);
        return controller;
    }

    public static ControlFunction getAccDirReference(boolean constant) throws Exception {

        ControlFunction reference;

        if (constant) {
            reference = Utils.configureControlFunction("PhoneAccelerometerDirectionControlReference",
                    "direction reference, zero is centre", "Constant",
                    new String[][]{{"Constant", "0", "Double"}},
                    null);
        } else {
            reference = Utils.configureControlFunction("PhoneAccelerometerDirectionControlReference", "Pass through", "",
                    null,
                    new String[][]{{"DirectionConflictControlOutput"}});
        }

        return reference;
    }

    public static Layers.Layer.Controller configureControllerPhoneAccelerometerDirectionControl(ControlFunction reference) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("PhoneAccelerometerDirectionControl");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = Utils.configureControlFunction("PhoneAccelerometerDirectionControlInput", "Smoothed value of X reading", "Smooth",
                new String[][]{{"Smoothness", Globals.getInstance().get("PhoneAccelerometerDirectionControlInput_Smoothness"), "Double"}},
                new String[][]{{"PhoneAccelerometerX"}});
        inputFunctions.setInput(input);
        functions.setInputFunctions(inputFunctions);

        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();

        referenceFunctions.setReference(reference);
        functions.setReferenceFunctions(referenceFunctions);

        Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction("PhoneAccelerometerDirectionControlError", "Direction error", "Subtract",
                null, new String[][]{{"PhoneAccelerometerDirectionControlInput"}, {"PhoneAccelerometerDirectionControlReference"}});
        errorFunctions.setError(error);
        functions.setErrorFunctions(errorFunctions);

        Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("PhoneAccelerometerDirectionControlOutput", "Sigmoid output", "Sigmoid",
                new String[][]{
                    {"InScale", Globals.getInstance().get("PhoneAccelerometerDirectionControlOutput_InScale"), "Double"},
                    {"OutScale", Globals.getInstance().get("PhoneAccelerometerDirectionControlOutput_OutScale"), "Double"},
                    {"XShift", Globals.getInstance().get("PhoneAccelerometerDirectionControlOutput_XShift"), "Double"},
                    {"YShift", Globals.getInstance().get("PhoneAccelerometerDirectionControlOutput_YShift"), "Double"},
                    {"Absolute", "true", "Boolean"},
                    {"InputTolerance", Globals.getInstance().get("PhoneAccelerometerDirectionControlOutput_InputTolerance"), "Double"}}, null);
        outputFunctions.setOutput(output);
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerBeaconHeadDirectionControl() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("BeaconHeadDirectionControl");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = Utils.configureControlFunction("BeaconHeadDirectionControlInput", "IR Sensor direction", "",
                null, new String[][]{{"IRSensorDirection"}});
        inputFunctions.setInput(input);
        functions.setInputFunctions(inputFunctions);

        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction("BeaconHeadDirectionControlReference", "Head direction reference, zero is centre", "Constant",
                new String[][]{{"Constant", "0", "Double"}}, null);
        referenceFunctions.setReference(reference);
        functions.setReferenceFunctions(referenceFunctions);

        Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction("BeaconHeadDirectionControlError", "Direction error", "Subtract",
                null, new String[][]{{"BeaconHeadDirectionControlInput"}, {"BeaconHeadDirectionControlReference"}});
        errorFunctions.setError(error);
        functions.setErrorFunctions(errorFunctions);

        Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("BeaconHeadDirectionControlOutput", "Proportional output", "Proportional",
                new String[][]{{"Gain", "5", "Double"}}, null);
        outputFunctions.setOutput(output);
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerHeadPositionControl() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("HeadPositionControl");
        controller.setActivation(Utils.configureActivation("BeaconActiveOutputSwitch", new String[]{"Input", "Reference"}));

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = Utils.configureControlFunction("HeadPositionControlInput", "Head position", "",
                null, new String[][]{{"HeadPositionInput"}});
        inputFunctions.setInput(input);
        functions.setInputFunctions(inputFunctions);

        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction("HeadPositionControlReference", "Head motor position reference, with respect to initial position, zero is centre", "Constant",
                new String[][]{{"Constant", "0", "Double"}}, null);
        referenceFunctions.setReference(reference);
        functions.setReferenceFunctions(referenceFunctions);

        Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction("HeadPositionControlError", "Position error", "Subtract",
                null, null);
        //null, new String[][]{{"HeadPositionControlInput"}, {"HeadPositionControlReference"}});
        errorFunctions.setError(error);
        functions.setErrorFunctions(errorFunctions);

        Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("HeadPositionControlOutput", "Proportional output", "Proportional",
                new String[][]{{"Gain", Globals.getInstance().get("HeadPositionControlOutput_Gain"), "Double"}}, null);
        outputFunctions.setOutput(output);
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerBeaconActive() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("BeaconActive");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = Utils.configureControlFunction("BeaconActiveInput", "Binary value representing beacon active", "DigitalLimit",
                new String[][]{{"Threshold", "100", "Double"}, {"Upper", "0", "Double"}, {"Lower", "1", "Double"}},
                new String[][]{{"IRSensorDistance"}});
        inputFunctions.setInput(input);
        functions.setInputFunctions(inputFunctions);

        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction("BeaconActiveReference", "Reference for beacon active", "Constant",
                new String[][]{{"Constant", "1", "Double"}}, null);
        referenceFunctions.setReference(reference);
        functions.setReferenceFunctions(referenceFunctions);

        Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction("BeaconActiveError", "Active error", "Subtract",
                null, null);
        errorFunctions.setError(error);
        functions.setErrorFunctions(errorFunctions);

        Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("BeaconActiveOutput", "Binary value representing beacon active", "",
                null, null);
        outputFunctions.setOutput(output);
        Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
        List<ControlFunction> transfersList = transfers.getTransfer();
        ControlFunction transfer = Utils.configureControlFunction("BeaconActiveOutputSwitch", "Binary value switched, so zero is off", "LimitBanded",
                new String[][]{{"Threshold", "0.5", "Double"}, {"BandWidth", "0", "Double"}, {"BandValue", "1", "Double"}, {"Upper", Globals.getInstance().get("BeaconActiveSwitch_Upper"), "Double"}, {"Lower", "1", "Double"}},
                new String[][]{{"BeaconActiveOutput"}});
        transfersList.add(transfer);
        outputFunctions.setTransfers(transfers);

        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerProximityPeriod() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("ProximityPeriodControl");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = Utils.configureControlFunction("ProximityPeriodInput",
                "Sum of iteration time, while sonic is decreasing. Adds up the time for the iteration, but is reset if the second link is zero",
                "ProportionalIntegration",
                new String[][]{{"Gain", "1", "Double"}}, new String[][]{{"Rate"}, {"ProximityDifferenceChangeBinary"}});

        inputFunctions.setInput(input);
        {
            Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();

            ControlFunction transfer = Utils.configureControlFunction("ProximityDifferenceChange",
                    "Change of difference, greater than 0 if sonic is decreasing", "Change",
                    null, new String[][]{{"ProximityControlError"}});
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("ProximityDifferenceChangeBinary",
                    "Binary value of change direction, 1 if decreasing, 0 if not", "DigitalLimit",
                    new String[][]{{"Threshold", "0", "Double"}, {"Upper", "1", "Double"}, {"Lower", "0", "Double"}},
                    new String[][]{{"ProximityDifferenceChange"}});
            transfersList.add(transfer);

            //  transfer = Utils.configureControlFunction("ProximityPeriodDifference", "Difference between period threshold and actual period", "Subtract",
            //        null, new String[][]{{"ProximityPeriodThreshold"}, {"ProximityPeriod"}});
            //transfersList.add(transfer);
            inputFunctions.setTransfers(transfers);
        }
        functions.setInputFunctions(inputFunctions);

        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction("ProximityPeriodReference", "Length of period that unit is allowed to turn before flip", "Constant",
                new String[][]{{"Constant", Globals.getInstance().get("ProximityPeriodReference_Constant"), "Double"}}, null);
        referenceFunctions.setReference(reference);
        functions.setReferenceFunctions(referenceFunctions);

        Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction("ProximityPeriodControlError", "Error", "Subtract",
                null, null);
        errorFunctions.setError(error);
        functions.setErrorFunctions(errorFunctions);

        Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("ProximityPeriodControlOutput",
                "Signal non-zero if input crosses zero", "ZeroTransition",
                new String[][]{{"Direction", "Negative", "String"}, {"Infinity", "false", "Boolean"}}, null);

        outputFunctions.setOutput(output);
        {
            Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer = Utils.configureControlFunction("FlipLimiter",
                    "Suppresses the direction change for a period of time", "Product",
                    null, new String[][]{{"TimerWeight"}, {"ProximityPeriodControlOutput"}});

            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("DirectionFlip",
                    "Flips the direction of rotation", "Flip",
                    new String[][]{{"ValueA", "1", "Double"}, {"ValueB", "-1", "Double"}}, new String[][]{{"FlipLimiter"}});
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("TimerWeight", "0 for the period starting when input is 1", "BinaryTimer",
                    new String[][]{{"Period", "5000", "Long"}}, new String[][]{{"ProximityPeriodControlOutput"}});
            transfersList.add(transfer);

            outputFunctions.setTransfers(transfers);

        }
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerShakingControl() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("ShakingControl");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction("ShakingControlInput", "...", "Smooth",
                    new String[][]{{"Smoothness", "0.99", "Double"}}, new String[][]{{"ShakingChangeAbsolute"}});

            //= Utils.configureControlFunction("ShakingControlInput", "...", "Smooth",
            //new String[][]{{"Smoothness", Globals.getInstance().get("ShakingControlInput_Smooth"), "Double"}},
            //new String[][]{{"ShakingChangeSquare"}});
            inputFunctions.setInput(input);

            {
                Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();

                ControlFunction transfer = Utils.configureControlFunction("ShakingControlModulus", "Modulus of acceleromter values", "Modulus",
                        null,
                        new String[][]{{"AccelerometerX"}, {"AccelerometerY"}, {"AccelerometerZ"}});
                transfersList.add(transfer);

                //transfer = Utils.configureControlFunction("ShakingControlModulusSmooth", "...", "Smooth",                        new String[][]{{"Smoothness", "0.5", "Double"}}, new String[][]{{"ShakingControlModulus"}});
                //transfersList.add(transfer);
                transfer = Utils.configureControlFunction("ShakingChange", "Change of modulus value", "TimeChange",
                        null,//new String[][]{{"Period", Globals.getInstance().get("ShakingChange_Period"), "Integer"}},
                        new String[][]{{"ShakingControlModulus"}, {"SmoothRate"}});
                transfersList.add(transfer);

                //transfer = Utils.configureControlFunction("ShakingChangeRaw", "Change of modulus value", "Change",                        new String[][]{{"Gain", Globals.getInstance().get("ShakingChangeRaw_Gain"), "Integer"}},                       new String[][]{{"ShakingControlModulus"}});
                //transfersList.add(transfer);
                //transfer = Utils.configureControlFunction("ShakingChangeSquare", "...", "Product",                        null, new String[][]{{"ShakingChange"}, {"ShakingChange"}});
                //transfersList.add(transfer);
                transfer = Utils.configureControlFunction("ShakingChangeAbsolute", "...", "Absolute",
                        null, new String[][]{{"ShakingChange"}});
                transfersList.add(transfer);

                //transfer = Utils.configureControlFunction("ShakingChangeAbsoluteSmooth", "...", "Smooth",
                //      new String[][]{{"Smoothness", "0.99", "Double"}}, new String[][]{{"ShakingChangeAbsolute"}});
                //transfersList.add(transfer);
                //transfer = Utils.configureControlFunction("ShakingChangeRawAbsolute", "...", "Absolute",                        null, new String[][]{{"ShakingChangeRaw"}});
                //transfersList.add(transfer);
                //transfer = Utils.configureControlFunction("ShakingChangeRawAbsoluteSmooth", "Smooth of shaking", "Smooth",                        new String[][]{{"Smoothness", "0.9", "Double"}}, new String[][]{{"ShakingChangeRawAbsolute"}});
                //transfersList.add(transfer);
                /*
                 transfer = Utils.configureControlFunction("ShakingSmooth", "Smooth of shaking", "Smooth",
                 new String[][]{{"Smoothness", "0.5", "Double"}}, new String[][]{{"ShakingControlInput"}});
                 transfersList.add(transfer);

                 */
                inputFunctions.setTransfers(transfers);
            }
            functions.setInputFunctions(inputFunctions);
        }

        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
            ControlFunction reference = Utils.configureControlFunction("ShakingControlReference", "Reference for shaking", "Constant",
                    new String[][]{{"Constant", Globals.getInstance().get("ShakingControlReference_Constant"), "Double"}}, null);
            referenceFunctions.setReference(reference);
            functions.setReferenceFunctions(referenceFunctions);
        }

        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction("ShakingControlError", "Active error", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            functions.setErrorFunctions(errorFunctions);
        }

        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction("ShakingControlOutput", "...", "Integration",
                    new String[][]{{"Initial", "1", "Double"}, {"Gain", Globals.getInstance().get("ShakingControlOutput_Gain"), "Double"},
                    {"Slow", Globals.getInstance().get("ShakingControlOutput_Slow"), "Double"},
                    {"Min", "0", "Double"}, {"Max", "1", "Double"}},
                    new String[][]{{"ShakingControlError"}, {"SmoothRate", "Rate"}});
            outputFunctions.setOutput(output);
            /*
             {
             Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
             List<ControlFunction> transfersList = transfers.getTransfer();
             ControlFunction transfer = Utils.configureControlFunction("ShakingControlOutputSwitch", "Binary value switched, so zero is off", "LimitBanded",
             new String[][]{{"Threshold", "0.5", "Double"}, {"BandWidth", "0", "Double"}, {"BandValue", "1", "Double"}, {"Upper", Globals.getInstance().get("ShakingControlSwitch_Upper"), "Double"}, {"Lower", "1", "Double"}},
             new String[][]{{"ShakingControlOutput"}});
             transfersList.add(transfer);
             outputFunctions.setTransfers(transfers);
             }*/
            functions.setOutputFunctions(outputFunctions);
        }
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureProximitySensorTest() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("ProximitySensor");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = Utils.configureControlFunction("ProximityRaw", "Value of sonic sensor", "Constant",
                new String[][]{{"Constant", "100", "Double"}}, null);
        inputFunctions.setInput(input);

        functions.setInputFunctions(inputFunctions);
        controller.setFunctions(functions);
        return controller;
    }

    static public Layers.Layer.Controller configureSensorProximity() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("ProximitySensor");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = getSensorProximityRaw();
        inputFunctions.setInput(input);

        functions.setInputFunctions(inputFunctions);
        controller.setFunctions(functions);
        return controller;
    }

    static public ControlFunction getSensorProximityRaw() throws Exception {
        ControlFunction function;
        function = Utils.configureControlFunction("ProximityRaw", "Value of sonic sensor", Globals.getInstance().get("ProximitySensor_Type"),
                new String[][]{{"SensorPort", Globals.getInstance().get("ProximitySensor_Port"), ""},
                {"Max", Globals.getInstance().get("ProximitySensor_Max"), "Double"},
                {"Mode", Globals.getInstance().get("ProximitySensor_Mode"), "String"}},
                null);
        return function;
    }

    static public Layers.Layer.Controller configureControllerDriveMotorsOutput(boolean test) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("MotorsDrive");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction("SpeedReference", "Speed reference", "",
                null, new String[][]{{"SpeedConsolidated"}});
        referenceFunctions.setReference(reference);
        {
            Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer = Utils.configureControlFunction("RotationWeightReference",
                    "Weight representing degree of on the spot rotation", "",
                    null, new String[][]{{"RotationWeightConsolidated"}});
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("DirectionSpeedAdjustment",
                    "Weight representing degree of direction rotation", "",
                    null, new String[][]{{"DirectionSpeedAdjustmentConsolidated"}});
            transfersList.add(transfer);

            referenceFunctions.setTransfers(transfers);

        }
        functions.setReferenceFunctions(referenceFunctions);

        Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();

        ControlFunction output = Utils.configureControlFunction("WeightedRotationSpeed", "Speed weighted for rotation", "Product",
                null, new String[][]{{"SpeedReference"}, {"RotationWeightReference"}});
        outputFunctions.setOutput(output);
        Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
        List<ControlFunction> transfersList = transfers.getTransfer();
        ControlFunction transfer = Utils.configureControlFunction("MotorBSpeed", "Add the speeds weighted for rotation and direction", "Subtract",
                null, new String[][]{{"WeightedRotationSpeed"}, {"DirectionSpeedAdjustment"}});
        transfersList.add(transfer);

        if (!test) {
            transfer = Utils.configureControlFunction("MotorBOutput", "Setting value of motor B speed", "MotorWrite",
                    new String[][]{{"MotorIndex", "B", ""},
                    {"Sign", Globals.getInstance().get("B_Sign"), "Integer"},
                    {"Acceleration", Globals.getInstance().get("B_Acceleration"), "Integer"}},
                    new String[][]{{"MotorBSpeed"}});
            transfersList.add(transfer);
        }
        transfer = Utils.configureControlFunction("MotorCSpeed", "Subtract the speed weighted for direction from the speed weighted for rotation", "Addition",
                null, new String[][]{{"WeightedRotationSpeed"}, {"DirectionSpeedAdjustment"}});
        transfersList.add(transfer);

        if (!test) {
            transfer = Utils.configureControlFunction("MotorCOutput", "Setting value of motor C speed", "MotorWrite",
                    new String[][]{{"MotorIndex", "C", ""},
                    {"Sign", Globals.getInstance().get("C_Sign"), "Integer"},
                    {"Acceleration", Globals.getInstance().get("C_Acceleration"), "Integer"}},
                    new String[][]{{"MotorCSpeed"}});
            transfersList.add(transfer);
        }
        outputFunctions.setTransfers(transfers);
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerCompassBearingControl(Layers.Layer.Controller.Functions.ReferenceFunctions compassBearingReference) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("CompassBearingControl");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = Utils.configureControlFunction("CompassBearingControlInput", "Compass Bearing reading", "",
                null, new String[][]{{"CompassRead"}});
        inputFunctions.setInput(input);
        functions.setInputFunctions(inputFunctions);

        /*Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
         ControlFunction reference = Utils.configureControlFunction("CompassBearingControlReference",
         "Head direction reference, zero is centre", "",
         null, new String[][]{{Globals.getInstance().get("CompassBearingControlReference_Link")}});
         referenceFunctions.setReference(reference);*/
        functions.setReferenceFunctions(compassBearingReference);

        Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction("CompassBearingControlError", "Bearing error",
                "SubtractBearing", null, null);
        errorFunctions.setError(error);
        functions.setErrorFunctions(errorFunctions);

        Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("CompassBearingControlOutput", "Proportional output", "Proportional",
                new String[][]{{"Gain", Globals.getInstance().get("CompassBearingControlOutput_Gain"), "Double"}}, null);
        outputFunctions.setOutput(output);
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerDirectionConflictControl() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("DirectionConflictControl");

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = Utils.configureControlFunction("DirectionConflictControlInput",
                "Indication of proximity error, 0 if > 0, 1 if 0 or Inf. If object is near this will be 0, otherwise 1",
                "DigitalLimit",
                new String[][]{{"Threshold", "0", "Double"}, {"Upper", "0", "Double"},
                {"Lower", "1", "Double"}, {"InfinityValue", "1", "Double"}},
                new String[][]{{"ProximityControlError"}});
        inputFunctions.setInput(input);
        functions.setInputFunctions(inputFunctions);

        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction("DirectionConflictControlReference",
                "DirectionConflict reference, representing goal of not near object", "Constant",
                new String[][]{{"Constant", "1", "Double"}}, null);
        referenceFunctions.setReference(reference);
        functions.setReferenceFunctions(referenceFunctions);

        Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction("DirectionConflictControlError", "DirectionConflict error", "Subtract",
                null, null);

        errorFunctions.setError(error);
        functions.setErrorFunctions(errorFunctions);

        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction("DirectionConflictControlOutput",
                    "If 0 error then 0, if 1 then Inf. That is, if object is near then output is infinity, otherwise 0",
                    "DigitalLimit",
                    new String[][]{{"Threshold", "0", "Double"}, {"Upper", "Infinity", "Double"}, {"Lower", "0", "Double"}},
                    null);

            outputFunctions.setOutput(output);
            {
                Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction("DirectionConflictControlOutputWeight",
                        "If 0 error then 1, if 1 then Inf. That is, if object is near then output is infinity, otherwise 1",
                        "DigitalLimit",
                        new String[][]{{"Threshold", "0", "Double"}, {"Upper", "Infinity", "Double"}, {"Lower", "1", "Double"}},
                        new String[][]{{"DirectionConflictControlError"}});
                transfersList.add(transfer);
                outputFunctions.setTransfers(transfers);

            }

            functions.setOutputFunctions(outputFunctions);
        }
        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerPhoneAccLocationConflictControl() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("PhoneAccLocationConflictControl");

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = Utils.configureControlFunction("PhoneAccLocationConflictControlInput",
                "Indication of PhoneAcc presence, 1 if Inf, else 0. If phone acc is on 0, otherwise 1",
                "DigitalLimit",
                new String[][]{{"Threshold", "100", "Double"}, {"Upper", "1", "Double"},
                {"Lower", "0", "Double"}},
                new String[][]{{"PhoneAccelerometerX"}});
        inputFunctions.setInput(input);
        functions.setInputFunctions(inputFunctions);

        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction("PhoneAccLocationConflictControlReference",
                "PhoneAccLocationConflict reference, representing goal of phone acc not active", "Constant",
                new String[][]{{"Constant", "1", "Double"}}, null);
        referenceFunctions.setReference(reference);
        functions.setReferenceFunctions(referenceFunctions);

        Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction("PhoneAccLocationConflictControlError",
                "PhoneAccLocationConflict error, 0 if phone acc not active", "Subtract",
                null, null);

        errorFunctions.setError(error);
        functions.setErrorFunctions(errorFunctions);

        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction("PhoneAccLocationConflictControlOutput",
                    "If 0 error then 1, if 1 (>0) then Inf. That is, if phone acc is active output is infinity, otherwise 0",
                    "DigitalLimit",
                    new String[][]{{"Threshold", "0", "Double"}, {"Upper", "Infinity", "Double"}, {"Lower", "1", "Double"}},
                    null);

            outputFunctions.setOutput(output);

            functions.setOutputFunctions(outputFunctions);
        }
        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerLatitudeControl() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("LatitudeControl");

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = Utils.configureControlFunction("LatitudeControlInput", "Latitude", "",
                null, new String[][]{{"LatitudeSensor"}});
        inputFunctions.setInput(input);
        functions.setInputFunctions(inputFunctions);

        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction("LatitudeControlReference", "Latitude reference", "",
                null, new String[][]{{Globals.getInstance().get("LatitudeControlReference_Link")}});
        referenceFunctions.setReference(reference);
        functions.setReferenceFunctions(referenceFunctions);

        Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction("LatitudeControlError", "Latitude error", "Subtract",
                null, null);

        errorFunctions.setError(error);
        functions.setErrorFunctions(errorFunctions);

        Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("LatitudeControlOutput", "Proportional output", "Proportional",
                new String[][]{{"Gain", Globals.getInstance().get("LatitudeControlOutput_Gain"), "Double"}}, null);
        outputFunctions.setOutput(output);
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerLongitudeControl() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("LongitudeControl");

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = Utils.configureControlFunction("LongitudeControlInput", "Longitude", "",
                null, new String[][]{{"LongitudeSensor"}});
        inputFunctions.setInput(input);
        functions.setInputFunctions(inputFunctions);

        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction("LongitudeControlReference", "Longitude reference", "",
                null, new String[][]{{Globals.getInstance().get("LongitudeControlReference_Link")}});
        referenceFunctions.setReference(reference);
        functions.setReferenceFunctions(referenceFunctions);

        Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction("LongitudeControlError", "Longitude error", "Subtract",
                null, null);

        errorFunctions.setError(error);
        functions.setErrorFunctions(errorFunctions);

        Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("LongitudeControlOutput", "Proportional output", "Proportional",
                new String[][]{{"Gain", Globals.getInstance().get("LongitudeControlOutput_Gain"), "Double"}}, null);
        outputFunctions.setOutput(output);
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerProximityControl(boolean errorSwitch) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("ProximityControl");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = Utils.configureControlFunction("ProximityInputSmooth", "Smoothed value of sonic sensor", "Smooth",
                new String[][]{{"Smoothness", Globals.getInstance().get("ProximityInputSmooth_Smoothness"), "Double"}}, new String[][]{{"ProximityInputLimiting"}});
        inputFunctions.setInput(input);
        {
            Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();

            ControlFunction transfer = Utils.configureControlFunction("ProximityRawSmooth", "Smooth value of raw sonic", "Smooth",
                    new String[][]{{"Smoothness", Globals.getInstance().get("ProximityRawSmooth_Smoothness"), "Double"}},
                    new String[][]{{"ProximityRaw"}});
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("ProximityInputLimiting", "Limiting sonic input to max value", "Limit",
                    new String[][]{{"Min", "0", "Double"}, {"Infinity", Globals.getInstance().get("ProximityInputLimiting_Infinity"), "Boolean"}},
                    new String[][]{{"ProximityRawSmooth"}, {"ProximityControlReference", "Max"}});
            transfersList.add(transfer);

            inputFunctions.setTransfers(transfers);
        }
        functions.setInputFunctions(inputFunctions);

        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction("ProximityControlReference", "Proximity reference", "Constant",
                new String[][]{{"Constant", Globals.getInstance().get("ProximityControlReference_Constant"), "Double"}}, null);
        referenceFunctions.setReference(reference);
        functions.setReferenceFunctions(referenceFunctions);

        Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction("ProximityControlError", "Error", "Subtract",
                null, null);
        errorFunctions.setError(error);
        functions.setErrorFunctions(errorFunctions);

        Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();

        ControlFunction output = Utils.configureControlFunction("ProximityControlOutput", "...", "",
                null, null);
        outputFunctions.setOutput(output);
        /*
         String prefix = Globals.getInstance().get("ProximityVelocity_NamePrefix");
      
         ControlFunction output = Utils.configureControlFunction(prefix, "Velocity reference, as a sigmoid function of the sonic proximity", "Sigmoid",
         new String[][]{{"InScale", Globals.getInstance().get(prefix+"_InScale"), "Double"},
         {"OutScale", Globals.getInstance().get(prefix+"_OutScale"), "Double"},
         {"XShift", Globals.getInstance().get(prefix+"_XShift"), "Double"},
         {"YShift", Globals.getInstance().get(prefix+"_YShift"), "Double"},
         {"InputTolerance", Globals.getInstance().get(prefix+"_InputTolerance"), "Double"}},
         new String[][]{{Globals.getInstance().get(prefix+"_Link")}});

         outputFunctions.setOutput(output);
         {
         Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
         List<ControlFunction> transfersList = transfers.getTransfer();
         ControlFunction transfer = Utils.configureControlFunction("ProximityVelocityDirectionReference", "Velocity reference, as a sigmoid function of the sonic proximity", "Sigmoid",
         new String[][]{{"InScale", Globals.getInstance().get("ProximityVelocityDirectionReference_InScale"), "Double"},
         {"OutScale", Globals.getInstance().get("ProximityVelocityDirectionReference_OutScale"), "Double"},
         {"XShift", Globals.getInstance().get("ProximityVelocityDirectionReference_XShift"), "Double"},
         {"YShift", Globals.getInstance().get("ProximityVelocityDirectionReference_YShift"), "Double"},
         {"InputTolerance", Globals.getInstance().get("ProximityVelocityDirectionReference_InputTolerance"), "Double"}},
         new String[][]{{Globals.getInstance().get("ProximityVelocityDirectionReference_Link")}});
         transfersList.add(transfer);

         if (errorSwitch) {
         transfer = Utils.configureControlFunction("ProximityErrorSwitch", "1 if error is not zero, 0 otherwise", "LimitBanded",
         new String[][]{{"Threshold", "0", "Double"}, {"BandWidth", "0.01", "Double"},
         {"BandValue", "0", "Double"}, {"Upper", "1", "Double"}, {"Lower", "0", "Double"}},
         new String[][]{{"ProximityControlError"}});
         transfersList.add(transfer);

         transfer = Utils.configureControlFunction("ProximityVelocityReferenceSwitch", "Weighted product of sonic error ratio and velocity", "WeightedMultiply",
         new String[][]{{"Weights", "1,1", "String"}},
         new String[][]{{"ProximityErrorSwitch"}, {"ProximityVelocityReference"}});
         transfersList.add(transfer);

         }

         transfer = Utils.configureControlFunction("ProximityControlRatio", "Ratio of error and reference", "Division",
         null, new String[][]{{"ProximityControlError"}, {"ProximityControlReference"}});
         transfersList.add(transfer);

         transfer = Utils.configureControlFunction("ProximityControlOutputWeighted", "Weighted product of sonic error ratio, velocity and direction", "WeightedMultiply",
         new String[][]{{"Weights", "2,1,1", "String"}}, new String[][]{{"ProximityControlRatio"}, {"DirectionFlip"}, {"ProximityVelocityDirectionReference"}});
         transfersList.add(transfer);

         outputFunctions.setTransfers(transfers);

         }
         */
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerLocationDistanceControl() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("LocationDistanceControl");

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction("LocationDistanceControlWeight", "Velocity reference, as a sigmoid function of the location distance", "Sigmoid",
                new String[][]{{"InScale", Globals.getInstance().get("LocationDistanceControlWeight_InScale"), "Double"},
                {"OutScale", Globals.getInstance().get("LocationDistanceControlWeight_OutScale"), "Double"},
                {"XShift", Globals.getInstance().get("LocationDistanceControlWeight_XShift"), "Double"},
                {"YShift", Globals.getInstance().get("LocationDistanceControlWeight_YShift"), "Double"}},
                new String[][]{{"LocationDistanceControlModulus"}});

        referenceFunctions.setReference(reference);
        {
            Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer = Utils.configureControlFunction("LocationDistanceControlModulus", "Location distance", "Modulus",
                    null, new String[][]{{"LatitudeControlOutput"}, {"LongitudeControlOutput"}});
            transfersList.add(transfer);

            referenceFunctions.setTransfers(transfers);
        }

        functions.setReferenceFunctions(referenceFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControlReorganisationSimulationProp() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("ControlSimulation");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction("ControlSimulationInput", "...", "Addition",
                    null, new String[][]{{"Disturbance"}, {"World"}});
            inputFunctions.setInput(input);
            {
                pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();

                ControlFunction transfer;

                transfer = Utils.configureControlFunction("Disturbance", "", "WeightedSum",
                        new String[][]{{"Weights", Globals.getInstance().get("Disturbance_Weights"), "String"}}, new String[][]{{"Random"}, {"Step"}, {"Sine"}});
                transfersList.add(transfer);

                inputFunctions.setTransfers(transfers);
            }
            functions.setInputFunctions(inputFunctions);
        }

        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
            ControlFunction reference = Utils.configureControlFunction("ControlSimulationReference", "Distance constant", "Constant",
                    new String[][]{{"Constant", "0", "Double"}}, null);
            referenceFunctions.setReference(reference);
            {
                Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction("RMSErrorResponse", "...", "RMSErrorResponse",
                        new String[][]{
                            {"Period", Globals.getInstance().get("RMSErrorResponse_Period"), "Double"},
                            {"Limit", Globals.getInstance().get("RMSErrorResponse_Limit"), "Double"}},
                        new String[][]{{"ControlSimulationError"},});
                transfersList.add(transfer);

                transfer = Utils.configureControlFunction("GainReorganisation", "Reorganisation of gain", "ParameterReorganisation",
                        new String[][]{{"LearningRate", Globals.getInstance().get("GainReorganisation_LearningRate"), "Double"},
                        {"Type", Globals.getInstance().get("GainReorganisation_Type"), "Double"}
                        },
                        new String[][]{{"RMSErrorResponse", "ErrorResponse"}, {"ControlSimulationOutput", "Parameter"}});
                transfersList.add(transfer);

                referenceFunctions.setTransfers(transfers);

            }

            functions.setReferenceFunctions(referenceFunctions);
        }

        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction("ControlSimulationError", "...", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            functions.setErrorFunctions(errorFunctions);
        }
        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction("ControlSimulationOutput", "output", "ProportionalIntegration",
                    new String[][]{
                        {"Gain", Globals.getInstance().get("ControlSimulationOutput_Gain"), "Double"},
                        {"Min", Globals.getInstance().get("ControlSimulationOutput_Min"), "Double"},
                        {"Max", Globals.getInstance().get("ControlSimulationOutput_Max"), "Double"}}, null);
            outputFunctions.setOutput(output);
            functions.setOutputFunctions(outputFunctions);
            {
                Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction("World", "", "Proportional",
                        new String[][]{{"Gain", Globals.getInstance().get("World_Gain"), "Double"}},
                        new String[][]{{"ControlSimulationOutput"}});
                transfersList.add(transfer);
                outputFunctions.setTransfers(transfers);

            }
        }
        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControlReorganisationSimulationInt() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("ControlSimulation");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction("ControlSimulationInput", "...", "Addition",
                    null, new String[][]{{"Disturbance"}, {"World"}});
            inputFunctions.setInput(input);
            {
                pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();

                ControlFunction transfer;

                transfer = Utils.configureControlFunction("Disturbance", "", "WeightedSum",
                        new String[][]{{"Weights", Globals.getInstance().get("Disturbance_Weights"), "String"}}, new String[][]{{"Random"}, {"Step"}, {"Sine"}});
                transfersList.add(transfer);

                inputFunctions.setTransfers(transfers);
            }
            functions.setInputFunctions(inputFunctions);
        }

        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
            ControlFunction reference = Utils.configureControlFunction("ControlSimulationReference", "Distance constant", "Constant",
                    new String[][]{{"Constant", "0", "Double"}}, null);
            referenceFunctions.setReference(reference);
            {
                Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction("RMSErrorResponse", "...", "RMSErrorResponse",
                        new String[][]{
                            {"Period", Globals.getInstance().get("RMSErrorResponse_Period"), "Double"},
                            {"Limit", Globals.getInstance().get("RMSErrorResponse_Limit"), "Double"}},
                        new String[][]{{"ControlSimulationError"},});
                transfersList.add(transfer);

                transfer = Utils.configureControlFunction("GainReorganisation", "Reorganisation of gain", "ParameterReorganisation",
                        new String[][]{
                            {"Continuous", Globals.getInstance().get("GainReorganisation_Continuous"), "Boolean"},
                            {"LearningRate", Globals.getInstance().get("GainReorganisation_LearningRate"), "Double"},
                            {"LearningRateMax", Globals.getInstance().get("GainReorganisation_LearningRateMax"), "Double"},
                            {"AdaptiveSmoothUpper", Globals.getInstance().get("GainReorganisation_AdaptiveSmoothUpper"), "Double"},
                            {"AdaptiveSmoothLower", Globals.getInstance().get("GainReorganisation_AdaptiveSmoothLower"), "Double"},
                            {"AdaptiveFactor", Globals.getInstance().get("GainReorganisation_AdaptiveFactor"), "Double"},
                            {"ParameterSmoothFactor", Globals.getInstance().get("GainReorganisation_ParameterSmoothFactor"), "Double"},
                            {"Type", Globals.getInstance().get("GainReorganisation_Type"), ""}
                        },
                        new String[][]{{"RMSErrorResponse", "ErrorResponse"}, {"ControlSimulationOutput", "Parameter"}});
                transfersList.add(transfer);

                referenceFunctions.setTransfers(transfers);

            }

            functions.setReferenceFunctions(referenceFunctions);
        }

        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction("ControlSimulationError", "...", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            functions.setErrorFunctions(errorFunctions);
        }
        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction("ControlSimulationOutput", "output", "Integration",
                    new String[][]{
                        {"Gain", Globals.getInstance().get("ControlSimulationOutput_Gain"), "Double"},
                        {"Slow", Globals.getInstance().get("ControlSimulationOutput_Slow"), "Double"},
                        {"Min", Globals.getInstance().get("ControlSimulationOutput_Min"), "Double"},
                        {"Max", Globals.getInstance().get("ControlSimulationOutput_Max"), "Double"}}, null);
            outputFunctions.setOutput(output);
            functions.setOutputFunctions(outputFunctions);
            {
                Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction("World", "", "Proportional",
                        //ControlFunction transfer = Utils.configureControlFunction("World", "", "Integration",
                        new String[][]{
                            {"Gain", Globals.getInstance().get("World_Gain"), "Double"}/*,
                            {"Slow", Globals.getInstance().get("World_Slow"), "Double"},
                            {"Min", Globals.getInstance().get("World_Min"), "Double"},
                            {"Max", Globals.getInstance().get("World_Max"), "Double"}*/
                        },
                        new String[][]{{"ControlSimulationOutput"}});
                transfersList.add(transfer);
                outputFunctions.setTransfers(transfers);

            }
        }
        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerXBeeSignalControl() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("XBeeSignalControl");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction("XBeeSignalControlInput", "...", "",
                    null, new String[][]{{"XBeeSensorSmooth"}});
            inputFunctions.setInput(input);
            /*
             {
             Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
             List<ControlFunction> transfersList = transfers.getTransfer();
             ControlFunction transfer = Utils.configureControlFunction("DistanceFilter", "Distance or zero if beacon inactive", "Threshold",
             new String[][]{{"Threshold", "128", "Double"}, {"LessThan", "true", "Boolean"}, {"Alternate", "0", "Double"}},
             new String[][]{{"IRSensorDistance"}});
             transfersList.add(transfer);
             inputFunctions.setTransfers(transfers);
             }
             */
            functions.setInputFunctions(inputFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
            ControlFunction reference = Utils.configureControlFunction("XBeeSignalControlReference", "RSSI reference signal", "Constant",
                    new String[][]{{"Constant", "1", "Double"}}, null);
            referenceFunctions.setReference(reference);
            /*
             {
             Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
             List<ControlFunction> transfersList = transfers.getTransfer();
             ControlFunction transfer = Utils.configureControlFunction("DistanceReference", "Distance constant", "Constant",
             new String[][]{{"Constant", Globals.getInstance().get("XBeeSignalControl_Reference"), "Double"}}, null);
             transfersList.add(transfer);
             referenceFunctions.setTransfers(transfers);
             }
             */
            functions.setReferenceFunctions(referenceFunctions);
        }

        Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction("XBeeSignalControlError", "Distance error", "Subtract",
                new String[][]{{"Sign", "-1", "Double"}}, null);
        errorFunctions.setError(error);
        functions.setErrorFunctions(errorFunctions);

        Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("XBeeSignalControlOutput", "Proportional output", "Scaling",
                new String[][]{
                    {"Scale", Globals.getInstance().get("XBeeSignalControlOutput_Scale"), "Double"},
                    {"Offset", Globals.getInstance().get("XBeeSignalControlOutput_Offset"), "Double"}

                }, null);
        outputFunctions.setOutput(output);
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerXBeeChangeControl() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("XBeeChangeControl");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction("XBeeChangeControlInput", "> 0 change value", "Threshold",
                    new String[][]{{"Threshold", "0", "Double"}}, new String[][]{{"XBeeSensorTimeChangeSmooth"}});
            inputFunctions.setInput(input);
            /*
             {
             Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
             List<ControlFunction> transfersList = transfers.getTransfer();
             ControlFunction transfer = Utils.configureControlFunction("DistanceFilter", "Distance or zero if beacon inactive", "Threshold",
             new String[][]{{"Threshold", "128", "Double"}, {"LessThan", "true", "Boolean"}, {"Alternate", "0", "Double"}},
             new String[][]{{"IRSensorDistance"}});
             transfersList.add(transfer);
             inputFunctions.setTransfers(transfers);
             }
             */
            functions.setInputFunctions(inputFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
            ControlFunction reference = Utils.configureControlFunction("XBeeChangeControlReference", "...", "Constant",
                    new String[][]{{"Constant", "0", "Double"}}, null);
            referenceFunctions.setReference(reference);
            /*
             {
             Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
             List<ControlFunction> transfersList = transfers.getTransfer();
             ControlFunction transfer = Utils.configureControlFunction("DistanceReference", "Distance constant", "Constant",
             new String[][]{{"Constant", Globals.getInstance().get("XBeeChangeControl_Reference"), "Double"}}, null);
             transfersList.add(transfer);
             referenceFunctions.setTransfers(transfers);
             }
             */
            functions.setReferenceFunctions(referenceFunctions);
        }

        Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction("XBeeChangeControlError", "Distance error", "Subtract",
                null, null);
        errorFunctions.setError(error);
        functions.setErrorFunctions(errorFunctions);

        Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("XBeeChangeControlOutput", "Proportional output", "Proportional",
                new String[][]{{"Gain", Globals.getInstance().get("XBeeChangeControlOutput_Gain"), "Double"}},
                null);
        outputFunctions.setOutput(output);
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureSubscribers3D(String side) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("3D" + side);
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();
        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();

        ControlFunction input = Utils.configureControlFunction(side + "EndpointSubX", "...", "ROSSubscriber",
                new String[][]{
                    {"Topic", Globals.getInstance().get(side + "EndpointSub_Topic"), ""},
                    {"Variable", Globals.getInstance().get(side + "EndpointSubX_Variable"), ""},
                    {"Initial", Globals.getInstance().get(side + "EndpointSubX_Initial"), ""}
                }, null);
        inputFunctions.setInput(input);

        {
            Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer;

            transfer = Utils.configureControlFunction(side + "EndpointSubY", "...", "ROSSubscriber",
                    new String[][]{
                        {"Topic", Globals.getInstance().get(side + "EndpointSub_Topic"), ""},
                        {"Variable", Globals.getInstance().get(side + "EndpointSubY_Variable"), ""},
                        {"Initial", Globals.getInstance().get(side + "EndpointSubY_Initial"), ""}
                    }, null);
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction(side + "EndpointSubZ", "...", "ROSSubscriber",
                    new String[][]{
                        {"Topic", Globals.getInstance().get(side + "EndpointSub_Topic"), ""},
                        {"Variable", Globals.getInstance().get(side + "EndpointSubZ_Variable"), ""},
                        {"Initial", Globals.getInstance().get(side + "EndpointSubZ_Initial"), ""}
                    }, null);
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction(side + "EndpointSubQW", "...", "ROSSubscriber",
                    new String[][]{
                        {"Topic", Globals.getInstance().get(side + "EndpointSub_Topic"), ""},
                        {"Variable", Globals.getInstance().get(side + "EndpointSubQW_Variable"), ""},
                        {"Initial", Globals.getInstance().get(side + "EndpointSubQW_Initial"), ""}
                    }, null);
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction(side + "EndpointSubQY", "...", "ROSSubscriber",
                    new String[][]{
                        {"Topic", Globals.getInstance().get(side + "EndpointSub_Topic"), ""},
                        {"Variable", Globals.getInstance().get(side + "EndpointSubQY_Variable"), ""},
                        {"Initial", Globals.getInstance().get(side + "EndpointSubQY_Initial"), ""}
                    }, null);
            transfersList.add(transfer);

            inputFunctions.setTransfers(transfers);
        }
        functions.setInputFunctions(inputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static void addPSensorActuators(Layers layers, String side, String joints[], String[] refLinks, String armReach) throws Exception {
        Controller controller = Utils.emptyController(side + "SensorsActuators", true, false, false, true);
        Utils.addController(layers, 0, controller);
        int i = 0;
        for (String joint : joints) {
            boolean transfer = true;
            boolean publish = i + 1 == joints.length;
            if (i == 0) {
                transfer = false;
            }
            switch (joint) {
                case "ShoulderPitchS1":
                    if (armReach != null) {
                        String[] refs = refLinks[i].split(":");
                        if (refs.length > 1) {
                            Generic.addPositionSubPub(controller, transfer, joint, side, publish,
                                    Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"),
                                    Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"),
                                    Globals.getInstance().getAssert(side + joint + "PositionControl_Last"),
                                    side + refs[0], side + refs[1]);
                        } else {
                            Generic.addPositionSubPub(controller, transfer, joint, side, publish,
                                    Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"),
                                    Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"),
                                    Globals.getInstance().getAssert(side + joint + "PositionControl_Last"),
                                    side + armReach + "ControlOutputToShoulder", false);
                        }
                    } else {
                        Generic.addPositionSubPub(controller, transfer, joint, side, publish,
                                Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"),
                                Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"),
                                Globals.getInstance().getAssert(side + joint + "PositionControl_Last"),
                                side + refLinks[i], false);
                    }
                    break;
                case "ElbowAngleE1":
                    if (armReach != null) {
                        String[] refs = refLinks[i].split(":");
                        if (refs.length > 1) {
                            Generic.addPositionSubPub(controller, transfer, joint, side, publish,
                                    Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"),
                                    Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"),
                                    Globals.getInstance().getAssert(side + joint + "PositionControl_Last"),
                                    side + refs[0], side + refs[1]);
                        } else {
                            Generic.addPositionSubPub(controller, transfer, joint, side, publish,
                                    Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"),
                                    Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"),
                                    Globals.getInstance().getAssert(side + joint + "PositionControl_Last"),
                                    side + refLinks[i], false);
                        }

                    } else {
                        Generic.addPositionSubPub(controller, transfer, joint, side, publish,
                                Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"),
                                Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"),
                                Globals.getInstance().getAssert(side + joint + "PositionControl_Last"),
                                side + refLinks[i], false);
                    }
                    break;
                default:
                    Generic.addPositionSubPub(controller, transfer, joint, side, publish,
                            Globals.getInstance().getAssert(side + joint + "PositionControl_Tolerance"),
                            Globals.getInstance().getAssert(side + joint + "PositionControl_Initial"),
                            Globals.getInstance().getAssert(side + joint + "PositionControl_Last"),
                            side + refLinks[i], false);
                    break;
            }
            i++;
        }
    }

    public static void addPositionSubPub(Controller controller, boolean tr, String joint, String side, Boolean publish, String tolerance, String initial, String last, String link1, boolean weight) throws Exception {
        String[][] links = new String[1][1];
        links[0][0] = link1;

        ControlFunction function = Utils.configureControlFunction(side + joint + "ControlInput", "...", "ROSSubscriber",
                new String[][]{
                    {"Index", side.toLowerCase() + "_" + Globals.getInstance().get(joint + "_Joint"), ""},
                    {"Variable", "Position", ""}}, null);

        Utils.addFunction(controller, function, Utils.INPUT, tr);

        if (weight) {
            function = Utils.configureControlFunction(side + joint + "PubWeighted", "...", "WeightedSum",
                    new String[][]{
                        {"Weights", Globals.getInstance().get(side + joint + "PubWeighted_Weights"), "String"}
                    },
                    links);
            Utils.addFunction(controller, function, Utils.OUTPUT, tr);
        }

        function = Utils.configureBaxterPublisher(side + joint + "Pub",
                "JointPublish",
                side.toLowerCase() + "_" + Globals.getInstance().get(joint + "_Joint"),
                Globals.getInstance().get(side + "JointPublish_Topic"),
                "Position",
                String.valueOf(publish), tolerance,
                null,
                initial,
                last,
                (weight ? new String[][]{{side + joint + "PubWeighted"}} : links));

        Utils.addFunction(controller, function, Utils.OUTPUT, tr);

        if (weight) {
            function = Utils.configureControlFunction(side + joint + "PubWeighted", "...", "WeightedSum",
                    new String[][]{
                        {"Weights", Globals.getInstance().get(side + joint + "PubWeighted_Weights"), "String"}
                    },
                    links);
            Utils.addFunction(controller, function, Utils.OUTPUT, true);
        }

    }

    public static void addPositionSubPub(Controller controller, boolean tr, String joint, String side, Boolean publish, String tolerance, String initial, String last, String link1, String link2) throws Exception {
        String[][] links = new String[2][1];
        links[0][0] = link1;
        links[1][0] = link2;
        ControlFunction function = Utils.configureControlFunction(side + joint + "ControlInput", "...", "ROSSubscriber",
                new String[][]{
                    {"Index", side.toLowerCase() + "_" + Globals.getInstance().get(joint + "_Joint"), ""},
                    {"Variable", "Position", ""}}, null);

        Utils.addFunction(controller, function, Utils.INPUT, tr);

        function = Utils.configureControlFunction(side + joint + "PubWeighted", "...", "WeightedSum",
                new String[][]{
                    {"Weights", Globals.getInstance().get(side + joint + "PubWeighted_Weights"), ""}
                },
                links);
        Utils.addFunction(controller, function, Utils.OUTPUT, tr);

        function = Utils.configureBaxterPublisher(side + joint + "Pub",
                "JointPublish",
                side.toLowerCase() + "_" + Globals.getInstance().get(joint + "_Joint"),
                Globals.getInstance().get(side + "JointPublish_Topic"),
                "Position",
                String.valueOf(publish),
                tolerance,
                null,
                initial,
                last,
                new String[][]{{side + joint + "PubWeighted"}});

        Utils.addFunction(controller, function, Utils.OUTPUT, tr);

    }

}
