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
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Rupert Young
 */
public interface BaseRMIInterface extends Remote {

    public void close() throws RemoteException, NotBoundException;

    //protected void unbindService() throws RemoteException, NotBoundException;

    //protected void bindService() throws RemoteException;
}
