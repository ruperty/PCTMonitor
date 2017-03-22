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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import pct.moons.co.uk.schema.layers.ControlFunction;
import pct.moons.co.uk.schema.layers.Layers;
import pct.moons.co.uk.schema.layers.Link;
import pct.moons.co.uk.schema.layers.NeuralFunction;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.ControlHierarchy;

/**
 *
 * @author ReStart
 */
public class Utils {

    // Margin on ODG drawing
    private static final int DRAWING_MARGIN = 1;
    private static final float CONFIG_MARGIN = 0.1f;

    public static final int INPUT = 0;
    public static final int REFERENCE = 1;
    public static final int ERROR = 2;
    public static final int OUTPUT = 3;
    public static final int TRANSFER = 4;

    public static ControlFunction configureControlFunction(String name, String description, String neuralType, String[][] parameters, String[][] links) throws Exception {
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

    public static NeuralFunction configureNeuralFunction(String type, String[][] pars, String[][] lks) throws Exception {

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
                        throw new Exception("Weights wrong size, parameter " + par[0] + ", type " + type + ", found " + par[1] + " expected size " + numLinks);
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

        return function;
    }

    private static void addLinks(NeuralFunction function, String[][] lks) {
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

    public static void setOrderedControllers(Layers layers, String[] list) {

        Layers.OrderedControllers ordered = new Layers.OrderedControllers();
        List<String> order = ordered.getController();
        order.addAll(Arrays.asList(list));
        layers.setOrderedControllers(ordered);
    }

    public static ControlHierarchy verify(String config) throws JAXBException, IOException, Exception {
        ControlHierarchy ch = new ControlHierarchy(config);
        //Globals.getInstance().verify();
        return ch;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static boolean saveToXML(Layers layers, String fileName) {

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
    public static boolean saveToXML(Layers layers, String fileName, String path) {

        try {

            File file = new File(fileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(Layers.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://uk.co.moons.pct/schema/Layers " + path);
            jaxbMarshaller.marshal(layers, file);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static Layers.Layer emptyLayer(String name) throws Exception {
        Layers.Layer layer = new Layers.Layer();
        layer.setName(name);

        return layer;
    }

    public static double convertCmDimension(String dim) {
        return Double.parseDouble(dim.substring(0, dim.indexOf("cm"))) + CONFIG_MARGIN;
    }

    public static double convertCmCoordinate(String dim) {
        return Double.parseDouble(dim.substring(0, dim.indexOf("cm"))) - DRAWING_MARGIN;
    }

    public static void addFunction(Layers.Layer.Controller controller, ControlFunction control, int function, boolean transfer) {

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
                    Layers.Layer.Controller.Functions.InputFunctions input = controller.getFunctions().getInputFunctions();
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
                    Layers.Layer.Controller.Functions.OutputFunctions output = controller.getFunctions().getOutputFunctions();
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
                    Layers.Layer.Controller.Functions.ReferenceFunctions reference = controller.getFunctions().getReferenceFunctions();
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
                    Layers.Layer.Controller.Functions.ErrorFunctions error = controller.getFunctions().getErrorFunctions();
                    error.setError(control);
                }
                break;
            }
        }
    }

    public static void addTransferFunction(Layers.Layer.Controller controller, ControlFunction control, int function) {

        switch (function) {

            case INPUT: {
                Layers.Layer.Controller.Functions.InputFunctions input = controller.getFunctions().getInputFunctions();
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
                Layers.Layer.Controller.Functions.OutputFunctions output = controller.getFunctions().getOutputFunctions();
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
                Layers.Layer.Controller.Functions.ReferenceFunctions reference = controller.getFunctions().getReferenceFunctions();
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
                Layers.Layer.Controller.Functions.ErrorFunctions error = controller.getFunctions().getErrorFunctions();
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

}
