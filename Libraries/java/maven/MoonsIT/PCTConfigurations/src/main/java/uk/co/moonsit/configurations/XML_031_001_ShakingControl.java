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
public class XML_031_001_ShakingControl {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        boolean test = false;
                String file = Utils.getFileName("Robot", "031-001-ShakingControl.xml");
        XML_031_001_ShakingControl xml = new XML_031_001_ShakingControl();

        Globals globals = Globals.getInstance();
        globals.put("SmoothRate_Smoothness", "0.999");
        globals.put("SmoothRate_Initial", "10");
        globals.put("MeanRate_Initial", "10");
        globals.put("Pause_Pause", "10");

        globals.put("RotationWeightConsolidated_Constant", "1");

        globals.put("AccelerometerSensor_Type", "HiTechnicAccelerometerSensor");
        globals.put("AccelerometerSensor_Port", "S1");
        globals.put("Accelerometer_Threaded", "true");
        globals.put("Accelerometer_Smooth", "0.9");
        globals.put("Accelerometer_Interval", "5");

        globals.put("VelocityReference_Constant", "100");
        globals.put("Zero_Constant", "0");

        globals.put("B_Acceleration", "1000");
        globals.put("C_Acceleration", "1000");
        globals.put("B_Sign", "1");
        globals.put("C_Sign", "1");

        globals.put("Consolidated_Weights", "1,1,1");

        globals.put("DirectionConsolidated_Weights", "1");

        globals.put("PhoneAccelerometerSensor_Port", "6669");
        globals.put("PhoneAccelerometerDirectionControlInput_Smoothness", "0.7");
        globals.put("PhoneAccelerometerSpeedControlInput_Smoothness", "0.7");

        globals.put("PhoneAccelerometerSpeedControlOutput_InScale", "0.2");
        globals.put("PhoneAccelerometerSpeedControlOutput_OutScale", "1");
        globals.put("PhoneAccelerometerSpeedControlOutput_XShift", "0");
        globals.put("PhoneAccelerometerSpeedControlOutput_YShift", "0");
        globals.put("PhoneAccelerometerSpeedControlOutput_InputTolerance", "0.1");

        globals.put("PhoneAccelerometerDirectionControlOutput_InScale", "0.3");
        globals.put("PhoneAccelerometerDirectionControlOutput_OutScale", "200");
        globals.put("PhoneAccelerometerDirectionControlOutput_XShift", "-8");
        globals.put("PhoneAccelerometerDirectionControlOutput_YShift", "197");
        globals.put("PhoneAccelerometerDirectionControlOutput_InputTolerance", "0.1");

        globals.put("ShakingControlReference_Constant", "6000");
        globals.put("ShakingControlOutput_Gain", "1");
        globals.put("ShakingControlOutput_Slow", "50000");
        globals.put("ShakingChange_Period", "10");
        globals.put("ShakingControlInput_Smooth", "0.99");
        globals.put("ShakingChangeRaw_Gain", "100");
        globals.put("VelocityTarget_Constant", "900");


        Utils.saveToXML(xml.run(test), file);
        Utils.verify(file);
    }

    public XML_031_001_ShakingControl() {

    }

    public Layers run(boolean test) throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Control of shaking perception");
        List layer = layers.getLayer();
        int layerNum = 1;

        layer.add(configureLayer0("Interface"));
        Modules.moduleDriveMotorsOutput(layers, test);
        Modules.moduleShakingControlSystems(layers, false, layerNum, true);
        Modules.modulePhoneAccelerometerSystems(layers, test, layerNum, true);
        if (Globals.getInstance().get("CompassSensor_Type") != null) {
            Modules.moduleCombinedSensors(layers, new String[]{"CompassSensor"}, test);
        }

        return layers;
    }

    private Layer configureLayer0(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        Controller env = Generic.configureControllerEnv();
        controllers.add(env);
        controllers.add(configureControllerMotorConsolidation());
        ControlFunction function = Utils.configureControlFunction("RotationWeightConsolidated", "Rotation weight, 1 = no rotation", "Constant", new String[][]{{"Constant", Globals.getInstance().get("RotationWeightConsolidated_Constant"), "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);
        function = Utils.configureControlFunction("Zero", "Zero", "Constant", new String[][]{{"Constant", Globals.getInstance().get("Zero_Constant"), "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);
        function = Utils.configureControlFunction("VelocityTarget", "Speed target", "Constant",
                new String[][]{{"Constant", Globals.getInstance().get("VelocityTarget_Constant"), "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);

        //controllers.add(Common.configureSonicSensor());
        return layer;
    }

    private Controller configureControllerMotorConsolidation() throws Exception {
        Controller controller = new Controller();
        controller.setName("MotorConsolidation");
        Functions functions = new Functions();

        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
            ControlFunction reference = Utils.configureControlFunction("PhoneAccelerometerSpeedWeight", "...", "Threshold",
                    new String[][]{{"Threshold", "2", "Double"}, {"LessThan", "true", "Boolean"}, {"Alternate", "0", "Double"}},
                    new String[][]{{"PhoneAccelerometerSpeedControlOutput"}});
            referenceFunctions.setReference(reference);

            functions.setReferenceFunctions(referenceFunctions);
        }

        {
            OutputFunctions outputFunctions = new OutputFunctions();
            ControlFunction output = Utils.configureControlFunction("SpeedConsolidated",
                    "Exclusive function of speed input signals", "WeightedMultiply",
                    new String[][]{{"Weights", Globals.getInstance().get("Consolidated_Weights"), "String"}},
                    new String[][]{{"ShakingControlOutput"}, {"PhoneAccelerometerSpeedWeight"}, {"VelocityTarget"}});

            outputFunctions.setOutput(output);
            {
                Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction("DirectionSpeedAdjustmentSum",
                        "Sum function of direction input signals", "WeightedSum",
                        new String[][]{{"Weights", Globals.getInstance().get("DirectionConsolidated_Weights"), "String"}},
                        new String[][]{{"PhoneAccelerometerDirectionControlOutput"}});
                transfersList.add(transfer);

                transfer = Utils.configureControlFunction("DirectionSpeedAdjustmentConsolidated",
                        "Direction adjustment weighted by shaking", "Product", null,
                        new String[][]{{"ShakingControlOutput"}, {"DirectionSpeedAdjustmentSum"}});

                transfersList.add(transfer);

                outputFunctions.setTransfers(transfers);
            }
            functions.setOutputFunctions(outputFunctions);
        }

        controller.setFunctions(functions);
        return controller;
    }

}
