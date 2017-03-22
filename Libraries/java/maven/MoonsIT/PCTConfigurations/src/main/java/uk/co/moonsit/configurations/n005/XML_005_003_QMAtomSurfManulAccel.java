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
import uk.co.moonsit.config.functions.Globals;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_005_003_QMAtomSurfManulAccel {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        String suffix = "005-003-QMAtomSurfManualAccel";
        String file = Utils.getFileName("Models", "QuantumMoves", suffix + ".xml");

        System.out.println(file);

        XML_005_003_QMAtomSurfManulAccel xml = new XML_005_003_QMAtomSurfManulAccel();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "250");
                globals.put("Reset_Pulse", "0");

        globals.put("SimTime_Timeout", "30000");
        globals.put("SimTime_Host", "localhost");
        globals.put("SimTime_Port", "30000");
        globals.put("SimTime_X", "-1550");
        globals.put("SimTime_Y", "0");
        globals.put("SimTime_Width", "640");
        globals.put("SimTime_Height", "480");
        globals.put("Max_Model", suffix);

        globals.put("Constant_Constant", "0");
        String level = "1";
        globals.put("SimTime_Level", level);
        globals.put("Level_Constant", level);
        globals.put("TargetY_Constant", "0");
        globals.put("EnergyVariance_Size", "5");
        globals.put("AtomXOffsetVariance_Size", "5");
        //globals.put("QM_Level", "1");
        globals.put("Max_Key", "Score");
        globals.put("SimTime_TargetPercentage", "50");
        globals.put("SimTime_TimeLimit", "1.002");
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


        //globals.put("PositionControlOutput_Gain", "1");

        globals.put("AtomOffsetControlOutput_Gain", "100");
        globals.put("AtomOffsetControlOutput_Slow", "1000");

        globals.put("LaserAccelControlOutput_Gain", "800");
        globals.put("LaserAccelControlOutput_Slow", "1000");
        globals.put("LaserVelocityControlReference_Gain", "1200");
        globals.put("LaserVelocityControlReference_Slow", "1000");
        globals.put("LaserVelocityControlOutput_Gain", "1");

        //globals.put("AtomOffsetControlReference_InScale", "10");
        //globals.put("AtomOffsetControlReference_OutScale", "0.01875");
        //globals.put("AtomOffsetControlReference_XShift", "-0.1875");
        //globals.put("AtomOffsetControlReference_YShift", "0.01875");
        //globals.put("AtomOffsetControlReference_InputTolerance", "0");

        globals.put("AtomOffsetControlOutput_Integrate", "true");
        globals.put("AtomOffsetControlOutput_Tolerance", "0");
        globals.put("LaserAccelControlOutput_Tolerance", "0.0000");
        globals.put("LaserAccelControlOutput_Integrate", "true");
        globals.put("LaserVelocityControlReference_Tolerance", "0.00001");
        globals.put("LaserVelocityControlReference_Integrate", "true");
        //globals.put("LaserVelocityControlOutput_Integrate", "false");

        globals.put("LaserOutputIntegrateY_Gain", "1");
        globals.put("LaserOutputIntegrateX_Gain", "1");

        globals.put("AccelTarget_Constant", "0");

        Utils.saveToXML(xml.run(), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    public XML_005_003_QMAtomSurfManulAccel() {
    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Model");
        layers.setDescription("QuantumMoves");
        Utils.setOrderedControllers(layers, new String[]{"Env", "QMSensor", "QMVariables", /*"PositionControl", */ "AtomOffsetControl",
            "LaserAccelControl", "LaserVelocityControl", "QMActuator"});

        layers.getLayer().add(configureLayer0("Interface"));
        layers.getLayer().add(configureLayer1("Variables"));
        layers.getLayer().add(configureLayer2("Controllers"));

        return layers;
    }

    private static Layers.Layer configureLayer0(String name) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();
        controllers.add(configureControllerEnv("Env", "SimTime"));
        controllers.add(Common.configureControllerQMSensor("QMSensor"));
        controllers.add(Common.configureQMActuator("QMActuator", "LaserVelocityControlOutput", "TargetY"));

        return layer;
    }

    private static Layers.Layer configureLayer1(String name) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();

        String[][] displayList = Common.getDisplayList();
        String[][] maxDisplayList = Common.getMaxLinks();
        controllers.add(Common.configureQMVariables("QMVariables", displayList, maxDisplayList, null, null));

        String prefix = "LaserAccelControl";
        controllers.add(Common.configureManualLaserAccelController(prefix, "AtomOffsetControlOutput"));
        //controllers.add(Common.configureLaserAccelController(prefix, "Constant"));

        prefix = "LaserVelocityControl";
        controllers.add(Common.configureLaserVelocityController(prefix, "LaserAccelControlOutput"));

        return layer;
    }

    private static Layers.Layer configureLayer2(String name) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();

        /*
        String prefix = "PositionControl";
        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "Division", null, new String[][]{{"TargetX"}, {"Constant"}});
        controllers.add(Generic.configureProportionalController(prefix, "LaserX", reference));
         */
        String prefix = "AtomOffsetControl";
        //reference = Utils.sigmoidFunction(prefix + "Reference", "PositionControlOutput", "false");
        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "...", "Constant",
                new String[][]{{"Constant", "0.01", "Integer"}}, null);
        controllers.add(Common.configureAtomOffsetControllerIntegration(prefix, reference));

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
                transfer = Utils.configureControlFunction("Constant", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get( "Constant_Constant"), "Integer"}}, null);
                Utils.addTransferFunction(controller, transfer, Utils.INPUT);

                transfer = Utils.configureControlFunction("Zero", "...", "Constant",
                        new String[][]{{"Constant", "0", ""}}, null);
                Utils.addTransferFunction(controller, transfer, Utils.INPUT);

            }
        }

        return controller;
    }

}
