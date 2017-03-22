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
import uk.co.moonsit.config.functions.Modules;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_005_006_PVA_DampenControl {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        String suffix = "005-006-PVA-DampenControl";
        String file = Utils.getFileName("Models", "QuantumMoves", suffix + ".xml");
        int level = 5;
        System.out.println(file);

        XML_005_006_PVA_DampenControl xml = new XML_005_006_PVA_DampenControl();
        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "20");
        globals.put("SimTime_Timeout", "30000");
        globals.put("SimTime_Host", "localhost");
        globals.put("SimTime_Port", "30000");
        globals.put("SimTime_X", "400");
        globals.put("SimTime_Y", "0");
        globals.put("SimTime_Width", "1024");
        globals.put("SimTime_Height", "750");
        globals.put("Max_Model", suffix);

        globals.put("SimTime_Level", Integer.toString(level));
        globals.put("SimTime_TimeLimit", "0.3");
        globals.put("Max_Key", "Score");
        globals.put("Level_Constant", Integer.toString(level));
        globals.put("TargetY_Constant", "0");
        globals.put("EnergyVariance_Size", "5");
        globals.put("AtomXOffsetVariance_Size", "5");
        globals.put("Reset_Pulse", "0");

        globals.put("RecordQM_Threshold", "800000");
        globals.put("RecordQM_Key", "Score");

        globals.put("SimTime_DiffY", "0");

        globals.put("QMDisplay_Width", "200");
        globals.put("QMDisplay_Height", "150");
        globals.put("QMDisplay_Font", "36");
        globals.put("QMDisplay_Alignment", "Vertical");

        globals.put("QMDisplay_X", "0");
        globals.put("QMDisplay_Y", "0");
        globals.put("QMMaxDisplay_X", "200");
        globals.put("QMMaxDisplay_Y", "0");
        globals.put("QMMaxDisplay_Width", "200");
        globals.put("QMMaxDisplay_Height", "150");
        globals.put("QMMaxDisplay_Font", "36");
        globals.put("QMMaxDisplay_Alignment", "Vertical");

        //globals.put("LaserXPositionControlOutput_Gain", "0.02");//0.05
        /*globals.put("LaserXPositionControlOutput_Slow", "1000");
        globals.put("LaserXPositionControlOutput_Tolerance", "0");
        globals.put("LaserXPositionControlOutput_Integrate", "true");*/
        globals.put("LaserXPositionControlOutput_XShift", "0");
        globals.put("LaserXPositionControlOutput_YShift", "0");
        globals.put("LaserXPositionControlOutput_InputTolerance", "0");

        /*globals.put("LaserXVelocityControlOutput_Slow", "1000");
        globals.put("LaserXVelocityControlOutput_Tolerance", "0");
        globals.put("LaserXVelocityControlOutput_Integrate", "false");*/
        globals.put("LaserXAccelerationControlOutput_Tolerance", "0");

        globals.put("LaserXAccelerationControlOutput_Integrate", "true");

        globals.put("LaserOutputIntegrateY_Gain", "1");
        globals.put("LaserOutputIntegrateX_Gain", "1");

        globals.put("AtomOffsetControlReference_Constant", "0");
        globals.put("AtomOffsetControlOutput_XShift", "0");
        globals.put("AtomOffsetControlOutput_YShift", "0");
        globals.put("AtomOffsetControlOutput_InputTolerance", "0");


        /*
        globals.put("EnergyControlOutput_Gain", "100");
        globals.put("EnergyControlOutput_Slow", "10000");
        globals.put("EnergyControlOutput_Tolerance", "0");
        globals.put("EnergyControlOutput_Integrate", "true");
        globals.put("EnergyControlInput_Smooth", "0.1"); */
        globals.put("EnergyControlOutput_XShift", "0");
        globals.put("EnergyControlOutput_YShift", "0");
        globals.put("EnergyControlOutput_InputTolerance", "0");

        //globals.put("DiffYReference_Constant", "-5");
        configure(level);
        Utils.saveToXML(xml.run(), file, "../../Layers.xsd");
        Utils.verify(file);
    }

    private static void configure(int level) {
        Globals globals = Globals.getInstance();

        switch (level) {
            case 1:
                globals.put("LaserXPositionControlOutput_InScale", "2.4");
                globals.put("LaserXPositionControlOutput_OutScale", "0.022");
                globals.put("LaserXVelocityControlInput_Smoothness", "0.25");
                globals.put("LaserXVelocityControlOutput_Gain", ".5");
                globals.put("LaserXAccelerationControlInput_Smoothness", "0.75");
                globals.put("LaserXAccelerationControlOutput_Gain", "5000");
                globals.put("LaserXAccelerationControlOutput_Slow", "10000");
                globals.put("AtomOffsetControlOutput_InScale", "2.0");
                globals.put("AtomOffsetControlOutput_OutScale", "0.1");
                globals.put("EnergyControlReference_Constant", "-425");
                globals.put("EnergyControlOutput_InScale", "0.05");
                globals.put("EnergyControlOutput_OutScale", "8");
                globals.put("SimTime_TargetPercentage", "10");
                break;
            case 2:
                globals.put("LaserXPositionControlOutput_InScale", "2.5");
                globals.put("LaserXPositionControlOutput_OutScale", "0.025");
                globals.put("LaserXVelocityControlInput_Smoothness", "0.55");
                globals.put("LaserXVelocityControlOutput_Gain", ".65");
                globals.put("LaserXAccelerationControlInput_Smoothness", "0.15");
                globals.put("LaserXAccelerationControlOutput_Gain", "20000");
                globals.put("LaserXAccelerationControlOutput_Slow", "100000");
                globals.put("AtomOffsetControlOutput_InScale", "3.0");
                globals.put("AtomOffsetControlOutput_OutScale", "0.12");
                globals.put("EnergyControlReference_Constant", "-500");
                globals.put("EnergyControlOutput_InScale", "0.02");
                globals.put("EnergyControlOutput_OutScale", "5");
                globals.put("SimTime_TargetPercentage", "1");
                break;
            case 3:
                globals.put("LaserXPositionControlOutput_InScale", "2.5");
                globals.put("LaserXPositionControlOutput_OutScale", "0.023");
                globals.put("LaserXVelocityControlInput_Smoothness", "0.2");
                globals.put("LaserXVelocityControlOutput_Gain", ".5");
                globals.put("LaserXAccelerationControlInput_Smoothness", "0");
                globals.put("LaserXAccelerationControlOutput_Gain", "5000");
                globals.put("LaserXAccelerationControlOutput_Slow", "10000");
                globals.put("AtomOffsetControlOutput_InScale", "2.25");
                globals.put("AtomOffsetControlOutput_OutScale", "0.12");
                globals.put("EnergyControlReference_Constant", "-500");
                globals.put("EnergyControlOutput_InScale", "0.02");
                globals.put("EnergyControlOutput_OutScale", "13");
                globals.put("SimTime_TargetPercentage", "10");
                break;
            case 4:
                globals.put("LaserXPositionControlOutput_InScale", "2");
                globals.put("LaserXPositionControlOutput_OutScale", "0.02");
                globals.put("LaserXVelocityControlInput_Smoothness", "0.69");
                globals.put("LaserXVelocityControlOutput_Gain", ".2");
                globals.put("LaserXAccelerationControlInput_Smoothness", "0.31");
                globals.put("LaserXAccelerationControlOutput_Gain", "5000");
                globals.put("LaserXAccelerationControlOutput_Slow", "10000");
                globals.put("AtomOffsetControlOutput_InScale", "3.0");
                globals.put("AtomOffsetControlOutput_OutScale", "0.25");
                globals.put("EnergyControlReference_Constant", "-200");
                globals.put("EnergyControlOutput_InScale", "0.06");
                globals.put("EnergyControlOutput_OutScale", "10");
                globals.put("SimTime_TargetPercentage", "2");
                break;
            case 5:
                globals.put("LaserXPositionControlOutput_InScale", "2");
                globals.put("LaserXPositionControlOutput_OutScale", "0.03");
                globals.put("LaserXVelocityControlInput_Smoothness", "0.2");
                globals.put("LaserXVelocityControlOutput_Gain", ".5");
                globals.put("LaserXAccelerationControlInput_Smoothness", "0.1");
                globals.put("LaserXAccelerationControlOutput_Gain", "5000");
                globals.put("LaserXAccelerationControlOutput_Slow", "10000");
                globals.put("AtomOffsetControlOutput_InScale", "3.0");
                globals.put("AtomOffsetControlOutput_OutScale", "0.2");
                globals.put("EnergyControlReference_Constant", "-450");
                globals.put("EnergyControlOutput_InScale", "0.05");
                globals.put("EnergyControlOutput_OutScale", "10");
                globals.put("SimTime_TargetPercentage", "10");
                break;
        }
    }

    public XML_005_006_PVA_DampenControl() {
    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Model");
        layers.setDescription("QuantumMoves");
        //Utils.setOrderedControllers(layers, new String[]{"Env", "QMSensor", "QMVariables", "PositionControl", "QMActuator"});
        Utils.setOrderedControllers(layers, new String[]{"Env", "QMSensor", "QMVariables", "LaserXPositionControl",
            "LaserXVelocityControl", "LaserXAccelerationControl", "AtomOffsetControl", "EnergyControl", "QMActuator"});

        layers.getLayer().add(configureLayer0("Interface"));
        layers.getLayer().add(configureLayer1("Variables"));
        layers.getLayer().add(Utils.emptyLayer("Control"));

        Modules.moduleAtomOffset(layers, 1, 2);
        Modules.moduleLaserXPosition(layers, 2, 2, 2, "TargetX");

        return layers;
    }

    private static Layers.Layer configureLayer0(String name) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();
        controllers.add(configureControllerEnv("Env", "SimTime"));
        controllers.add(Common.configureControllerQMSensor("QMSensor"));

        {
            Layers.Layer.Controller qmactuator = Common.configureQMActuator("QMActuator", "DiffXSum", "DiffYSum");
            controllers.add(qmactuator);

            ControlFunction reference = Utils.configureControlFunction("DiffXSum", "...", "Addition",
                    null, new String[][]{{"AtomOffsetControlOutput"}, {"LaserXAccelerationControlOutput"}});
            Utils.addFunction(qmactuator, reference, Utils.REFERENCE, false);

            ControlFunction transfer = Utils.configureControlFunction("DiffYSum", "...", "Addition",
                    null, new String[][]{{"EnergyControlOutput"}, {"DiffY"}});
            Utils.addTransferFunction(qmactuator, transfer, Utils.REFERENCE);

            //ControlFunction transfer = Utils.configureControlFunction("DiffYSum", "...", "Addition",
            //      null, new String[][]{{"EnergyChangeControlOutput"}, {"DiffY"}});        
            //Utils.addTransferFunction(qmactuator, transfer, Utils.REFERENCE);
        }

        return layer;
    }

    private static Layers.Layer configureLayer1(String name) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();
        String[][] displayList = Common.getDisplayList();
        String[][] maxDisplayList = Common.getMaxLinks();

        // List of links of parameters to record
        String[][] recordLinks = new String[][]{{"MaxResults", "KeyData"}, {"Level", "Data:0"}, {"SimTime", "Data"},
        {"LaserXPositionControlOutput", "Parameters"},
        {"LaserXVelocityControlInput", "Parameters"}, {"LaserXVelocityControlOutput", "Parameters"},
        {"LaserXAccelerationControlInput", "Parameters"}, {"LaserXAccelerationControlOutput", "Parameters"},
        {"AtomOffsetControlOutput", "Parameters"},
        {"EnergyControlReference", "Parameters"}, {"EnergyControlOutput", "Parameters"},
        {"SimTime", "Parameters"}, {"Reset", "Reset"}};

        controllers.add(Common.configureQMVariables("QMVariables", displayList, maxDisplayList, recordLinks, null));

        /*String prefix = "AtomOffsetControl";
        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "", "Constant",
                new String[][]{{"Constant", Globals.getInstance().get(prefix + "Reference_Constant"), "Double"}},
                null);
        controllers.add(Common.configureAtomOffsetControllerSigmoid(prefix, reference));
         */
        String prefix = "EnergyControl";
        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "", "Constant",
                new String[][]{{"Constant", Globals.getInstance().get(prefix + "Reference_Constant"), "Double"}},
                null);

        controllers.add(Generic.configureSigmoidController(prefix, "Energy", reference, false));

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
            }

            {
                ControlFunction reference = Utils.configureControlFunction("DiffY", "...", "Constant",
                        new String[][]{{"Constant", Globals.getInstance().get(prefix + "_DiffY"), "Integer"}}, null);
                Utils.addFunction(controller, reference, Utils.REFERENCE, false);
            }
        }

        return controller;
    }

}
