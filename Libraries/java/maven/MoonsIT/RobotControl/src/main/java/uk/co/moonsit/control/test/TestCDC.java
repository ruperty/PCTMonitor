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
package uk.co.moonsit.control.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moons.control.RemoteControlHierarchy;
import uk.co.moonsit.sockets.ControlDataClient;
import uk.co.moonsit.sockets.ControlDataGraphServer;
import uk.co.moonsit.sockets.ControlParameterClient;

/**
 *
 * @author ReStart
 */
public class TestCDC {

    private static final Logger logger = Logger.getLogger(ControlDataClient.class.getName());

    public static void main(String[] args) {

        try {
            int type = 1;
            String host = "localhost";
            int port = 6666;
            int port1 = 6667;
            int freq = 2;

            switch (type) {
                case 0:

                    //ControlDataClient cdc = new ControlDataClient(host, port, freq);
                    //ControlParameterClient cpc = new ControlParameterClient(host, port1);
                    //cdc.connect();
                    //cdc.initConfig();
                    //String config = cdc.getConfig();
                    //String path = cdc.getPath();
                    //logger.log(Level.INFO, "Path {0}", path);
                    //logger.log(Level.INFO, "Config {0}", config);
                    RemoteControlHierarchy ch = new RemoteControlHierarchy(host, port, port1, freq, true, 5000);
                    for (int i = 0; i < 100; i++) {
                        ch.iterate();
                        System.out.println(ch.getDelimitedString());
                    }
                    break;
                case 1:

                    host = "Lulu";
                    ControlDataClient cdc = new ControlDataClient(host, port, freq, 5000);
                    cdc.connect();
                    cdc.initConfig();
                    System.out.println("Sleeping ...");
                    Thread.sleep(5000);
                    cdc.close();
                    System.out.println("Closed. Sleeping ...");
                    Thread.sleep(5000);
                    cdc.connect();
                    cdc.initConfig();
                    System.out.println("Connected. Sleeping ...");
                    Thread.sleep(5000);
                    cdc.close();
                    break;

                case 2:

                    host = "Lulu";
                    ControlParameterClient cpc = new ControlParameterClient(host, port1, 5000);
                    cpc.connect();
                    cpc.sendResponse("1_1");
                    System.out.println("Sleeping ...");
                    Thread.sleep(5000);
                    cpc.close();
                    System.out.println("Closed. Sleeping ...");
                    Thread.sleep(5000);
                    cpc.connect();
                    cpc.sendResponse("2_2");
                    System.out.println("Connected. Sleeping ...");
                    Thread.sleep(5000);
                    cpc.close();
                    break;

            }
        } catch (Exception ex) {
            Logger.getLogger(TestCDC.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
