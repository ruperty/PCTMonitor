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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;

/**
 *
 * @author ReStart
 */
public class PipeConfig extends BaseControlConfig {

    private static final Logger logger = Logger.getLogger(PipeConfig.class.getName());
    private List<List<String>> layers = null;
    private BufferedReader in = null;
    private List<String> orderedControllers = null;

    public PipeConfig(String config) throws Exception {
        layers = config(config);
    }

    public List<List<String>> getLayers() {
        return layers;
    }

    public void setLayers(List<List<String>> layers) {
        this.layers = layers;
    }

    private List<List<String>> config(String config) throws JAXBException, IOException {
        logger.log(Level.INFO, "+++ Pipe file {0}", config);

        List<List<String>> lys = new ArrayList<List<String>>();

        in = new BufferedReader(new FileReader(config));
        String line = null;
        List<String> list = null;
        while ((line = in.readLine()) != null) {
            //logger.info(line);
            if (line.startsWith("ORD")) {
                processOrderedControllers(line);
            } else {
                if (line.startsWith("LYR")) {
                    if (list != null) {
                        lys.add(list);
                    }
                    list = new ArrayList<String>();
                } else {
                    list.add(line);
                }
            }
        }
        lys.add(list);

        in.close();
        return lys;
    }

    public void processOrderedControllers(String or) {
        String[] ords = or.split("\\|");

        orderedControllers = new ArrayList<String>();
        for (int i = 1; i < ords.length; i++) {
            orderedControllers.add(ords[i]);
        }

    }

    public List<String> getOrderedControllers() {
        return orderedControllers;
    }

    public int size() {
        return layers.size();
    }

    public String getType() {
        return "Robot";
    }
}
