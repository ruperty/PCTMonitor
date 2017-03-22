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

import pct.moons.co.uk.schema.layers.ControlFunction;
import pct.moons.co.uk.schema.layers.Layers;

/**
 *
 * @author Rupert Young
 */
public class Common {

    public static String[][] getDisplayList() {
        return new String[][]{{"SimTime", "Fidelity:4"}, {"SimTime", "SimulatedTime"}, {"SimTime", "FidelityScore:0"},
        {"SimTime", "TimeScore:0"}, {"SimTime", "Score:0"}};
    }

    public static String[][] getMaxLinks() {
        return new String[][]{{"SimTime", "Fidelity:4"}, {"SimTime", "SimulatedTime:3"}, {"SimTime", "FidelityScore:0"},
        {"SimTime", "TimeScore:0"}, {"SimTime", "Score:0"}, {"Reset", "Reset"}};
    }

    public static Layers.Layer.Controller configureControllerQMSensor(String name) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        String prefix = "SimTime";
        controller.setName(name);

        {
            ControlFunction input = Utils.configureControlFunction(prefix, "...", "QMSensor",
                    new String[][]{
                        {"Subscribe", "true", ""},
                        {"Exe", "C:\\packages\\Perceptual learning-20170103T142628Z\\Perceptual learning\\Builds\\2017-01-03\\Server\\Quantum Moves.exe", ""},
                        {"X", Globals.getInstance().get(prefix + "_X"), ""},
                        {"Y", Globals.getInstance().get(prefix + "_Y"), ""},
                        {"Width", Globals.getInstance().get(prefix + "_Width"), ""},
                        {"Height", Globals.getInstance().get(prefix + "_Height"), ""},
                        {"Level", Globals.getInstance().get(prefix + "_Level"), ""},
                        {"Host", Globals.getInstance().get(prefix + "_Host"), ""},
                        {"Port", Globals.getInstance().get(prefix + "_Port"), ""},
                        {"Timeout", Globals.getInstance().get(prefix + "_Timeout"), ""},
                        {"TargetPercentage", Globals.getInstance().get(prefix + "_TargetPercentage"), "Double"},
                        {"TimeLimit", Globals.getInstance().get(prefix + "_TimeLimit"), "Double"}
                    }, new String[][]{{"Level", "QMLevel"}, {"Reset", "QMReset"}});
            Utils.addFunction(controller, input, Utils.INPUT, false);
            {
                ControlFunction transfer = Utils.configureControlFunction("Level", "", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get("Level_Constant"), "Integer"}}, null);
                Utils.addTransferFunction(controller, transfer, Utils.INPUT);

                 transfer = Utils.configureControlFunction("AutoReset", "", "Parameter",
                        new String[][]{{"Parameter", "QMReset", ""}},
                        new String[][]{{"SimTime"}});
                Utils.addTransferFunction(controller, transfer, Utils.INPUT);

                transfer = Utils.configureControlFunction("Reset", "", "BinaryPulse",
                        new String[][]{{"Pulse", Globals.getInstance().get("Reset_Pulse"), "Integer"}},
                        new String[][]{{"AutoReset"}});
                Utils.addTransferFunction(controller, transfer, Utils.INPUT);

                //transfer = Utils.configureControlFunction("OppositeReset", "", "BinaryFlip",
                //        null, new String[][]{{"Reset"}});
                //Utils.addTransferFunction(controller, transfer, Utils.INPUT);
            }

        }

        return controller;
    }

    public static Layers.Layer.Controller configureQMActuator(String name, String xref, String yref) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        //String prefix = "QuantumMoves";
        controller.setName(name);

        {
            String[][] links = new String[][]{{yref}, {"Reset", "Reset"}, {"StartY", "ResetValue"}};
            ControlFunction output = Utils.proportionalIntegrationFunction("LaserOutputIntegrateY", links);
            Utils.addFunction(controller, output, Utils.OUTPUT, false);
            {

                ControlFunction transfer = Utils.configureControlFunction("LaserOutputY", "...", "QMActuator",
                        new String[][]{
                            {"Variable", "LaserOutputY", ""},
                            {"Publish", "false", ""}
                        }, new String[][]{{"LaserOutputIntegrateY"}});
                Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);

                links = new String[][]{{xref}, {"Reset", "Reset"}, {"StartX", "ResetValue"}};
                transfer = Utils.proportionalIntegrationFunction("LaserOutputIntegrateX", links);
                Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);

                transfer = Utils.configureControlFunction("LaserOutputX", "...", "QMActuator",
                        new String[][]{
                            {"Variable", "LaserOutputX", ""},
                            {"Publish", "true", ""}
                        }, new String[][]{{"LaserOutputIntegrateX"}});
                Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);
            }
        }
        return controller;
    }

    public static Layers.Layer.Controller configureQMVariables(String name, String[][] displays, String[][] maxLinks, String[][] recordLinks, String activeLink) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(name);

        {
            ControlFunction input = Utils.configureControlFunction("Fidelity", "...", "Parameter",
                    new String[][]{{"Parameter", "Fidelity", ""}},
                    new String[][]{{"SimTime"}});
            Utils.addFunction(controller, input, Utils.INPUT, false);
            {
                ControlFunction transfer = Utils.configureControlFunction("Energy", "...", "Parameter",
                        new String[][]{{"Parameter", "Energy", ""}},
                        new String[][]{{"SimTime"}});
                Utils.addTransferFunction(controller, transfer, Utils.INPUT);

                transfer = Utils.configureControlFunction("AtomX", "...", "Parameter",
                        new String[][]{{"Parameter", "AtomX", ""}},
                        new String[][]{{"SimTime"}});
                Utils.addTransferFunction(controller, transfer, Utils.INPUT);

                transfer = Utils.configureControlFunction("LaserX", "...", "Parameter",
                        new String[][]{{"Parameter", "LaserX", ""}},
                        new String[][]{{"SimTime"}});
                Utils.addTransferFunction(controller, transfer, Utils.INPUT);

                transfer = Utils.configureControlFunction("LaserY", "...", "Parameter",
                        new String[][]{{"Parameter", "LaserY", ""}},
                        new String[][]{{"SimTime"}});
                Utils.addTransferFunction(controller, transfer, Utils.INPUT);

                String[][] links;
                if (activeLink == null) {
                    links = new String[][]{{"LaserX", "Initial"}, {"Reset", "Reset"}};
                } else {
                    links = new String[][]{{"LaserX", "Initial"}, {"Reset", "Reset"}, {activeLink, "Active"}};
                }

                transfer = Utils.configureControlFunction("LaserXVelocity", "", "Change",
                        new String[][]{{"LinkInitial", "true", ""}},
                        links);
                Utils.addTransferFunction(controller, transfer, Utils.INPUT);

                if (activeLink == null) {
                    links = new String[][]{{"LaserXVelocity"}, {"Reset", "Reset"}};
                } else {
                    links = new String[][]{{"LaserXVelocity"}, {"Reset", "Reset"}, {activeLink, "Active"}};
                }

                transfer = Utils.configureControlFunction("LaserXAcceleration", "", "Change",
                        new String[][]{{"ZeroReset", "true", ""}},
                        links);
                Utils.addTransferFunction(controller, transfer, Utils.INPUT);

                transfer = Utils.configureControlFunction("AtomXOffset", "", "Subtract",
                        null, new String[][]{{"LaserX"}, {"AtomX"}});
                Utils.addTransferFunction(controller, transfer, Utils.INPUT);

            }
        }

        {
            ControlFunction reference = Utils.configureControlFunction("Display", "", "Display",
                    new String[][]{
                        {"X", Globals.getInstance().get("QMDisplay_X"), ""},
                        {"Y", Globals.getInstance().get("QMDisplay_Y"), ""},
                        {"Width", Globals.getInstance().get("QMDisplay_Width"), ""},
                        {"Height", Globals.getInstance().get("QMDisplay_Height"), ""},
                        {"Font", Globals.getInstance().get("QMDisplay_Font"), ""},
                        {"Alignment", Globals.getInstance().get("QMDisplay_Alignment"), ""}},
                    displays);

            Utils.addFunction(controller, reference, Utils.REFERENCE, false);

            {
                ControlFunction transfer = Utils.configureControlFunction("MaxResults", "", "MaxSeries",
                        new String[][]{
                            {"Key", Globals.getInstance().get("Max_Key"), ""},
                            {"Model", Globals.getInstance().get("Max_Model"), ""}
                        },
                        maxLinks);
                Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);

                if (recordLinks != null) {
                    transfer = Utils.configureControlFunction("RecordQM", "", "RecordQMNode",
                            new String[][]{
                                {"Threshold", Globals.getInstance().get("RecordQM_Threshold"), ""},
                                {"Key", Globals.getInstance().get("RecordQM_Key"), ""}
                            },
                            recordLinks);
                    Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);
                }
                transfer = Utils.configureControlFunction("MaxDisplay", "", "Display",
                        new String[][]{
                            {"X", Globals.getInstance().get("QMMaxDisplay_X"), ""},
                            {"Y", Globals.getInstance().get("QMMaxDisplay_Y"), ""},
                            {"Width", Globals.getInstance().get("QMMaxDisplay_Width"), ""},
                            {"Height", Globals.getInstance().get("QMMaxDisplay_Height"), ""},
                            {"Font", Globals.getInstance().get("QMMaxDisplay_Font"), ""},
                            {"Alignment", Globals.getInstance().get("QMMaxDisplay_Alignment"), ""}},
                        new String[][]{{"MaxResults", "Indexed"}});
                Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);

                transfer = Utils.configureControlFunction("TargetX", "...", "Parameter",
                        new String[][]{{"Parameter", "TargetX", ""}},
                        new String[][]{{"SimTime"}});
                Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);

                transfer = Utils.constantFunction("TargetY");
                Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);

                transfer = Utils.configureControlFunction("StartX", "...", "Parameter",
                        new String[][]{{"Parameter", "StartX", ""}},
                        new String[][]{{"SimTime"}});
                Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);

                transfer = Utils.configureControlFunction("StartY", "...", "Parameter",
                        new String[][]{{"Parameter", "StartY", ""}},
                        new String[][]{{"SimTime"}});
                Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);

            }
            /*
                transfer = Utils.configureControlFunction("FidelityScore", "...", "Parameter",
                        new String[][]{{"Parameter", "FidelityScore", ""}},
                        new String[][]{{"SimTime"}});
                Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);
                transfer = Utils.configureControlFunction("TimeScore", "...", "Parameter",
                        new String[][]{{"Parameter", "TimeScore", ""}},
                        new String[][]{{"SimTime"}});
                Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);
                transfer = Utils.configureControlFunction("Score", "...", "Parameter",
                        new String[][]{{"Parameter", "Score", ""}},
                        new String[][]{{"SimTime"}});
                Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);
             */
        }

        {
            ControlFunction output = Utils.configureControlFunction("EnergyVariance", "...", "Variance",
                    new String[][]{
                        {"Size", Globals.getInstance().get("EnergyVariance_Size"), ""}},
                    new String[][]{{"Energy"}, {"Reset", "Reset"}});
            Utils.addFunction(controller, output, Utils.OUTPUT, false);
            {
                ControlFunction transfer = Utils.configureControlFunction("AtomXOffsetVariance", "...", "Variance",
                        new String[][]{{"Size", Globals.getInstance().get("AtomXOffsetVariance_Size"), ""}},
                        new String[][]{{"AtomXOffset"}, {"Reset", "Reset"}});
                Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);

                transfer = Utils.configureControlFunction("EnergyChange", "", "Change",
                        new String[][]{{"LinkInitial", "true", ""}},
                        new String[][]{{"Energy", "Initial"}, {"Reset", "Reset"}});
                Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);
            }
        }

        return controller;
    }

    public static Layers.Layer.Controller configureAtomOffsetControllerIntegration(String name, ControlFunction reference) throws Exception {

        ControlFunction input = Utils.configureControlFunction(name + "Input", "", "",
                null, new String[][]{{"AtomXOffset"}});

        String[][] links = new String[][]{{name + "Error"}, {"Reset", "Reset"}};
        Layers.Layer.Controller controller = Generic.configureIntegrationController(name, input, reference, links);

        ControlFunction transfer = Utils.configureControlFunction("AtomOffsetVelocity", "", "Change",
                new String[][]{{"LinkInitial", "true", ""}}, new String[][]{{name + "Input", "Initial"}, {"Reset", "Reset"}});
        Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);

        transfer = Utils.configureControlFunction("AtomOffsetAccel", "", "Change",
                new String[][]{{"ZeroReset", "true", ""}}, new String[][]{{"AtomOffsetVelocity"}, {"Reset", "Reset"}});
        Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);
        return controller;
    }

    public static Layers.Layer.Controller configureAtomOffsetControllerProportional(String name, ControlFunction reference) throws Exception {

        ControlFunction input = Utils.configureControlFunction(name + "Input", "", "Subtract",
                null, new String[][]{{"LaserX"}, {"AtomX"}});

        Layers.Layer.Controller controller = Generic.configureProportionalController(name, input, reference);

        ControlFunction transfer = Utils.configureControlFunction("AtomOffsetVelocity", "", "Change",
                new String[][]{{"LinkInitial", "true", ""}}, new String[][]{{name + "Input", "Initial"}, {"Reset", "Reset"}});
        Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);

        transfer = Utils.configureControlFunction("AtomOffsetAccel", "", "Change",
                new String[][]{{"ZeroReset", "true", ""}}, new String[][]{{"AtomOffsetVelocity"}, {"Reset", "Reset"}});
        Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);

        return controller;
    }

    public static Layers.Layer.Controller configureAtomOffsetControllerSigmoid(String name, ControlFunction reference) throws Exception {

        ControlFunction input = Utils.configureControlFunction(name + "Input", "", "Subtract",
                null, new String[][]{{"LaserX"}, {"AtomX"}});

        Layers.Layer.Controller controller = Generic.configureSigmoidController(name, input, reference, false);

        ControlFunction transfer = Utils.configureControlFunction("AtomOffsetVelocity", "", "Change",
                new String[][]{{"LinkInitial", "true", ""}}, new String[][]{{name + "Input", "Initial"}, {"Reset", "Reset"}});
        Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);

        transfer = Utils.configureControlFunction("AtomOffsetAccel", "", "Change",
                new String[][]{{"ZeroReset", "true", ""}}, new String[][]{{"AtomOffsetVelocity"}, {"Reset", "Reset"}});
        Utils.addTransferFunction(controller, transfer, Utils.OUTPUT);

        return controller;
    }

    public static Layers.Layer.Controller configureEnergyChangeControllerSigmoid(String name, ControlFunction reference) throws Exception {

        ControlFunction input = Utils.configureControlFunction(name + "Input", "", "",
                null, new String[][]{{"EnergyChange"}});

        Layers.Layer.Controller controller = Generic.configureSigmoidController(name, input, reference, false);

        return controller;
    }

    public static Layers.Layer.Controller configureLaserVelocityController(String name, String referenceLink) throws Exception {

        ControlFunction input = Utils.configureControlFunction(name + "Input", "", "Change",
                new String[][]{{"LinkInitial", "true", ""}}, new String[][]{{"LaserX", "Initial"}, {"Reset", "Reset"}});

        ControlFunction reference = Utils.configureControlFunction(name + "Reference", "", "Integration",
                new String[][]{{"Gain", Globals.getInstance().get(name + "Reference_Gain"), "String"},
                {"Slow", Globals.getInstance().get(name + "Reference_Slow"), "String"},
                {"Tolerance", Globals.getInstance().get(name + "Reference_Tolerance"), "String"},
                {"Integrate", Globals.getInstance().get(name + "Reference_Integrate"), ""}},
                new String[][]{{referenceLink}, {"Reset", "Reset"}});

        Layers.Layer.Controller controller = Generic.configureProportionalController(name, input, reference);

        return controller;
    }

    public static Layers.Layer.Controller configureManualLaserAccelController(String name, String referenceLink) throws Exception {

        ControlFunction input = Utils.configureControlFunction(name + "Input", "", "Change",
                new String[][]{{"ZeroReset", "true", ""}}, new String[][]{{"LaserVelocityControlInput"}, {"Reset", "Reset"}});

        ControlFunction reference = Utils.configureControlFunction(name + "Reference", "...", "Addition",
                null, new String[][]{{referenceLink}, {"AccelTarget"}});

        String[][] outputLinks = new String[][]{{name + "Error"}, {"Reset", "Reset"}};

        Layers.Layer.Controller controller = Generic.configureIntegrationController(name, input, reference, outputLinks);

        ControlFunction transfer = Utils.constantFunction("AccelTarget");
        Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);

        return controller;
    }

    public static Layers.Layer.Controller configureLaserAccelController(String name, String referenceLink) throws Exception {

        ControlFunction input = Utils.configureControlFunction(name + "Input", "", "Change",
                new String[][]{{"ZeroReset", "true", ""}}, new String[][]{{"LaserVelocityControlInput"}, {"Reset", "Reset"}});

        String[][] referenceLinks = new String[][]{{referenceLink}, {"Reset", "Reset"}};
        ControlFunction reference = Utils.integrationFunction(name + "Reference", referenceLinks);

        String[][] outputLinks = new String[][]{{name + "Error"}, {"Reset", "Reset"}};

        Layers.Layer.Controller controller = Generic.configureIntegrationController(name, input, reference, outputLinks);

        ControlFunction transfer = Utils.constantFunction("AccelTarget");
        Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);

        return controller;
    }

    /*
    public static Layers.Layer.Controller configurePVAProportionalController(String name, String inputLink, String referenceLink) throws Exception {

        ControlFunction input = Utils.configureControlFunction(name + "Input", "", "",
                null, new String[][]{{inputLink}});

        //ControlFunction reference = Utils.configureControlFunction(name + "Reference", "...", "",                null, new String[][]{{referenceLink}});
        //String[][] outputLinks = new String[][]{{name + "Error"}, {"Reset", "Reset"}};
        //Layers.Layer.Controller controller = Generic.configureIntegrationController(name, input, reference, outputLinks);
        Layers.Layer.Controller controller = Generic.configureProportionalController(name, input, referenceLink);

        return controller;
    }*/

 /*
    public static Layers.Layer.Controller configurePVAController(String name, String inputLink, String referenceLink) throws Exception {

        //ControlFunction input = Utils.configureControlFunction(name + "Input", "", "",                null, new String[][]{{inputLink}});

        //ControlFunction reference = Utils.configureControlFunction(name + "Reference", "...", "",                null, new String[][]{{referenceLink}});

        String[][] outputLinks = new String[][]{{name + "Error"}, {"OppositeReset", "Reset"}};
        Layers.Layer.Controller controller = Generic.configureIntegrationController(name, inputLink, referenceLink, outputLinks);

        return controller;
    }*/
}
