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
package uk.co.moonsit.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rupert Young
 */
public class FloatQueueReceiver extends Thread {

    private static final Logger logger = Logger.getLogger(FloatQueueReceiver.class.getName());

    private ConcurrentLinkedQueue<List<Float>> queue = null;
    private boolean running = true;
    private final OutputStream out;

    public FloatQueueReceiver(ConcurrentLinkedQueue<List<Float>> q, OutputStream o, String header) throws IOException {
        queue = q;
        out = o;
        out.write(header.getBytes());
        out.write("\n".getBytes());
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {

        while (running) {

            while (queue.peek() != null) {
                //logger.info("SIZE: " + queue.size());
                List<Float> l = queue.poll();
                if (l == null) {
                    continue;
                }
                StringBuilder sb = new StringBuilder();
                for (Float f : l) {
                    sb.append(f).append(",");
                }
                sb.append("\n");
                //logger.info(sb.toString());
                try {
                    out.write(sb.toString().getBytes());
                } catch (IOException ex) {
                    logger.info(ex.toString());
                    break;
                }
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(FloatQueueReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
