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
import uk.co.moonsit.utils.DisplayObject;

/**
 *
 * @author ReStart
 */
public class GraphServer extends BaseRMIServer implements GraphDataInterface{

    private final String config;
    private final DisplayObject dob;

    public GraphServer(String c, DisplayObject d, String policy, String n) throws RemoteException {
        super();
        config = c;
        dob = d;
        name=n;
        init(policy);
    }

    /*
    static public void createRegistry(int port) throws RemoteException {
        LocateRegistry.createRegistry(port);
    }*/

    @Override
    public String getConfig() throws RemoteException {
        return config;
    }

    @Override
    public float[] getValues() throws RemoteException {
        return dob.getValues();
    }

}
