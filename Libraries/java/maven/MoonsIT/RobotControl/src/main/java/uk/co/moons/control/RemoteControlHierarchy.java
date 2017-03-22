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
package uk.co.moons.control;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moons.config.ControlBuildFactory;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moonsit.sockets.ControlDataClient;
import uk.co.moonsit.sockets.ControlParameterClient;
import uk.co.moonsit.utils.Environment;

/**
 *
 * @author ReStart
 */
public class RemoteControlHierarchy extends ControlHierarchy {

    private static final Logger logger = Logger.getLogger(RemoteControlHierarchy.class.getName());

    private ControlDataClient dataClient = null;
    private ControlParameterClient paramterClient = null;
    private String path;
    private boolean useParameterClient = true;
    private boolean shutdown = false;

    public RemoteControlHierarchy(String host, int dport, int pport, int freq, boolean up, int timeout) throws Exception {
        super();
        this.useParameterClient = up;
        logger.info("Constructor");

        dataClient = new ControlDataClient(host, dport, freq, timeout);
        initialiseDataStream();

        if (useParameterClient) {
            paramterClient = new ControlParameterClient(host, pport, timeout);
        }

    }

    /*
     @Override
     public void stop() throws Exception{
     super.stop();
     logger.info("Closing clients");
     dataClient.close();
     paramterClient.close();
     }*/
    private void initialiseDataStream() throws Exception {
        logger.info("initialiseDataStream");
        dataClient.connect();
        dataClient.initConfig();
        config = dataClient.getConfig();
        path = dataClient.getPath();

        setControlBuild(ControlBuildFactory.getControlBuildFunction(config));
        this.layers = getControlBuild().getLayers();
        this.hmControllers = getControlBuild().getHmControllers();
        this.hmControls = getControlBuild().getHmControls();
        this.orderedControllers = getControlBuild().getOrderedControllers();

        setListOutputFunctions(Environment.getInstance().getListOutputFunctions());

        dataClient.createArray(this.hmControls.size());
        dataClient.sendReady();
    }

    public String getPath() {
        return path;
    }

    @Override
    public void iterate() throws Exception {
        receiveData();

        sendData();
        //Thread.sleep(5);
    }

    public void receiveData() throws IOException, Exception {
        if (dataClient.connect()) {
            initialiseDataStream();
        }
        setValues(dataClient.getValues());
    }

    public void shutdown() {
        shutdown = true;
    }

    private void sendData() throws Exception {
        paramterClient.connect();
        String response = getParameterChanges();
        if (shutdown) {
            response = "SHUTDOWN";
        }

        if (response.length() > 0) {
            logger.log(Level.INFO, "+++ {0}", response);
            paramterClient.sendResponse(response);
        }

    }

    /*
     private String addParameterChanges(BaseNeuralFunction function) {
     Collection<String> pars = function.getParametersSet();
     StringBuilder sb = new StringBuilder();
     if (!pars.isEmpty()) {
     sb.append(function.getName());
     sb.append("_");
     for (String st : pars) {
     sb.append(st);
     sb.append("|");
     }
     sb.append("^");
     function.clearParametersHashMap();
     }
     return sb.toString();
     }
     */
    private String getParameterChanges() {
        StringBuilder sb = new StringBuilder();
        for (int layer = 0; layer < layers.length; layer++) {
            int systems = layers[layer].size();
            for (int system = 0; system < systems; system++) {

                if (getController(layer, system).getInputFunction1() != null) {
                    List<BaseControlFunction> transfers = getController(layer, system).getInputFunction1().getTransferFunctions();
                    //rtn.append(" * ");
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            String adds = paramterClient.addParameterChanges(transfer.getNeural());
                            if (adds.length() > 0) {
                                sb.append(adds);
                            }
                        }
                    }
                    String adds = paramterClient.addParameterChanges(getController(layer, system).getInputFunction1().getMainFunction().getNeural());
                    if (adds.length() > 0) {
                        sb.append(adds);
                    }
                }
                if (getController(layer, system).getReferenceFunction1() != null) {
                    List<BaseControlFunction> transfers = getController(layer, system).getReferenceFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            String adds = paramterClient.addParameterChanges(transfer.getNeural());
                            if (adds.length() > 0) {
                                sb.append(adds);
                            }
                        }
                    }
                    String adds = paramterClient.addParameterChanges(getController(layer, system).getReferenceFunction1().getMainFunction().getNeural());
                    if (adds.length() > 0) {
                        sb.append(adds);
                    }
                }
                if (getController(layer, system).getErrorFunction1() != null) {
                    String adds = paramterClient.addParameterChanges(getController(layer, system).getErrorFunction1().getMainFunction().getNeural());
                    if (adds.length() > 0) {
                        sb.append(adds);
                    }
                    List<BaseControlFunction> transfers = getController(layer, system).getErrorFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            String adds1 = paramterClient.addParameterChanges(transfer.getNeural());
                            if (adds1.length() > 0) {
                                sb.append(adds1);
                            }
                        }
                    }
                }
                if (getController(layer, system).getOutputFunction1() != null) {
                    String adds = paramterClient.addParameterChanges(getController(layer, system).getOutputFunction1().getMainFunction().getNeural());
                    if (adds.length() > 0) {
                        sb.append(adds);
                    }
                    List<BaseControlFunction> transfers = getController(layer, system).getOutputFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            String adds1 = paramterClient.addParameterChanges(transfer.getNeural());
                            if (adds1.length() > 0) {
                                sb.append(adds1);
                            }
                        }
                    }
                }
            }
        }

        return sb.toString();
    }

    public void setDataFinished(boolean flag) {
        dataClient.setFinished(flag);
    }

    @Override
    public void close() throws Exception {
        if (dataClient != null) {
            dataClient.close();
        }
        if (useParameterClient) {
            paramterClient.close();
        }
    }

    public static void main(String[] args) {

        try {
            RemoteControlHierarchy ch = new RemoteControlHierarchy("192.168.43.75", 6666, 0, 5, false, 5000);
        } catch (Exception ex) {
            Logger.getLogger(RemoteControlHierarchy.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
