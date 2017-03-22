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
package uk.co.moonsit.sockets;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moons.control.ControlHierarchy;
import uk.co.moons.control.ControlLayer;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moons.control.neural.BaseNeuralFunction;
import uk.co.moonsit.outputs.Alert;

public class ControlDataGraphServer extends Thread {

    static final Logger logger = Logger.getLogger(ControlDataGraphServer.class.getName());

    private SocketServer ss = null;
    private boolean finished = false;
    private boolean sentConfig = false;
    //private boolean receivedConfig = false;
    //private boolean configChanged = false;
    private String config = null;
    private float rate = 0;
    private long start = 0;
    private long mark = 0;

    private String subscription = null;
    private int subscriptionType;
    private String[] subscriptions;
    private final ControlHierarchy ch;
    private final Alert alert;
    private boolean waitForRequest = false;
    private final boolean debug = false;

    //private final long time;
    public ControlDataGraphServer(int port, int timeout, int rtimeout, String con, ControlHierarchy c) {
        config = con;
        ch = c;
        //time = System.currentTimeMillis();
        try {
            ss = new SocketServer(port, timeout, rtimeout);
        } catch (IOException e) {
            logger.warning(e.toString());
        }
        alert = new Alert();

    }

    /*public String getMessage() throws IOException {
     String rtn = null;
     if (!receivedConfig) {
     rtn = config;
     }

     return rtn;
     }*/
    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    private String setSubscriptions() throws Exception {
        String rtn=null;
        if (subscription.equalsIgnoreCase("all")) {
            subscriptionType = 0;
        } else {
            subscriptionType = 1;
            subscriptions = subscription.split(":");
            rtn=checkSubscriptions();
        }
        return rtn;
    }

    @Override
    @SuppressWarnings("LoggerStringConcat")
    public void run() {

        start = System.currentTimeMillis();
        mark = start;

        //long logs[] = new long[5];
        while (!finished) {
            //long start = System.currentTimeMillis();
            try {
                if (!ss.isConnected() || ss.isClosed()) {
                    ss.connect();
                    alert.soundAlert(Alert.ASCENDINGARPEGGIO);
                }
            } catch (java.net.SocketTimeoutException ex) {
                logger.log(Level.INFO, "Timed out on connect - socket status {0}", ss.socketStatus());
                continue;
            } catch (IOException ex) {
                Logger.getLogger(SocketServer.class.getName()).log(Level.WARNING, null, ex);
                logger.log(Level.WARNING, "IOEx on connect - socket status {0}", ss.socketStatus());
                alert.soundAlert(Alert.DESCENDINGARPEGGIO);
            }
            //logs[0] = System.currentTimeMillis() - start;

            if (ss.isConnected()) {
                try {

                    if (sentConfig) {
                        sendValues();
                        if (!waitForRequest) {
                            pause();
                        }
                    } else {
                        getRate();

                        logger.info("Rate " + rate);
                        subscription = ss.receiveEnvelopedMessage();
                        logger.info("Subscription " + subscription);
                        logger.info("waitForRequest " + waitForRequest);
                        String readyMessage = setSubscriptions();
                        // Thread.sleep(5000);

                        switch (subscriptionType) {
                            case 0:
                                sendPath();
                                String msg = getConfigData();
                                sendConfig(msg);
                                isReady();
                                break;
                            case 1:
                                ss.writeUTF(readyMessage);
                                if(!readyMessage.equalsIgnoreCase("ready"))
                                     throw new Exception(readyMessage);            
                                break;
                        }
                        sentConfig = true;
                    }

                } catch (IOException ex) {
                    //logger.warning(ex.toString());
                    logger.log(Level.WARNING, "Data communication lost will close streams - IOEx - socket status {0}", ss.socketStatus());
                    closeStreams();
                    sentConfig = false;
                } catch (Exception ex) {
                    logger.log(Level.WARNING, "Data communication lost will close streams - Ex - socket status {0}", ss.socketStatus());
                    Logger.getLogger(ControlDataGraphServer.class.getName()).log(Level.WARNING, null, ex);
                    closeStreams();
                    sentConfig = false;
                }

                if (ch.isFinished()) {
                    setFinished(true);
                }
            }
        }
        logger.info("loop finished, about to close socket ");
        closeSocket();

        logger.info("Server finished");

    }

    private void getRate() throws IOException {
        rate = ss.readInt();
        waitForRequest = rate == 0;
    }

    @SuppressWarnings("LoggerStringConcat")
    public boolean isReady() throws IOException {
        String ready = ss.readUTF();
        logger.info("ready? " + ready);
        return true;
    }

    private void log(String s) {
        if (debug) {
            logger.info(s);
        }
    }

    private void waitForRequest() throws IOException, Exception {
        if (waitForRequest) {
            log("waitForRequest");
            int avail = ss.available();
            log("avail " + avail);

            int req = ss.read();

            log("request rcvd");
            if (req == -1) {
                throw new Exception("No data available on stream");
            }
        }

    }

    private void sendValues() throws IOException, Exception {
        waitForRequest();
        log("sending data");
        switch (subscriptionType) {
            case 0:
                sendFullValues();
                break;
            case 1:
                sendSubscribedValues();
                break;
        }
        log("data sent");

    }

    private void sendSubscribedValues() throws IOException {

        for (String sub : subscriptions) {
            BaseNeuralFunction bf = ch.getNeuralFunction(sub);
            //if (sub.equalsIgnoreCase("speedconsolidated")) {
            //logger.info("speedc " + bf.getOutput());
            //}
            ss.writeDouble(bf.getOutput());
        }

    }

    private String checkSubscriptions() throws Exception {
        String rtn = "ready";
        for (String sub : subscriptions) {
            BaseNeuralFunction bf = ch.getNeuralFunction(sub);
            if (bf == null) {
               rtn = "Incorrect function subscription " + sub;
            }
        }
        return rtn;
    }

    private void sendFullValues() throws IOException {

        ControlLayer[] layers = ch.getLayers();
        for (int layer = 0; layer < layers.length; layer++) {
            //String prefix = " ly " + layer;
            //rtn.append(prefix);
            int systems = layers[layer].size();
            for (int system = 0; system < systems; system++) {

                if (ch.getController(layer, system).getInputFunction1() != null) {
                    List<BaseControlFunction> transfers = ch.getController(layer, system).getInputFunction1().getTransferFunctions();
                    //rtn.append(" * ");
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            double d = transfer.getValue();
                            ss.writeDouble(d);
                            //logger.info("writ "+d);
                        }
                    }
                    ss.writeDouble(ch.getController(layer, system).getInputFunction1().getMainFunction().getValue());
                }
                if (ch.getController(layer, system).getReferenceFunction1() != null) {
                    List<BaseControlFunction> transfers = ch.getController(layer, system).getReferenceFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            ss.writeDouble(transfer.getValue());
                        }
                    }
                    ss.writeDouble(ch.getController(layer, system).getReferenceFunction1().getMainFunction().getValue());
                }
                if (ch.getController(layer, system).getErrorFunction1() != null) {
                    ss.writeDouble(ch.getController(layer, system).getErrorFunction1().getMainFunction().getValue());
                    List<BaseControlFunction> transfers = ch.getController(layer, system).getErrorFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            ss.writeDouble(transfer.getValue());
                        }
                    }
                }
                if (ch.getController(layer, system).getOutputFunction1() != null) {
                    ss.writeDouble(ch.getController(layer, system).getOutputFunction1().getMainFunction().getValue());
                    List<BaseControlFunction> transfers = ch.getController(layer, system).getOutputFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            ss.writeDouble(transfer.getValue());
                        }
                    }
                }
            }
        }
    }

    private String getConfigData() throws IOException {
        String rtn = null;
        if (!sentConfig) {
            Charset cs = Charset.defaultCharset();
            rtn = new String(Files.readAllBytes(Paths.get(config)), cs);
        }

        return rtn;
    }

    private void pause() {
        long elapsed = System.currentTimeMillis() - mark;
        try {
            Integer pause = null;
            if (rate > elapsed) {
                pause = (int) (rate - elapsed);
                Thread.sleep(pause);
            }
            log(rate + " " + elapsed + " " + pause);
        } catch (InterruptedException ex) {
            Logger.getLogger(uk.co.moonsit.rmi.GraphClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        mark = System.currentTimeMillis();
    }

    private void sendPath() throws Exception {
        ss.sendEnvelopedMessage(config);
        logger.log(Level.INFO, "config path sent {0}", config);
    }

    @SuppressWarnings("LoggerStringConcat")
    private void sendConfig(String msg) throws Exception {
        ss.sendEnvelopedMessage(msg);
        logger.log(Level.INFO, "config sent " + msg.length());
        //logger.log(Level.INFO, "config sent {0}", msg);
        sentConfig = true;
    }

    public void closeStreams() {
        try {
            ss.closeStreams();
        } catch (IOException ex) {
            Logger.getLogger(ControlDataGraphServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() {
        setFinished(true);
        //closeSocket();
        logger.log(Level.INFO, "Closed ControlDataGraphServer");

    }

    public void closeSocket() {
        //setFinished(true);
        if (ss != null) {
            try {
                //if (!ss.isClosed()) {
                ss.close();
                logger.log(Level.WARNING, "Closed ControlDataGraphServer socket");
                //}
            } catch (IOException | NullPointerException ex) {
                Logger.getLogger(ControlDataGraphServer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @SuppressWarnings("SleepWhileInLoop")
    public static void main(String[] args) {
        try {

            int port = 6666;
            int timeout = 10000;
            int rtimeout = 0;
            //String config = "C:\\Versioning\\PCTSoftware\\Controllers\\Models\\000-Sine.xml";
            String config = "C:\\Versioning\\PCTSoftware\\Controllers\\Robot\\041-002-LocationControlTest.xml";
            ControlHierarchy ch = new ControlHierarchy(config);
            ch.init();

            ControlDataGraphServer gsm = new ControlDataGraphServer(port, timeout, rtimeout, config, ch);
            gsm.start();

            ControlParameterGraphServer cpgs = new ControlParameterGraphServer(port + 1, 10000, 0, ch);
            cpgs.start();

            int i = 1;
            while (i == 1) {
                //for (int i = 0; i < 3000; i++) {
                ch.iterate();
                //ch.print("");
                //Thread.sleep(1000);
            }
            gsm.close();
            cpgs.close();
            ch.close();
        } catch (Exception ex) {
            Logger.getLogger(ControlDataGraphServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
