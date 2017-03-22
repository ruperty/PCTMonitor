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
package uk.co.moons.control.run;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moons.control.ModelControlHierarchy;
import uk.co.moonsit.utils.Environment;

/**
 *
 * @author ReStart
 */
public class PCTRun {

    static final Logger logger = Logger.getLogger(PCTRun.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PCTRun pr = new PCTRun();
        pr.run(args);
    }

    public static String run(String[] args) {
        String output = null;
        //PCTRun rc = new PCTRun();
        ModelControlHierarchy ch = null;
        try {
            logger.info("+++ Start ");
            boolean print = false;
            long runTime = 0;
            long runIter = 0;
            String config = null;
            String outputFile = null;

            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-print")) {
                    print = true;//Boolean.parseBoolean(args[++i]);
                }
                if (args[i].equals("-runTime")) {
                    runTime = Long.parseLong(args[++i]);
                }
                if (args[i].equals("-runIter")) {
                    runIter = Long.parseLong(args[++i]);
                }
                if (args[i].equals("-xml")) {
                    config = args[++i];
                }
                if (args[i].equals("-out")) {
                    outputFile = args[++i];
                }
            }

            List<String> list = new ArrayList<>();
            Environment.getInstance().setListOutputFunctions(list);

            ch = new ModelControlHierarchy(config);
            ch.init();
            ch.setPrint(print);
            if (runTime > 0) {
                ch.setRunTime(runTime);
            }
            if (runIter > 0) {
                ch.setRunIter(runIter);
            }
            if (outputFile != null) {
                ch.setOutputFile(outputFile);
            }

            ch.run();
            /*} else {
             logger.info("+++ Not connected ");
             }*/
            output=ch.getDelimitedString();
        } catch (Exception ex) {
            Logger.getLogger(PCTRun.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ch != null) {
                try {
                    ch.close();
                } catch (Exception ex) {
                    Logger.getLogger(PCTRun.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return output;
    }

}
