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
import java.io.InterruptedIOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moonsit.outputs.Alert;
import uk.co.moonsit.utils.FrequencyMeasure;
import uk.co.moonsit.utils.Print;

public class WifiPhoneDoubleDataServer extends Thread {

    static final Logger logger = Logger.getLogger(WifiPhoneDoubleDataServer.class.getName());

    private SocketServer ss = null;
    private boolean finished = false;
    private final double[] data;

    private final FrequencyMeasure fm;
    private final Alert alert;
    private final boolean debug = false;

    @SuppressWarnings("LoggerStringConcat")
    public WifiPhoneDoubleDataServer(int port, int timeout, int rtimeout, int size, Double initial) throws IOException {
        logger.info("Port # " + port);
        data = new double[size];
        if(initial!=null)
            for(int i=0;i<data.length;i++)
                data[i]=initial;
        
        ss = new SocketServer(port, timeout, rtimeout);
        fm = new FrequencyMeasure(500);
        alert = new Alert();
    }

    @Override
    public void run() {


        while (!finished) {

            try {
                if (!ss.isConnected() || ss.isClosed()) {
                    ss.connect();
                    alert.soundAlert(Alert.ASCENDINGARPEGGIO);
                }
            } catch (java.net.SocketTimeoutException ex) {
                logger.log(Level.INFO, "Timed out on connect - socket status {0}", ss.socketStatus());
                continue;
            } catch (IOException ex) {
                Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
                logger.log(Level.SEVERE, "IOEx on connect - socket status {0}", ss.socketStatus());
                alert.soundAlert(Alert.DESCENDINGARPEGGIO);
            }


            if (ss.isConnected()) {
                try {
                    readData();
                } catch (java.net.SocketTimeoutException ex) {
                    logger.warning(ex.toString());
                    closeStreams();
                } catch (InterruptedIOException ex) {
                    logger.warning(ex.toString());
                } catch (IOException ex) {
                    logger.log(Level.WARNING, "Data communication lost will close streams - IOEx - socket status {0}", ss.socketStatus());
                    closeStreams();
                } catch (Exception ex) {
                    logger.log(Level.WARNING, "Data communication lost will close streams - Ex - socket status {0}", ss.socketStatus());
                    Logger.getLogger(WifiPhoneDoubleDataServer.class.getName()).log(Level.SEVERE, null, ex);
                    closeStreams();
                }

            }
        }
        logger.info("loop finished, about to close socket ");
        closeSocket();

        logger.info("Server finished");

    }

    public  void readData() throws IOException {
        fm.init();

        fm.mark();
        for (int i = 0; i < data.length; i++) {
            data[i] = ss.readDouble();
        }
        //logger.info("Read data " + data[0]+ " "+ data[1]);
        if (debug) {
            Print.logArray("WifiPhoneDoubleDataServer", data);
        }
    }

    public void closeStreams() {
        try {
            ss.closeStreams();
            logger.info("Streams closed");
        } catch (IOException ex) {
            Logger.getLogger(WifiPhoneDoubleDataServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() {
        setFinished(true);
        logger.log(Level.INFO, "Closed WifiPhoneDoubleDataServer");
    }

    public void closeSocket() {

        if (ss != null) {
            try {

                ss.close();
                logger.log(Level.INFO, "Closed WifiPhoneDoubleDataServer socket");

            } catch (IOException | NullPointerException ex) {
                Logger.getLogger(WifiPhoneDoubleDataServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public synchronized double[] getData() {
        return data;
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

            int port = 6669;
            int timeout = 10000;
            int size = 3;

            WifiPhoneDoubleDataServer wifiServer = new WifiPhoneDoubleDataServer(port, timeout, 0, size, null);
            wifiServer.start();

            for (int i = 0; i < 50000; i++) {
                double[] data = wifiServer.getData();
                    Print.logArray("main", data);
                Thread.sleep(500);
            }

            wifiServer.close();
            wifiServer.closeSocket();
            Thread.sleep(1000);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(WifiPhoneDoubleDataServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
