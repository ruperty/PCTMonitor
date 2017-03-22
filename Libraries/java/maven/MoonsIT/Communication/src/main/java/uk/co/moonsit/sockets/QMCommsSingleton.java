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
package uk.co.moonsit.sockets;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.util.logging.Logger;

/**
 *
 * @author Rupert Young
 */
public class QMCommsSingleton {

    //static final Logger LOG = Logger.getLogger(QMCommsSingleton.class.getName());
    private int level;

    private static QMCommsSingleton instance = null;
    private final QMClient qmclient;

    //private final BaseSensorState state;
    /**
     * Creates a new instance of USensor
     */
    /**
     * Creates a new instance of USensor
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
     * @throws java.net.ConnectException
     * @throws java.io.IOException
     *
     */
    protected QMCommsSingleton(String host, int port, int timeout, int level, int x, int y, int width, int height, String exe) throws ConnectException, IOException {
        qmclient = new QMClient(host, port, timeout, x, y, width, height, exe);
        this.level = level;
        qmclient.initialiseCommunication(level);
    }

    public void init() throws IOException {
        qmclient.initialiseCommunication(level);
    }

    public static QMCommsSingleton getInstance() {
        return instance;
    }

    public static QMCommsSingleton getInstance(String host, int port, int timeout, int level, int x, int y, int width, int height, String exe) throws IOException {
        if (instance == null) {
            instance = new QMCommsSingleton(host, port, timeout, level,  x,  y,  width,  height,  exe);
        }
        return instance;
    }

    public void receiveCursor() throws SocketException, IOException {
        qmclient.receiveCursor();
    }

    /*
    private void receiveConfig() throws IOException {
        qmclient.receiveConfig();
    }

    private void sendReset() throws IOException {
        qmclient.sendReset(level);
    }*/
    public void sendMove() throws IOException {
        qmclient.sendMove();//outputx, outputy);
    }

    public double getValue(int i) {
        return qmclient.getValue(i);
    }

    public void setValue(int index, double input) {
        qmclient.setValue(index, input);
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void close() throws IOException {
        qmclient.close();
        if (instance != null) {
            instance = null;
        }
    }

    public boolean isReset() {
        return qmclient.isReset();
    }

    public void unset() {
        qmclient.unset();
    }
}
