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

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moons.config.BaseControlBuild;
import uk.co.moons.config.ControlBuildFactory;

/**
 *
 * @author Rupert Young
 */
public class ControlDataClient {

    private static final Logger logger = Logger.getLogger(ControlDataClient.class.getName());

    private final SocketClient client;
    //private boolean firstMessage = true;
    //private boolean running = true;
    private String config = null;
    private String path = null;
    private String subscription = null;
    //private final String response = null;
    private float rate;
    private double[] values = null;
    private boolean finished = false;
    private int subscriptionType;
    private final boolean debug = false;

    public ControlDataClient(String serverName, int port, float frequency, int timeout) {
        client = new SocketClient(serverName, port, timeout);
        setRate(frequency);
        setSubscriptions(null);
    }

    public ControlDataClient(String serverName, int port, float frequency, String sub, int timeout) {
        client = new SocketClient(serverName, port, timeout);
        setRate(frequency);
        setSubscriptions(sub);
    }

    private void setSubscriptions(String sub) {
        if (sub == null) {
            subscriptionType = 0;
            subscription = "ALL";
        } else {
            subscription = sub;
            subscriptionType = 1;
            String[] subs = subscription.split(":");
            createArray(subs.length);
        }
    }

    private void setRate(float frequency) {
        if (frequency == 0) {
            rate = 0;
        } else {
            rate = 1000f / frequency;
        }
    }

    @SuppressWarnings({"SleepWhileInLoop", "LoggerStringConcat"})
    public boolean connect() throws ConnectException, NoRouteToHostException, IOException {
        boolean rtn = false;
        if (finished) {
            logger.log(Level.INFO, "Connection finished ");
            rtn = false;
        } else {
            while (!client.isConnected() || client.isClosed()) {
                try {
                    client.connect();
                    rtn = true;
                    
                } catch (NoRouteToHostException e) {
                    logger.log(Level.INFO, "Connection failed - " + e.toString());
                    throw e;
                } catch (ConnectException e) {
                    logger.log(Level.INFO, "Connection failed -- " + e.toString());
                    throw e;
                }/* catch (IOException e) {
                    logger.info("Connection failed --- " + e.toString());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MessageClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    rtn = false;
                }*/
                logger.log(Level.INFO, "Finished " + finished);
                if (finished) {
                    break;
                }

            }
        }
        return rtn;
    }

    @SuppressWarnings("LoggerStringConcat")
    public void initConfig() throws IOException, Exception {
        client.writeInt((int) rate);
        logger.info("Sent rate " + rate);
        client.sendEnvelopedMessage(subscription);
        logger.info("Sent subscriptions " + subscription);

        switch (subscriptionType) {
            case 0:
                path = client.receiveEnvelopedMessage();
                logger.info("Rcvd path " + path);
                setConfig(client.receiveEnvelopedMessage());
                logger.info("Config len " + config.length());
                //logger.info("Config " + config.substring(config.length() - 100));

                break;
            case 1:
                isReady();
                /*
                 try {
                 Thread.sleep(3000);
                 } catch (InterruptedException ex) {
                 Logger.getLogger(ControlDataClient.class.getName()).log(Level.SEVERE, null, ex);
                 }*/
                break;
        }
    }

    public void sendReady() throws IOException {
        client.writeUTF("ready");
    }

    @SuppressWarnings("LoggerStringConcat")
    public boolean isReady() throws IOException, Exception {
        String ready = client.readUTF();
        logger.info("ready? " + ready);
        if (!ready.equalsIgnoreCase("ready")) {
            throw new Exception(ready);
        }
        return true;
    }

    private void log(String s) {
        if (debug) {
            logger.info(s);
        }
    }

    @SuppressWarnings("LoggerStringConcat")
    private void receiveValues() throws IOException {
        if (rate == 0) {
            byte b = 1;
            log("Sending request for data");
            client.write(b);
            client.flush();
            log("Sent request for data");
        } else {
            log("Rate not zero " + rate);
        }
        for (int i = 0; i < values.length; i++) {
            values[i] = client.readDouble();
            //logger.info("rd " + values[i]);
        }
        log("Rcvd data");

    }

    public String getPath() {
        return path;
    }

    synchronized public String getConfig() {
        return config;
    }

    public void createArray(int size) {
        values = new double[size];
    }

    private void setConfig(String config) {

        this.config = config;
    }

    synchronized public double[] getValues() throws IOException, Exception {
        try {
            receiveValues();
        } catch (EOFException e) {
            throw new Exception("Probable incorrect function subscription, or lost connection " + e.toString());
        }
        return values;
    }

    public void setFinished(boolean finshed) {
        this.finished = finshed;
    }

    public void close() throws Exception {
        logger.log(Level.INFO, "Close");

        if (client != null) {
            client.close();
        }
    }

    public static void main(String[] args) {
        try {
            boolean sub = true;
            //String serverName = "localhost";
            //String serverName = "192.168.43.202";
            String serverName = "192.168.43.37";
            ControlDataClient cdc;

            if (sub) {
                String subs = "LatitudeSensor:LongitudeSensor:LatitudePhoneReference:LongitudePhoneReference:LocationDistanceControlModulus:SpeedConsolidated:ProximityInputSmooth";

                cdc = new ControlDataClient(serverName, 6666, 0, subs, 5000);
            } else {
                cdc = new ControlDataClient(serverName, 6668, 5, 10000);
            }

            cdc.connect();
            cdc.initConfig();

            if (!sub) {
                String config = cdc.getConfig();
                BaseControlBuild controlBuild = ControlBuildFactory.getControlBuildFunction(config);
                cdc.createArray(controlBuild.getHmControls().size());
                cdc.sendReady();
            }
            for (int i = 0; i < 10; i++) {
                double[] vals = cdc.getValues();
                StringBuilder sb = new StringBuilder();
                for (Double val : vals) {
                    sb.append(val);
                    sb.append(" ");
                }
                logger.info(sb.toString());
                if (sub) {
                    Thread.sleep(1000);
                }
            }
        } catch (ConnectException ex) {
            Logger.getLogger(ControlDataClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ControlDataClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ControlDataClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
