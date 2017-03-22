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
import pct.moons.co.uk.schema.layers.Layers.OrderedControllers;
import uk.co.moonsit.config.functions.Globals;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_060_005_PneuTwoLevelPropIntRef {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
                String file = Utils.getFileName("Robot", "PneuControl", "060-005-PneuTwoLevelPropIntRef.xml");
        boolean test = true;

        XML_060_005_PneuTwoLevelPropIntRef xml = new XML_060_005_PneuTwoLevelPropIntRef();

        Globals globals = Globals.getInstance();
        globals.put("Pause_Pause", "20");

        globals.put("Position_Smooth", "0");
        globals.put("PositionRead_Position", "1");

        globals.put("Serial_Port", "COM76");
        globals.put("Serial_TimeOut", "2000");
        globals.put("Serial_DataRate", "9600");

        globals.put("PneuPositionUpperControlReference_Position", "0");
        globals.put("PneuPositionUpperControlOutput_Gain", "0.03");

        globals.put("PneuPositionControl_Gain", "0.5");

        globals.put("LowerRMSErrorResponse_Period", "400");
        globals.put("LowerRMSErrorResponse_Limit", "100000");
        globals.put("LowerGainReorganisation_LearningRate", "0.0");
        globals.put("LowerGainReorganisation_Type", "HillClimb");

        globals.put("UpperRMSErrorResponse_Period", "400");
        globals.put("UpperRMSErrorResponse_Limit", "100000");
        globals.put("UpperGainReorganisation_LearningRate", "0.0");
        globals.put("UpperGainReorganisation_Type", "HillClimb");

        globals.put("VoltageMid_Constant", "127");
        globals.put("ReferenceConstant_Constant", "100");
        globals.put("Reference_Weights", "0,0,0,1");

        globals.put("Sine_Frequency", "0.5");
        globals.put("Sine_Amplitude", "75");
        globals.put("Sine_Step", "1");
        globals.put("Sine_YShift", "125");

        globals.put("Step_Period", "400");
        globals.put("Step_Upper", "200");
        globals.put("Step_Lower", "50");

        globals.put("Random_Scale", "100");
        globals.put("Random_Slow", "0.05");
        globals.put("Random_Initial", "100");
        globals.put("Random_Max", "220");
        globals.put("Random_Min", "30");

        Utils.saveToXML(xml.run(test), file);
        Utils.verify(file);

    }

    public XML_060_005_PneuTwoLevelPropIntRef() {

    }

    public Layers run(boolean test) throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Pneu control - single level");

        List layer = layers.getLayer();

        layer.add(configureLayer0("Interface"));
        layer.add(configureLayer1("Position"));

        OrderedControllers oc = new OrderedControllers();
        List<String> ocl = oc.getController();
        ocl.add("Env");
        ocl.add("PneuPositionUpperControl");
        ocl.add("PneuPositionControl");
        layers.setOrderedControllers(oc);

        return layers;
    }

    public static Layers.Layer.Controller configureEnv() throws Exception {
        Layers.Layer.Controller env = configureControllerEnv();

        //ControlFunction function = Utils.stepDisturbanceFunction();
        ControlFunction function = Utils.randomFunction();
        Utils.addTransferFunction(env, function, Utils.INPUT);
        function = Utils.stepFunction();
        Utils.addTransferFunction(env, function, Utils.INPUT);
        function = Utils.sineFunction();
        Utils.addTransferFunction(env, function, Utils.INPUT);

        return env;
    }

    private static Layers.Layer configureLayer0(String name) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();
        controllers.add(configureEnv());

        controllers.add(configureControllerPneuPositionControl());

        return layer;
    }

    private static Layers.Layer configureLayer1(String name) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);
        List<Layers.Layer.Controller> controllers = layer.getController();

        controllers.add(configureControllerPneuPositionUpperControl());

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

            transfer = Utils.configureControlFunction("ReferenceFiltered", "", "WeightedSum",
                    new String[][]{{"Weights", Globals.getInstance().get("Reference_Weights"), "String"}},
                    new String[][]{{"Random"}, {"Step"}, {"Sine"}, {"ReferenceConstant"}});
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
            ControlFunction reference = Utils.configureControlFunction("PneuPositionControlReference", "...", "",
                    null, new String[][]{{"PneuPositionUpperControlOutput"}});

            referenceFunctions.setReference(reference);
            {
                Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction("LowerRMSErrorResponse", "...", "RMSErrorResponse",
                        new String[][]{
                            {"Period", Globals.getInstance().get("LowerRMSErrorResponse_Period"), "Double"},
                            {"Limit", Globals.getInstance().get("LowerRMSErrorResponse_Limit"), "Double"}},
                        new String[][]{{"PneuPositionControlError"}});
                transfersList.add(transfer);

                transfer = Utils.configureControlFunction("LowerGainReorganisation", "Reorganisation of gain", "ParameterReorganisation",
                        new String[][]{
                            {"LearningRate", Globals.getInstance().get("LowerGainReorganisation_LearningRate"), "Double"},
                            {"Type", Globals.getInstance().get("LowerGainReorganisation_Type"), "Double"}},
                        new String[][]{{"LowerRMSErrorResponse", "ErrorResponse"}, {"PneuPositionControlOutput", "Parameter"}});
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
                        new String[][]{{"ReferenceFiltered"}});
                transfersList.add(transfer);
                outputFunctions.setTransfers(transfers);
            }
            functions.setOutputFunctions(outputFunctions);
        }
        controller.setFunctions(functions);
        return controller;
    }

    public static Layers.Layer.Controller configureControllerPneuPositionUpperControl() throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName("PneuPositionUpperControl");
        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();

        {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            ControlFunction input = Utils.configureControlFunction("PneuPositionUpperControlInput", "", "Smooth",
                    new String[][]{{"Smoothness", Globals.getInstance().get("Position_Smooth"), "Double"}}, new String[][]{{"PneuPositionControlInput"}});

            inputFunctions.setInput(input);

            functions.setInputFunctions(inputFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
            ControlFunction reference = Utils.configureControlFunction("PneuPositionUpperControlReference", "", "SerialCSVSensor",
                    new String[][]{{"SensorPort", Globals.getInstance().get("Serial_Port"), ""},
                    {"TimeOut", Globals.getInstance().get("Serial_TimeOut"), "Integer"},
                    {"DataRate", Globals.getInstance().get("Serial_DataRate"), "Integer"},
                    {"Update", "true", "Boolean"},
                    {"Position", Globals.getInstance().get("PneuPositionUpperControlReference_Position"), "Integer"}}, null);

            referenceFunctions.setReference(reference);
            {
                Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
                List<ControlFunction> transfersList = transfers.getTransfer();
                ControlFunction transfer = Utils.configureControlFunction("UpperRMSErrorResponse", "...", "RMSErrorResponse",
                        new String[][]{
                            {"Period", Globals.getInstance().get("UpperRMSErrorResponse_Period"), "Double"},
                            {"Limit", Globals.getInstance().get("UpperRMSErrorResponse_Limit"), "Double"}},
                        new String[][]{{"PneuPositionControlError"}});
                transfersList.add(transfer);

                transfer = Utils.configureControlFunction("UpperGainReorganisation", "Reorganisation of gain", "ParameterReorganisation",
                        new String[][]{
                            {"LearningRate", Globals.getInstance().get("UpperGainReorganisation_LearningRate"), "Double"},
                            {"Type", Globals.getInstance().get("UpperGainReorganisation_Type"), "Double"}},
                        new String[][]{{"UpperRMSErrorResponse", "ErrorResponse"}, {"PneuPositionUpperControlOutput", "Parameter"}});
                transfersList.add(transfer);

                referenceFunctions.setTransfers(transfers);

            }

            functions.setReferenceFunctions(referenceFunctions);
        }
        {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            ControlFunction error = Utils.configureControlFunction("PneuPositionUpperControlError", "Difference error", "Subtract",
                    null, null);
            errorFunctions.setError(error);
            functions.setErrorFunctions(errorFunctions);
        }
        {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
            ControlFunction output = Utils.configureControlFunction("PneuPositionUpperControlOutput", "...", "ProportionalIntegration",
                    new String[][]{
                        {"Gain", Globals.getInstance().get("PneuPositionUpperControlOutput_Gain"), "Double"}},
                    null);
            outputFunctions.setOutput(output);

            functions.setOutputFunctions(outputFunctions);
        }
        controller.setFunctions(functions);
        return controller;
    }

}
