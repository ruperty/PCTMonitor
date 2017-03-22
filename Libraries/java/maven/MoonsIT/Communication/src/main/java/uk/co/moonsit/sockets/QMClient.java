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
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moonsit.messaging.QMState;
import uk.co.moonsit.windows.WindowsComms;

/**
 *
 * @author Rupert Young
 */
public class QMClient extends SocketClient {

    private final boolean debug = false;

    //private static final Logger LOG = Logger.getLogger(QMClient.class.getName());
    private final QMState qmstate;
    private int level;
    private final ProcessBuilder pbuilder;
    private Process process;
    private final int x, y, width, height;

    //private boolean finished = false;
    public QMClient(String server, int port, int timeout, int x, int y, int width, int height, String exe) {
        super(server, port, timeout);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        qmstate = new QMState();
        pbuilder = new ProcessBuilder(exe);
    }

    public void initialiseCommunication(int level) throws ConnectException, IOException {
        WindowsComms wc = new WindowsComms();
        String name = "Quantum Moves";
        if (process == null) {
            process = pbuilder.start();
            wc.openWindow(name);
            wc.setWindowFocus();
            wc.setForegroundWindow();
            wc.pressReturn();
        }

        makeConnection();
        wc.moveWindow(name, x, y, width, height);
        initialise(level);
    }

    protected void initialise(int level) throws IOException {
        this.level = level;
        init();
    }

    private void init() {
        boolean connected = false;
        while (!connected) {
            try {
                sendReset();
                receiveConfig();
                connected = true;
            } catch (SocketException ex) {
                Logger.getLogger(QMClient.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    reconnect();
                } catch (IOException ex1) {
                    Logger.getLogger(QMClient.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } catch (IOException ex) {
                Logger.getLogger(QMClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        LOG.info("Closing QM");
        process.destroy();
    }

    private void reconnect() throws ConnectException, IOException {
        close();
        makeConnection();
    }

    private void sendReset() throws IOException {
        //flush();
        String msg = qmstate.setReset(level);

        // Need this pause for the terminated server to reconnect
        // don't know why !!
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(QMClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        sendData(msg);
        if (debug) {
            LOG.log(Level.INFO, "Reset:{0} ", new Object[]{msg});
        }
    }

    public void sendMove() throws SocketException, IOException {
        String msg = qmstate.setCursor();
        if (debug) {
            LOG.log(Level.INFO, "Move:{0}", msg);
        }
        writeLine(msg);
    }

    private void receiveConfig() throws IOException {
        String config = "";
        while (config.length() == 0) {
            try {
                config = readLine();
            } catch (SocketTimeoutException ex) {
                Logger.getLogger(QMClient.class.getName()).log(Level.SEVERE, null, ex);
                LOG.info("Read timeout no message received, resending reset");
                throw ex;
                //sendReset();
                //config = "";
            }

            if (debug) {
                LOG.log(Level.INFO, "Config:{0}", config);
            }
            if (config != null && config.contains("MOVED_CURSOR_TO_POINT")) {
                LOG.info("Incorrect message received, resending reset");
                sendReset();
                config = "";
            }
            
        }
        qmstate.setConfig(config);
    }
    

    public void receiveCursor() throws SocketException, IOException {
        String cursor = receiveData();
        qmstate.getCursor(cursor);
        if (debug) {
            LOG.info(cursor);
        }
    }

    public boolean isReset() {
        return qmstate.isReset();
    }

    public void unset() {
        qmstate.setReset(false);
    }

    public Double getValue(int i) {
        return qmstate.getValue(i);
    }

    public void setValue(int i, double value) {
        qmstate.setValue(i, value);
    }

    @SuppressWarnings("SleepWhileInLoop")
    public static void main(String[] args) {

        String host = "localhost";
        //String host = "192.168.1.12";
        int port = 30000;
        int level = 1;

        int outerlimit = 5;

        int limit = 1;

        QMClient client = new QMClient(host, port, 5000, -1550, 0, 640, 480, "C:\\packages\\Perceptual learning-20170103T142628Z\\Perceptual learning\\Builds\\2017-01-03\\Server\\Quantum Moves.exe");

        try {
            client.initialiseCommunication(level);
        } catch (ConnectException ex) {
            Logger.getLogger(QMClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(QMClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int j = 0; j < outerlimit; j++) {
            for (int i = 0; i < limit; i++) {
                client.setValue(QMState.LASEROUTPUTX, -0.25);
                client.setValue(QMState.LASEROUTPUTY, -400);

                try {
                    client.sendMove();
                    client.receiveCursor();
                } catch (SocketException ex) {
                    try {
                        Logger.getLogger(QMClient.class.getName()).log(Level.SEVERE, null, ex);
                        client.close();
                        client.initialiseCommunication(level);
                        continue;
                    } catch (ConnectException ex1) {
                        Logger.getLogger(QMClient.class.getName()).log(Level.SEVERE, null, ex1);
                    } catch (IOException ex1) {
                        Logger.getLogger(QMClient.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(QMClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                //LOG.log(Level.INFO, "Fidelity {0} {1}", new Object[]{i, client.getValue(QMState.FIDELITY)});
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                    Logger.getLogger(QMClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            System.out.println("+++ inner loop finished");
            level++;

            try {
                client.initialise(level);
            } catch (IOException ex) {
                Logger.getLogger(QMClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(QMClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("+++ outer loop finished");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(QMClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
