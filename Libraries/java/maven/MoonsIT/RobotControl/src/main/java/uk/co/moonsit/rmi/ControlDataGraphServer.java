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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moons.control.BaseControlHierarchy;
import uk.co.moons.control.ControlHierarchy;

/**
 *
 * @author ReStart
 */
public class ControlDataGraphServer extends BaseRMIServer implements ControlDataInterface {

    private final String config;
    private final BaseControlHierarchy ch;

    public ControlDataGraphServer(String con, BaseControlHierarchy c, String policy, String n) throws RemoteException {
        super();
        config = con;
        ch = c;
        name = n;
        init(policy);
    }

    /*
     static public void createRegistry(int port) throws RemoteException {
     LocateRegistry.createRegistry(port);
     }*/
    @Override
    public String getConfig() throws RemoteException, IOException {
        Charset cs = Charset.defaultCharset();
        return new String(Files.readAllBytes(Paths.get(config)), cs);
    }

    @Override
    public String getValues() throws RemoteException {
        return ch.getDelimitedString();
    }

    @Override
    public String getConfigPath() throws RemoteException {
        return config;
    }

    @Override
    public void setParameters(String st) {
        String[] cons = st.split("\\^");
        for (String con : cons) {
            String[] namePars = con.split("_");
            String[] pars = namePars[1].split("\\|");
            for (String par : pars) {
                ch.setControllerParameter(namePars[0], par);
            }
        }
    }

    public static void main(String[] args) {
        try {
            /*
             System.setProperty("java.security.policy", "file:./server.policy");
             if (System.getSecurityManager() == null) {
             System.setSecurityManager(new RMISecurityManager());
             }
             */
            String config = "C:\\Versioning\\Software\\Controllers\\Models\\000-Sine.xml";
            ControlHierarchy ch = new ControlHierarchy(config);

            ControlDataGraphServer gsm = new ControlDataGraphServer(config, ch, "file:./server.policy", "ControlData");
            gsm.start();
            
            Thread.sleep(30000);
            gsm.close();
        } catch (Exception ex) {
            Logger.getLogger(ControlDataGraphServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
