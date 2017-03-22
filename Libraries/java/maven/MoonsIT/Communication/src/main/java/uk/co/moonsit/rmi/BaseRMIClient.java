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

import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rupert
 */
public class BaseRMIClient extends Thread {
    
    @SuppressWarnings("SleepWhileInLoop")
    public BaseRMIInterface init(String policy, String host, String name) throws RemoteException {
        System.setProperty("java.security.policy", policy);
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        System.out.println(host);
        Registry registry = LocateRegistry.getRegistry(host);
        String[] list = registry.list();
        for (String s : list) {
            System.out.println(s);
        }
        boolean bound = false;
        BaseRMIInterface rmi = null;
        while (!bound) {
            try {
                if (name.equals("ControlData")) {
                    rmi = (ControlDataInterface) registry.lookup(name);
                }else
                    rmi = (GraphServer) registry.lookup(name);
                
                bound = true;
            } catch (NotBoundException e) {
                System.err.println("RMI client exception:" + e.toString());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(BaseRMIClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return rmi;
    }
    
}
