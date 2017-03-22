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
import java.util.logging.Logger;
import uk.co.moons.sensors.states.BaseSensorState;
import uk.co.moonsit.sockets.WifiPhoneDoubleDataServer;
import uk.co.moonsit.utils.Print;

/**
 *
 * @author Rupert Young
 */
public class PhoneGPSSensor extends BaseSensorServer {

    static final Logger logger = Logger.getLogger(PhoneGPSSensor.class.getName());
    private static PhoneGPSSensor instance = null;
    private final boolean debug = false;

    //private final BaseSensorState state;
    /**
     * Creates a new instance of USensor
     */
    /**
     * Creates a new instance of USensor
     *
     * @param port
     * @param timeout
     * @param rtimeout
     * @param i
     * @param initial
     * @throws java.io.IOException
     *
     */
    protected PhoneGPSSensor(int port, int timeout, int rtimeout, long i, Double initial) throws IOException {
        interval = i;
        state = new BaseSensorState(2);
        server = new WifiPhoneDoubleDataServer(port, timeout, rtimeout, 2, initial);
        //sensor.start();
    }

    public static PhoneGPSSensor getInstance(int port, int timeout, int rtimeout, long i, Double initial) throws IOException {
        if (instance == null) {
            instance = new PhoneGPSSensor(port, timeout, rtimeout, i, initial);
        }
        return instance;
    }

    @SuppressWarnings({"SleepWhileInLoop", "CallToThreadDumpStack"})
    @Override
    public void run() {
        //int i = 0;
        logger.info("+++ phoneGps started-");
        server.start();

        try {
            while (state.isSensorActive()) {
                //i++;
                double[] data = server.getData();
                if (debug) {
                    Print.logArray("PhoneGPSSensor", data);
                }

                state.setValues(data);

                if (debug) {
                    Print.logArray("PhoneGPSSensor state", state.getValues());
                }
                Thread.sleep(interval);

                //logger.info(state.isSensorActive() + " rdg "  + reading + " " + System.currentTimeMillis() );
            }
            logger.info("phoneGps finished");
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            logger.warning(ex.toString());
        } finally {
            close();
            instance = null;
        }

    }

    //public static void main(String[] args) throws Exception {
      //  PhoneGPSSensor.getInstance(6668, 10, 10, 20, null);
    //}
}
