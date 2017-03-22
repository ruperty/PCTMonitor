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
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.ErrorFunctions;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.OutputFunctions;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.ReferenceFunctions;
import uk.co.moonsit.config.functions.Generic;
import uk.co.moonsit.config.functions.Globals;
import uk.co.moonsit.config.functions.Modules;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_030_001_AvoidanceProximityTurning {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
                String file = Utils.getFileName("Robot", "030-001-AvoidanceProximityTurning.xml");
        XML_030_001_AvoidanceProximityTurning xml = new XML_030_001_AvoidanceProximityTurning();

        Globals globals = Globals.getInstance();
        globals.put("SmoothRate_Smoothness", "0.999");
        globals.put("SmoothRate_Initial", "0");
        globals.put("MeanRate_Initial", "0");
        globals.put("Pause_Pause", "10");
        globals.put("ProximitySensor_Port", "S2");
        globals.put("ProximityControlOutput_Scale", "40");
        globals.put("RotationWeightConsolidated_Constant", "1");
        globals.put("ObjectVelocityReference_Constant", "400");

        globals.put("ProximitySensor_Type", "NXTUltrasonicSensor");
        globals.put("ProximitySensor_Mode", "NULL");
        
        
        globals.put("ProximitySensor_Max", "210");
        globals.put("ProximityControlReference_Constant", "30");
        globals.put("ProximityInput_Smoothness", "0.5");

        Utils.saveToXML(xml.run(), file);
        Utils.verify(file);

    }

    public XML_030_001_AvoidanceProximityTurning() {

    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Basic control of value of sonic reading for obstacle avoidance");
        List layer = layers.getLayer();

        layer.add(configureLayer0("Interface"));

        Modules.moduleAvoidanceProximityTurning(layers);

        ControlFunction transfer = Utils.configureControlFunction("ProximityWeightAdd", "Add the sonic output to the velocity", "Addition",
                null, new String[][]{{"ProximityControlOutput"}, {"ObjectVelocityReference"}});
        Utils.addTransferFunction(layers, 1, "ProximityControl", transfer, Utils.OUTPUT);

        transfer = Utils.configureControlFunction("ProximityWeightSub", "Subtract the sonic output from the velocity", "Subtract",
                null, new String[][]{{"ObjectVelocityReference"}, {"ProximityControlOutput"}});
        Utils.addTransferFunction(layers, 1, "ProximityControl", transfer, Utils.OUTPUT);

        return layers;
    }

    /*
     private Layer configureLayer1(String name) {
     Layer layer = new Layer();
     layer.setName(name);
     List<Controller> controllers = layer.getController();

     controllers.add(configureControllerProximityControl());

     return layer;
     }
     */
    private Layer configureLayer0(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        Controller env = Generic.configureControllerEnv();
        controllers.add(env);

        controllers.add(configureControllerMotorBOutput());
        controllers.add(configureControllerMotorCOutput());

        return layer;
    }

    public static Controller configureControllerProximityControl() throws Exception {
        Controller controller = new Controller();
        controller.setName("ProximityControl");
        Functions functions = new Functions();

        InputFunctions inputFunctions = new InputFunctions();
        ControlFunction input = Utils.configureControlFunction("ProximityInputSmooth", "Smoothed value of sonic sensor", "Smooth",
                new String[][]{{"Smoothness", Globals.getInstance().get("ProximityInput_Smoothness"), "Double"}}, new String[][]{{"ProximityInputLimiting"}});
        inputFunctions.setInput(input);
        {
            Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();

            ControlFunction transfer = Utils.configureControlFunction("ProximityInputLimiting", "Limiting sonic input to max value", "Limit",
                    new String[][]{{"Max", "30.0", "Double"}, {"Min", "0", "Double"}}, new String[][]{{"ProximityRaw"}});
            transfersList.add(transfer);
            inputFunctions.setTransfers(transfers);
        }
        functions.setInputFunctions(inputFunctions);

        ReferenceFunctions referenceFunctions = new ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction("ProximityControlReference", "Proximity reference", "Constant",
                new String[][]{{"Constant", Globals.getInstance().get("ProximityControlReference_Constant"), "Double"}}, null);
        referenceFunctions.setReference(reference);
        functions.setReferenceFunctions(referenceFunctions);

        ErrorFunctions errorFunctions = new ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction("ProximityControlError", "Error", "Subtract",
                null, null);
        errorFunctions.setError(error);
        functions.setErrorFunctions(errorFunctions);

        OutputFunctions outputFunctions = new OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("ProximityControlOutput", "Scaled sonic output", "Scaling",
                new String[][]{{"Scale", Globals.getInstance().get("ProximityControlOutput_Scale"), "Double"}}, new String[][]{{"ProximityControlError"}});
        outputFunctions.setOutput(output);

        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Controller configureControllerMotorBOutput() throws Exception {
        Controller controller = new Controller();
        controller.setName("Motor B Output");
        Functions functions = new Functions();

        OutputFunctions outputFunctions = new OutputFunctions();

        ControlFunction output = Utils.configureControlFunction("MotorBOutput", "Setting value of motor B speed", "MotorWrite",
                new String[][]{{"MotorIndex", "B", ""}, {"Acceleration", "1000", "Integer"}},
                new String[][]{{"ProximityWeightAdd"}});
        outputFunctions.setOutput(output);

        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Controller configureControllerMotorCOutput() throws Exception {
        Controller controller = new Controller();
        controller.setName("Motor C Output");
        Functions functions = new Functions();

        OutputFunctions outputFunctions = new OutputFunctions();

        ControlFunction output = Utils.configureControlFunction("MotorCOutput", "Setting value of motor C speed", "MotorWrite",
                new String[][]{{"MotorIndex", "C", ""}, {"Acceleration", "1000", "Integer"}},
                new String[][]{{"ProximityWeightSub"}});
        outputFunctions.setOutput(output);

        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

}
