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
package uk.co.moons.sensors;

import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moonsit.sockets.QMCommsSingleton;

/**
 *
 * @author Rupert Young
 */
public class QMSensor {

    //static final Logger LOG = Logger.getLogger(QMSensor.class.getName());

    private final QMCommsSingleton qmsingleton;

    public QMSensor() {
        qmsingleton = QMCommsSingleton.getInstance();
    }

    /**
     * Creates a new instance of QMSensor
     *
     * @param host
     * @param port
     * @param timeout
     * @param level
     * @param x
     * @param y
     * @param width
     * @param height
     * @param exe
     * @throws java.io.IOException
     *
     */
    public QMSensor(String host, int port, int timeout, int level, int x, int y, int width, int height, String exe) throws IOException {
        //state = new BaseSensorState();
        qmsingleton = QMCommsSingleton.getInstance(host, port, timeout, level,  x,  y,  width,  height,  exe);
    }

    public double getValue(int i) {
        return qmsingleton.getValue(i);
    }

    public void setValue(int index, double input) {
        qmsingleton.setValue(index, input);
    }

    public boolean subscribe() throws IOException {
        try {
            qmsingleton.receiveCursor();
        } catch (SocketException ex) {
            Logger.getLogger(QMCommsSingleton.class.getName()).log(Level.SEVERE, null, ex);
            qmsingleton.close();
            return false;
        }
        return true;
    }

    public void reset(int level) throws IOException {
        qmsingleton.setLevel(level);
        qmsingleton.init();
    }

    public void close() throws Exception {
        qmsingleton.close();
    }
}
