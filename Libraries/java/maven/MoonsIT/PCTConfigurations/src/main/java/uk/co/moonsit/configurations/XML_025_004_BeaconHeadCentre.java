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

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_025_004_BeaconHeadCentre {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
                String file = Utils.getFileName("Robot", "025-004-BeaconHeadCentre.xml");
        XML_025_004_BeaconHeadCentre xml = new XML_025_004_BeaconHeadCentre();

        Globals globals = Globals.getInstance();
        globals.put("SmoothRate_Smoothness", "0.999");
        globals.put("SmoothRate_Initial", "0");
        globals.put("MeanRate_Initial", "0");
        globals.put("Pause_Pause", "10");
        globals.put("IRSensor_Port", "S1");
        globals.put("IRSensor_Channel", "1");
        globals.put("BeaconDistanceControlOutput_OutScale", "900");
        globals.put("HeadPositionControlOutput_Gain", "2.5");
        globals.put("BeaconActiveSwitch_Upper", "0");

        Utils.saveToXML(xml.run(), file);
        try {
            Utils.verify(file);
        } catch (IOException ex) {
            Logger.getLogger(XML_025_004_BeaconHeadCentre.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(XML_025_004_BeaconHeadCentre.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public XML_025_004_BeaconHeadCentre() {

    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Beacon follower");
        List layer = layers.getLayer();
        layer.add(configureLayer0("Interface"));
        layer.add(configureLayer1("MidControl"));
        layer.add(configureLayer2("UpperControl"));

        return layers;
    }

    private Layer configureLayer2(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        controllers.add(Generic.configureControllerBeaconActive());
        return layer;
    }

    private Layer configureLayer1(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        controllers.add(configureControllerBeaconHeadDirectionControl());
        controllers.add(Generic.configureControllerHeadPositionControl());

        return layer;
    }

    private Layer configureLayer0(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        Controller env = Generic.configureControllerEnv();
        controllers.add(env);
        controllers.add(Generic.configureSensorIR());
        controllers.add(configureControllerMotorsInput());
        controllers.add(configureControllerMotorsOutput());

        return layer;
    }

    public static Controller configureControllerBeaconHeadDirectionControl() throws Exception {
        Controller controller = new Controller();
        controller.setName("BeaconHeadDirectionControl");
        Functions functions = new Functions();

        InputFunctions inputFunctions = new InputFunctions();
        ControlFunction input = Utils.configureControlFunction("BeaconHeadDirectionControlInput", "IR Sensor direction", "",
                null, new String[][]{{"IRSensorDirection"}});
        inputFunctions.setInput(input);
        functions.setInputFunctions(inputFunctions);

        ReferenceFunctions referenceFunctions = new ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction("BeaconHeadDirectionControlReference", "Head direction reference, zero is centre", "Constant",
                new String[][]{{"Constant", "0", "Double"}}, null);
        referenceFunctions.setReference(reference);
        functions.setReferenceFunctions(referenceFunctions);

        ErrorFunctions errorFunctions = new ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction("BeaconHeadDirectionControlError", "Direction error", "Subtract",
                null, new String[][]{{"BeaconHeadDirectionControlInput"}, {"BeaconHeadDirectionControlReference"}});
        errorFunctions.setError(error);
        functions.setErrorFunctions(errorFunctions);

        OutputFunctions outputFunctions = new OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("BeaconHeadDirectionControlOutput", "Proportional output", "Proportional",
                new String[][]{{"Gain", "5", "Double"}}, null);
        outputFunctions.setOutput(output);
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

   
    public static Controller configureControllerMotorsInput() throws Exception {
        Controller controller = new Controller();
        controller.setName("MotorInput");
        Functions functions = new Functions();

        InputFunctions inputFunctions = new InputFunctions();

        ControlFunction input = Utils.configureControlFunction("HeadPositionInput", "Tacho count of Motor A - Head", "MotorCount",
                new String[][]{{"MotorIndex", "A", ""}, {"MotorType", "NXT", ""}}, null);
        inputFunctions.setInput(input);

        functions.setInputFunctions(inputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    private Controller configureControllerMotorsOutput() throws Exception {
        Controller controller = new Controller();
        controller.setName("MotorOutput");
        Functions functions = new Functions();

        OutputFunctions outputFunctions = new OutputFunctions();

        ControlFunction output = Utils.configureControlFunction("HeadPositionConsolidated", "Exclusive function of speed input signals", "WeightedExclusive",
                new String[][]{{"Weights", "1,1", "String"}}, new String[][]{{"BeaconHeadDirectionControlOutput"}, {"HeadPositionControlOutput"}});

        outputFunctions.setOutput(output);
        {
            Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer = Utils.configureControlFunction("MotorAOutput", "Setting value of motor A speed", "MotorWrite",
                    new String[][]{{"MotorIndex", "A", ""}, {"MotorType", "NXT", ""}, {"LowerPositionLimit", "-150", "Integer"}, {"UpperPositionLimit", "150", "Integer"}, {"Acceleration", "6000", "Integer"}},
                    new String[][]{{"HeadPositionConsolidated"}, {"HeadPositionInput"}});
            transfersList.add(transfer);

            outputFunctions.setTransfers(transfers);

        }
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

}
