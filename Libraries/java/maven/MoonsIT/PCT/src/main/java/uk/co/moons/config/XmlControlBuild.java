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
package uk.co.moons.config;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
//import pct.moons.co.uk.schema.layers.Activation;
import pct.moons.co.uk.schema.layers.Layers.Layer;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions.Transfers;
import uk.co.moons.control.ControlFunctionCollection;
import uk.co.moons.control.Controller;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moons.control.functions.ControlFunction;
import uk.co.moons.control.neural.BaseNeuralFunction;
import uk.co.moons.control.neural.factory.NeuralFunctionFactory;

/**
 *
 * @author Rupert Young Moon's Information Technology
 *
 */
public class XmlControlBuild extends BaseControlBuild {

    private static final Logger LOG = Logger.getLogger(XmlControlBuild.class.getName());
    private XmlConfig xmlConfig = null;
    protected int index = 0;
    private final boolean debug = false;

    public XmlControlBuild(String config) throws Exception {
        super(config);
        xmlConfig = (XmlConfig) controlConfig;
        //long start = System.currentTimeMillis();
        try {
            buildLayers(controlConfig);
        } catch (Exception e) {
            LOG.severe("Exception in XmlControlBuild constructor " + e.toString());
            throw e;
        }
        //LOG.log(Level.INFO, "---> build {0} ms", (System.currentTimeMillis() - start));
    }

    private void buildLayers(BaseControlConfig controlConfig) throws Exception {

        List<String> ordered = controlConfig.getOrderedControllers();
        if (ordered != null) {
            for (String cont : ordered) {
                orderedControllers.add(cont);
            }
        }

        initLayers(controlConfig.size());
        int layerLevel = 0;
        for (Layer layer : xmlConfig.getLayer()) {
            //logger.info("+++ layer type " + layer.getType());
            buildLayer(layer, layerLevel++);
        }
        if (orderedControllers.size() > 0 && hmControllers.size() != orderedControllers.size()) {
            checkControllers();
        }
        addActivation();
        setLinks();
    }

    private void addActivation() throws Exception {

        for (Layer layer : xmlConfig.getLayer()) {
            for (pct.moons.co.uk.schema.layers.Layers.Layer.Controller cont : layer.getController()) {
                String name = cont.getName();
                pct.moons.co.uk.schema.layers.Activation activation = cont.getActivation();
                if (activation != null) {
                    Activation ac = null;
                    ac = new Activation(activation.getDeactivateLink(), activation.getFunctions().getList(), hmControls);
                    Controller controller = hmControllers.get(name);
                    controller.setActivation(ac);
                }

            }
        }

    }

    private void buildLayer(Layer layer, int layerLevel) throws Exception {
        //int cnt = 0;
        for (pct.moons.co.uk.schema.layers.Layers.Layer.Controller cont : layer.getController()) {
            if (debug) {
                LOG.log(Level.INFO, "+++ controller name {0}", cont.getName());
            }
            Controller control = buildController(cont);
            if (hmControllers.get(cont.getName()) != null) {
                throw new Exception("Controller " + cont.getName() + " already present");
            }
            hmControllers.put(cont.getName(), control);
            layers[layerLevel].add(control);
            //layers[layerLevel].setType(layer.getType());
        }
    }

    private List<BaseControlFunction> buildTransfers(List<pct.moons.co.uk.schema.layers.ControlFunction> transfers) throws Exception {
        if (transfers == null) {
            return null;
        }
        List<BaseControlFunction> transferList = new ArrayList<>();
        for (pct.moons.co.uk.schema.layers.ControlFunction tf : transfers) {
            //pct.moons.co.uk.schema.layers.ControlFunction tf = transfer.getTransfer();
            BaseControlFunction transferControl;
            if (debug) {
                LOG.log(Level.FINE, "+++ transfer function name {0}", tf.getName());//.getType());
            }
            pct.moons.co.uk.schema.layers.NeuralFunction inf = tf.getNeuralFunction();
            BaseNeuralFunction binf = NeuralFunctionFactory.getNeuralFunction(inf);
            transferControl = new ControlFunction(binf);
            transferControl.setName(tf.getName());
            binf.setName(tf.getName());
            binf.setPosindex(index++);
            addControlToHashMap(tf.getName(), transferControl);
            transferList.add(transferControl);
        }
        return transferList;
    }

    private ControlFunctionCollection buildControlFunctionCollection(pct.moons.co.uk.schema.layers.ControlFunction function, List<pct.moons.co.uk.schema.layers.ControlFunction> transfers) throws Exception {
        ControlFunctionCollection collection = null;
        if (function != null) {
            collection = new ControlFunctionCollection();
            BaseControlFunction control;
            if (debug) {
                LOG.log(Level.FINE, "+++ control function name {0}", function.getName());//.getType());
            }
            pct.moons.co.uk.schema.layers.NeuralFunction inf = function.getNeuralFunction();
            BaseNeuralFunction binf = NeuralFunctionFactory.getNeuralFunction(inf);
            control = new ControlFunction(binf);
            control.setName(function.getName());
            binf.setName(function.getName());
            binf.setPosindex(index++);
            addControlToHashMap(function.getName(), control);
            collection.setMainFunction(control);
            collection.setTransferFunctions(buildTransfers(transfers));
        }
        return collection;
    }

    private List<pct.moons.co.uk.schema.layers.ControlFunction> convertTransfers(pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions function) {
        List<pct.moons.co.uk.schema.layers.ControlFunction> list = null;
        Transfers transfers = function.getTransfers();
        if (transfers != null) {
            list = new ArrayList<>();
            for (pct.moons.co.uk.schema.layers.ControlFunction transfer : transfers.getTransfer()) {
                list.add(transfer);
            }
        }
        return list;
    }

    private List<pct.moons.co.uk.schema.layers.ControlFunction> convertTransfers(pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.ReferenceFunctions function) {
        List<pct.moons.co.uk.schema.layers.ControlFunction> list = null;
        pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = function.getTransfers();
        if (transfers != null) {
            list = new ArrayList<>();
            for (pct.moons.co.uk.schema.layers.ControlFunction transfer : transfers.getTransfer()) {
                list.add(transfer);
            }
        }
        return list;
    }

    private List<pct.moons.co.uk.schema.layers.ControlFunction> convertTransfers(pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.ErrorFunctions function) {
        List<pct.moons.co.uk.schema.layers.ControlFunction> list = null;
        pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.ErrorFunctions.Transfers transfers = function.getTransfers();
        if (transfers != null) {
            list = new ArrayList<>();
            for (pct.moons.co.uk.schema.layers.ControlFunction transfer : transfers.getTransfer()) {
                list.add(transfer);
            }
        }
        return list;
    }

    private List<pct.moons.co.uk.schema.layers.ControlFunction> convertTransfers(pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.OutputFunctions function) {
        List<pct.moons.co.uk.schema.layers.ControlFunction> list = null;
        pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = function.getTransfers();
        if (transfers != null) {
            list = new ArrayList<>();
            for (pct.moons.co.uk.schema.layers.ControlFunction transfer : transfers.getTransfer()) {
                list.add(transfer);
            }
        }
        return list;
    }

    private Controller buildController(pct.moons.co.uk.schema.layers.Layers.Layer.Controller controller) throws Exception {

        Functions functions = controller.getFunctions();
        if (functions == null) {
            throw new Exception("No functions present for controller " + controller.getName());
        }

        ControlFunctionCollection inputCollection = null;
        if (functions.getInputFunctions() != null) {
            inputCollection = buildControlFunctionCollection(functions.getInputFunctions().getInput(), convertTransfers(functions.getInputFunctions()));
        }

        ControlFunctionCollection referenceCollection = null;
        if (functions.getReferenceFunctions() != null) {
            referenceCollection = buildControlFunctionCollection(functions.getReferenceFunctions().getReference(), convertTransfers(functions.getReferenceFunctions()));
        }

        ControlFunctionCollection errorCollection = null;
        if (functions.getErrorFunctions() != null) {
            errorCollection = buildControlFunctionCollection(functions.getErrorFunctions().getError(), convertTransfers(functions.getErrorFunctions()));
            if (referenceCollection != null) {
                errorCollection.getMainFunction().getNeural().setLink(referenceCollection.getMainFunction());
            }
            if (inputCollection != null) {
                errorCollection.getMainFunction().getNeural().setLink(inputCollection.getMainFunction());
            }
        }

        ControlFunctionCollection outputCollection = null;
        if (functions.getOutputFunctions() != null) {
            outputCollection = buildControlFunctionCollection(functions.getOutputFunctions().getOutput(), convertTransfers(functions.getOutputFunctions()));
            if (errorCollection != null) {
                outputCollection.getMainFunction().getNeural().setLink(errorCollection.getMainFunction());
            }
        }

        Controller control = new Controller(inputCollection, errorCollection, outputCollection, referenceCollection);
        return control;
    }

    public String getType() {
        return controlConfig.getType();
    }
}
