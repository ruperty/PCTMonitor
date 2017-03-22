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
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moonsit.sockets.SocketClient;

/**
 *
 * @author Rupert Young
 */
public class MessageClient extends Thread {

    private static final Logger logger = Logger.getLogger(MessageClient.class.getName());

    private final SocketClient client;
    private boolean firstMessage = true;
    private boolean running = true;
    private String config = null;
    private String message = null;
    private String response = null;

    public MessageClient(String serverName, int port, int timeout) {
        client = new SocketClient(serverName, port, timeout);
    }

    @SuppressWarnings("SleepWhileInLoop")
    @Override
    public void run() {

        while (!client.isConnected()) {
            try {
                client.connect();
            } catch (IOException e) {
                logger.log(Level.INFO, "Connection failed {0}", e.toString());
                setConfig("Failed");
                running = false;
                break;
                /*
                 try {
                 Thread.sleep(5000);
                 } catch (InterruptedException ex) {
                 Logger.getLogger(MessageClient.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 
                 */
            }
        }

        String msg;
        try {
            while (running) {

                if (firstMessage) {
                    msg = client.receiveEnvelopedMessage();
                    setConfig(msg);
                    firstMessage = false;
                } else {
                    msg = client.receiveAndSend(getResponse());
                    //clearResponse();
                    setMessage(msg);
                }
            }
        } catch (EOFException ex) {
            Logger.getLogger(MessageClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MessageClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (client != null) {
                client.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(MessageClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("RedundantStringConstructorCall")
    synchronized private String getResponse() {
        String rtn;
        if (response == null || response.length() == 0) {
            rtn = "Ok";
        } else {
            rtn = new String(response);
        }
        response = null;
        //logger.log(Level.INFO, "+++ {0}", rtn);
        return rtn;
    }

    synchronized public void setResponse(String r) {
        response = r;
        logger.log(Level.INFO, "+++ {0}", response);
    }

    /*
    synchronized public void clearResponse() {
        if (response != null) {
            logger.log(Level.INFO, "+++ {0}", response);
            response = null;
        }
    }
*/
    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    synchronized public String getConfig() {
        return config;
    }

    synchronized public void setConfig(String config) {
        this.config = config;
    }

    synchronized public String getMessage() {
        return message;
    }

    synchronized public void setMessage(String message) {
        this.message = message;
    }

}
