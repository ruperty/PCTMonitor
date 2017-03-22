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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import pct.moons.co.uk.schema.layers.Layers;
import pct.moons.co.uk.schema.layers.Layers.Layer;

/**
 *
 * @author ReStart
 */
public class XmlConfig extends BaseControlConfig {

    private static final Logger logger = Logger.getLogger(XmlConfig.class.getName());
    private Layers layers = null;

    public XmlConfig(String config) throws Exception {
        //logger.info("+++ XmlConfig constructor");
        layers = config(config);
        if (layers.getType().equals("RobotNXT")) {
            XmlToPipe xml2pipe = new XmlToPipe();
            xml2pipe.write(config, layers);
        }

        //XmlToLatex xml2latex = new XmlToLatex();
        //xml2latex.write(config, layers);

        //System.exit(1);
    }

    public List<Layer> getLayer() {
        return layers.getLayer();
    }

    public Layers getLayers() {
        return layers;
    }

    public void setLayers(Layers layers) {
        this.layers = layers;
    }

    @SuppressWarnings("LoggerStringConcat")
    private Layers config(String config) throws IOException, JAXBException, Exception {
        //logger.info("+++ Xml  " + config.substring(0, 100));

        if (!config.startsWith("<")) {
            logger.info("+++ Xml file " + config);
            updateConfig(config);
        }

        InputStream xml;

        //if (config.startsWith(".")) {
          //  xml = this.getClass().getClassLoader().getResourceAsStream(config);
        //} else 
        if (config.startsWith("<")) {
            xml = new ByteArrayInputStream(config.getBytes(Charset.defaultCharset()));
        } else {
            xml = new FileInputStream(config);
        }

        Layers lys = null;
        try {
            //long start = System.currentTimeMillis();
            lys = jaxbUnmarshalFromInputStream(xml);
            //logger.log(Level.INFO, "---> jaxb load {0}", (System.currentTimeMillis() - start));
        } catch (JAXBException ex) {
            Logger.getLogger(XmlConfig.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return lys;
    }

    private void updateConfig(String file) throws FileNotFoundException, IOException {
        InputStream xml = null;

        try {
            xml = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XmlConfig.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        int size = xml.available();
        byte[] b = new byte[size];
        xml.read(b);
        String st = new String(b);
        //System.out.println(st);

        String rtn;
        if (st.contains("<ns1:")) {
            if (st.contains("<ns1:ReferenceFunctions>") || st.contains("<ns1:OutputFunctions>") || st.contains("<ns1:InputFunctions>") || st.contains("<ns1:ErrorFunctions>")) {
                //logger.info(("Xml file already updated"));
            } else {
                rtn = st.replaceAll("<ns1:Reference>", "<ns1:ReferenceFunctions><ns1:Reference>");
                rtn = rtn.replaceAll("</ns1:Reference>", "</ns1:Reference></ns1:ReferenceFunctions>");

                rtn = rtn.replaceAll("<ns1:Input>", "<ns1:InputFunctions><ns1:Input>");
                rtn = rtn.replaceAll("</ns1:Input>", "</ns1:Input></ns1:InputFunctions>");

                rtn = rtn.replaceAll("<ns1:Output>", "<ns1:OutputFunctions><ns1:Output>");
                rtn = rtn.replaceAll("</ns1:Output>", "</ns1:Output></ns1:OutputFunctions>");

                rtn = rtn.replaceAll("<ns1:Error>", "<ns1:ErrorFunctions><ns1:Error>");
                rtn = rtn.replaceAll("</ns1:Error>", "</ns1:Error></ns1:ErrorFunctions>");
                System.out.println(rtn);
                File f = new File(file);
                try (FileOutputStream fos = new FileOutputStream(f)) {
                    fos.write(rtn.getBytes());
                }
            }
        } else {
            if (st.contains("<ReferenceFunctions>") || st.contains("<OutputFunctions>") || st.contains("<InputFunctions>") || st.contains("<ErrorFunctions>")) {
                //logger.info(("Xml file already updated"));
            } else {
                rtn = st.replaceAll("<Reference>", "<ReferenceFunctions><Reference>");
                rtn = rtn.replaceAll("</Reference>", "</Reference></ReferenceFunctions>");

                rtn = rtn.replaceAll("<Input>", "<InputFunctions><Input>");
                rtn = rtn.replaceAll("</Input>", "</Input></InputFunctions>");

                rtn = rtn.replaceAll("<Output>", "<OutputFunctions><Output>");
                rtn = rtn.replaceAll("</Output>", "</Output></OutputFunctions>");

                rtn = rtn.replaceAll("<Error>", "<ErrorFunctions><Error>");
                rtn = rtn.replaceAll("</Error>", "</Error></ErrorFunctions>");
                //System.out.println(rtn);
                File f = new File(file);
                try (FileOutputStream fos = new FileOutputStream(f)) {
                    fos.write(rtn.getBytes());
                }
            }
        }

        //System.exit(0);
    }

    public Layers jaxbUnmarshalFromInputStream(java.io.InputStream is) throws javax.xml.bind.JAXBException, IOException {
        Layers ret = null;
        try {
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(Layers.class.getPackage().getName());
            javax.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
            ret = (Layers) unmarshaller.unmarshal(is);
        } finally {
            //try {
            is.close();
            /*} catch (Exception ex) {
             java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex);
             }*/
        }
        return ret;
    }

    @Override
    public List<String> getOrderedControllers() {
        List<String> rtn = null;
        if (layers.getOrderedControllers() != null) {
            rtn = layers.getOrderedControllers().getController();
        }
        return rtn;
    }

    @Override
    public int size() {
        return layers.getLayer().size();
    }

    @Override
    public String getType() {
        return layers.getType();
    }
}
