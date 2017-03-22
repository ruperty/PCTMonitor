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
package uk.co.moonsit.outputs;

import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moons.sensors.SerialCSVSensorSingleton;

/**
 *
 * @author Rupert
 */
public class SerialCSVWrite extends SerialWrite {

    private static final Logger logger = Logger.getLogger(SerialCSVWrite.class.getName());
    
    //private SerialCSVInterfaceSingleton singleton;

    public SerialCSVWrite(String comPort, int timeout, int datarate) {
        super(comPort, timeout,  datarate);        
    }

    @Override
    public void init() throws UnsupportedCommOperationException, IOException, TooManyListenersException, Exception {
        singleton = SerialCSVSensorSingleton.getInstance(comPort, timeout, datarate);
        //write(0f);
    }

    

    public static void main(String[] args) {

        try {
            String comPort = "COM72";
            int timeout = 2000;
            int datarate = 9600;
            SerialCSVWrite serial = new SerialCSVWrite(comPort, timeout, datarate);
            serial.init();
            float f = 123f;
            serial.write(f);
            logger.log(Level.INFO, "Data sent {0}", f);
            serial.close();
        } catch (IOException ex) {
            Logger.getLogger(SerialCSVWrite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TooManyListenersException ex) {
            Logger.getLogger(SerialCSVWrite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SerialCSVWrite.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
