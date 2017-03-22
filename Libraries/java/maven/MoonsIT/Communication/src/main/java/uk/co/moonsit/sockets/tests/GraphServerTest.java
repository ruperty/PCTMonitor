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
package uk.co.moonsit.sockets.tests;

import uk.co.moonsit.sockets.GraphServer;
import uk.co.moonsit.sockets.SocketConstants;
import uk.co.moonsit.utils.DisplayObject;

/**
 *
 * @author ReStart
 */
public class GraphServerTest {

    /**
     * @param args the command line arguments
     */
    private float[] values;

    private void display(float a, float b, float c, float d, 
            DisplayObject dob) {

        if (dob.isReceivedConfig()) {
            if (dob.isConfigChanged()) {
                values = new float[dob.getSize() - 1];
                dob.setConfigChanged(false);
            }

            switch (dob.getDisplayType()) {
                case SocketConstants.A_1_B_1_M_2:
                    values[0] = a;
                    values[1] = b;
                    values[2] = c;
                    break;
                case SocketConstants.rBA_1_rBV_1_rCP_2_rCV_3:
                    values[0] = a;
                    values[1] = b;
                    values[2] = c;
                    values[3] = d;
                    break;
            }
            dob.setValues( values);
        }

    }

    public static void main(String[] args) throws Exception {
        GraphServerTest gst = new GraphServerTest();
        int port = 6666;
        int timeout = 10000;
        //boolean running = true;
        DisplayObject dob = new DisplayObject();
        GraphServer gs = new GraphServer(port, timeout, 0, dob);
        gs.start();
        int i = 0;

        //DateFormat dateFormat = new SimpleDateFormat("k");
        //Calendar cal = Calendar.getInstance();
        //long start = System.currentTimeMillis();
        //long time;

        while (!gs.isFinished()) {

            float a = i;
            float b = i + 50;
            float c = Runtime.getRuntime().freeMemory();
            float d = i + 250;

            gst.display(a, b, c, d, dob);
            i++;
            if (i > 99) {
                i = -100;
            }

            Thread.sleep(10);
        }

    }

}
