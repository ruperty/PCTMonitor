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
package uk.co.moonsit.sockets.tests;

import uk.co.moonsit.sockets.*;
import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rupert Young
 */
public class QMClientTest {

    private static final Logger LOG = Logger.getLogger(QMClientTest.class.getName());

    private final SocketClient client;
    private double[] values = null;
    private boolean finished = false;
    private final boolean debug = true;

    public QMClientTest(String serverName, int port, int timeout) {
        client = new SocketClient(serverName, port, timeout);
    }

    @SuppressWarnings({"SleepWhileInLoop", "LoggerStringConcat"})
    public boolean connect() throws ConnectException, NoRouteToHostException, IOException {
        boolean rtn = false;
        if (finished) {
            LOG.log(Level.INFO, "Connection finished ");
            rtn = false;
        } else {
            while (!client.isConnected() || client.isClosed()) {
                try {
                    client.connect();
                    rtn = true;

                } catch (NoRouteToHostException e) {
                    LOG.log(Level.INFO, "Connection failed - " + e.toString());
                    throw e;
                } catch (ConnectException e) {
                    LOG.log(Level.INFO, "Connection failed -- " + e.toString());
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
                LOG.log(Level.INFO, "Finished " + finished);
                if (finished) {
                    break;
                }

            }
        }
        return rtn;
    }

    public void sendData(float a) throws IOException {
        client.writeFloat(a);
    }

    private void log(String s) {
        if (debug) {
            LOG.info(s);
        }
    }

    @SuppressWarnings("LoggerStringConcat")
    private void receiveData() throws IOException {

        float a = client.readFloat();
        log("Rcvd data " + a);

    }

    public void setFinished(boolean finshed) {
        this.finished = finshed;
    }

    public void close() throws Exception {
        LOG.log(Level.INFO, "Close");

        if (client != null) {
            client.close();
        }
    }

    public static void main(String[] args) {
        try {
            boolean sub = true;
            String serverName = "localhost";
            int port = 6666;
            QMClientTest qm;

            qm = new QMClientTest(serverName, port, 5000);

            qm.connect();

            for (int i = 0; i < 10; i++) {

                qm.sendData(i);
                qm.receiveData();

            }
        } catch (ConnectException ex) {
            Logger.getLogger(QMClientTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(QMClientTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(QMClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
