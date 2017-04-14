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
package uk.co.moons.actuators;

import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moonsit.sockets.QMCommsSingleton;

/**
 *
 * @author Rupert Young
 */
public class QMActuator {

    static final Logger LOG = Logger.getLogger(QMActuator.class.getName());
    //private double x;
    //private double y;
    //private double z;
    private final QMCommsSingleton qmsingelton;

    public QMActuator() {
        qmsingelton = QMCommsSingleton.getInstance();
    }

    

    public void setValue(int index, double input) {
        qmsingelton.setValue(index, input);
    }

    public double getValue(int i) {
        return qmsingelton.getValue(i);
    }

    public boolean isReset() {
        return qmsingelton.isReset();
    }

    public void unset() {
        qmsingelton.unset();
    }

    public void publish() throws IOException, Exception {
        try {
            qmsingelton.sendMove();
        } catch (SocketException ex) {
            Logger.getLogger(QMCommsSingleton.class.getName()).log(Level.SEVERE, null, ex);
            qmsingelton.init();
        }
    }
}
