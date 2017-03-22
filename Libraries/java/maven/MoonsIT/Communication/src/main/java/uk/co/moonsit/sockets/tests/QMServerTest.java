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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moonsit.sockets.SocketServer;

/**
 *
 * @author ReStart
 */
public class QMServerTest {

    private static final Logger LOG = Logger.getLogger(QMServerTest.class.getName());

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    @SuppressWarnings("null")
    public static void main(String[] args) throws Exception {
        int port = 30000;
        int timeout = 1000000;
        int rtimeout = 0;

        //boolean running = true;
        SocketServer ss = null;

        try {
            ss = new SocketServer(port, timeout, rtimeout);
        } catch (IOException ex) {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            ss.connect();
        } catch (java.net.SocketTimeoutException ex) {
            Logger.getLogger(ex.toString());
        } catch (IOException ex) {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (ss.isConnected()) {
            try {
                String level = ss.readLine();

                LOG.log(Level.INFO, "Level {0}", level);
                String config = "1,2,3,4";
                ss.writeLine(config);

                while (true) {
                    ss.writeLine("5,6,7,8,9,10");
                    String a = ss.readLine();
                    LOG.info("Data "+ a+ " "+ a.length());
                }
            } catch (IOException ex) {
                Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
