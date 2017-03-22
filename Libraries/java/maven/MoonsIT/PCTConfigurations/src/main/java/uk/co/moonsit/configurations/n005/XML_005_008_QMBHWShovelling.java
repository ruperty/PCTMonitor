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
package uk.co.moonsit.configurations.n005;

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
import uk.co.moonsit.config.functions.Common;
import uk.co.moonsit.config.functions.Generic;
import uk.co.moonsit.config.functions.Globals;
import uk.co.moonsit.config.functions.Modules;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_005_008_QMBHWShovelling {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        String suffix = "005-008-QMBHWShovelling";
        String type = "Models";
        String dir = "QuantumMoves";
        String file = Utils.getFileName(type, dir, suffix + ".xml");

        System.out.println(file);

        String[] outputReferenceNames = new String[]{"MoveLaserReference", "CollectReference", "TargetX"};
        /*StringBuilder sb = new StringBuilder();
        for (String s : outputReferenceNames) {
            sb.append("1,");
        }
        String weights = sb.toString().substring(0, sb.toString().length() - 1);*/
        String qmtype = "shovel";//"tunnel";

        XML_005_008_QMBHWShovelling xml = new XML_005_008_QMBHWShovelling();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "500");

        globals.put("SimTime_Timeout", "30000");
        globals.put("SimTime_Host", "localhost");
        globals.put("SimTime_Port", "30000");
        globals.put("SimTime_X", "400");
        globals.put("SimTime_Y", "0");
        globals.put("SimTime_Width", "1024");
        globals.put("SimTime_Height", "750");

        //globals.put("SimTime_Width", "600");
        //globals.put("SimTime_Height", "480");
        globals.put("DiffY_Constant", "0");

        globals.put("Max_Model", suffix);
        globals.put("Max_Key", "Score");
        globals.put("Reset_Pulse", "0");
        String level = "13";
        globals.put("SimTime_Level", level);
        globals.put("Level_Constant", level);
        globals.put("TargetY_Constant", "0");
        globals.put("EnergyVariance_Size", "5");
        globals.put("AtomXOffsetVariance_Size", "5");

        globals.put("RecordQM_Threshold", "800000");
        globals.put("RecordQM_Key", "Score");

        //globals.put("SimTime_TargetY", "0");
        globals.put("SimTime_TargetPercentage", "50");
        globals.put("SimTime_TimeLimit", "0.19");
        globals.put("QMDisplay_X", "0");
        globals.put("QMDisplay_Y", "0");
        globals.put("QMMaxDisplay_X", "200");
        globals.put("QMMaxDisplay_Y", "0");
        globals.put("QMMaxDisplay_Width", "200");
        globals.put("QMMaxDisplay_Height", "150");
        globals.put("QMMaxDisplay_Font", "24");
        globals.put("QMMaxDisplay_Alignment", "Vertical");

        globals.put("QMDisplay_Width", "200");
        globals.put("QMDisplay_Height", "150");
        globals.put("QMDisplay_Font", "24");
        globals.put("QMDisplay_Alignment", "Vertical");

        globals.put("MoveLaserControlOutput_Gain", "1");

        globals.put("LaserOutputIntegrateY_Gain", "1");
        globals.put("LaserOutputIntegrateX_Gain", "1");

        globals.put("AmplitudeControlReference_Constant", "-150");
        globals.put("AmplitudeControlOutput_InScale", "0.01");
        globals.put("AmplitudeControlOutput_OutScale", "10");
        globals.put("AmplitudeControlOutput_XShift", "0");
        globals.put("AmplitudeControlOutput_YShift", "0");
        globals.put("AmplitudeControlOutput_InputTolerance", "0");

        //globals.put("ManualTargetX_Constant", "0.97");
        globals.put("SequenceControlReference_Constant", "3");
        globals.put("SequenceControlInput_Tolerances", "0.01,0.01,0.112,");
        globals.put("SequenceControlErrorFlipped_Offset", "1");

        globals.put("AtomXOffsetLimited_Min", "-0.3");
        globals.put("AtomXOffsetLimited_Max", "1");
        globals.put("DiffXSumLimited_Min", "-0.05");
        globals.put("DiffXSumLimited_Max", "0.05");

        globals.put("PointOne_Constant", "0.1");

        globals.put("LaserXPositionControlOutput_XShift", "0");
        globals.put("LaserXPositionControlOutput_YShift", "0");
        globals.put("LaserXPositionControlOutput_InputTolerance", "0.001");
        globals.put("LaserXAccelerationControlOutput_Tolerance", "0.0001");
        globals.put("LaserXAccelerationControlOutput_Integrate", "true");
        //globals.put("CollectControlOutput_Tolerance", "0");
        //globals.put("CollectControlOutput_Integrate", "true");

        globals.put("DiffXSum_Weights", "1,1,1");
        globals.put("PosDiffXSum_Weights", "1");
        //globals.put("LaserXPositionReferencePropInt_Gain", "1");

        globals.put("CollectControlOutput_XShift", "0");
        globals.put("CollectControlOutput_YShift", "0");
        globals.put("CollectControlOutput_InputTolerance", "0.001");

        globals.put("LaserXVelocityControlReference_Weights", "1,1");

        globals.put("VelocityActive_Upper", "0");
        globals.put("VelocityActive_Lower", "0");
        globals.put("VelocityActive_BandWidth", "10");
        globals.put("VelocityActive_BandValue", "Infinity");
        globals.put("VelocityActive_Threshold", "0");

        globals.put("AtomOffsetReference_Constant", "0");
        globals.put("AtomOffsetControlOutput_XShift", "0");
        globals.put("AtomOffsetControlOutput_YShift", "0");
        globals.put("AtomOffsetControlOutput_InputTolerance", "0");
        globals.put("AtomOffsetControlOutput_InScale", "2.0");
        globals.put("AtomOffsetControlOutput_OutScale", "0.1");

        switch (qmtype) {
            case "shovel":
                globals.put("LaserXPositionControlOutput_InScale", "2");
                globals.put("LaserXPositionControlOutput_OutScale", "0.02");
                globals.put("LaserXVelocityControlInput_Smoothness", "0");
                globals.put("LaserXVelocityControlOutput_Gain", ".5");
                globals.put("LaserXAccelerationControlInput_Smoothness", "0");
                globals.put("LaserXAccelerationControlOutput_Gain", "5000");
                globals.put("LaserXAccelerationControlOutput_Slow", "10000");
                globals.put("MoveLaserReference_Constant", "1");
                globals.put("MoveLaserReferenceActive_Key", "1");
                globals.put("CollectReference_Constant", "0");
                globals.put("CollectReferenceActive_Key", "2");
                globals.put("TargetXActive_Key", "3");
                globals.put("AtomOffsetReferenceActive_Key", "3");

                //globals.put("CollectControlInput_Smooth", "0");
                //globals.put("CollectControlOutput_Gain", "5000");
                //globals.put("CollectControlOutput_Slow", "10000");
                globals.put("CollectControlOutput_InScale", "100");
                globals.put("CollectControlOutput_OutScale", ".02");
                break;
        }

        //globals.put("MoveLaserControlOutput_Gain", "5");
        //globals.put("MoveLaserControlOutput_Slow", "1000");
        //globals.put("MoveLaserControlOutput_Integrate", "false");
        Utils.loadPars(Utils.getParametersFileName(type, dir, suffix));

        Utils.saveToXML(xml.run(outputReferenceNames), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    public XML_005_008_QMBHWShovelling() {
    }

    public Layers run(String[] outputReferenceNames) throws Exception {
        Layers layers = new Layers();
        layers.setType("Model");
        layers.setDescription("QuantumMoves");
        Utils.setOrderedControllers(layers, new String[]{"Env", "QMSensor", "QMVariables", "SequenceControl", "MoveLaserControl",
            "CollectControl", "LaserXPositionControl", "LaserXVelocityControl", "LaserXAccelerationControl", "AtomOffsetControl",
            "AmplitudeControl", "QMActuator"});

        layers.getLayer().add(configureLayer0("Interface"));
        layers.getLayer().add(configureLayer1("Variables"));
        layers.getLayer().add(Utils.emptyLayer("LaserControl"));
        layers.getLayer().add(configureLayer2("Sequence", outputReferenceNames));

        Modules.moduleAtomOffset(layers, 1, 2, "SequenceControlOutput");

        {
            Modules.moduleLaserXPosition(layers, 2, 2, 2, "TargetXActive", "VelocityActive", new String[][]{{"LaserXPositionControlOutput"}, {"CollectControlOutput"}});
            ControlFunction transfer = Utils.limitBandedFunction("VelocityActive", "...", "MoveLaserControlReference", false);
            Utils.addTransferFunction(layers, "LaserXVelocityControl", transfer, Utils.REFERENCE, 0);
        }

        return layers;
    }

    private static Layers.Layer configureLayer0(String name) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();
        controllers.add(configureControllerEnv("Env", "QuantumMoves"));
        controllers.add(Common.configureControllerQMSensor("QMSensor"));
        controllers.add(configureQMActuator());

        return layer;
    }

    private static Controller configureQMActuator() throws Exception {

        Layers.Layer.Controller qmactuator = Common.configureQMActuator("QMActuator", "DiffXSum", "DiffYSum");

        ControlFunction reference = Utils.configureControlFunction("DiffXSum", "...", "WeightedSum",
                new String[][]{{"Weights", Globals.getInstance().get("DiffXSum_Weights"), ""}},
                new String[][]{{"MoveLaserControlOutput"}, {"LaserXAccelerationControlOutput"}, {"AtomOffsetControlOutput"}});
        Utils.addFunction(qmactuator, reference, Utils.REFERENCE, false);

        ControlFunction transfer = Utils.configureControlFunction("DiffYSum", "...", "Addition",
                null, new String[][]{{"AmplitudeControlOutput"}, {"DiffY"}});
        Utils.addTransferFunction(qmactuator, transfer, Utils.REFERENCE);

        transfer = Utils.configureControlFunction("DiffXSumLimited", "...", "Limit",
                new String[][]{{"Min", Globals.getInstance().get("DiffXSumLimited_Min"), "Double"},
                {"Max", Globals.getInstance().get("DiffXSumLimited_Max"), "Double"}
                }, new String[][]{{"DiffXSum"}});
        Utils.addTransferFunction(qmactuator, transfer, Utils.REFERENCE);

        return qmactuator;
    }

    private static Layers.Layer configureLayer1(String name) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();
        String[][] displayList = Common.getDisplayList();
        String[][] maxLinks = Common.getMaxLinks();
        String[][] recordLinks = new String[][]{{"MaxResults", "KeyData"}, {"Level", "Data:0"}, {"SimTime", "Data"},
        {"MoveLaserControlOutput", "Parameters"}, {"AmplitudeControlReference", "Parameters"}, {"AmplitudeControlOutput", "Parameters"},
        {"AtomOffsetReference", "Parameters"}, {"MoveLaserReference", "Parameters"}, {"CollectReference", "Parameters"},
        {"AtomOffsetControlOutput", "Parameters"}, {"LaserXPositionControlOutput", "Parameters"}, {"LaserXVelocityControlInput", "Parameters"},
        {"LaserXVelocityControlOutput", "Parameters"}, {"LaserXAccelerationControlInput", "Parameters"}, {"LaserXAccelerationControlOutput", "Parameters"},
        {"SequenceControlInput", "Parameters"}, {"CollectControlOutput", "Parameters"},
        {"SimTime", "Parameters"}, {"Reset", "Reset"}};

        Controller qmvariables = Common.configureQMVariables("QMVariables", displayList, maxLinks, recordLinks, "VelocityActive");
        controllers.add(qmvariables);
        ControlFunction transfer = Utils.configureControlFunction("AtomXOffsetLimited", "...", "Limit",
                new String[][]{{"Min", Globals.getInstance().get("AtomXOffsetLimited_Min"), "Double"},
                {"Max", Globals.getInstance().get("AtomXOffsetLimited_Max"), "Double"}
                }, new String[][]{{"AtomXOffset"}});
        Utils.addTransferFunction(qmvariables, transfer, Utils.OUTPUT);


        /*prefix = "AmplitudeControl";
        reference = Utils.constantFunction(prefix + "Reference");
        controllers.add(Generic.configureSigmoidController(prefix, "LaserY", reference));*/
        String prefix = "AmplitudeControl";
        ControlFunction reference = Utils.constantFunction(prefix + "Reference");
        controllers.add(Generic.configureSigmoidController(prefix, "LaserY", reference, false));

        return layer;
    }

    private static Layers.Layer configureLayer2(String name, String[] outputReferenceNames) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();

        String prefix = "SequenceControl";
        String[][] inputLinks = new String[][]{{"MoveLaserControlError"}, {"CollectControlError"}, {"LaserXPositionControlError"}, {"Reset", "Reset"}};
        ControlFunction reference = Utils.constantFunction(prefix + "Reference");

        Controller sequence = Generic.configureSequenceController(prefix, inputLinks, reference, outputReferenceNames, new boolean[]{true, true, false});
        controllers.add(sequence);

        prefix = "MoveLaserControl";
        reference = Utils.configureControlFunction(prefix + "Reference", "...", "", null, new String[][]{{"MoveLaserReferenceActive"}});
        controllers.add(Generic.configureProportionalController("MoveLaserControl", "LaserX", reference));

        prefix = "CollectControl";
        //inputLinks = new String[][]{{"LaserX"}, {"Reset", "Reset"}};
        //String[][] outputLinks = new String[][]{{prefix + "Error"}, {"Reset", "Reset"}};
        Controller collectControl = Generic.configureSigmoidController(prefix, "LaserX", "CollectReferenceActive", false);
        controllers.add(collectControl);

        ControlFunction transfer = Utils.configureControlFunction("PosDiffXSum", "...", "WeightedSum",
                new String[][]{{"Weights", Globals.getInstance().get("PosDiffXSum_Weights"), ""}},
                new String[][]{{"CollectControlOutput"}});
        Utils.addTransferFunction(collectControl, transfer, Utils.OUTPUT);

        //String[][] links = new String[][]{{"PosDiffXSum"}, {"Reset", "Reset"}, {"LaserOutputX", "ResetValue"}};
        String[][] links = new String[][]{{"PosDiffXSum"}, {"CollectControlReference"}};
        //transfer = Utils.proportionalIntegrationFunction("LaserXPositionReferencePropInt", inputLinks, true);
        transfer = Utils.configureControlFunction("LaserXPositionReferenceAdd", "...", "Addition", null, links);
        Utils.addTransferFunction(collectControl, transfer, Utils.OUTPUT);

        return layer;
    }

    private static Layers.Layer.Controller configureControllerEnv(String name, String prefix) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(name);
        {
            ControlFunction input = Utils.configureControlFunction("Rate", "Iteration rate", "Rate", null, null);
            Utils.addFunction(controller, input, Utils.INPUT, false);
            {
                ControlFunction transfer = Utils.configureControlFunction("Pause", "Function for pausing processing to maintain constant iteration rate", "Pause",
                        new String[][]{{"Pause", Globals.getInstance().get("Pause_Pause"), "Long"}, {"ConstantRate", "true", ""}}, null);
                Utils.addTransferFunction(controller, transfer, Utils.INPUT);

                transfer = Utils.configureControlFunction("Zero", "...", "Constant",
                        new String[][]{{"Constant", "0", ""}}, null);
                Utils.addTransferFunction(controller, transfer, Utils.INPUT);

                //transfer = Utils.constantFunction("ManualTargetX", false);
                //Utils.addTransferFunction(controller, transfer, Utils.INPUT);
            }
        }

        {
            ControlFunction reference = Utils.constantFunction("DiffY");
            Utils.addFunction(controller, reference, Utils.REFERENCE, false);
        }
        return controller;
    }

}
