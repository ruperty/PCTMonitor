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
public class XML_003_005_AccelerometerControl {

    private final boolean test = false;

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
                String file = Utils.getFileName("Robot", "003-005-AccelerometerControl.xml");
        XML_003_005_AccelerometerControl xml = new XML_003_005_AccelerometerControl();

        Globals globals = Globals.getInstance();
        globals.put("SmoothRate_Smoothness", "0.999");
        globals.put("SmoothRate_Initial", "0");
        globals.put("MeanRate_Initial", "0");
        globals.put("Pause_Pause", "100");

        globals.put("RotationWeightConsolidated_Constant", "1");

        globals.put("PhoneAccelerometerSensor_Port", "6669");
        globals.put("PhoneAccelerometerDirectionControlInput_Smoothness", "0.7");
        globals.put("PhoneAccelerometerSpeedControlInput_Smoothness", "0.7");

        globals.put("PhoneAccelerometerSpeedControlOutput_InScale", "0.2");
        globals.put("PhoneAccelerometerSpeedControlOutput_OutScale", "800");
        globals.put("PhoneAccelerometerSpeedControlOutput_XShift", "0");
        globals.put("PhoneAccelerometerSpeedControlOutput_YShift", "0");
        globals.put("PhoneAccelerometerSpeedControlOutput_InputTolerance", "0.4");

        globals.put("PhoneAccelerometerDirectionControlOutput_InScale", "0.2");
        globals.put("PhoneAccelerometerDirectionControlOutput_OutScale", "200");
        globals.put("PhoneAccelerometerDirectionControlOutput_XShift", "-8");
        globals.put("PhoneAccelerometerDirectionControlOutput_YShift", "185");
        globals.put("PhoneAccelerometerDirectionControlOutput_InputTolerance", "0.4");

        globals.put("B_Acceleration", "1000");
        globals.put("C_Acceleration", "1000");
        globals.put("B_Sign", "1");
        globals.put("C_Sign", "1");

        Utils.saveToXML(xml.run(), file);
        Utils.verify(file);

    }

    public XML_003_005_AccelerometerControl() {

    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("...");
        List layer = layers.getLayer();

        layer.add(configureLayer0("Interface"));
        layer.add(Utils.emptyLayer("AccelerometerControl"));
        Modules.modulePhoneAccelerometerSystems(layers, test, 1, true);
        Modules.moduleDriveMotorsOutput(layers, test);

        return layers;
    }

    public static Controller configureEnv() throws Exception {
        Controller env = Generic.configureControllerEnv();
        ControlFunction function = Utils.configureControlFunction("RotationWeightConsolidated", "Rotation weight, 1 = no rotation", "Constant", new String[][]{{"Constant", Globals.getInstance().get("RotationWeightConsolidated_Constant"), "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);

        return env;
    }

    private Layer configureLayer0(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        controllers.add(configureEnv());
        controllers.add(configureControllerMotorConsolidation());

        return layer;
    }

    public static Controller configureControllerMotorConsolidation() throws Exception {
        Controller controller = new Controller();
        controller.setName("MotorConsolidation");
        Functions functions = new Functions();

        OutputFunctions outputFunctions = new OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("SpeedConsolidated", "Exclusive function of speed input signals", "WeightedExclusive",
                new String[][]{{"Weights", "1", "String"}}, new String[][]{{"PhoneAccelerometerSpeedControlOutput"}});

        outputFunctions.setOutput(output);
        {
            Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer = Utils.configureControlFunction("DirectionSpeedAdjustmentConsolidated", "Exclusive function of direction input signals", "WeightedExclusive",
                    new String[][]{{"Weights", "1", "String"}}, new String[][]{{"PhoneAccelerometerDirectionControlOutput"}});

            transfersList.add(transfer);
            outputFunctions.setTransfers(transfers);
        }
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

}
