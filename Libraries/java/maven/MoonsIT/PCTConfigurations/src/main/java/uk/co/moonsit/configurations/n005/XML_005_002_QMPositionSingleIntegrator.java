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
import uk.co.moonsit.config.functions.Common;
import uk.co.moonsit.config.functions.Generic;
import uk.co.moonsit.config.functions.Globals;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_005_002_QMPositionSingleIntegrator {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        String suffix = "005-002-QMPositionSingleIntegrator";
        String file = Utils.getFileName("Models", "QuantumMoves", suffix + ".xml");
        System.out.println(file);

        XML_005_002_QMPositionSingleIntegrator xml = new XML_005_002_QMPositionSingleIntegrator();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "20");
        globals.put("Reset_Pulse", "0");

        globals.put("SimTime_Timeout", "30000");
        globals.put("SimTime_Host", "localhost");
        globals.put("SimTime_Port", "30000");
        globals.put("SimTime_X", "-1550");
        globals.put("SimTime_Y", "0");
        globals.put("SimTime_Width", "640");
        globals.put("SimTime_Height", "480");
        globals.put("Max_Model", suffix);
        globals.put("Max_Key", "Score");
        String level = "1";
        globals.put("SimTime_Level", level);
        globals.put("Level_Constant", level);
        globals.put("TargetY_Constant", "0");
        globals.put("EnergyVariance_Size", "5");
        globals.put("AtomXOffsetVariance_Size", "5");

        globals.put("RecordQM_Threshold", "900000");
        globals.put("RecordQM_Key", "Score");

        globals.put("SimTime_TargetPercentage", "50");
        globals.put("SimTime_TimeLimit", "0.4");
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

        globals.put("PositionControlOutput_InScale", "5");
        globals.put("PositionControlOutput_OutScale", "0.018");
        globals.put("PositionControlOutput_XShift", "0");
        globals.put("PositionControlOutput_YShift", "0");
        globals.put("PositionControlOutput_InputTolerance", "0");

        globals.put("LaserOutputIntegrateY_Gain", "1");
        globals.put("LaserOutputIntegrateX_Gain", "1");


        Utils.saveToXML(xml.run(), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    public XML_005_002_QMPositionSingleIntegrator() {
    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Model");
        layers.setDescription("QuantumMoves");
        Utils.setOrderedControllers(layers, new String[]{"Env", "QMSensor", "QMVariables", "PositionControl", "QMActuator"});

        layers.getLayer().add(configureLayer0("Interface"));
        layers.getLayer().add(configureLayer1("Variables"));

        return layers;
    }

    private static Layers.Layer configureLayer0(String name) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();
        controllers.add(configureControllerEnv("Env", "SimTime"));
        controllers.add(Common.configureControllerQMSensor("QMSensor"));
        controllers.add(Common.configureQMActuator("QMActuator", "PositionControlOutput", "TargetY"));

        return layer;
    }

    private static Layers.Layer configureLayer1(String name) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();
        String[][] displayList = Common.getDisplayList();
        String[][] maxLinks = Common.getMaxLinks();
        String[][] recordLinks = new String[][]{{"MaxResults", "KeyData"}, {"Level", "Data:0"}, {"SimTime", "Data"},
        {"PositionControlOutput", "Parameters"}, {"SimTime", "Parameters"}, {"Reset", "Reset"}};

        controllers.add(Common.configureQMVariables("QMVariables", displayList, maxLinks, recordLinks, null));

        String prefix = "PositionControl";
        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "", null, new String[][]{{"TargetX"}});
        controllers.add(Generic.configureSigmoidController("PositionControl", "LaserX", reference, false));

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
            }
        }

        return controller;
    }

}
