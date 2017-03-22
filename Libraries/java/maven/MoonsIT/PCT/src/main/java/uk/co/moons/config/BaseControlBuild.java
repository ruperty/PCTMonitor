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
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moons.control.ControlFunctionCollection;
import uk.co.moons.control.ControlLayer;
import uk.co.moons.control.Controller;
import uk.co.moons.control.functions.BaseControlFunction;

/**
 *
 * @author ReStart
 */
public abstract class BaseControlBuild implements ControlBuildInterface {

    private static final Logger LOG = Logger.getLogger(BaseControlBuild.class.getName());
    protected ControlLayer[] layers;
    protected List<String> orderedControllers = null;
    protected HashMap<String, Controller> hmControllers = null;
    protected HashMap<String, BaseControlFunction> hmControls = null;
    protected BaseControlConfig controlConfig = null;
    private final boolean debug=false;

    public BaseControlBuild(String config) throws Exception {
        controlConfig = ControlConfigFactory.getControlConfigFunction(config);
        orderedControllers = new ArrayList<>();
        hmControllers = new HashMap<>();
        hmControls = new HashMap<>();
    }

    public BaseControlBuild(int num) {
        init(num);
    }

    private void init(int num) {
        initLayers(num);
    }

    public BaseControlConfig getControlConfig() {
        return controlConfig;
    }

    public void setControlConfig(BaseControlConfig controlConfig) {
        this.controlConfig = controlConfig;
    }

    public HashMap<String, Controller> getHmControllers() {
        return hmControllers;
    }

    public void setHmControllers(HashMap<String, Controller> hmControllers) {
        this.hmControllers = hmControllers;
    }

    public HashMap<String, BaseControlFunction> getHmControls() {
        return hmControls;
    }

    public void setHmControls(HashMap<String, BaseControlFunction> hmControls) {
        this.hmControls = hmControls;
    }

    public ControlLayer[] getLayers() {
        return layers;
    }

    public void setLayers(ControlLayer[] layers) {
        this.layers = layers;
    }

    public List<String> getOrderedControllers() {
        return orderedControllers;
    }

    public void setOrderedControllers(List<String> orderedControllers) {
        this.orderedControllers = orderedControllers;
    }

    protected void addControlToHashMap(String name, BaseControlFunction cf) throws Exception {
        if (name.length() == 0) {
            throw new Exception("Empty control function name ");
        }
        if (hmControls.get(name) == null) {
            if(debug)LOG.log(Level.INFO, "+++ put controller {0}", name);
            hmControls.put(name, cf);
        } else {
            throw new Exception("Duplicate control function - " + name);
        }

    }

    protected void setLinks() throws Exception {
        for (int i = 0; i < layers.length; i++) {
            ListIterator<Controller> li = layers[i].listIterator();
            while (li.hasNext()) {
                Controller controller = li.next();

                ControlFunctionCollection error = controller.getErrorFunction1();
                if (error != null) {
                    error.getMainFunction().getNeural().setLinks(hmControls);
                    List<BaseControlFunction> transfers = error.getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            transfer.getNeural().setLinks(hmControls);
                        }
                    }
                }
                ControlFunctionCollection input = controller.getInputFunction1();
                if (input != null) {
                    input.getMainFunction().getNeural().setLinks(hmControls);
                    List<BaseControlFunction> transfers = input.getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            transfer.getNeural().setLinks(hmControls);
                        }
                    }
                }
                ControlFunctionCollection output = controller.getOutputFunction1();
                if (output != null) {
                    output.getMainFunction().getNeural().setLinks(hmControls);
                    List<BaseControlFunction> transfers = output.getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            transfer.getNeural().setLinks(hmControls);
                        }
                    }
                }
                ControlFunctionCollection reference = controller.getReferenceFunction1();
                if (reference != null) {
                    reference.getMainFunction().getNeural().setLinks(hmControls);
                    List<BaseControlFunction> transfers = reference.getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            transfer.getNeural().setLinks(hmControls);
                        }
                    }
                }
            }
        }
    }

    protected void checkControllers() throws Exception {

        for (String cont : hmControllers.keySet()) {
            if (!orderedControllers.contains(cont)) {
                throw new Exception("Controller " + cont + " is not in orderded list");
            }
        }

        for (String con : orderedControllers) {
            Controller cont = hmControllers.get(con);
            if (cont == null) {
                throw new Exception("+++ controller " + con + " doesn't exist");
            }
        }

    }

    protected void initLayers(int num) {
        layers = new ControlLayer[num];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new ControlLayer();
        }
    }

    /*
    public Controller addController(int layer, BaseNeuralFunction[] neurals) {

        // Neural functions
        BaseNeuralFunction errorNeuralFunction = neurals[0];
        BaseNeuralFunction referenceNeuralFunction = neurals[1];
        BaseNeuralFunction outputNeuralFunction = neurals[2];
        BaseNeuralFunction inputNeuralFunction = neurals[3];

        BaseControlFunction reference = new ControlFunction(referenceNeuralFunction);
        BaseControlFunction error = new ControlFunction(errorNeuralFunction);
        BaseControlFunction output = new ControlFunction(outputNeuralFunction);
        BaseControlFunction input = new ControlFunction(inputNeuralFunction);


        ControlFunctionCollection i = new ControlFunctionCollection();
        i.setMainFunction(input);
        ControlFunctionCollection r = new ControlFunctionCollection();
        r.setMainFunction(reference);
        ControlFunctionCollection e = new ControlFunctionCollection();
        e.setMainFunction(error);
        ControlFunctionCollection o = new ControlFunctionCollection();
        o.setMainFunction(output);

        Controller control = new Controller(i, e, o, r);
        layers[layer].add(control);

        return control;
    }
    */
}
