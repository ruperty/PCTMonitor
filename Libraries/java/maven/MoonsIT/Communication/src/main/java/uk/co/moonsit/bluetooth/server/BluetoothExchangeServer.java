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
package uk.co.moonsit.bluetooth.server;

import com.intel.bluetooth.RemoteDeviceHelper;
import uk.co.moonsit.sensors.Sensors;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import uk.co.moonsit.messaging.BeginEndEnvelope;

/**
 *
 * @author Rupert Young
 * Moon's Information Technology
 *
 */
public class BluetoothExchangeServer implements Runnable {

    private static final Logger logger = Logger.getLogger(BluetoothExchangeServer.class.getName());
    private BeginEndEnvelope envelope;
    private StreamConnection connection;
    private String id;
    private String serviceName;
    private boolean isConnected = false;
    private BlockingQueue<String> queue = null;
    private Sensors sensorState = null;
    private boolean isNotTest = true;
    private StreamConnectionNotifier service = null;
    private InputStream is = null;
    private OutputStream os = null;
    private int ctr = 1;
    private Long mark = null;
    private final int limit = 1000;

    public BluetoothExchangeServer(String serviceName, String id, String start, String end, BlockingQueue<String> q, Sensors sensorState) {
        queue = q;
        this.sensorState = sensorState;
        envelope = new BeginEndEnvelope(start, end);
        this.serviceName = serviceName;
        this.id = id;
    }

    @Override
    public void run() {
        logger.info("Starting bluetooth run: isConnected " + isConnected);
        while (!isConnected) {
            try {
                connect();
                if (!processData()) {
                    break;
                }
            } catch (Exception ex) {
                Logger.getLogger(BluetoothExchangeServer.class.getName()).log(Level.SEVERE, null, ex);
                isConnected = false;
            } finally {
                close();
            }
        }
        close();
        logger.info("Ending bluetooth run: isConnected " + isConnected);
    }

    private void close() {
        closeConnection();
        closeStreams();
        isConnected = false;
    }

    private void closeStreams() {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(BluetoothExchangeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if (os != null) {
                os.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(BluetoothExchangeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException ex) {
                Logger.getLogger(BluetoothExchangeServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (service != null) {
            try {
                service.close();
            } catch (IOException ex) {
                Logger.getLogger(BluetoothExchangeServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void connect() throws IOException {

        try {
            if (isNotTest) {
                service = (StreamConnectionNotifier) Connector.open("btspp://localhost:"
                        + new UUID(id,
                        false).toString() + ";name=" + serviceName);
                logger.info("Waiting ...");
                connection = (StreamConnection) service.acceptAndOpen();
            }
            logger.info("Connected ");
            if (isNotTest) {
                is = connection.openInputStream();
                os = connection.openOutputStream();
            }

            isConnected = true;
        } catch (IOException e) {
            logger.severe("+++ err: " + e.toString());
            throw e;
        }
    }

    public boolean processData() throws Exception {
        String data = null;
        double ctr = 0.0;
        try {
            while (isConnected) {
// System.out.println("Android RSSI = " + RemoteDeviceHelper.r .readRSSI(connection));
                if (sensorState != null && !sensorState.getLinAccFlag()) {
                    return false;
                }
                if (isNotTest) {
                    data = envelope.receiveMessage(is);
                    logDT(data);
                } else {
                    data = "LinAcc|0|0|" + ctr++;
                }
                //logger.info("+++ data: received:" + data);

                if (queue != null) {
                    //logger.info("+++ data: queueing:" + data);
                    if (!queue.offer(data, 5, TimeUnit.SECONDS)) {
                        logger.info("+++ QUEUE TIMEDOUT");
                        if (sensorState.getLinAccFlag()) {
                            throw new Exception("+++ QUEUE TIMEDOUT while flag true");
                        }
                    }
                }
                markRate();
                //logger.info("+++ data: queued:" + data);
                if (isNotTest) {
                    String now = String.valueOf(System.currentTimeMillis());
                    envelope.sendMessage(now, os);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "+++ err: {0}", e.toString());
            isConnected = false;
            throw e;
        } catch (Exception e) {
            String err = e.toString();
            logger.log(Level.SEVERE, "+++ err: {0}", err);
            throw e;
        }
        return false;
    }

    private void logDT(String data) {
        String sarr[] = data.split("\\|");
        if (sarr[5] != null && sarr[5].length() > 0) {
            long then = Long.parseLong(sarr[5]);
            long now = System.currentTimeMillis();
            logger.info("+++ DT: " + (then - now) + " " + then + " " + now);
        }
    }

    private void markRate() {
        float rate = 0;
        if (ctr == 1) {
            mark = System.currentTimeMillis();
            // Log.i(TAG, "+++ btmark " + mark);
        }
        if (ctr++ == limit) {
            rate = (System.currentTimeMillis() - mark) / (float) limit;
            ctr = 1;
            logger.info("+++ btrate " + rate);
        }
    }
}