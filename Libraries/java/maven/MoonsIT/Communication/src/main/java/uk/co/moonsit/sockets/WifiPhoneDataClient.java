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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rupert Young
 */
public class WifiPhoneDataClient extends Thread {

    private static final Logger logger = Logger.getLogger(WifiPhoneDataClient.class.getName());

    private final SocketClient client;
    private float longitude;
    private float latitude;
    private boolean finshed = false;
    private ConcurrentLinkedQueue<Float> queue;

    public WifiPhoneDataClient(String serverName, int port, ConcurrentLinkedQueue<Float> queue, int timeout) {
        client = new SocketClient(serverName, port, timeout);
        this.queue = queue;
    }

    public boolean connect() {
        boolean rtn = false;
        while (!client.isConnected() || client.isClosed()) {
            try {
                client.connect();
                rtn = true;
            } catch (IOException e) {
                logger.log(Level.INFO, "Connection failed {0}", e.toString());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MessageClient.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        return rtn;
    }

    @Override
    public void run() {
        while (!finshed) {
            while (!client.isConnected() || client.isClosed()) {
                try {
                    client.connect();
                } catch (IOException e) {
                    logger.log(Level.INFO, "Connection failed {0}", e.toString());
                    //running = false;
                    //break;
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MessageClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            try {
// get data from queue
                while (queue.peek() != null) {
                    latitude = queue.poll();
                    client.writeFloat(latitude);
                }
                while (queue.peek() != null) {
                    longitude = queue.poll();
                    client.writeFloat(longitude);
                }
            } catch (IOException ex) {
                Logger.getLogger(WifiPhoneDataClient.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public boolean isFinshed() {
        return finshed;
    }

    public void setFinshed(boolean finshed) {
        this.finshed = finshed;
    }

    public void close() throws Exception {
        setFinshed(true);
        client.close();
    }
/*
    public static void main(String[] args) throws Exception {

        String host = "localhost";
        int port = 6668;
        ConcurrentLinkedQueue<Float> queue = new ConcurrentLinkedQueue<Float>();

        WifiPhoneDataClient demo = new WifiPhoneDataClient(host, port, queue);
        demo.start();

        for (int i = 0; i < 10; i++) {
            queue.offer(1f * 10);
            queue.offer(1f * 20);
            System.out.println("+++ " + i);
            Thread.sleep(1000);
        }
        System.out.println("+++ loop finished");
            Thread.sleep(5000);

        demo.close();
    }
*/
}
