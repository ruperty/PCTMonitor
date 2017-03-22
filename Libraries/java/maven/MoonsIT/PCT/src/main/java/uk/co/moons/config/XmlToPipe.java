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

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import pct.moons.co.uk.schema.layers.Layers;
import pct.moons.co.uk.schema.layers.Layers.Layer;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions;
import pct.moons.co.uk.schema.layers.Link;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.neural.types.NeuralNames;
import uk.co.moons.control.neural.types.NeuralTypes;

/**
 *
 * @author Rupert Young
 * Moon's Information Technology
 *
 */
public class XmlToPipe {

    public void write(String file, Layers layers) throws Exception {
        StringBuilder sb = new StringBuilder();

        if (layers.getOrderedControllers() != null) {
            List<String> ordered = layers.getOrderedControllers().getController();
            sb.append("ORD");
            for (String cont : ordered) {
                sb.append("|").append(cont);
            }
            sb.append("\n");
        }
        int i = 0;
        for (Layer layer : layers.getLayer()) {
            sb.append("LYR|").append(i++);
            sb.append("\n");
            processLayer(layer, sb);
        }

        FileOutputStream fos = new FileOutputStream(file.replaceAll(".xml", ".dat"));
        fos.write(sb.toString().getBytes());
        fos.close();

        System.out.println(sb.toString());
    }

    private void processLayer(Layer layer, StringBuilder sb) throws Exception {
        //int cnt = 0;
        for (pct.moons.co.uk.schema.layers.Layers.Layer.Controller cont : layer.getController()) {
            processController(cont, sb);
            //hmControllers.put(cont.getName(), control);
            //layers[layerLevel].add(control);
        }
    }

    private void processController(pct.moons.co.uk.schema.layers.Layers.Layer.Controller controller, StringBuilder sb) throws Exception {

        Functions functions = controller.getFunctions();
        sb.append("CON|").append(controller.getName()).append("\n");
        //pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.ErrorFunctions.Transfers 

        if (functions.getInputFunctions() != null) {
            sb.append("INP|");
            processControlFunctionCollection(functions.getInputFunctions().getInput(), convertTransfers(functions.getInputFunctions()), sb);
        }

        if (functions.getReferenceFunctions() != null) {
            sb.append("REF|");
            processControlFunctionCollection(functions.getReferenceFunctions().getReference(), convertTransfers(functions.getReferenceFunctions()), sb);
        }

        if (functions.getErrorFunctions() != null) {
            sb.append("ERR|");
            processControlFunctionCollection(functions.getErrorFunctions().getError(), convertTransfers(functions.getErrorFunctions()), sb);
        }

        if (functions.getOutputFunctions() != null) {
            sb.append("OUT|");
            processControlFunctionCollection(functions.getOutputFunctions().getOutput(), convertTransfers(functions.getOutputFunctions()), sb);
        }

    }

    private void processControlFunctionCollection(pct.moons.co.uk.schema.layers.ControlFunction function, List<pct.moons.co.uk.schema.layers.ControlFunction> transfers, StringBuilder sb) throws Exception {
        if (function != null) {
            //ControlFunctionCollection collection = new ControlFunctionCollection();
            sb.append(NeuralNames.getNeuralNameInt(function.getName()));
            pct.moons.co.uk.schema.layers.NeuralFunction inf = function.getNeuralFunction();
            sb.append("|").append(getIntType(inf.getType())).append("|");
            processParameters(inf.getParameters(), sb);
            sb.append("&");
            processLinks(inf.getLinks(), sb);
            sb.append("\n");
            processTransfers(transfers, sb);

        }
    }

    private int getIntType(String type) throws Exception {
        return NeuralTypes.getNeuralTypes(type);
    }

    private void processTransfers(List<pct.moons.co.uk.schema.layers.ControlFunction> transfers, StringBuilder sb) throws Exception {
        if (transfers == null) {
            return;
        }
        for (pct.moons.co.uk.schema.layers.ControlFunction tf : transfers) {
            pct.moons.co.uk.schema.layers.NeuralFunction inf = tf.getNeuralFunction();
            sb.append("TRS|").append(NeuralNames.getNeuralNameInt(tf.getName()));
            sb.append("|").append(getIntType(inf.getType())).append("|");
            processParameters(inf.getParameters(), sb);
            sb.append("&");
            processLinks(inf.getLinks(), sb);
            sb.append("\n");
        }
    }

    private void processParameters(List<Parameters> parameters, StringBuilder sb) {
        for (Parameters parameter : parameters) {
            sb.append(parameter.getName()).append("^");
            sb.append(parameter.getValue()).append("^");
            //sb.append(parameter.getDataType());
            sb.append("~");
        }
    }

    private void processLinks(List<Link> links, StringBuilder sb) throws Exception {
        for (Link link : links) {
            sb.append(NeuralNames.getNeuralNameInt(link.getName())).append("^");
            sb.append(link.getIndex()).append("^");
            //sb.append(link.getType());
            sb.append("~");
        }
    }

    private List<pct.moons.co.uk.schema.layers.ControlFunction> convertTransfers(pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions function) {
        List<pct.moons.co.uk.schema.layers.ControlFunction> list = null;
        pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = function.getTransfers();
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
}
