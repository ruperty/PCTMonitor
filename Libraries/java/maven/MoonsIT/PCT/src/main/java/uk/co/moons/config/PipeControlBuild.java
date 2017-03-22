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
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moons.control.ControlFunctionCollection;
import uk.co.moons.control.Controller;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moons.control.functions.ControlFunction;
import uk.co.moons.control.neural.BaseNeuralFunction;
import uk.co.moons.control.neural.factory.NeuralFunctionFactory;

/**
 *
 * @author Rupert Young
 * Moon's Information Technology
 *
 */
public class PipeControlBuild extends BaseControlBuild {

    private static final Logger logger = Logger.getLogger(PipeControlBuild.class.getName());
    //protected ArrayList<String> orderedControllers = new ArrayList<String>();
    protected int index = 0;
    private PipeConfig pipeConfig = null;

    public PipeControlBuild(String config) throws Exception {
        super(config);
        pipeConfig = (PipeConfig) controlConfig;

        buildLayers(controlConfig);
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
        for (List<String> layer : pipeConfig.getLayers()) {
            //logger.info("+++ layer type " + layer.getType());
            buildLayer(layer, layerLevel++);
            for (String st : layer) {
                //logger.info(st);
            }
        }
        if (orderedControllers.size() > 0 && hmControllers.size() != orderedControllers.size()) {
            checkControllers();
        }
        setLinks();
    }

    private void buildLayer(List<String> layer, int layerLevel) throws Exception {
        //int cnt = 0;
        List<String> cont = new ArrayList<String>();
        Controller control = null;
        for (String line : layer) {
            if (line.startsWith("CON")) {
                logger.log(Level.INFO, "+++ controller name {0}", line.substring(4));
                if (!cont.isEmpty()) {
                    control = buildController(cont);
                    hmControllers.put(cont.get(0).substring(4), control);
                    layers[layerLevel].add(control);
                    cont.clear();
                }
            }
            cont.add(line);
        }
        control = buildController(cont);
        hmControllers.put(cont.get(0).substring(4), control);
        layers[layerLevel].add(control);
        cont.clear();
    }

    private Controller buildController(List<String> controller) throws Exception {
        
        // TODO: add activation
        
        ControlFunctionCollection inputCollection = null;
        ControlFunctionCollection referenceCollection = null;
        ControlFunctionCollection errorCollection = null;
        ControlFunctionCollection outputCollection = null;

        ListIterator<String> li = controller.listIterator();

        li.next(); //CON
        if (li.next().startsWith("INP")) {
            li.previous();
            inputCollection = buildControlFunctionCollection(li);
        } else {
            li.previous();
        }

        if (li.hasNext()) {
            if (li.next().startsWith("REF")) {
                li.previous();
                referenceCollection = buildControlFunctionCollection(li);
            } else {
                li.previous();
            }
        }

        if (li.hasNext()) {
            if (li.next().startsWith("ERR")) {
                li.previous();
                errorCollection = buildControlFunctionCollection(li);
                errorCollection.getMainFunction().getNeural().setLink(referenceCollection.getMainFunction());
                errorCollection.getMainFunction().getNeural().setLink(inputCollection.getMainFunction());
            } else {
                li.previous();
            }
        }

        if (li.hasNext()) {
            if (li.next().startsWith("OUT")) {
                li.previous();
                outputCollection = buildControlFunctionCollection(li);
                if (errorCollection != null) {
                    outputCollection.getMainFunction().getNeural().setLink(errorCollection.getMainFunction());
                }
            } else {
                li.previous();
            }
        }

        Controller control = new Controller(inputCollection, errorCollection, outputCollection, referenceCollection);
        return control;
    }

    private ControlFunctionCollection buildControlFunctionCollection(ListIterator<String> li) throws Exception {
        ControlFunctionCollection collection = null;
        String[] function = li.next().split("\\|");
        collection = new ControlFunctionCollection();
        BaseControlFunction control = null;
        String name = function[1];
        logger.fine("+++ control function type " + name);//.getType());
        BaseNeuralFunction binf = NeuralFunctionFactory.getNeuralFunction(name, function[2], function[3]);
        control = new ControlFunction(binf);
        control.setName(name);
        binf.setName(name);
        binf.setPosindex(index++);
        addControlToHashMap(name, control);
        collection.setMainFunction(control);
        collection.setTransferFunctions(buildTransfers(li));

        return collection;
    }

    private List<BaseControlFunction> buildTransfers(ListIterator<String> li) throws Exception {

        List<BaseControlFunction> transferList = new ArrayList<BaseControlFunction>();
        while (li.hasNext()) {
            String line = li.next();
            if (!line.startsWith("TRS")) {
                li.previous();
                break;
            }

            String[] function = line.split("\\|");
            String name = function[1];

            BaseControlFunction transferControl = null;
            logger.fine("+++ transfer function type " + name);//.getType());
            BaseNeuralFunction binf = NeuralFunctionFactory.getNeuralFunction(name, function[2], function[3]);
            transferControl = new ControlFunction(binf);
            transferControl.setName(name);
            binf.setName(name);
            binf.setPosindex(index++);
            addControlToHashMap(name, transferControl);
            transferList.add(transferControl);
        }
        return transferList;
    }

    /*
    
    private List<pct.moons.co.uk.schema.layers.ControlFunction> convertTransfers(pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions function) {
    List<pct.moons.co.uk.schema.layers.ControlFunction> list = null;
    Transfers transfers = function.getTransfers();
    if (transfers != null) {
    list = new ArrayList<pct.moons.co.uk.schema.layers.ControlFunction>();
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
    list = new ArrayList<pct.moons.co.uk.schema.layers.ControlFunction>();
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
    list = new ArrayList<pct.moons.co.uk.schema.layers.ControlFunction>();
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
    list = new ArrayList<pct.moons.co.uk.schema.layers.ControlFunction>();
    for (pct.moons.co.uk.schema.layers.ControlFunction transfer : transfers.getTransfer()) {
    list.add(transfer);
    }
    }
    return list;
    }
    
    
     */
    public String getType() {
        return controlConfig.getType();
    }
}
