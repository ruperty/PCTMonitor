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
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_040_001_IntegrationDirectionControl {
private final boolean test = false;
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
                String file = Utils.getFileName("Robot", "040-001-IntegrationDirectionControl.xml");
        XML_040_001_IntegrationDirectionControl xml = new XML_040_001_IntegrationDirectionControl();

        Globals globals = Globals.getInstance();
        globals.put("SmoothRate_Smoothness", "0.999");
        globals.put("SmoothRate_Initial", "0");
        globals.put("MeanRate_Initial", "0");
        globals.put("Pause_Pause", "10");
        globals.put("B_Acceleration","1000");
        globals.put("C_Acceleration","1000");
        globals.put("B_Sign", "1");
        globals.put("C_Sign", "1");

        Utils.saveToXML(xml.run(), file);
       
            Utils.verify(file);
        
    }

    public XML_040_001_IntegrationDirectionControl() {

    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Direction control. Systems for control of rotation and forward direction, "
                + "with a standardised interface.");
        List layer = layers.getLayer();

        layer.add(configureLayer0("Interface"));
        layer.add(configureLayer1("Control"));
        //layer.add(configureLayer2("Direction"));

        return layers;
    }

   
    private Layer configureLayer1(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        controllers.add(configureControllerMotorConsolidation());

        return layer;
    }

    private Layer configureLayer0(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        Controller env = Generic.configureControllerEnv();
        ControlFunction function = Utils.configureControlFunction("ObjectSpeed", "Tmp", "Constant", new String[][]{{"Constant", "100", "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);
        function = Utils.configureControlFunction("ObjectDirectionSpeedAdjustment", "Tmp", "Constant", new String[][]{{"Constant", "50", "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);
        function = Utils.configureControlFunction("BeaconSpeed", "Tmp", "Constant", new String[][]{{"Constant", "0", "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);
        function = Utils.configureControlFunction("BeaconDirectionSpeedAdjustment", "Tmp", "Constant", new String[][]{{"Constant", "50", "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);
        function = Utils.configureControlFunction("RotationWeightConsolidated", "Rotation weight, 1 = no rotation", "Constant", new String[][]{{"Constant", "1", "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);

        controllers.add(env);

        //controllers.add(Common.configureSonicSensor());
        controllers.add(Generic.configureControllerDriveMotorsOutput(test));

        return layer;
    }

    
    
    
    private Controller configureControllerMotorConsolidation() throws Exception {
        Controller controller = new Controller();
        controller.setName("MotorConsolidation");
        Functions functions = new Functions();

        OutputFunctions outputFunctions = new OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("SpeedConsolidated", "Exclusive function of speed input signals", "WeightedExclusive",
                 new String[][]{{"Weights", "1,1", "String"}}, new String[][]{{"ObjectSpeed"}, {"BeaconSpeed"}});

        outputFunctions.setOutput(output);
        {
            Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer =  Utils.configureControlFunction("DirectionSpeedAdjustmentConsolidated", "Exclusive function of direction input signals", "WeightedExclusive",
                 new String[][]{{"Weights", "1,1", "String"}}, new String[][]{{"ObjectDirectionSpeedAdjustment"}, {"BeaconDirectionSpeedAdjustment"}});

            transfersList.add(transfer);

            outputFunctions.setTransfers(transfers);

        }
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

   


    

}
