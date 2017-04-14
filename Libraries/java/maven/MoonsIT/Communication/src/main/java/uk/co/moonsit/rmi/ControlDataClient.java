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

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rupert Young
 */
public class ControlDataClient extends BaseRMIClient {

    private static final Logger logger = Logger.getLogger(ControlDataClient.class.getName());
    private float frequency;
    private final boolean log;
    private ControlDataInterface rmi = null;
    private final String config;
    private final String path;

    public ControlDataClient(String policy, String host, String name, float frequency, boolean l) throws RemoteException, IOException {
        super();
        this.frequency = frequency;
        log = l;
        rmi = (ControlDataInterface) init(policy, host, name);

        config = rmi.getConfig();
        path = rmi.getConfigPath();
    }

    public String getPath() {
        return path;
    }

    public String getConfig() {
        return config;
    }

    public void setResponse(String s) throws RemoteException, Exception {
        rmi.setParameters(s);

    }

    public String getMessage() throws RemoteException {
        return rmi.getValues();
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public float getFrequency() {
        return frequency;
    }

    public static void main(String args[]) {
        try {
            ControlDataClient mc = new ControlDataClient("file:./client.policy", "localhost", "ControlData", 5, true);

            String st = mc.getConfig();

            System.out.println(st);

        } catch (IOException ex) {
            Logger.getLogger(ControlDataClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
