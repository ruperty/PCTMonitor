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
package uk.co.moonsit.config.odg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.odftoolkit.odfdom.dom.element.draw.DrawCustomShapeElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawGElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawPageElement;
import org.odftoolkit.odfdom.dom.element.office.OfficeDrawingElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.odftoolkit.odfdom.dom.element.text.TextSpanElement;
import org.odftoolkit.simple.GraphicsDocument;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pct.moons.co.uk.schema.layers.ControlFunction;
import pct.moons.co.uk.schema.layers.Layers;
import pct.moons.co.uk.schema.layers.Layers.Layer;
import uk.co.moonsit.config.functions.Utils;
import uk.co.moonsit.utils.MoonsString;

/**
 *
 * @author ReStart
 */
public class ODGProcessing {

    private static final Logger LOG = Logger.getLogger(ODGProcessing.class.getName());

    private final List<ODGLayer> layerList;
    private final List<ODGConnector> connectorList;
    private final List<ODGController> controllerList;
    private final List<ODGFunctionConfig> configList;
    private final List<ODGFunction> functionList;
    private String[] order;

    //private final HashMap<String, ODGConnector> fwdConnectorMap;
    //private final HashMap<String, ODGConnector> bckConnectorMap;
    public final static double FUNCTION_SIZE = 0.5;
    public final static double FUNCTION_PROXIMITY = 3;

    public final static String CONTROLLER = "Controller";
    public final static String FUNCTION_CONFIG = "FunctionConfig";
    public final static String FUNCTION = "Function";
    public final static String CONNECTOR = "Connector";
    public final static String ORDER = "Order";

    public ODGProcessing() {
        layerList = new ArrayList<>();
        connectorList = new ArrayList<>();
        controllerList = new ArrayList<>();
        configList = new ArrayList<>();
        functionList = new ArrayList<>();

        //fwdConnectorMap = new HashMap<>();
        //bckConnectorMap = new HashMap<>();
    }

    public void verifyDiagram() throws Exception {
        // check that all configs are connected
        for (ODGFunctionConfig config : configList) {
            boolean found = false;
            for (ODGConnector connector : connectorList) {
                if (config.getName() != null && config.getName().equals(connector.getStartConnectorPoint().getName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new Exception("FunctionConfig " + (config.getName() == null ? "<unnamed>" : config.getName()) + " not connected at "
                        + MoonsString.formatPlaces(config.getLocation().getX(), 1) + " " + MoonsString.formatPlaces(config.getLocation().getY(), 1));
            }
        }
        // check if function has forward connection 
        /*
        for (ODGFunction function : functionList) {
            boolean found = false;
            for (ODGConnector connector : connectorList) {
                if (connector.isFunction() && function.getName().equals(connector.getStartConnectorPoint().getName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new Exception("Function " + function.getName() + " not connected at "
                        + MoonsString.formatPlaces(function.getLocation().getX(), 1) + " " + MoonsString.formatPlaces(function.getLocation().getY(), 1));
            }
        }*/
        // check that each function has been connected to a config
        for (ODGFunction function : functionList) {
            if (function.getConfigID() == null) {
                throw new Exception("Function not connected to configuration at " + MoonsString.formatPlaces(function.getLocation().getX(), 1)
                        + " " + MoonsString.formatPlaces(function.getLocation().getY(), 1));
            }
        }

        // for each controller check if each function has a connection to it
        // if so check if there is a config
        for (ODGController controller : controllerList) {
            ODGFunction function = controller.getInput();
            String name = function.getName();
            if (isFunctionConnected(name)) {
                if (function.getConfigID() == null) {
                    throw new Exception("Function " + name + " not configured at " + MoonsString.formatPlaces(function.getLocation().getX(), 1)
                            + " " + MoonsString.formatPlaces(function.getLocation().getY(), 1));
                }
            }

            function = controller.getReference();
            name = function.getName();
            if (isFunctionConnected(name)) {
                if (function.getConfigID() == null) {
                    throw new Exception("Function " + name + " not configured at " + MoonsString.formatPlaces(function.getLocation().getX(), 1)
                            + " " + MoonsString.formatPlaces(function.getLocation().getY(), 1));
                }
            }

            function = controller.getOutput();
            name = function.getName();
            if (isFunctionConnected(name)) {
                if (function.getConfigID() == null) {
                    throw new Exception("Function " + name + " not configured at " + MoonsString.formatPlaces(function.getLocation().getX(), 1)
                            + " " + MoonsString.formatPlaces(function.getLocation().getY(), 1));
                }
            }
        }

    }

    private boolean isFunctionConfigured(String name) {
        for (ODGConnector connector : connectorList) {
            if (connector.isFunctionConfig() && name.equals(connector.getStartConnectorPoint().getName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isFunctionConnected(String name) {

        for (ODGConnector connector : connectorList) {
            if (connector.isFunctionLink() && name.equals(connector.getEndConnectorPoint().getName())) {
                return true;
            }
        }
        return false;
    }

    public void constructPCTXML(Layers layers) throws Exception {

        if (order != null && order.length > 0) {
            Utils.setOrderedControllers(layers, order);
        }

        for (ODGLayer odglayer : layerList) {
            Layer layer = Utils.emptyLayer(odglayer.getName());
            layers.getLayer().add(odglayer.getLevel(), layer);
            List<Layers.Layer.Controller> controllers = layer.getController();

            constructControllers(odglayer, controllers);
            //if (controllers.size() > 0) {
            //  LOG.info(controllers.get(0).getFunctions().getInputFunctions().getInput().getNeuralFunction().getType());
            //}
        }

    }

    public boolean layerExists(ODGLayer odglayer1) throws Exception {
        for (ODGLayer odglayer : layerList) {
            if (odglayer.getName().equalsIgnoreCase(odglayer1.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean layerNumberExists(ODGLayer odglayer1) throws Exception {
        for (ODGLayer odglayer : layerList) {
            if (odglayer.getLevel() == odglayer1.getLevel()) {
                return true;
            }
        }
        return false;
    }

    public void constructControllers(ODGLayer odglayer, List<Layers.Layer.Controller> controllers) throws Exception {
        odglayer.sortControllers(controllerList);
        for (Integer i : odglayer.getControllers()) {
            Layers.Layer.Controller controller = new Layers.Layer.Controller();
            ODGController odgcontroller = controllerList.get(i);
            controller.setName(odgcontroller.getName());

            {
                ODGFunction input;
                if ((input = odgcontroller.getInput()) != null && input.getConfigID() != null) {
                    Utils.addFunction(controller, constructFunction(input), Utils.INPUT, false);
                    List<Integer> list = input.getTransferList();
                    Collections.reverse(list);
                    for (Integer transferID : list) {
                        ODGFunction transfer = functionList.get(transferID);
                        Utils.addTransferFunction(controller, constructFunction(transfer), Utils.INPUT);
                    }
                }
            }
            {
                ODGFunction reference;
                if ((reference = odgcontroller.getReference()) != null && reference.getConfigID() != null) {
                    Utils.addFunction(controller, constructFunction(reference), Utils.REFERENCE, false);
                    List<Integer> list = reference.getTransferList();
                    Collections.reverse(list);
                    for (Integer transferID : list) {
                        ODGFunction transfer = functionList.get(transferID);
                        Utils.addTransferFunction(controller, constructFunction(transfer), Utils.REFERENCE);
                    }
                }
            }

            {
                ODGFunction error = odgcontroller.getError();
                if (error != null && error.getConfigID() == null) {
                    if (odgcontroller.isDefaultError()) {
                        String name = controller.getName();
                        ControlFunction function = Utils.configureControlFunction(name + "Error", "...", "Subtract", null, 
                                new String[][]{{getLinkName( odgcontroller.getReference().getName())}, {getLinkName(odgcontroller.getInput().getName())}});
                        Utils.addFunction(controller, function, Utils.ERROR, false);
                    }
                } else {
                    Utils.addFunction(controller, constructFunction(error), Utils.OUTPUT, false);
                }
            }

            {
                ODGFunction output;
                if ((output = odgcontroller.getOutput()) != null && output.getConfigID() != null) {
                    if (odgcontroller.isDefaultError()) {
                        output.addLink(odgcontroller.getError().getName());
                    }
                    Utils.addFunction(controller, constructFunction(output), Utils.OUTPUT, false);
                    for (Integer transferID : output.getTransferList()) {
                        ODGFunction transfer = functionList.get(transferID);
                        Utils.addTransferFunction(controller, constructFunction(transfer), Utils.OUTPUT);
                    }
                }
            }

            controllers.add(controller);
        }
    }

    public ControlFunction constructFunction(ODGFunction function) throws Exception {
        String name = function.getName();
        int index = function.getConfigID();
        ODGFunctionConfig config = configList.get(index);
        if(function.getOverrideName()!=null)
            name = function.getOverrideName();

        ControlFunction controlFunction = Utils.configureControlFunction(name, "...", config.getType(), config.getParameters(), function.getLinks());

        return controlFunction;
    }

    public void parseDocument(String config) throws Exception {
        //long start = System.currentTimeMillis();
        GraphicsDocument gd;
        try {
            gd = GraphicsDocument.loadDocument(config);
            //LOG.log(Level.INFO, "processing time {0}", (System.currentTimeMillis() - start));
        } catch (Exception ex) {
            Logger.getLogger(ODGProcessing.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Path " + config + " is incorrect");
        }

        OfficeDrawingElement odfEl = gd.getContentRoot();
        DrawPageElement pageEl = (DrawPageElement) odfEl.getFirstChild();
        NodeList nodeList = pageEl.getChildNodes();

        processLayers(nodeList);
        processElements(nodeList);
        //LOG.log(Level.INFO, "processing time {0}", (System.currentTimeMillis() - start));
        processConnectors();
        //LOG.log(Level.INFO, "processing time {0}", (System.currentTimeMillis() - start));

    }

    private void processLayers(NodeList nodeList) throws Exception {

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                String name = elem.getTagName();
                switch (name) {
                    case "draw:custom-shape":
                        if (isLayer(elem)) {
                            ODGLayer layer = new ODGLayer(elem);
                            if (layerExists(layer)) {
                                throw new Exception("Layer " + layer.getName() + " already exists");
                            }
                            if (layerNumberExists(layer)) {
                                throw new Exception("Layer " + layer.getName() + ", there is already a layer with number " + layer.getLevel());
                            }
                            layerList.add(layer);
                        }
                        break;
                }
            }
        }
    }

    private void processElements(NodeList nodeList) throws Exception {

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                String name = elem.getTagName();
                switch (name) {
                    case "draw:frame":
                        break;
                    case "draw:custom-shape":
                        if (isLayer(elem)) {

                        } else {
                            ODGFunction function = new ODGFunction(elem);
                            functionList.add(function);
                            //LOG.log(Level.INFO, FUNCTION + " {0}", function.toString());
                        }
                        break;
                    case "draw:g":
                        processGroup(elem);
                        break;
                    case "draw:connector":
                    case "draw:path":
                        ODGConnector connector = new ODGConnector(elem);
                        connectorList.add(connector);
                        break;
                    default:
                        LOG.info("Unknown");
                        break;
                }
            }
        }
    }

    private void displayConnectors() {
        for (ODGConnector connector : connectorList) {
            String dis = connector.toString();
            if (dis.length() > 0) {
                LOG.info(dis);
            }
        }
    }

    // for each connector
    // go through configs and check if this connector is connected
    // if so set the start point as a config
    private void fillInConfigs() {
        for (ODGConnector connector : connectorList) {
            for (ODGFunctionConfig config : configList) {
                if (config.isIn(connector.getStartPoint())) {
                    String name = config.getName();
                    ODGConnectorPoint odgpoint = new ODGConnectorPoint(name, ODGProcessing.FUNCTION_CONFIG, configList.indexOf(config), null);
                    connector.setStartConnectorPoint(odgpoint);
                    break;
                }
            }
        }
    }

    // for each connector
    // for each controller check if this connector is connected and return the specific function
    // set the connection point for that function name
    private void fillInControllerFunctions() {
        for (ODGConnector connector : connectorList) {
            for (ODGController controller : controllerList) {
                ODGFunction function;
                if ((function = controller.isIn(connector.getEndPoint())) != null) {
                    ODGConnectorPoint odgpoint = new ODGConnectorPoint(function.getName(), ODGProcessing.FUNCTION, null, controllerList.indexOf(controller));
                    connector.setEndConnectorPoint(odgpoint);
                }
                if ((function = controller.isIn(connector.getStartPoint())) != null) {
                    ODGConnectorPoint odgpoint = new ODGConnectorPoint(function.getName(), ODGProcessing.FUNCTION, null, controllerList.indexOf(controller));
                    connector.setStartConnectorPoint(odgpoint);
                }
            }
        }
    }

    // for each function check if this connector is connected, to either the start or end point
    // and set the connection points accordingly
    // names of those functions still need to be determined from the configs
    private void fillInFunctions() {
        for (ODGConnector connector : connectorList) {
            for (ODGFunction function : functionList) {
                if (function.isIn(connector.getEndPoint())) {
                    ODGConnectorPoint odgpoint = new ODGConnectorPoint(function.getName(), ODGProcessing.FUNCTION, functionList.indexOf(function), null);
                    connector.setEndConnectorPoint(odgpoint);
                }
                /*Point2D p = new Point2D.Double();
                p.setLocation(10.3, 102.65);
                if(connector.getStartPoint().distance(p)<0.1){
                    LOG.log(Level.INFO, "Test {0} {1} {2} {3}", new Object[]{p, connector.getStartPoint(), connector.getStartPoint().distance(function.getLocation()),function.toString()});
                }*/
                if (function.isIn(connector.getStartPoint())) {
                    ODGConnectorPoint odgpoint = new ODGConnectorPoint(function.getName(), ODGProcessing.FUNCTION, functionList.indexOf(function), null);
                    connector.setStartConnectorPoint(odgpoint);
                }
            }
        }
    }

    // loop connectors for function configs
    // search for associated function by name
    // set the config ID for that function  
    // for functions that are not controllers set the name of the connector endpoint from the config name    
    private void fillInFunctionsConnectedToConfig() {
        for (ODGConnector connector : connectorList) {
            if (connector.isFunctionConfig()) {
                //LOG.info(connector.toString());
                if (connector.isEndController()) {
                    String fname = connector.getEndConnectorPoint().getName();
                    ODGFunction function = controllerList.get(connector.getEndConnectorPoint().getControllerId())
                            .getFunction(fname);
                    String configName = connector.getStartConnectorPoint().getName();
                    if (configName != null) {
                        function.setOverrideName(configName);
                    }

                    function.setConfigID(connector.getStartConnectorPoint().getId());
                } else {
                    ODGFunction function = functionList.get(connector.getEndConnectorPoint().getId());
                    function.setConfigID(connector.getStartConnectorPoint().getId());
                    String name = connector.getStartConnectorPoint().getName();
                    connector.getEndConnectorPoint().setName(name);
                    function.setName(name);
                }
            }

        }
    }

    // for each function check if this connector is connected, to either the start or end point
    private void fillInFunctionEndpoints() {
        for (ODGConnector connector : connectorList) {
            for (ODGFunction function : functionList) {
                //LOG.log(Level.INFO, "Test {0} {1} {2} ", new Object[]{connector.getEndPoint(), function.getLocation(), connector.getEndPoint().distance(function.getLocation())});

                if (function.isIn(connector.getEndPoint())) {
                    //LOG.info(function.getName());
                    String config = connector.getStartConnectorPoint().getName();
                    function.setName(config);
                    ODGConnectorPoint odgpoint = new ODGConnectorPoint(config, ODGProcessing.FUNCTION, functionList.indexOf(function), null);
                    connector.setEndConnectorPoint(odgpoint);
                    break;
                }

            }
        }
    }

    // set name for connector point that is a function
    private void fillInFunctionNames() throws Exception {
        for (ODGConnector connector : connectorList) {

            ODGConnectorPoint startConnectorPoint = connector.getStartConnectorPoint();
            ODGConnectorPoint endConnectorPoint = connector.getEndConnectorPoint();
            if (startConnectorPoint == null) {
                throw new Exception("Connector at " + MoonsString.formatPlaces(connector.getEndPoint().getX(), 1) + " "
                        + MoonsString.formatPlaces(connector.getEndPoint().getY(), 1) + " does not have start connector");
            }
            if (endConnectorPoint == null) {
                throw new Exception("Connector at " + MoonsString.formatPlaces(connector.getStartPoint().getX(), 1) + " "
                        + MoonsString.formatPlaces(connector.getStartPoint().getY(), 1) + " does not have end connector");
            }
            if (!connector.isStartController()) {
                if (startConnectorPoint.getType().equals(ODGProcessing.FUNCTION)) {
                    ODGFunction function = functionList.get(startConnectorPoint.getId());
                    if (function.getName() == null) {
                        throw new Exception("Function at " + MoonsString.formatPlaces(connector.getStartPoint().getX(), 1) + " "
                                + MoonsString.formatPlaces(connector.getStartPoint().getY(), 1) + " has no configured name");
                    }
                    startConnectorPoint.setName(function.getName());
                }
            }

            if (!connector.isEndController()) {
                if (endConnectorPoint.getType().equals(ODGProcessing.FUNCTION)) {
                    ODGFunction function = functionList.get(endConnectorPoint.getId());
                    if (function.getName() == null) {
                        throw new Exception("Function at " + MoonsString.formatPlaces(connector.getEndPoint().getX(), 1) + " "
                                + MoonsString.formatPlaces(connector.getEndPoint().getY(), 1) + " has no configured name");
                    }
                    endConnectorPoint.setName(function.getName());
                }
            }

        }
    }

    // if config name is null set name from end point
    private void fillInNullConfigs() {
        for (ODGConnector connector : connectorList) {
            if (connector.isFunctionConfig()) {
                if (connector.getStartConnectorPoint().getName() == null) {
                    connector.getStartConnectorPoint().setName(connector.getEndConnectorPoint().getName());
                    ODGFunctionConfig config = configList.get(connector.getStartConnectorPoint().getId());
                    config.setName(connector.getEndConnectorPoint().getName());
                }
            }
        }
    }

    /*
    private void setConnectorHashMaps() {
        for (ODGConnector connector : connectorList) {
            String fwd = connector.getStartConnectorPoint().getName();
            String bck = connector.getEndConnectorPoint().getName();
            //fwdConnectorMap.put(fwd, connector);
            //bckConnectorMap.put(bck, connector);
        }
    }*/
    private void fillInFunctionLinks() {
        for (ODGConnector connector : connectorList) {
            if (connector.isFunction()) {
                if (connector.getEndConnectorPoint().getControllerId() != null) {
                    ODGFunction function = controllerList.get(connector.getEndConnectorPoint().getControllerId())
                            .getFunction(connector.getEndConnectorPoint().getName());

                    function.addLink(getLinkName(connector.getStartConnectorPoint().getName()), connector.getType());
                }
            }
        }
    }

    private String getLinkName(String lname) {

        for (ODGController controller : controllerList) {
            ODGFunction function = controller.getFunction(lname);
            if (function != null) {
                if (function.getOverrideName() != null) {
                    return function.getOverrideName();
                }
            }
        }

        return lname;
    }

    // for connectors that are links between a controller function and another function
    // follow the links if the function is nearby 
    private void fillInTransferLinks() throws Exception {
        for (ODGConnector connector : connectorList) {
            try {
                if (connector.isFunctionLink()) {
                    if (connector.isEndController()) {
                        ODGController controller = controllerList.get(connector.getEndConnectorPoint().getControllerId());
                        ODGFunction controllerFunction = controller.getFunction(connector.getEndConnectorPoint().getName());
                        followReverseLinks(connector, controllerFunction, controllerFunction);
                    }
                    if (connector.isStartController()) {
                        if (connector.isEndController() && connector.isLoopback()) { // check if the connector is connecting two controller functions
                        } else {
                            ODGController controller = controllerList.get(connector.getStartConnectorPoint().getControllerId());
                            if (!connector.isError(controller)) {
                                ODGFunction controllerFunction = controller.getFunction(connector.getStartConnectorPoint().getName());
                                followForwardLinks(connector, controllerFunction, controllerFunction);
                            }
                        }
                    }
                }

            } catch (StackOverflowError ex) {
                throw new Exception("Infinite loop has occured, is there a duplicate function name associated or near " + connector.toString());
            }
        }

    }

    // if the function linked by the connector is nearby define as transfer function
    // and add link
    //
    // if start of connector is a controller
    // then get the function from the controller, don't add as a transfer
    // 
    // if not then get the function from the function list
    // and if it is near to current function add as transfer
    //
    // in both cases add as a link
    // if the function is not near to current function then return
    //
    // if the transfer function links to another then follow that
    // direction from input/reference
    private void followReverseLinks(ODGConnector connector, ODGFunction thisFunction, ODGFunction controllerFunction) {
        ODGFunction linkFunction;
        Integer transferID = null;
        if (connector.isStartController()) {
            int controllerID = connector.getStartConnectorPoint().getControllerId();
            ODGController cont = controllerList.get(controllerID);
            linkFunction = cont.getFunction(connector.getStartConnectorPoint().getName());

        } else {
            transferID = connector.getStartConnectorPoint().getId();
            linkFunction = functionList.get(transferID);
        }

        String start = connector.getStartConnectorPoint().getName();
        if (thisFunction != controllerFunction) {
            thisFunction.addLink(getLinkName(start), connector.getType());
        }

        if (!thisFunction.isNear(linkFunction)) {
            LOG.log(Level.WARNING, "{0} {1} {2}", new Object[]{thisFunction.getName(), linkFunction.getName(), thisFunction.distance(linkFunction)});
            return;
        }

        if (transferID != null) {
            controllerFunction.addTransfer(transferID);
        }

        for (ODGConnector nextConnector : getNextReverseFunctionConnectors(start)) {
            followReverseLinks(nextConnector, linkFunction, controllerFunction);
        }

    }

    private void followForwardLinks(ODGConnector connector, ODGFunction thisFunction, ODGFunction controllerFunction) {
        int transferID = connector.getEndConnectorPoint().getId();
        ODGFunction linkFunction = functionList.get(transferID);
        if (!thisFunction.isNear(linkFunction)) {
            LOG.log(Level.WARNING, "{0} {1} {2}", new Object[]{thisFunction.getName(), linkFunction.getName(), thisFunction.distance(linkFunction)});
            return;
        }

        controllerFunction.addTransfer(transferID);
        //String start = connector.getStartConnectorPoint().getName();
        //linkFunction.addLink(start, connector.getType());
        for (ODGConnector linkConnector : getNextReverseFunctionConnectors(linkFunction.getName())) {
            linkFunction.addLink(getLinkName(linkConnector.getStartConnectorPoint().getName()), linkConnector.getType());
        }

        for (ODGConnector nextConnector : getNextForwardFunctionConnectors(linkFunction.getName())) {
            followForwardLinks(nextConnector, linkFunction, controllerFunction);
        }
    }

    private List<ODGConnector> getNextForwardFunctionConnectors(String name) {
        List<ODGConnector> list = new ArrayList<>();
        for (ODGConnector connector : connectorList) {
            if (connector.isFunctionLink() && connector.getStartConnectorPoint().getName().equals(name)) {
                list.add(connector);
            }
        }

        return list;
    }

    private List<ODGConnector> getNextReverseFunctionConnectors(String name) {
        List<ODGConnector> list = new ArrayList<>();
        for (ODGConnector connector : connectorList) {
            if (connector.isFunctionLink() && connector.getEndConnectorPoint().getName().equals(name)) {
                list.add(connector);
            }
        }

        return list;
    }

    private void processConnectors() throws Exception {
        fillInConfigs();
        //displayConnectors();
        //LOG.info("done fillInConfigs");

        fillInControllerFunctions();
        //displayConnectors();
        //LOG.info("done fillInControllerFunctions");

        fillInFunctions();
        //displayConnectors();
        //LOG.info("done fillInFunctions");

        fillInFunctionsConnectedToConfig();
        //LOG.info("done fillInFunctionsConnectedToConfig");
        //displayConnectors();

        //fillInFunctionEndpoints();
        //LOG.info("done fillInFunctionEndpoints");
        //displayConnectors();
        fillInFunctionNames();
        //displayConnectors();
        //LOG.info("done fillInFunctionNames");

        fillInNullConfigs();
        //LOG.info("done fillInNullConfigs");
        //displayConnectors();

        //setConnectorHashMaps();
        fillInFunctionLinks();

        fillInTransferLinks();
    }

    private void processGroup(Element elem) throws Exception {
        String type = getGroupType(elem);
        //LOG.info(type);

        switch (type) {
            case CONTROLLER:
                ODGController odgc = new ODGController(elem);
                Integer layer = odgc.setLayer(layerList);
                if (layer == null) {
                    throw new Exception("Controller " + odgc.getName() + " is not in a layer");
                }
                controllerList.add(odgc);
                layerList.get(layer).addController(controllerList.size() - 1);

                /*LOG.log(Level.INFO, "{0} ", new Object[]{odgc.getInput().toString()});
                LOG.log(Level.INFO, "{0} ", new Object[]{odgc.getReference().toString()});
                LOG.log(Level.INFO, "{0} ", new Object[]{odgc.getError().toString()});
                LOG.log(Level.INFO, "{0} ", new Object[]{odgc.getOutput().toString()});*/
                break;
            case FUNCTION_CONFIG:
                ODGFunctionConfig config = new ODGFunctionConfig(elem);
                configList.add(config);
                //LOG.log(Level.INFO, "FC: {0}", config.toString());

                break;
            case ORDER:
                processOrder(elem);
                break;
        }

    }

    // get text from second element of first element 
    private void processOrder(Element elem) {
        order = elem.getFirstChild().getChildNodes().item(1).getTextContent().split(",");
    }

    private String getGroupType(Element group) {
        DrawGElement g = (DrawGElement) group;
        int gluepoints = 0;
        int groups = 0;
        int customs = 0;

        //LOG.log(Level.INFO, "DrawCaptionId {0}", g.getDrawCaptionIdAttribute());
        //LOG.log(Level.INFO, "Length {0}", g.getChildNodes().getLength());
        //LOG.log(Level.INFO, "LocalName {0}", g.getLocalName());
        if (g.getFirstChild().getLocalName().equals("custom-shape")) {
            String s = g.getFirstChild().getFirstChild().getFirstChild().getTextContent();
            if (s.equals(FUNCTION_CONFIG)) {
                return FUNCTION_CONFIG;
            }
        }

        if (g.getFirstChild().getNodeName().equals("draw:g")) {
            String s = g.getFirstChild().getFirstChild().getFirstChild().getTextContent();
            if (s.equals("Type")) {
                return FUNCTION_CONFIG;
            }
            if (s.equals("Order")) {
                return ORDER;
            }
        }

        if (g.getFirstChild().getNodeName().equals("draw:g")) {
            String s = g.getFirstChild().getFirstChild().getFirstChild().getTextContent();
            if (s.equals("Name")) {
                return FUNCTION_CONFIG;
            }
        }

        NodeList nodeList = g.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                String name = elem.getTagName();

                if (name.equals("draw:glue-point")) {
                    gluepoints++;
                }
                if (name.equals("draw:g")) {
                    groups++;
                }
                if (name.equals("draw:custom-shape")) {
                    customs++;
                }
            }

            if (gluepoints == 3) {
                return CONTROLLER;
            }
            if (groups == 3) {
                return FUNCTION_CONFIG;
            }
            if (customs == 2) {
                return FUNCTION_CONFIG;
            }
        }

        return null;
    }

    private boolean isLayer(Element elem) {
        DrawCustomShapeElement custom = (DrawCustomShapeElement) elem;
        //LOG.info("Nname " + custom.getLocalName());
        TextPElement tp = (TextPElement) custom.getFirstChild();
        TextSpanElement tsp = (TextSpanElement) tp.getFirstChild();
        if (tsp != null) {
            //LOG.log(Level.INFO, "Text {0}", tsp.getTextContent());
            if (tsp.getTextContent().startsWith("Layer")) {
                return true;
            }
        }
        return false;
    }

}
