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
package uk.co.moonsit.configurations;

import java.util.List;
import pct.moons.co.uk.schema.layers.ControlFunction;
import pct.moons.co.uk.schema.layers.Layers;
import pct.moons.co.uk.schema.layers.Layers.Layer;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.OutputFunctions;
import uk.co.moonsit.config.functions.Generic;
import uk.co.moonsit.config.functions.Globals;
import uk.co.moonsit.config.functions.Modules;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_041_001_CompassBearing {

    private final boolean test = false;

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
                String file = Utils.getFileName("Robot", "041-001-CompassBearing.xml");
        XML_041_001_CompassBearing xml = new XML_041_001_CompassBearing();

        Globals globals = Globals.getInstance();

        globals.put("CompassSensor_Type", "HiTechnicCompassSensor");
        globals.put("CompassSensor_Port", "S4");
        globals.put("CompassSensor_AngleType", "AngleClockwise");
        globals.put("CompassSensor_Interval", "5");

        globals.put("SmoothRate_Smoothness", "0.999");
        globals.put("SmoothRate_Initial", "0");
        globals.put("MeanRate_Initial", "0");
        globals.put("Pause_Pause", "10");
        globals.put("RotationWeightConsolidated_Constant", "1");

        globals.put("CompassVelocityReference_Constant", "0");
        globals.put("CompassBearingControlOutput_Gain", "-3.5");
        globals.put("CompassBearingControlReference_Type", "Constant");

        globals.put("LocationDistanceControlReference_InScale", "20000");
        globals.put("LocationDistanceControlReference_OutScale", "500");
        globals.put("LocationDistanceControlReference_XShift", "0");
        globals.put("LocationDistanceControlReference_YShift", "0");

        globals.put("B_Acceleration", "1000");
        globals.put("C_Acceleration", "1000");
        globals.put("B_Sign", "1");
        globals.put("C_Sign", "1");

        Utils.saveToXML(xml.run(), file);
        Utils.verify(file);
    }

    public XML_041_001_CompassBearing() {

    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Integration of beacon following and obstcale avoidance.");
        List layer = layers.getLayer();

        layer.add(configureLayer0("Interface"));
        layer.add(configureLayer1("CompassControl"));
        Modules.moduleDriveMotorsOutput(layers, test);
        Modules.moduleCompassSystems(layers, false, true, compassBearingReference());

        return layers;
    }

    public static Layers.Layer.Controller.Functions.ReferenceFunctions compassBearingReference() throws Exception {
        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction("CompassBearingControlReference",
                "Head direction reference, zero is centre", "",
                null, new String[][]{{"LocationBearingReference"}});

        referenceFunctions.setReference(reference);
        return referenceFunctions;
    }

    private Controller configureEnv() throws Exception {
        Controller env = Generic.configureControllerEnv();
        ControlFunction function = Utils.configureControlFunction("RotationWeightConsolidated", "Rotation weight, 1 = no rotation", "Constant", new String[][]{{"Constant", Globals.getInstance().get("RotationWeightConsolidated_Constant"), "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);

        function = Utils.configureControlFunction("CompassVelocityReference", "Velocity reference", "Constant", new String[][]{{"Constant", Globals.getInstance().get("CompassVelocityReference_Constant"), "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);

        function = Utils.configureControlFunction("LocationBearingReference", "Head direction reference, zero is centre", "Constant",
                new String[][]{{"Constant", "0", "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);

        return env;
    }

    private Layer configureLayer0(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        controllers.add(configureEnv());
        // controllers.add(Common.configureControllerMotorsInput());

        return layer;
    }

    private Layer configureLayer1(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        controllers.add(configureControllerMotorConsolidation());

        return layer;
    }

    private Controller configureControllerMotorConsolidation() throws Exception {
        Controller controller = new Controller();
        controller.setName("MotorConsolidation");
        Functions functions = new Functions();

        OutputFunctions outputFunctions = new OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("SpeedConsolidated", "Exclusive function of speed input signals", "WeightedExclusive",
                new String[][]{{"Weights", "1", "String"}}, new String[][]{{"CompassVelocityReference"}});

        outputFunctions.setOutput(output);
        {
            Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer = Utils.configureControlFunction("DirectionSpeedAdjustmentConsolidated", "Exclusive function of direction input signals", "WeightedExclusive",
                    new String[][]{{"Weights", "1", "String"}}, new String[][]{{"CompassBearingControlOutput"}});

            transfersList.add(transfer);
            outputFunctions.setTransfers(transfers);
        }
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

}
