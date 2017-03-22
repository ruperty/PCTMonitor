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
package uk.co.moonsit.bluetooth.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import java.util.logging.Logger;
import uk.co.moonsit.messaging.BeginEndEnvelope;
import uk.co.moonsit.sensors.SensorData;

public abstract class BaseBluetoothExchangeClient implements BluetoothExchangeClientInterface, Runnable {

    private static final Logger logger = Logger.getLogger("BaseBluetoothExchangeClient");
    private int markCtr = 1;
    private Long mark = null;
    protected boolean isConnected = false;
    protected DataInputStream in = null;
    protected DataOutputStream out = null;
    protected ConcurrentLinkedQueue<SensorData> queue = null;
    protected boolean isRunning = true;
    protected SensorData smoothSD = null;
    protected Float smoothness = null;
    protected BeginEndEnvelope envelope;
    private final int limit = 1000;

    public BaseBluetoothExchangeClient(ConcurrentLinkedQueue<SensorData> q,
            String start, String end, Float sm) {
        smoothness = sm;
        queue = q;
        envelope = new BeginEndEnvelope(start, end);
    }

    public BaseBluetoothExchangeClient(ConcurrentLinkedQueue<SensorData> q,
            Float sm) {
        smoothness = sm;
        queue = q;
    }

    public void markRate() {
        float rate = 0;
        if (markCtr == 1) {
            mark = System.currentTimeMillis();
            // logger.info( "+++ btmark " + mark);
        }
        if (markCtr++ == limit) {
            rate = (System.currentTimeMillis() - mark) / (float) limit;
            markCtr = 1;
            logger.info("+++ btrate " + rate);
        }
    }

    public void stop() {
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void processData() throws Exception {
        try {
            if (!connect()) {
                return;
            }

            int size = queue.size();
            if (size > 0) {
                // logger.info( "+++ bt size " + size);

                for (int i = 0; i < size; i++) {
                    SensorData sd = null;
                    try {
                        sd = queue.remove();
                    } catch (Exception e) {
                        logger.info("+++ processData error 2a i " + i + " "
                                + e.toString());
                        throw e;
                    }
                    markRate();
                    smoothSD.smooth(sd);
                    if (i == size - 1) {
                        smoothSD.setTimestamp(sd.getTimestamp());
                    }
                }
                String now = String.valueOf(System.currentTimeMillis());
                String outData = smoothSD.toString() + "|" + now;
                //logger.info("+++ output " + outData);
                envelope.sendMessage(outData, out);
                String inData = envelope.receiveMessage(in);
                long nowl = System.currentTimeMillis();
                long then = Long.parseLong(inData);
                logger.info("+++ DT: " + (then - nowl) + " " + then + " " + nowl);
            }

        } catch (Exception e) {
            logger.info("+++ processData error " + e.toString());
            throw e;
        }

    }

    protected void closeStreams() {
        if (in != null) {
            try {
                in.close();
                logger.info("+++  input stream closed");
            } catch (IOException e) {
                logger.info("+++  input stream not closed " + e.toString());
            }
        }

        if (out != null) {
            try {
                out.close();
                logger.info("+++  output stream closed");
            } catch (IOException e) {
                logger.info("+++  output stream not closed " + e.toString());
            }
        }
    }
}
