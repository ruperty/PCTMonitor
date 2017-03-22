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
import uk.co.moons.sensors.SerialSensorSingleton;

/**
 *
 * @author Rupert
 */
public class SerialWrite {

    private static final Logger logger = Logger.getLogger(SerialWrite.class.getName());

    protected SerialSensorSingleton singleton = null;
    protected int timeout;
    protected int datarate;
    protected String comPort;

    public SerialWrite(String comPort, int timeout, int datarate) {
        this.comPort = comPort;
        this.timeout = timeout;
        this.datarate = datarate;
    }

    public void init() throws UnsupportedCommOperationException, IOException, TooManyListenersException, Exception {
        singleton = SerialSensorSingleton.getInstance(comPort, timeout, datarate);
    }

    public void write(float f) throws IOException {
        singleton.write(f);
    }

    public void write(double d) throws IOException {
        singleton.write(d);
    }

    public void close() {
        singleton.close();
    }

    public static void main(String[] args) {

        try {
            String comPort = "COM72";
            int timeout = 2000;
            int datarate = 9600;
            SerialWrite serial = new SerialWrite(comPort, timeout, datarate);
            serial.init();
            float f = 123f;
            serial.write(f);
            logger.log(Level.INFO, "Data sent {0}", f);
            serial.close();
        } catch (IOException ex) {
            Logger.getLogger(SerialWrite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TooManyListenersException ex) {
            Logger.getLogger(SerialWrite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SerialWrite.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
