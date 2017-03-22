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
import uk.co.moonsit.config.functions.Globals;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_005_001_QuantumMovesBasic {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        String suffix = "005-001-QuantumMovesBasic";
        String file = Utils.getFileName("Models", "QuantumMoves", suffix + ".xml");
        System.out.println(file);

        XML_005_001_QuantumMovesBasic xml = new XML_005_001_QuantumMovesBasic();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "100");
        globals.put("Reset_Pulse", "0");

        globals.put("SimTime_Timeout", "30000");
        globals.put("SimTime_Host", "localhost");
        globals.put("SimTime_Port", "30000");
        globals.put("SimTime_X", "-1550");
        globals.put("SimTime_Y", "0");
        globals.put("SimTime_Width", "640");
        globals.put("SimTime_Height", "480");
        globals.put("Max_Model", suffix);
        globals.put("TargetY_Constant", "0");

        String level = "1";
        globals.put("SimTime_Level", level);
        globals.put("Level_Constant", level);

        globals.put("SimTime_DiffX", "0.01");
        globals.put("SimTime_DiffY", "0");
        //globals.put("QM_Level", "1");

        globals.put("EnergyVariance_Size", "5");
        globals.put("AtomXOffsetVariance_Size", "5");

        globals.put("SimTime_TargetPercentage", "50");
        globals.put("SimTime_TimeLimit", "0");
        globals.put("QMDisplay_X", "0");
        globals.put("QMDisplay_Y", "0");
        globals.put("QMMaxDisplay_X", "200");
        globals.put("QMMaxDisplay_Y", "0");
        globals.put("QMMaxDisplay_Width", "200");
        globals.put("QMMaxDisplay_Height", "150");
        globals.put("QMMaxDisplay_Font", "24");
        globals.put("QMMaxDisplay_Alignment", "Vertical");

        globals.put("AtomOffsetControlOutput_InScale", "2.5");
        globals.put("AtomOffsetControlOutput_OutScale", "0.05");
        globals.put("AtomOffsetControlOutput_XShift", "0");
        globals.put("AtomOffsetControlOutput_YShift", "0");
        globals.put("AtomOffsetControlOutput_InputTolerance", "0");

        globals.put("EnergyChangeControlOutput_InScale", ".5");
        globals.put("EnergyChangeControlOutput_OutScale", "5.0");
        globals.put("EnergyChangeControlOutput_XShift", "0");
        globals.put("EnergyChangeControlOutput_YShift", "0");
        globals.put("EnergyChangeControlOutput_InputTolerance", "0");

        globals.put("LaserOutputIntegrateY_Gain", "1");
        /*globals.put("LaserOutputIntegrateY_Slow", "100000");
        globals.put("LaserOutputIntegrateY_Integrate", "true");
        globals.put("LaserOutputIntegrateY_Tolerance", "0");*/
        globals.put("LaserOutputIntegrateX_Gain", "1");
        /*globals.put("LaserOutputIntegrateX_Slow", "10000");
        globals.put("LaserOutputIntegrateX_Integrate", "true");
        globals.put("LaserOutputIntegrateX_Tolerance", "0");*/

        globals.put("Max_Key", "Score");
        globals.put("QMDisplay_Width", "200");
        globals.put("QMDisplay_Height", "150");
        globals.put("QMDisplay_Font", "24");
        globals.put("QMDisplay_Alignment", "Vertical");

        globals.put("AtomOffsetControlReference_Constant", "0");
        globals.put("EnergyChangeControlReference_Constant", "0");
        //globals.put("AtomOffsetControlOutput_Gain", "0.1");
        /*globals.put("AtomOffsetControlOutput_Slow", "1000");
        globals.put("AtomOffsetControlOutput_Integrate", "true");
        globals.put("AtomOffsetControlOutput_Tolerance", "0");*/

        Utils.saveToXML(xml.run(), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    public XML_005_001_QuantumMovesBasic() {
    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Model");
        layers.setDescription("Manual movement of the laser and atom. "
                + "DiffX is an amount added on to the current position of the laser, which will move the laser, e.g. 0.01. "
                + "The AtomOffset system endeavours to keep the difference between the atom and laser at a reference value. "
                + "If the reference is non-zero (e.g. -0.01) the atom will surf on the moving laser. "
                + "If the reference is zero the system will damp down any sloshing, e.g gain -0.4.");
        Utils.setOrderedControllers(layers, new String[]{"Env", "QMSensor", "QMVariables", "AtomOffsetControl", "EnergyChangeControl", "QMActuator"});

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

        {
            Controller qmactuator = Common.configureQMActuator("QMActuator", "DiffXSum", "DiffYSum");
            controllers.add(qmactuator);

            ControlFunction reference = Utils.configureControlFunction("DiffXSum", "...", "Addition",
                    null, new String[][]{{"AtomOffsetControlOutput"}, {"DiffX"}});
            Utils.addFunction(qmactuator, reference, Utils.REFERENCE, false);

            ControlFunction transfer = Utils.configureControlFunction("DiffYSum", "...", "Addition",
                    null, new String[][]{{"EnergyChangeControlOutput"}, {"DiffY"}});
            Utils.addTransferFunction(qmactuator, transfer, Utils.REFERENCE);
        }
        return layer;
    }

    private static Layers.Layer configureLayer1(String name) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();

        String[][] displayList = Common.getDisplayList();
        String[][] maxDisplayList = Common.getMaxLinks();
        controllers.add(Common.configureQMVariables("QMVariables", displayList, maxDisplayList, null, null));

        String prefix = "AtomOffsetControl";
        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "", "Constant",
                new String[][]{{"Constant", Globals.getInstance().get(prefix + "Reference_Constant"), "Double"}},
                null);
        controllers.add(Common.configureAtomOffsetControllerSigmoid(prefix, reference));

        prefix = "EnergyChangeControl";
        reference = Utils.configureControlFunction(prefix + "Reference", "", "Constant",
                new String[][]{{"Constant", Globals.getInstance().get(prefix + "Reference_Constant"), "Double"}},
                null);
        controllers.add(Common.configureEnergyChangeControllerSigmoid(prefix, reference));
        return layer;
    }

    private static Layers.Layer.Controller configureControllerEnv(String name, String prefix) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(name);
        {
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
                }
            }

            {
                ControlFunction reference = Utils.configureControlFunction("DiffX", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get(prefix + "_DiffX"), "Integer"}}, null);
                Utils.addFunction(controller, reference, Utils.REFERENCE, false);
                {
                    ControlFunction transfer = Utils.configureControlFunction("DiffY", "...", "Constant",
                            new String[][]{{"Constant", Globals.getInstance().get(prefix + "_DiffY"), "Integer"}}, null);
                    Utils.addTransferFunction(controller, transfer, Utils.REFERENCE);
                }
            }
        }

        return controller;
    }

}
