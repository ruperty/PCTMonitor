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
import java.net.ConnectException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rupert Young
 */
public class WifiPhoneDoubleDataClient {

    private static final Logger logger = Logger.getLogger(WifiPhoneDoubleDataClient.class.getName());

    private final SocketClient client;
    private boolean finished = false;
    private boolean newConnection = false;

    public WifiPhoneDoubleDataClient(String serverName, int port, int timeout) {
        client = new SocketClient(serverName, port, timeout);
    }

    public boolean connect() throws ConnectException {
        boolean rtn = true;

        if (finished) {
            logger.log(Level.INFO, "Connection finished ");
            rtn = false;
        } else {
            while (!client.isConnected() || client.isClosed()) {
                try {
                    client.connect();
                    rtn = true;
                    newConnection=true;
                } catch (ConnectException e) {
                    logger.log(Level.INFO, "Connection failed " + e.toString());
                    throw e;
                } catch (IOException e) {
                    logger.log(Level.INFO, "Connection failed " + e.toString());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MessageClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    rtn = false;
                }
                logger.log(Level.INFO, "Finished " + finished);
                if (finished) {
                    break;
                }
            }
        }
        return rtn;
    }

    public boolean isNewConnection() {
        boolean rtn = newConnection;
        setNewConnection(false);
        return rtn;
    }

    public void setNewConnection(boolean newConnection) {
        this.newConnection = newConnection;
    }

    
   
    public void sendData(double[] data) throws IOException {
        for (double d : data) {
            client.writeDouble(d);
        }
    }

    
    public void sendData(Double[] data) throws IOException {
        for (double d : data) {
            client.writeDouble(d);
        }
    }
    
    public boolean isFinshed() {
        return finished;
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

    public boolean isConnected() {
        if (client == null) {
            return false;
        }
        return client.isConnected();
    }

    public static void main(String[] args) throws Exception {

        String host = "localhost";
        int port = 6668;

        WifiPhoneDoubleDataClient demo = new WifiPhoneDoubleDataClient(host, port, 5000);
        demo.connect();
        double[] data = new double[2];
        for (int i = 0; i < 10; i++) {
            data[0] = 1f * 10;
            data[1] = 1f * 20;
            demo.sendData(data);
            System.out.println("+++ " + i);
            Thread.sleep(1000);
        }
        System.out.println("+++ loop finished");
        Thread.sleep(5000);

        demo.close();
    }

}
