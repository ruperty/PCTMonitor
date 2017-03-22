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
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moons.control.BaseControlHierarchy;
import uk.co.moons.control.ControlHierarchy;
import uk.co.moonsit.outputs.Alert;

public class ControlParameterGraphServer extends Thread {

    static final Logger LOG = Logger.getLogger(ControlParameterGraphServer.class.getName());

    private SocketServer ss = null;
    private boolean finished = false;

    private final BaseControlHierarchy ch;
    private final Alert alert;

    //private final long time;
    public ControlParameterGraphServer(int port, int timeout, int rtimeout, BaseControlHierarchy c) {
        ch = c;
        try {
            ss = new SocketServer(port, timeout, rtimeout);
        } catch (IOException e) {
            LOG.warning(e.toString());
        }
        alert = new Alert();

    }

    @Override
    public void run() {
        boolean shutdown = false;
        //long logs[] = new long[5];
        while (!finished) {
            //long start = System.currentTimeMillis();
            try {
                if (!ss.isConnected() || ss.isClosed()) {
                    ss.connect();
                    alert.soundAlert(Alert.ASCENDINGARPEGGIO);
                }
            } catch (java.net.SocketTimeoutException ex) {
                LOG.log(Level.INFO, "Timed out on connect - socket status {0}", ss.socketStatus());
                continue;
            } catch (IOException ex) {
                Logger.getLogger(SocketServer.class.getName()).log(Level.WARNING, null, ex);
                LOG.log(Level.WARNING, "socket status {0}", ss.socketStatus());
                alert.soundAlert(Alert.DESCENDINGARPEGGIO);

            }
            //logs[0] = System.currentTimeMillis() - start;
            //logs[0] = System.currentTimeMillis() - start;

            if (ss.isConnected()) {
                try {
                    String msg = ss.receiveEnvelopedMessage();
                    LOG.log(Level.INFO, "Pars: {0}", msg);
                    if (msg.startsWith("SHUTDOWN")) {
                        shutdown = true;
                    } else {
                        setParameters(msg);
                    }

                } catch (IOException ex) {
                    LOG.warning(ex.toString());
                    LOG.log(Level.WARNING, "Data communication lost will close streams - IOEx - socket status {0}", ss.socketStatus());
                    closeStreams();
                } catch (Exception ex) {
                    LOG.log(Level.WARNING, "Data communication lost will close streams - Ex - socket status {0}", ss.socketStatus());
                    Logger.getLogger(ControlParameterGraphServer.class.getName()).log(Level.WARNING, null, ex);
                    closeStreams();
                }

                if (ch.isFinished()) {
                    setFinished(true);
                }
                if (shutdown) {
                    ch.setRunningFlag(false);
                }
            }
        }
        LOG.info("loop finished, about to close socket ");
        closeSocket();

        LOG.info("Server finished");

    }

    public void setParameters(String st) {
        String[] cons = st.split("\\^");
        for (String con : cons) {
            String[] namePars = con.split(";");
            String[] pars = namePars[1].split("\\|");
            for (String par : pars) {
                ch.setControllerParameter(namePars[0], par);
            }
        }
    }

    public void closeStreams() {
        try {
            ss.closeStreams();
        } catch (IOException ex) {
            Logger.getLogger(ControlDataGraphServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeSocket() {
        //setFinished(true);
        if (ss != null) {
            try {
                //if (!ss.isClosed()) {
                ss.close();
                LOG.log(Level.INFO, "Closed ControlParameterGraphServer socket");
                //}
            } catch (IOException ex) {
                Logger.getLogger(ControlParameterGraphServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullPointerException ex) {
                Logger.getLogger(ControlDataGraphServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void close() {
        setFinished(true);
        closeSocket();
        LOG.log(Level.INFO, "Closed ControlParameterGraphServer");
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public static void main(String[] args) {
        try {

            int port = 6667;
            int timeout = 10000;
            String config = "C:\\Versioning\\PCTSoftware\\Controllers\\Models\\000-001-SpeedWeight.xml";
            ControlHierarchy ch = new ControlHierarchy(config);

            ControlParameterGraphServer gsm = new ControlParameterGraphServer(port, timeout, 0, ch);
            gsm.start();

            Thread.sleep(300000);

            gsm.close();
        } catch (Exception ex) {
            Logger.getLogger(ControlParameterGraphServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
