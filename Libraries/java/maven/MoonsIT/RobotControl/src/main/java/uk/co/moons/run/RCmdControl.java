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
package uk.co.moons.run;

//import x. .*;
import java.io.File;
import java.util.logging.Level;
import uk.co.moons.handlers.MotorHandler;
import java.util.logging.Logger;
import uk.co.moons.control.RobotControlHierarchy;
import uk.co.moons.sensors.states.BaseSensorState;

/**
 *
 * @author Rupert Young
 */
public class RCmdControl {

    static final Logger LOG = Logger.getLogger(RCmdControl.class.getName());
    //private Thread actionThread;
    //private MotorHandler mh;
    private final BaseSensorState ss;
    //private USensor us;

    public RCmdControl() {
        ss = new BaseSensorState();
    }

    public void close() {
        try {
            //CommandHandler.getSingleton().setQuit(true);
            ss.setSensorActive(false);
            Thread.sleep(1000);
            //NXTCommand.close();
        } catch (InterruptedException e) {
            LOG.log(Level.FINE, "+++ Exception {0}", e.toString());
        }
    }

    @SuppressWarnings("SleepWhileInLoop")
    public boolean run(int secs, int function, int distance, int speed, double gain, double one, double two, int layer) {
        /*
         * boolean rtn = isConnected();
        if (rtn) {
        logger.info("+++ is Connected ");
        } else {
        logger.info("+++ is Not Connected ");
        return false;
        }*/

        try {
            while (secs-- > 0) {
                LOG.log(Level.INFO, "+++ secs {0}", secs);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            LOG.log(Level.FINE, "+++ Exception {0}", e.toString());
        }
        close();
        return true;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RCmdControl rc = new RCmdControl();
        LOG.info("+++ Start ");
        boolean print = false;
        long runTime = 0;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-print")) {
                print = Boolean.parseBoolean(args[++i]);
            }
            if (args[i].equals("-runTime")) {
                runTime = Long.parseLong(args[++i]);
            }
        }

        if (args[0].equals("-xml")) {
            String config = args[1];
            RobotControlHierarchy ch = null;
            try {
                ch = new RobotControlHierarchy(config);
                ch.setPrint(print);
                ch.setRunTime(runTime);
                ch.setOutputFile(System.getProperty("user.home") + File.separator + "tmp" + File.separator 
                        + "PCT" + File.separator + "Controllers" + File.separator  + ch.getFileNamePrefix() + ".csv");

                /*
                if (rc.isConnected()) {
                 */
                ch.init();//.initRobot();
                ch.run();
                /*} else {
                logger.info("+++ Not connected "
                
                );
                }*/

            } catch (Exception ex) {
                //logger.severe(ex.toString());
                Logger.getLogger(RCmdControl.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (ch != null) {
                        ch.stop();
                        ch.close();
                    }
                } catch (Exception ex) {
                    Logger.getLogger(RCmdControl.class.getName()).log(Level.SEVERE, null, ex);
                }
                rc.close();
            }
        } else {
            int secs = 60, d = 50, layer = 2, s = 500;
            double g = 0.2, one = .5, two = .8;
            boolean rtn = false;
            //rtn = rc.run(secs, 1, d, s, g, one, two, layer);
            rtn = rc.run(secs, 0, d, s, g, one, two, layer);
            if (rtn) {
                LOG.info("+++ Closed ");
            } else {
                LOG.info("+++ Not run");
            }
        }
    }
}
