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
package uk.co.moonsit.rmi;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moonsit.utils.DisplayObject;

/**
 *
 * @author ReStart
 */
public class GraphServerMain {

    public GraphServerMain() {
    }

    public void run() {
        try {
            MyKeyListener listener = new MyKeyListener();
            String config = "A,B;Error;Time";
            //String config = "A,B;Error;Time";
            DisplayObject dob = new DisplayObject();

            GraphDataInterface gs = new GraphServer(config, dob, "file:./server.policy", "GraphServer");
            
            float[] values = new float[2];
            int i = 0;
            
            
            while (!listener.esc()) {
                values[0] = i;
                values[1] = i + 50;
                //System.out.println(a + " " + b);
                dob.setValues(values);
                i++;
                if (i > 99) {
                    i = -100;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GraphServerMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            gs.close();
            //gs.unbindService(name);
            //System.exit(0);
        } catch (RemoteException e) {
            System.err.println("GraphServer exception:" + e.toString());
            //e.printStackTrace();
            System.exit(0);
        } catch (Exception e) {
            System.err.println("GraphServer exception:" + e.toString());
            //e.printStackTrace();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        /*
         System.setProperty("java.security.policy", "file:./server.policy");          
         if (System.getSecurityManager() == null) {
         System.setSecurityManager(new RMISecurityManager());
         }
         */

        GraphServerMain gsm = new GraphServerMain();
        gsm.run();
    }

    public class MyKeyListener implements KeyListener {

        private boolean esc = false;

        @Override
        public void keyTyped(KeyEvent e) {
        }

        public boolean esc() {
            return esc;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.out.println("esc Pressed");
                esc = true;
            } else {
                System.out.println("keyPressed=" + KeyEvent.getKeyText(e.getKeyCode()));
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            System.out.println("keyReleased=" + KeyEvent.getKeyText(e.getKeyCode()));
        }

    }
}

//-Djava.rmi.server.codebase=file:/C:/Versioning/Personal/Libraries/java/Communication/dist/Communication.jar -Djava.rmi.server.hostname=ReStart7 -Djava.security.policy=server.policy
