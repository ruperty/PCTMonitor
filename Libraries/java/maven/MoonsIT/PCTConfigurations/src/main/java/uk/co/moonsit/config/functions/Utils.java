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
package uk.co.moonsit.config.functions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import pct.moons.co.uk.schema.layers.Activation;
import pct.moons.co.uk.schema.layers.ControlFunction;
import pct.moons.co.uk.schema.layers.Layers;
import pct.moons.co.uk.schema.layers.Layers.Layer;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.ErrorFunctions;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions.Transfers;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.OutputFunctions;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.ReferenceFunctions;
import pct.moons.co.uk.schema.layers.Layers.OrderedControllers;
import pct.moons.co.uk.schema.layers.Link;
import pct.moons.co.uk.schema.layers.NeuralFunction;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.ControlHierarchy;
import uk.co.moonsit.utils.MoonsSystem;

/**
 *
 * @author Rupert Young
 */
public class Utils {

    private static final Logger LOG = Logger.getLogger(Utils.class.getName());

    public static final int INPUT = 0;
    public static final int REFERENCE = 1;
    public static final int ERROR = 2;
    public static final int OUTPUT = 3;
    public static final int TRANSFER = 4;

    public static void setOrderedControllers(Layers layers, String[] list) {

        OrderedControllers ordered = new OrderedControllers();
        List<String> order = ordered.getController();
        order.addAll(Arrays.asList(list));
        layers.setOrderedControllers(ordered);
    }

    public static ControlFunction configureBaxterPublisher(String name, String description, String joint, String topic, String mode, String publish, String tolerance, String disabled, String initial, String last, String[][] links) throws Exception {
        int size = 3;
        if (publish != null) {
            size++;
        }
        if (tolerance != null) {
            size++;
        }
        if (disabled != null) {
            size++;
        }
        if (initial != null) {
            size++;
        }
        if (last != null) {
            size++;
        }
        String[][] pars = new String[size][];
        pars[0] = new String[]{"Joint", joint, ""};
        pars[1] = new String[]{"Topic", topic, ""};
        pars[2] = new String[]{"Mode", mode, ""};
        int i = 3;

        if (publish != null) {
            pars[i++] = new String[]{"Publish", publish, ""};
        }
        if (tolerance != null) {
            pars[i++] = new String[]{"Tolerance", tolerance, ""};
        }
        if (disabled != null) {
            pars[i++] = new String[]{"Disabled", disabled, "Boolean"};
        }
        if (initial != null) {
            pars[i++] = new String[]{"Initial", initial, ""};
        }
        if (last != null) {
            pars[i++] = new String[]{"Last", last, ""};
        }

        ControlFunction function = Utils.configureControlFunction(name, description, "ROSBaxterJointPublisher",
                pars, links);

        return function;
    }

    static public Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions(String prefix) throws Exception {
        Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
        ControlFunction error = Utils.configureControlFunction(prefix + "Error", "error", "Subtract",
                null, null);
        errorFunctions.setError(error);
        return errorFunctions;
    }

    static public Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions(ControlFunction reference) throws Exception {
        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
        referenceFunctions.setReference(reference);
        return referenceFunctions;
    }

    static public Layers.Layer.Controller.Functions.OutputFunctions outputFunctions(ControlFunction output) throws Exception {
        Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
        outputFunctions.setOutput(output);
        return outputFunctions;
    }

    static public Layers.Layer.Controller.Functions.InputFunctions inputFunctions(ControlFunction input) throws Exception {
        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        inputFunctions.setInput(input);
        return inputFunctions;
    }

    static public ControlFunction configureControlFunction(String name, String description, String neuralType, String[][] parameters, String[][] links) throws Exception {
        if (neuralType == null) {
            throw new Exception("Neural function type is null for " + name);
        }

        ControlFunction control = new ControlFunction();
        control.setName(name);
        control.setDescription(description);
        try {
            control.setNeuralFunction(Utils.configureNeuralFunction(neuralType, parameters, links));
        } catch (Exception ex) {
            throw new Exception("Exception with function " + name + " " + ex.toString());
        }

        return control;
    }

    static public Layers.Layer.Controller emptyController(String name, boolean input, boolean reference, boolean error, boolean output) throws Exception {
        Layers.Layer.Controller controller = new Layers.Layer.Controller();
        controller.setName(name);

        Layers.Layer.Controller.Functions functions = new Layers.Layer.Controller.Functions();
        if (input) {
            Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
            functions.setInputFunctions(inputFunctions);
        }
        if (reference) {
            Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
            functions.setReferenceFunctions(referenceFunctions);
        }
        if (error) {
            Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
            functions.setErrorFunctions(errorFunctions);
        }
        if (output) {
            Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();

            functions.setOutputFunctions(outputFunctions);
        }
        controller.setFunctions(functions);

        return controller;
    }

    static public Layer emptyLayer(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);

        return layer;
    }

    public static void loadPars(String fname) throws Exception {
        File file = new File(fname);
        if (file.exists()) {
            Properties props;
            try {
                props = new Properties();
                props.load(new FileInputStream(file));
                for (String key : props.stringPropertyNames()) {
                    if (!Globals.getInstance().containsKey(key)) {
                        throw new Exception("Key " + key + " does not exist in global list");
                    }
                    String value = props.getProperty(key);
                    Globals.getInstance().put(key, value);
                }

            } catch (IOException ex) {
                LOG.warning(ex.toString());
            }
        }
    }

    public static String getFileName(String root, String file) {
        String prefix = MoonsSystem.getPrefix();
        String s = File.separator;
        String filename = prefix + "Versioning" + s + "PCTSoftware" + s + "Controllers"
                + s + root + s + file;

        return filename;
    }

    public static String getFileName(String root, String dir, String file) {
        String prefix = MoonsSystem.getPrefix();
        String s = File.separator;
        String filename = prefix + "Versioning" + s + "PCTSoftware" + s + "Controllers"
                + s + root + s + dir + s + file;

        return filename;
    }

    public static String getParametersFileName(String root, String dir, String file) {
        String prefix = MoonsSystem.getPrefix();
        String s = File.separator;
        String filename = prefix + "Versioning" + s + "PCTSoftware" + s + "Controllers"
                + s + root + s + dir + s + "parameters" + s + file + ".pars";

        return filename;
    }

    public static void addToProximity(Layers layers) throws Exception {
        ControlFunction transfer = Utils.configureControlFunction("AvoidanceActive", "Binary indication of avoidance active", "LimitBanded",
                new String[][]{{"Threshold", "0", "Double"}, {"BandWidth", "0.9", "Double"}, {"BandValue", "0", "Double"}, {"Upper", "1", "Double"}, {"Lower", "1", "Double"}},
                new String[][]{{"ProximityControlError"}});

        Utils.addTransferFunction(layers, 1, "ProximityControl", transfer, Utils.OUTPUT);

        transfer = Utils.configureControlFunction("ObjectSpeed", "Gating object speed if avoidance active", "Product",
                null, new String[][]{{"ObjectVelocityReference"}, {"AvoidanceActive"}});
        Utils.addTransferFunction(layers, 1, "ProximityControl", transfer, Utils.OUTPUT);
    }

    static public void addController(Layers layers, int layerNum, Controller controller) {
        if (layerNum >= layers.getLayer().size()) {
            layers.getLayer().add(new Layer());
        }
        Layer layer = layers.getLayer().get(layerNum);
        List<Controller> controllers = layer.getController();
        controllers.add(controller);
    }

    static public void addController(Layers layers, int layerNum, Controller controller, int position) {
        if (layerNum >= layers.getLayer().size()) {
            layers.getLayer().add(new Layer());
        }
        Layer layer = layers.getLayer().get(layerNum);
        List<Controller> controllers = layer.getController();
        controllers.add(position > controllers.size() ? controllers.size() : position, controller);
    }

    static public void addController(Layers layers, int layerNum, String layerName, Controller controller, int position) {
        Layer layer;

        if (layerNum >= layers.getLayer().size()) {
            layer = new Layer();
            layer.setName(layerName);
            layers.getLayer().add(layer);
        } else {
            layer = layers.getLayer().get(layerNum);
        }
        List<Controller> controllers = layer.getController();
        controllers.add(position > controllers.size() ? controllers.size() : position, controller);
    }

    static public void addTransferFunction(Layers layers, int layerNum, String controllerName, ControlFunction control, int functionType) throws Exception {
        Controller controller = getController(layers, layerNum, controllerName);
        if (controller == null) {
            throw new Exception("Controller " + controllerName + " not found at level " + layerNum);
        }
        addTransferFunction(controller, control, functionType);
    }

    static public void addTransferFunction(Layers layers, int layerNum, String controllerName, ControlFunction control, int functionType, int index) throws Exception {
        Controller controller = getController(layers, layerNum, controllerName);
        if (controller == null) {
            throw new Exception("Controller " + controllerName + " not found at level " + layerNum);
        }
        addTransferFunction(controller, control, functionType, index);
    }

    static public void addTransferFunction(Layers layers, String controllerName, ControlFunction control, int functionType, int index) throws Exception {
        Controller controller = getController(layers, controllerName);
        if (controller == null) {
            throw new Exception("Controller " + controllerName + " not found at any level ");
        }
        addTransferFunction(controller, control, functionType, index);
    }

    static public void addTransferFunction(Layers layers, String controllerName, ControlFunction control, int functionType) throws Exception {
        Controller controller = getController(layers, controllerName);
        if (controller == null) {
            throw new Exception("Controller " + controllerName + " not found at any level ");
        }
        addTransferFunction(controller, control, functionType);
    }

    static private Controller getController(Layers layers, String name) {
        for (int i = 0; i < layers.getLayer().size(); i++) {
            for (Controller controller : layers.getLayer().get(i).getController()) {
                if (name.equals(controller.getName())) {
                    return controller;
                }
            }
        }
        return null;
    }

    static public Layer getLayer(Layers layers, String name) {
        for (int i = 0; i < layers.getLayer().size(); i++) {
            Layer layer = layers.getLayer().get(i);

            if (name.equals(layer.getName())) {
                return layer;
            }
        }
        return null;
    }

    static public Integer getLayerNum(Layers layers, String name) {
        for (int i = 0; i < layers.getLayer().size(); i++) {
            Layer layer = layers.getLayer().get(i);

            if (name.equals(layer.getName())) {
                return i;
            }
        }
        return null;
    }

    static private Controller getController(Layers layers, int layerNum, String name) {
        for (Controller controller : layers.getLayer().get(layerNum).getController()) {
            if (name.equals(controller.getName())) {
                return controller;
            }
        }
        return null;
    }

    static private NeuralFunction getTransfer(Transfers transfers, String name) {
        NeuralFunction nf = null;

        for (ControlFunction transfer : transfers.getTransfer()) {
            if (transfer.getName().equals(name)) {
                nf = transfer.getNeuralFunction();
            }
        }
        return nf;
    }

    static public void addLinks(Controller controller, int funcType, int transType, String functionName, String[][] lks) {

        switch (funcType) {

            case INPUT: {
                InputFunctions input = controller.getFunctions().getInputFunctions();
                NeuralFunction nf;
                if (transType == TRANSFER) {
                    Transfers transfers = input.getTransfers();
                    nf = getTransfer(transfers, functionName);
                } else {
                    nf = input.getInput().getNeuralFunction();
                }
                addLinks(nf, lks);
                break;
            }
        }
    }

    static public void addFunction(Controller controller, ControlFunction control, int function, boolean transfer) {

        Layers.Layer.Controller.Functions functions = controller.getFunctions();

        if (functions == null) {
            functions = new Layers.Layer.Controller.Functions();
            controller.setFunctions(functions);
        }

        switch (function) {

            case INPUT: {
                if (transfer) {
                    addTransferFunction(controller, control, function);
                } else {
                    if (controller.getFunctions().getInputFunctions() == null) {
                        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
                        functions.setInputFunctions(inputFunctions);
                    }
                    InputFunctions input = controller.getFunctions().getInputFunctions();
                    input.setInput(control);
                }
                break;
            }

            case OUTPUT: {
                if (transfer) {
                    addTransferFunction(controller, control, function);
                } else {
                    if (controller.getFunctions().getOutputFunctions() == null) {
                        Layers.Layer.Controller.Functions.OutputFunctions outputFunctions = new Layers.Layer.Controller.Functions.OutputFunctions();
                        functions.setOutputFunctions(outputFunctions);
                    }
                    OutputFunctions output = controller.getFunctions().getOutputFunctions();
                    output.setOutput(control);
                }
                break;
            }

            case REFERENCE: {
                if (transfer) {
                    addTransferFunction(controller, control, function);
                } else {
                    if (controller.getFunctions().getReferenceFunctions() == null) {
                        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
                        functions.setReferenceFunctions(referenceFunctions);
                    }
                    ReferenceFunctions reference = controller.getFunctions().getReferenceFunctions();
                    reference.setReference(control);
                }
                break;
            }

            case ERROR: {
                if (transfer) {
                    addTransferFunction(controller, control, function);
                } else {
                    if (controller.getFunctions().getErrorFunctions() == null) {
                        Layers.Layer.Controller.Functions.ErrorFunctions errorFunctions = new Layers.Layer.Controller.Functions.ErrorFunctions();
                        functions.setErrorFunctions(errorFunctions);
                    }
                    ErrorFunctions error = controller.getFunctions().getErrorFunctions();
                    error.setError(control);
                }
                break;
            }
        }
    }

    static public void addTransferFunction(Controller controller, ControlFunction control, int function, int index) {

        switch (function) {

            case INPUT: {
                InputFunctions input = controller.getFunctions().getInputFunctions();
                Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = controller.getFunctions().getInputFunctions().getTransfers();
                if (transfers == null) {
                    transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
                }
                List<ControlFunction> transferList = transfers.getTransfer();
                transferList.add(index, control);
                input.setTransfers(transfers);
                break;
            }

            case OUTPUT: {
                OutputFunctions output = controller.getFunctions().getOutputFunctions();
                Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = controller.getFunctions().getOutputFunctions().getTransfers();
                if (transfers == null) {
                    transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
                }
                List<ControlFunction> transferList = transfers.getTransfer();
                transferList.add(index, control);
                output.setTransfers(transfers);
                break;
            }

            case REFERENCE: {
                ReferenceFunctions reference = controller.getFunctions().getReferenceFunctions();
                Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = controller.getFunctions().getReferenceFunctions().getTransfers();
                if (transfers == null) {
                    transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
                }
                List<ControlFunction> transferList = transfers.getTransfer();
                transferList.add(index, control);
                reference.setTransfers(transfers);
                break;
            }

            case ERROR: {
                ErrorFunctions error = controller.getFunctions().getErrorFunctions();
                Layers.Layer.Controller.Functions.ErrorFunctions.Transfers transfers = controller.getFunctions().getErrorFunctions().getTransfers();
                if (transfers == null) {
                    transfers = new Layers.Layer.Controller.Functions.ErrorFunctions.Transfers();
                }
                List<ControlFunction> transferList = transfers.getTransfer();
                transferList.add(index, control);
                error.setTransfers(transfers);
                break;
            }
        }
    }

    static public void addTransferFunction(Controller controller, ControlFunction control, int function) {

        switch (function) {

            case INPUT: {
                InputFunctions input = controller.getFunctions().getInputFunctions();
                Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = controller.getFunctions().getInputFunctions().getTransfers();
                if (transfers == null) {
                    transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
                }
                List<ControlFunction> transferList = transfers.getTransfer();
                transferList.add(control);
                input.setTransfers(transfers);
                break;
            }

            case OUTPUT: {
                OutputFunctions output = controller.getFunctions().getOutputFunctions();
                Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = controller.getFunctions().getOutputFunctions().getTransfers();
                if (transfers == null) {
                    transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
                }
                List<ControlFunction> transferList = transfers.getTransfer();
                transferList.add(control);
                output.setTransfers(transfers);
                break;
            }

            case REFERENCE: {
                ReferenceFunctions reference = controller.getFunctions().getReferenceFunctions();
                Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = controller.getFunctions().getReferenceFunctions().getTransfers();
                if (transfers == null) {
                    transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
                }
                List<ControlFunction> transferList = transfers.getTransfer();
                transferList.add(control);
                reference.setTransfers(transfers);
                break;
            }

            case ERROR: {
                ErrorFunctions error = controller.getFunctions().getErrorFunctions();
                Layers.Layer.Controller.Functions.ErrorFunctions.Transfers transfers = controller.getFunctions().getErrorFunctions().getTransfers();
                if (transfers == null) {
                    transfers = new Layers.Layer.Controller.Functions.ErrorFunctions.Transfers();
                }
                List<ControlFunction> transferList = transfers.getTransfer();
                transferList.add(control);
                error.setTransfers(transfers);
                break;
            }
        }
    }

    static public Activation configureActivation(String name, String[] functions) {

        pct.moons.co.uk.schema.layers.Link link = new pct.moons.co.uk.schema.layers.Link();
        link.setName(name);
        pct.moons.co.uk.schema.layers.Activation activation = new pct.moons.co.uk.schema.layers.Activation();
        activation.setDeactivateLink(link);
        pct.moons.co.uk.schema.layers.Activation.Functions fs = new pct.moons.co.uk.schema.layers.Activation.Functions();
        activation.setFunctions(fs);

        pct.moons.co.uk.schema.layers.Activation.Functions.List list = new pct.moons.co.uk.schema.layers.Activation.Functions.List();
        fs.setList(list);

        for (String s : functions) {
            list.getFunctionType().add(s);
        }
        activation.setFunctions(fs);
        return activation;
    }

    static public NeuralFunction configureNeuralFunction(String type, String[][] pars, String[][] lks) throws Exception {

        NeuralFunction function = new NeuralFunction();
        int numLinks = 0;
        if (lks != null) {
            numLinks = lks.length;
        }

        function.setType(type);
        if (pars != null) {
            List<Parameters> parameters = function.getParameters();
            for (String[] par : pars) {
                if (par[0].equalsIgnoreCase("weights")) {
                    if (par[1] == null) {
                        throw new Exception("Weights null, type " + type);

                    }
                    String[] wts = par[1].split(",");
                    if (numLinks != wts.length) {
                        throw new Exception("Weights wrong size, parameter " + par[0] + ", type " + type);
                    }
                }
                if (par[1] == null) {
                    throw new Exception("Value is null, parameter " + par[0] + ", type " + type);
                }
                Parameters p = new Parameters();
                p.setName(par[0]);
                p.setValue(par[1]);
                p.setDataType(par[2]);
                parameters.add(p);
            }
        }

        addLinks(function, lks);
        /*
         if (lks != null) {
         List<Link> links = function.getLinks();
         int ctr = 0;
         for (String[] lk : lks) {
         Link l = new Link();
         l.setIndex(String.valueOf(ctr++));
         l.setName(lk[0]);
         if (lk.length > 1) {
         l.setType(lk[1]);
         }
         links.add(l);
         }
         }*/
        return function;
    }

    static private void addLinks(NeuralFunction function, String[][] lks) {
        if (lks != null) {
            List<Link> links = function.getLinks();
            int ctr = 0;
            for (String[] lk : lks) {
                Link l = new Link();
                l.setIndex(String.valueOf(ctr++));
                l.setName(lk[0]);
                if (lk.length > 1) {
                    l.setType(lk[1]);
                }
                links.add(l);
            }
        }
    }

    public static ControlFunction valueActivateFunction(String name, String[][] links) throws Exception {
        return configureControlFunction(name, "", "ValueActivate",
                new String[][]{{"Key", Globals.getInstance().get(name + "_Key"), ""}}, links);
    }

    public static ControlFunction weightedSumFunction(String prefix, String desc, String[][] links) throws Exception {

        return configureControlFunction(prefix, desc, "WeightedSum",
                new String[][]{{"Weights", Globals.getInstance().get(prefix + "_Weights"), "String"}},
                links);
    }

    public static ControlFunction limitBandedFunction(String prefix, String desc, String link, boolean display) throws Exception {

        return configureControlFunction(prefix, desc, "LimitBanded",
                new String[][]{
                    {"Threshold", Globals.getInstance().get(prefix + "_Threshold"), "Double"},
                    {"Upper", Globals.getInstance().get(prefix + "_Upper"), display ? "Double" : ""},
                    {"Lower", Globals.getInstance().get(prefix + "_Lower"), display ? "Double" : ""},
                    {"BandWidth", Globals.getInstance().get(prefix + "_BandWidth"), display ? "Double" : ""},
                    {"BandValue", Globals.getInstance().get(prefix + "_BandValue"), display ? "Double" : ""}},
                new String[][]{{link}});
    }

    public static ControlFunction sigmoidFunction(String prefix, String link, String absolute, boolean display) throws Exception {
        return configureControlFunction(prefix, "Sigmoid function", "Sigmoid",
                new String[][]{
                    {"InScale", Globals.getInstance().get(prefix + "_InScale"), "Double"},
                    {"OutScale", Globals.getInstance().get(prefix + "_OutScale"), "Double"},
                    {"XShift", Globals.getInstance().get(prefix + "_XShift"), display ? "Double" : ""},
                    {"YShift", Globals.getInstance().get(prefix + "_YShift"), display ? "Double" : ""},
                    {"InputTolerance", Globals.getInstance().get(prefix + "_InputTolerance"), display ? "Double" : ""},
                    {"Absolute", absolute, ""}
                },
                new String[][]{{link}});
    }

    public static ControlFunction sigmoidFunction(String prefix, String link, String absolute) throws Exception {
        return configureControlFunction(prefix, "Sigmoid function", "Sigmoid",
                new String[][]{
                    {"InScale", Globals.getInstance().get(prefix + "_InScale"), "Double"},
                    {"OutScale", Globals.getInstance().get(prefix + "_OutScale"), "Double"},
                    {"XShift", Globals.getInstance().get(prefix + "_XShift"), "Double"},
                    {"YShift", Globals.getInstance().get(prefix + "_YShift"), "Double"},
                    {"InputTolerance", Globals.getInstance().get(prefix + "_InputTolerance"), "Double"},
                    {"Absolute", absolute, ""}
                },
                new String[][]{{link}});
    }

    public static ControlFunction integrationFunction(String prefix, String link) throws Exception {
        return configureControlFunction(prefix, "Integration function", "Integration",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "_Gain"), "String"},
                    {"Slow", Globals.getInstance().get(prefix + "_Slow"), "String"},
                    {"Integrate", Globals.getInstance().get(prefix + "_Integrate"), ""},
                    {"Tolerance", Globals.getInstance().get(prefix + "_Tolerance"), ""}
                },
                new String[][]{{link}});
    }

    public static ControlFunction integrationFunction(String prefix, String[][] links) throws Exception {
        return configureControlFunction(prefix, "Integration function", "Integration",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "_Gain"), "String"},
                    {"Slow", Globals.getInstance().get(prefix + "_Slow"), "String"},
                    {"Integrate", Globals.getInstance().get(prefix + "_Integrate"), ""},
                    {"Tolerance", Globals.getInstance().get(prefix + "_Tolerance"), ""}
                },
                links);
    }

    public static ControlFunction proportionalIntegrationFunction(String prefix, String[][] links) throws Exception {
        return configureControlFunction(prefix, "Proportional integration function", "ProportionalIntegration",
                new String[][]{
                    {"Gain", Globals.getInstance().get(prefix + "_Gain"), "String"}
                },
                links);
    }

    public static ControlFunction proportionalIntegrationFunction(String prefix, String[][] links, boolean infinity) throws Exception {

        String[][] pars;
        if (infinity) {
            pars = new String[][]{
                {"Gain", Globals.getInstance().get(prefix + "_Gain"), "String"},
                {"EnableInfinity", "true", "Boolean"}
            };
        } else {
            pars = new String[][]{
                {"Gain", Globals.getInstance().get(prefix + "_Gain"), "String"}
            };
        }

        return configureControlFunction(prefix, "Proportional integration function", "ProportionalIntegration",
                pars, links);
    }

    public static ControlFunction sineFunction() throws Exception {
        return configureControlFunction("Sine", "Sine", "SineGenerator",
                new String[][]{
                    {"Frequency", Globals.getInstance().get("Sine_Frequency"), "Double"},
                    {"Amplitude", Globals.getInstance().get("Sine_Amplitude"), "Double"},
                    {"YShift", Globals.getInstance().get("Sine_YShift"), "Double"},
                    {"Step", Globals.getInstance().get("Sine_Step"), "Double"}
                },
                null);
    }

    public static ControlFunction stepFunction() throws Exception {
        return configureControlFunction("Step", "Step", "Step",
                new String[][]{
                    {"Period", Globals.getInstance().get("Step_Period"), "Double"},
                    {"Upper", Globals.getInstance().get("Step_Upper"), "Double"},
                    {"Lower", Globals.getInstance().get("Step_Lower"), "Double"}
                },
                null);
    }

    public static ControlFunction randomFunction() throws Exception {
        return configureControlFunction("Random", "Random", "Random",
                new String[][]{
                    {"Scale", Globals.getInstance().get("Random_Scale"), "Double"},
                    {"Slow", Globals.getInstance().get("Random_Slow"), "Double"},
                    {"Initial", Globals.getInstance().get("Random_Initial"), "Double"},
                    {"Min", Globals.getInstance().get("Random_Min"), "Double"},
                    {"Max", Globals.getInstance().get("Random_Max"), "Double"},},
                null);
    }

    public static ControlFunction constantFunction(String name) throws Exception {
        return configureControlFunction(name, "Constant", "Constant",
                new String[][]{
                    {"Constant", Globals.getInstance().get(name + "_Constant"), "Double"}},
                null);
    }

    public static ControlFunction constantFunction(String name, boolean display) throws Exception {
        return configureControlFunction(name, "Constant", "Constant",
                new String[][]{
                    {"Constant", Globals.getInstance().get(name + "_Constant"), display ? "Double" : ""}},
                null);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    static public boolean saveToXML(Layers layers, String fileName) {

        try {

            File file = new File(fileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(Layers.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                    true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
                    "http://uk.co.moons.pct/schema/Layers ../Layers.xsd");

            jaxbMarshaller.marshal(layers, file);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return true;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    static public boolean saveToXML(Layers layers, String fileName, String path) {

        try {

            File file = new File(fileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(Layers.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                    true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
                    "http://uk.co.moons.pct/schema/Layers " + path);

            jaxbMarshaller.marshal(layers, file);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return true;
    }

    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    static public ControlHierarchy verify(String config) throws JAXBException, IOException, Exception {
        ControlHierarchy ch = new ControlHierarchy(config);
        Globals.getInstance().verify();
        return ch;
    }
}
