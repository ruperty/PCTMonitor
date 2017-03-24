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
package uk.co.moons.config;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Layers;
import uk.co.moonsit.config.functions.Utils;
import uk.co.moonsit.config.odg.ODGProcessing;

/**
 *
 * @author ReStart
 */
public class ODGConfig {

    private static final Logger LOG = Logger.getLogger(ODGConfig.class.getName());
    private Layers layers = null;

    public ODGConfig() throws Exception {
        LOG.info("+++ ODGConfig constructor");
        layers = new Layers();
        layers.setType("Model");
    }

    public void processDocument(String config) throws Exception {
        ODGProcessing odg = new ODGProcessing();
        long start = System.currentTimeMillis();
        odg.parseDocument(config);
        //LOG.log(Level.INFO, "ODG processing time {0}", ( System.currentTimeMillis()-start));
        odg.constructPCTXML(layers);
        //LOG.log(Level.INFO, "ODG processing time {0}", ( System.currentTimeMillis()-start));
        odg.verifyDiagram();
        LOG.log(Level.INFO, "ODG processing time {0}", (System.currentTimeMillis() - start));
    }

    public void saveConfig(String out) throws Exception {
        Utils.saveToXML(layers, out);
        Utils.verify(out);
    }

    public static void main(String[] args) throws Exception {

        //String prefix="DesignTest";
        //String prefix="GUITest001";
        //String prefix="GUITest002";
        String dir = args[0];
        String prefix = args[1];

        String config = dir + prefix + ".odg";
        String out = dir + prefix + ".xml";
        String valid = dir + prefix + ".valid.xml";

        ODGConfig odg = new ODGConfig();
        odg.processDocument(config);
        odg.saveConfig(out);

        List<String> original = odg.fileToLines(valid);
        List<String> revised = odg.fileToLines(out);

        // Compute diff. Get the Patch object. Patch is the container for computed deltas.
        Patch patch = DiffUtils.diff(original, revised);

        StringBuilder sb = new StringBuilder();
        for (Object delta : patch.getDeltas()) {
            sb.append(delta).append("\n");
        }
        if (sb.length() > 0) {
            throw new Exception(sb.toString());
        }
    }

    // Helper method for get the file content
    private List<String> fileToLines(String filename) {
        List<String> lines = new LinkedList<>();
        String line = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
