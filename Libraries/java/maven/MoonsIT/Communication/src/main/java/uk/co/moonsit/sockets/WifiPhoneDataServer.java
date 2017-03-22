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
import java.util.logging.Level;
import java.util.logging.Logger;

public class WifiPhoneDataServer extends Thread {

    static final Logger logger = Logger.getLogger(WifiPhoneDataServer.class.getName());

    private SocketServer ss = null;
    private boolean finished = false;
    private float latitude = 0;
    private float longitude = 0;
    private long start = 0;
    private long mark = 0;

    //private final long time;
    public static WifiPhoneDataServer instance = null;
    
    protected WifiPhoneDataServer(int port, int timeout, int rtimeout) {
         try {
            ss = new SocketServer(port, timeout, rtimeout);
        } catch (IOException e) {
            logger.warning(e.toString());
        }
    }

    public static WifiPhoneDataServer getInstance(int port, int timeout, int rtimeout) {
    if (instance == null) {
	instance = new WifiPhoneDataServer(port, timeout,  rtimeout);
    }
	return instance;
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
                logger.log(Level.INFO, "Timed out on connect - socket status {0}", ss.socketStatus());
                continue;
            } catch (IOException ex) {
                Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
                logger.log(Level.SEVERE, "IOEx on connect - socket status {0}", ss.socketStatus());
            }
            //logs[0] = System.currentTimeMillis() - start;

            if (ss.isConnected()) {
                try {
                    latitude  = ss.readFloat();
                    longitude  = ss.readFloat();
                    

                } catch (IOException ex) {
                    //logger.warning(ex.toString());
                    logger.log(Level.WARNING, "Data communication lost will close streams - IOEx - socket status {0}", ss.socketStatus());
                    closeStreams();
                } catch (Exception ex) {
                    logger.log(Level.WARNING, "Data communication lost will close streams - Ex - socket status {0}", ss.socketStatus());
                    Logger.getLogger(WifiPhoneDataServer.class.getName()).log(Level.SEVERE, null, ex);
                    closeStreams();
                }

            }
        }
        logger.info("loop finished, about to close socket ");
        closeSocket();

        logger.info("Server finished");

    }

  
    public void closeStreams() {
        try {
            ss.closeStreams();
        } catch (IOException ex) {
            Logger.getLogger(WifiPhoneDataServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() {
        setFinished(true);
        //closeSocket();
        logger.log(Level.INFO, "Closed WifiPhoneDataServer");

    }

    public void closeSocket() {
        //setFinished(true);
        if (ss != null) {
            try {
                //if (!ss.isClosed()) {
                    ss.close();
                    logger.log(Level.WARNING, "Closed WifiPhoneDataServer socket");
                //}
            } catch (IOException ex ) {
                Logger.getLogger(WifiPhoneDataServer.class.getName()).log(Level.SEVERE, null, ex);
            }catch (NullPointerException ex ) {
                Logger.getLogger(WifiPhoneDataServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            

        }

    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public static void main(String[] args) {
        try {

            int port = 6668;
            int timeout = 100000;

            WifiPhoneDataServer wifiServer = new WifiPhoneDataServer(port, timeout, 0);
            wifiServer.start();
            
            for (int i = 0; i < 50; i++) {
                System.out.print ( wifiServer.getLatitude() + " ");
                System.out.println(wifiServer.getLongitude());
                
                Thread.sleep(1000);
            }
            wifiServer.close();
        } catch (Exception ex) {
            Logger.getLogger(WifiPhoneDataServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
