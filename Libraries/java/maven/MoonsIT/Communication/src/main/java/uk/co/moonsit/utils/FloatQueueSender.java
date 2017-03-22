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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rupert Young
 */
public class FloatQueueSender extends ConcurrentLinkedQueue<List<Float>>{

    //private ConcurrentLinkedQueue<List<Float>> queue = null;

    public FloatQueueSender() {
        super();
        //queue = new ConcurrentLinkedQueue<List<Float>>();
    }

    
    public static void main(String[] args) {
        try {
            FloatQueueSender q = new FloatQueueSender();
            
            FloatQueueReceiver qr = new FloatQueueReceiver(q, new FileOutputStream(new File("out.csv")), "a,b,c");
            qr.start();
            
            for (int i = 0; i < 1000; i++) {
                List<Float> l = new ArrayList<Float>();
                for (int j = 0; j < 3; j++) {
                    l.add((float)j);
                }
                q.add(l);
            }
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(FloatQueueSender.class.getName()).log(Level.SEVERE, null, ex);
            }
            qr.setRunning(false);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FloatQueueSender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FloatQueueSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
