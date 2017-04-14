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
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moons.control.BaseControlHierarchy;

public class MessageServer extends Thread {

    static final Logger logger = Logger
            .getLogger(MessageServer.class.getName());

    private SocketServer ss = null;
    private boolean finished = false;
    private BaseControlHierarchy ch;
    private String config;
    private boolean sentConfig = false;
    private int port;

    public MessageServer(int port, int timeout, int rtimeout, BaseControlHierarchy ch,
            String config) {
        try {
            this.config = config;
            this.ch = ch;
            this.port = port;
            ss = new SocketServer(port, timeout, rtimeout);
        } catch (IOException e) {
            logger.warning(e.toString());
        }
    }

    public String getMessage() throws IOException {
        String rtn;
        if (!sentConfig) {
            Charset cs = Charset.defaultCharset();
            rtn = config + new String(Files.readAllBytes(Paths.get(config)), cs);
        } else {
            rtn = ch.getDelimitedString();
        }

        return rtn;
    }

    @Override
    public void run() {
        // int ctr=0;
        while (!finished) {
            try {
                if (!ss.isConnected()) {
                    logger.log(Level.INFO, "{0}.....", port);
                    ss.connect();
                    logger.info("connected");
                }
            } catch (java.net.SocketTimeoutException ex) {
                System.out.println("Timed out");
                continue;
            } catch (IOException ex) {
                Logger.getLogger(SocketServer.class.getName()).log(
                        Level.SEVERE, null, ex);
            }

            if (ss.isConnected()) {
                String response;
                try {

                    String msg = getMessage();

                    if (sentConfig) {
                        response = ss.sendAndReceive(msg);
                        if (!response.equals("Ok")) {
                            logger.log(Level.INFO, "Response  {0}", response);
                            setParameters(response);
                        }
                    } else {
                        sendConfig(msg);
                    }

                } catch (IOException ex) {
                    logger.warning(ex.toString());
                    logger.log(Level.INFO, "socket {0}", ss.socketStatus());
                    close();

                    sentConfig = false;
                } catch (Exception e) {
                    logger.warning(e.toString());
                }
            }
            // if(ctr++>5)
            // finished = true;
        }
        // System.out.println("Server finished");

    }

    public void close() {
        setFinished(true);
        if (ss != null) {
            try {
                ss.close();
            } catch (IOException ex) {
                Logger.getLogger(GraphServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        logger.log(Level.WARNING, "Closed message server");

    }

    private void setParameters(String st) throws Exception {
        String[] cons = st.split("\\^");
        for (String con : cons) {
            String[] namePars = con.split("_");
            String[] pars = namePars[1].split("\\|");
            for (String par : pars) {
                ch.setControllerParameter(namePars[0], par);
            }
        }
    }

    private void sendConfig(String msg) throws Exception {
        ss.sendEnvelopedMessage(msg);
        logger.info("config sent");
        sentConfig = true;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

}
