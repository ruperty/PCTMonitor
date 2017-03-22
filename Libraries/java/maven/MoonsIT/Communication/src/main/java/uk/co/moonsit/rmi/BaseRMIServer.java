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
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Rupert Young
 */
public abstract class BaseRMIServer extends Thread implements BaseRMIInterface {

    protected String name;

    protected void init(String policy) throws RemoteException {
        System.setProperty("java.security.policy", policy);
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        bindService();

    }

    @Override
    public void close() throws RemoteException, NotBoundException{
        unbindService();
        UnicastRemoteObject.unexportObject(this, true);
    }

    private void unbindService() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();
        registry.unbind(name);
        System.out.println(name + " unbound");
    }

    private void bindService() throws RemoteException {
        BaseRMIInterface stub = (BaseRMIInterface) UnicastRemoteObject.exportObject(this, 0);
        Registry registry = LocateRegistry.getRegistry();
        registry.rebind(name, stub);
        System.out.println(name + " bound");
    }
}
