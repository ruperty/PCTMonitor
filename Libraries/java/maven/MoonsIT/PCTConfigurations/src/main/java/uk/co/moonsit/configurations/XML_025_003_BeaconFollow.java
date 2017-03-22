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
public class XML_025_003_BeaconFollow {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
                String file = Utils.getFileName("Robot", "025-003-BeaconFollow.xml");
        XML_025_003_BeaconFollow xml = new XML_025_003_BeaconFollow();

        Globals globals = Globals.getInstance();
        globals.put("SmoothRate_Smoothness", "0.999");
        globals.put("SmoothRate_Initial", "0");
        globals.put("MeanRate_Initial", "0");
        globals.put("Pause_Pause", "10");
        globals.put("IRSensor_Port", "S2");
        globals.put("IRSensor_Channel", "2");
        globals.put("BeaconDistanceControl_Reference", "30");
        globals.put("BeaconActiveSwitch_Upper", "0");
        globals.put("BeaconDistanceControlOutput_OutScale", "900");
        globals.put("MotorRotationalDifferenceControlOutput_Gain", "2");

        Utils.saveToXML(xml.run(), file);
        try {
            Utils.verify(file);
        } catch (IOException ex) {
            Logger.getLogger(XML_025_003_BeaconFollow.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(XML_025_003_BeaconFollow.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public XML_025_003_BeaconFollow() {

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

        controllers.add(configureControllerHeadBody());
        controllers.add(Generic.configureControllerBeaconActive());
        return layer;
    }

    private Layer configureLayer1(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        controllers.add(Generic.configureControllerBeaconHeadDirectionControl());

        controllers.add(Generic.configureControllerMotorRotationalDifferenceControl());
        controllers.add(Generic.configureControllerBeaconDistanceControl());

        return layer;
    }

    private Layer configureLayer0(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        Controller env = Generic.configureControllerEnv();
        controllers.add(env);
        controllers.add(Generic.configureSensorIR());
        controllers.add(Generic.configureControllerMotorsInput());
        controllers.add(configureControllerMotorsOutput());

        return layer;
    }

    public Controller configureControllerHeadBody() throws Exception {
        Controller controller = new Controller();
        controller.setName("RelativeHeadBodyOrientation");
        Functions functions = new Functions();

        InputFunctions inputFunctions = new InputFunctions();
        ControlFunction input = Utils.configureControlFunction("RelativeHeadBodyOrientationInput", "Control of relationship of head to body", "",
                null, new String[][]{{"HeadPositionInput"}});
        inputFunctions.setInput(input);
        functions.setInputFunctions(inputFunctions);

        ReferenceFunctions referenceFunctions = new ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction("RelativeHeadBodyOrientationReference", "Desired relationship, of zero", "Constant",
                new String[][]{{"Constant", "0", "Double"}}, null);
        referenceFunctions.setReference(reference);
        functions.setReferenceFunctions(referenceFunctions);

        ErrorFunctions errorFunctions = new ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction("RelativeHeadBodyOrientationError", "Relationship error", "Subtract",
                null, null);
        errorFunctions.setError(error);
        functions.setErrorFunctions(errorFunctions);

        OutputFunctions outputFunctions = new OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("RelativeHeadBodyOrientationOutput", "Proportional output", "Proportional",
                new String[][]{{"Gain", "5", "Double"}}, null);
        outputFunctions.setOutput(output);
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    
    private Controller configureControllerMotorsOutput() throws Exception {
        Controller controller = new Controller();
        controller.setName("MotorOutput");
        Functions functions = new Functions();

        OutputFunctions outputFunctions = new OutputFunctions();

        ControlFunction output = Utils.configureControlFunction("SpeedReference", "Smoothed value of the output speed", "Smooth",
                new String[][]{{"Smoothness", "0.95", "Double"}}, new String[][]{{"BeaconDistanceControlOutput"}});
        outputFunctions.setOutput(output);

        pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
        List<ControlFunction> transfersList = transfers.getTransfer();

        ControlFunction transfer;
        transfer = Utils.configureControlFunction("SpeedReferenceB", "Reference speed for B", "Subtract",
                null, new String[][]{{"SpeedReference"}, {"MotorRotationalDifferenceControlOutput"}});
        transfersList.add(transfer);
        transfer = Utils.configureControlFunction("SpeedReferenceC", "Reference speed for C", "Addition",
                null, new String[][]{{"SpeedReference"}, {"MotorRotationalDifferenceControlOutput"}});

        transfersList.add(transfer);

        transfer = Utils.configureControlFunction("MotorAOutput", "Setting value of motor A speed", "MotorWrite",
                new String[][]{{"MotorIndex", "A", ""}, {"MotorType", "NXT", ""}, {"LowerPositionLimit", "-150", "Integer"}, {"UpperPositionLimit", "150", "Integer"}, {"Acceleration", "6000", "Integer"}},
                new String[][]{{"BeaconHeadDirectionControlOutput"}, {"HeadPositionInput"}});
        transfersList.add(transfer);

        transfer = Utils.configureControlFunction("MotorCOutput", "Setting value of motor C speed", "MotorWrite",
                new String[][]{{"MotorIndex", "C", ""}, {"Acceleration", "600", "Integer"}},
                new String[][]{{"SpeedReferenceC"}});
        transfersList.add(transfer);

        transfer = Utils.configureControlFunction("MotorBOutput", "Setting value of motor B speed", "MotorWrite",
                new String[][]{{"MotorIndex", "B", ""}, {"Acceleration", "600", "Integer"}},
                new String[][]{{"SpeedReferenceB"}});
        transfersList.add(transfer);

        outputFunctions.setTransfers(transfers);

        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

}
