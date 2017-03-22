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
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moons.control.neural.BaseNeuralFunction;

/**
 *
 * @author Rupert Young
 */
public class ControlParameterClient {

    private static final Logger logger = Logger.getLogger(ControlParameterClient.class.getName());

    private final SocketClient client;
    private boolean finished = false;

    public ControlParameterClient(String serverName, int port, int timeout) {
        client = new SocketClient(serverName, port, timeout);
    }

    @SuppressWarnings({"LoggerStringConcat", "SleepWhileInLoop"})
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

    synchronized public void sendResponse(String r) throws Exception {
        client.sendEnvelopedMessage(r);
    }

    public String addParameterChanges(BaseNeuralFunction function) {
        Collection<String> pars = function.getParametersSet();
        StringBuilder sb = new StringBuilder();
        if (!pars.isEmpty()) {
            sb.append(function.getName());
            sb.append(";");
            for (String st : pars) {
                sb.append(st);
                sb.append("|");
            }
            sb.append("^");
            function.clearParametersHashMap();
        }
        return sb.toString();
    }

    /*public static String formatParameterChange(String functionName, String parameterName, String parameterValue) {
        return functionName + "_" + parameterName + ":" + parameterValue;
    }*/

   
    
    public void setFinished(boolean finshed) {
        this.finished = finshed;
    }

    public void close() throws Exception {
        logger.log(Level.INFO, "Close");

        if (client != null) {
            client.close();
        }
    }

}
