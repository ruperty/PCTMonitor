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

import uk.co.moonsit.utils.DisplayObject;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GraphServer extends Thread {

    static final Logger logger = Logger.getLogger(GraphServer.class.getName());

    private SocketServer ss = null;
    private boolean finished = false;
    //private boolean receivedConfig = false;
    //private boolean configChanged = false;
    private String config = null;
    private final DisplayObject dob;
    private float rate = 0;
    private long start = 0;
    private long mark = 0;

    //private final long time;
    public GraphServer(int port, int timeout, int rtimeout, DisplayObject d) {
        dob = d;
        //time = System.currentTimeMillis();
        try {
            ss = new SocketServer(port, timeout, rtimeout);
        } catch (IOException e) {
            logger.warning(e.toString());
        }
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

    public float[] getFloats() {

        return dob.getValues();
    }

    @Override
    public void run() {

        start = System.currentTimeMillis();
        mark = start;

        //long logs[] = new long[5];
        while (!finished) {
            //long start = System.currentTimeMillis();
            try {
                if (!ss.isConnected() || ss.isClosed()) {
                    ss.connect();
                }
            } catch (java.net.SocketTimeoutException ex) {
                System.out.println("Timed out");
                logger.log(Level.INFO, "socket status {0}", ss.socketStatus());
                continue;
            } catch (IOException ex) {
                Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
                logger.log(Level.INFO, "socket status {0}", ss.socketStatus());
            }
            //logs[0] = System.currentTimeMillis() - start;

            if (ss.isConnected()) {
                try {

                    if (dob.isReceivedConfig()) {
                        if (dob.isDataStarted()) {
                            //logs[1] = System.currentTimeMillis() - start;

                            float[] fs = getFloats();
                            for (float f : fs) {
                                //System.out.print(f + " ");
                                ss.writeFloat(f);
                            }
                            //System.out.println();
                            pause();
                            //logs[2] = System.currentTimeMillis() - start;
                        }
                    } else {
                        rate = ss.readInt();
                        dob.setDisplayType(ss.readInt());
                        dob.createArray(ss.readInt());
                        logger.log(Level.INFO, "display type {0}", dob.getDisplayType());
                        logger.log(Level.INFO, "size {0}", dob.getSize());
                        //String msg = getMessage();
                        //sendConfig(msg);
                        dob.setReceivedConfig(true);
                        dob.setConfigChanged(true);
                    }

                } catch (IOException ex) {
                    logger.warning(ex.toString());
                    logger.log(Level.WARNING, "socket {0}", ss.socketStatus());
                    close();
                    dob.setReceivedConfig(false);
                }
                if (dob.isFinished()) {
                logger.info("Dob is finished");
                setFinished(true);
                }
            }
        }
        if (ss != null) {
            try {
                logger.info("Closing server socket");
                ss.close();
            } catch (IOException ex) {
                Logger.getLogger(GraphServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        logger.info("Server finished");

    }

    private void pause() {
        long elapsed = System.currentTimeMillis() - mark;
        try {

            if (rate > elapsed) {
                long pause = (int) (rate - elapsed);
                Thread.sleep(pause);
            }
            //logger.info(rate + " " + elapsed+ " " +pause);
        } catch (InterruptedException ex) {
            Logger.getLogger(uk.co.moonsit.rmi.GraphClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        mark = System.currentTimeMillis();
    }

    /*
     private void sendConfig(String msg) throws Exception {
     ss.sendEnvelopedMessage(msg);
     logger.log(Level.INFO, "config sent {0}", msg);
     receivedConfig = true;
     }
     */
    public void close()  {
        //setFinished(true);
        if (ss != null) {
            try {
                ss.close();
            } catch (IOException ex) {
                Logger.getLogger(GraphServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        logger.log(Level.WARNING, "Closed graph server");

    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

}
