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
import uk.co.moonsit.config.functions.Globals;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_060_004_PneuSingleLevelPropRef {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
                String file = Utils.getFileName("Robot", "PneuControl", "060-004-PneuSingleLevelPropRef.xml");
        boolean test = true;

        XML_060_004_PneuSingleLevelPropRef xml = new XML_060_004_PneuSingleLevelPropRef();

        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "20");

        globals.put("Position_Smooth", "0");
        globals.put("PositionRead_Position", "1");

        globals.put("Serial_Port", "COM76");
        globals.put("Serial_TimeOut", "2000");
        globals.put("Serial_DataRate", "9600");
        globals.put("PneuPositionControlReference_Position", "0");

        globals.put("PneuPositionControl_Gain", "0.75");

        
        globals.put("RMSErrorResponse_Period", "100");
        globals.put("RMSErrorResponse_Limit", "100000");

        globals.put("GainReorganisation_LearningRate", "0.0");
        globals.put("GainReorganisation_Type", "HillClimb");

        globals.put("VoltageMid_Constant", "127");
        globals.put("ReferenceConstant_Constant", "100");
        
        

        
        Utils.saveToXML(xml.run(test), file);
        Utils.verify(file);

    }

    public XML_060_004_PneuSingleLevelPropRef() {

    }

    public Layers run(boolean test) throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Pneu control - single level");

        List layer = layers.getLayer();

        layer.add(configureLayer0("Interface", test));

        return layers;
    }

    private static Layers.Layer configureLayer0(String name, boolean test) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();
        controllers.add(configureControllerEnv());

        controllers.add(configureControllerPneuPositionControl());

        return layer;
    }

    private static Layers.Layer.Controller configureControllerEnv() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("Env");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();

        ControlFunction input = Utils.configureControlFunction("Rate", "Iteration rate", "Rate", null, null);
        inputFunctions.setInput(input);

        {
            Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer;

            transfer = Utils.configureControlFunction("Pause", "Function for pausing processing to maintain constant iteration rate", "Pause",
                    new String[][]{{"Pause", Globals.getInstance().get("Pause_Pause"), "Long"}, {"ConstantRate", "true", "Boolean"}}, null);
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("VoltageMid", "...", "Constant",
                    new String[][]{{"Constant", Globals.getInstance().get("VoltageMid_Constant"), "Double"}}, null);
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("ReferenceConstant", "...", "Constant",
                    new String[][]{{"Constant", Globals.getInstance().get("ReferenceConstant_Constant"), "Double"}}, null);
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("Zero", "...", "Constant",
                    new String[][]{{"Constant", "0", "Double"}}, null);
            transfersList.add(transfer);
            inputFunctions.setTransfers(transfers);
        }
        functions.setInputFunctions(inputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerPneuPositionControl() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("PneuPositionControl");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction("PneuPositionControlInput", "", "Smooth",
                    new String[][]{{"Smoothness", Globals.getInstance().get("Position_Smooth"), "Double"}}, new String[][]{{"PositionRead"}});

            inputFunctions.setInput(input);
            {
                Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();

                ControlFunction transfer = Utils.configureControlFunction("PositionRead", "", "SerialCSVSensor",
                        new String[][]{{"SensorPort", Globals.getInstance().get("Serial_Port"), ""},
                        {"TimeOut", Globals.getInstance().get("Serial_TimeOut"), "Integer"},
                        {"DataRate", Globals.getInstance().get("Serial_DataRate"), "Integer"},
                        {"Position", Globals.getInstance().get("PositionRead_Position"), "Integer"}}, null);
                transfersList.add(transfer);

                inputFunctions.setTransfers(transfers);
            }
            functions.setInputFunctions(inputFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
            ControlFunction reference = Utils.configureControlFunction("PneuPositionControlReference", "", "SerialCSVSensor",
                    new String[][]{{"SensorPort", Globals.getInstance().get("Serial_Port"), ""},
                    {"TimeOut", Globals.getInstance().get("Serial_TimeOut"), "Integer"},
                    {"DataRate", Globals.getInstance().get("Serial_DataRate"), "Integer"},
                    {"Update", "true", "Boolean"},
                    {"Position", Globals.getInstance().get("PneuPositionControlReference_Position"), "Integer"}}, null);

            referenceFunctions.setReference(reference);
            {
                Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction("RMSErrorResponse", "...", "RMSErrorResponse",
                        new String[][]{
                            {"Period", Globals.getInstance().get("RMSErrorResponse_Period"), "Double"},
                            {"Limit", Globals.getInstance().get("RMSErrorResponse_Limit"), "Double"}},
                        new String[][]{{"PneuPositionControlError"},});
                transfersList.add(transfer);

                transfer = Utils.configureControlFunction("GainReorganisation", "Reorganisation of gain", "ParameterReorganisation",
                        new String[][]{
                            {"LearningRate", Globals.getInstance().get("GainReorganisation_LearningRate"), "Double"},
                            {"Type", Globals.getInstance().get("GainReorganisation_Type"), "Double"}},
                        new String[][]{{"RMSErrorResponse", "ErrorResponse"}, {"PneuPositionControlOutput", "Parameter"}});
                transfersList.add(transfer);

                referenceFunctions.setTransfers(transfers);

            }
            functions.setReferenceFunctions(referenceFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction("PneuPositionControlError", "Difference error", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            functions.setErrorFunctions(errorFunctions);
        }
        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction("PneuPositionControlOutput", "...", "Proportional",
                    new String[][]{
                        {"Gain", Globals.getInstance().get("PneuPositionControl_Gain"), "Double"}},
                    null);
            outputFunctions.setOutput(output);
            {
                Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction("VoltageOutput", "", "Addition",
                        null,
                        new String[][]{{"PneuPositionControlOutput"}, {"VoltageMid"}});
                transfersList.add(transfer);
                
                        transfer = Utils.configureControlFunction("VoltageWrite", "", "SerialCSVWrite",
                        new String[][]{{"SensorPort", Globals.getInstance().get("Serial_Port"), ""},
                        {"TimeOut", Globals.getInstance().get("Serial_TimeOut"), "Integer"},
                        {"DataRate", Globals.getInstance().get("Serial_DataRate"), "Integer"}},
                        new String[][]{{"VoltageOutput"}});
                transfersList.add(transfer);
                transfer = Utils.configureControlFunction("ReferenceWrite", "", "SerialCSVWrite",
                        new String[][]{{"SensorPort", Globals.getInstance().get("Serial_Port"), ""},
                        {"TimeOut", Globals.getInstance().get("Serial_TimeOut"), "Integer"},
                        {"DataRate", Globals.getInstance().get("Serial_DataRate"), "Integer"}},
                        new String[][]{{"ReferenceConstant"}});
                transfersList.add(transfer);
                outputFunctions.setTransfers(transfers);
            }
            functions.setOutputFunctions(outputFunctions);
        }
        controller.setFunctions(functions);
        return controller;
    }

}
