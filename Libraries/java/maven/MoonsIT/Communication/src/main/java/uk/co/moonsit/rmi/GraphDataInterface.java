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

import java.rmi.RemoteException;

/**
 *
 * @author Rupert Young
 */
public interface GraphDataInterface extends BaseRMIInterface {

    public String getConfig() throws RemoteException;

    public float[] getValues() throws RemoteException;

}
