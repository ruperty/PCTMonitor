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
//import org.apache.xerces.parsers.DOMParser;

//import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
//import java.util.ArrayList;
//import java.util.Iterator;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 *
 * @author Rupert
 */
public class Reader {

    private static final Logger logger = Logger.getLogger(Reader.class.getName());

    /**
     * Re-loads the configuration even if it has already been loaded
     *
     * @param xmlFile
     */
    public void load(String xmlFile) throws ParserConfigurationException {
        logger.log(Level.INFO, "Attempting to read xml file {0}", xmlFile);
        try {
            FileInputStream fis = new FileInputStream(xmlFile);
            this.load(fis);
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, "Unable to load configuration resource - {0} {1}", new Object[]{xmlFile, ioe.toString()});
            System.out.println("Unable to load configuration resource - " + xmlFile + " " + ioe.toString());
        }
    }

    public int layers() {
        return 1;
    }

    /**
     * Re-loads the configuration even if it has already been loaded
     *
     * @param configStream
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    public void load(InputStream configStream) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        //DOMParser builder = new DOMParser();
        try {

            // set a very strict error handler that will always propogate the exception
            builder.setErrorHandler(new ErrorHandler() {

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void warning(SAXParseException exception) throws SAXException {
                    throw exception;
                }
            });
            Document doc = builder.parse(new InputSource(configStream));

            //Document doc = builder.getDocument();
            Element root = doc.getDocumentElement();

            //get config element list
            NodeList childNodes = root.getChildNodes();
            for (int index = 0; index < childNodes.getLength(); index++) {
                Node element = childNodes.item(index);
                System.out.println("+++ " + index + " " + element.getNodeName() + " " + element.getNodeValue());
                //System.out.println("+++ " + element.getNodeName());

                NodeList recordList = element.getChildNodes();
                for (int recordIndex = 0; recordIndex < recordList.getLength(); recordIndex++) {
                    Node recordElement = recordList.item(recordIndex);
                    System.out.println("+++ " + recordIndex + " " + recordElement.getNodeName() + " " + recordElement.getNodeValue());
                }


                /*
            Node configElement = childNodes.item(index);
            if (configElement.getNodeName().equals("SystemCode") && configElement.getNodeType() == Node.ELEMENT_NODE) {
            NodeList recordList = configElement.getChildNodes();
            for (int recordIndex = 0; recordIndex < recordList.getLength(); recordIndex++) {
            Node recordElement = recordList.item(recordIndex);
            if (recordElement.getNodeName().equals("IncludeConfigurationFile")) {
            String fileName = recordElement.getFirstChild().getNodeValue();

            Iterator iterator = fileNames.iterator();
            boolean exists = false;
            while (iterator.hasNext()) {
            if (((String) iterator.next()).equals(EGATE_HOME + CONFIG_HOME + fileName)) {
            exists = true;
            break;
            }
            }
            if (!exists) {
            fileNames.add(EGATE_HOME + CONFIG_HOME + fileName);
            reLoadConfiguration(EGATE_HOME + CONFIG_HOME + fileName);

            }
            iterator = null;
            } else if (recordElement.getNodeName().equals("SystemRecord")) {
            SystemCodeDecode sysCodeDecode = new SystemCodeDecode();
            NodeList recordConfList = recordElement.getChildNodes();
            //get config elements
            for (int recordConfIndex = 0; recordConfIndex < recordConfList.getLength(); recordConfIndex++) {
            Node recordConfElement = recordConfList.item(recordConfIndex);
            String nodeName = recordConfElement.getNodeName();
            if (nodeName.equals("id")) {
            sysCodeDecode.setId(new Long(recordConfElement.getFirstChild().getNodeValue()).longValue());
            } else if (nodeName.equals("system")) {
            sysCodeDecode.setSystem(recordConfElement.getFirstChild().getNodeValue());
            } else if (nodeName.equals("category")) {
            sysCodeDecode.setCategory(recordConfElement.getFirstChild().getNodeValue());
            } else if (nodeName.equals("code")) {
            sysCodeDecode.setCode(recordConfElement.getFirstChild().getNodeValue());
            } else if (nodeName.equals("decode")) {
            sysCodeDecode.setDecode(recordConfElement.getFirstChild().getNodeValue());

            } else if (nodeName.equals("CommonCodeRecord")) {
            //System.out.println("CommonCodeRecord[" + recordConfIndex + "]" + recordConfElement.getFirstChild().getNodeValue());

            //get CommonCode values
            NodeList commonCodeValList = recordConfElement.getChildNodes();
            for (int commonCodeValIndex = 0; commonCodeValIndex < commonCodeValList.getLength(); commonCodeValIndex++) {
            Node commonCodeVal = commonCodeValList.item(commonCodeValIndex);
            String commonNodeName = commonCodeVal.getNodeName();
            if (commonNodeName.equals("commonId")) {
            CommonCode commonCode = sysCodeDecode.getCommonCode();
            if (commonCode == null) {
            commonCode = new CommonCode();
            }
            commonCode.setCommonId(new Long(commonCodeVal.getFirstChild().getNodeValue()).longValue());
            sysCodeDecode.setCommonCode(commonCode);
            } else if (commonNodeName.equals("commonCode")) {
            CommonCode commonCode = sysCodeDecode.getCommonCode();
            if (commonCode == null) {
            commonCode = new CommonCode();
            }
            commonCode.setCommonCode(commonCodeVal.getFirstChild().getNodeValue());
            sysCodeDecode.setCommonCode(commonCode);
            }
            }
            }

            }
            // add sysCodeDecode
            super.addSystemCodeDecode(sysCodeDecode);

            }

            }
            //                    String configNodeName = configElement.getNodeName();
            //                    String configNodeValue = configElement.getFirstChild().getNodeValue();
            //System.out.println("  " + configNodeName + " \t\t\t[" + configNodeValue + "]");
            //					configProperties.addConfiguration(configNodeName, configNodeValue);
            } //set debug level
            else if (configElement.getNodeName().equals("LogLevel") && configElement.getNodeType() == Node.ELEMENT_NODE) {
            String debugLevelStr = configElement.getFirstChild().getNodeValue();
            debugLevelStr = debugLevelStr.toUpperCase();
            if (debugLevelStr.equals("NONE")) {
            super.debugLevel = BaseRefDataHandler.NONE;
            } else if (debugLevelStr.equals("FATAL")) {
            super.debugLevel = BaseRefDataHandler.FATAL;
            } else if (debugLevelStr.equals("ERROR")) {
            super.debugLevel = BaseRefDataHandler.ERROR;
            } else if (debugLevelStr.equals("WARNING")) {
            super.debugLevel = BaseRefDataHandler.WARNING;
            } else if (debugLevelStr.equals("INFO")) {
            super.debugLevel = BaseRefDataHandler.INFO;
            } else if (debugLevelStr.equals("DEBUG")) {
            super.debugLevel = BaseRefDataHandler.DEBUG;
            } else if (debugLevelStr.equals("TRACE")) {
            super.debugLevel = BaseRefDataHandler.TRACE;
            }
            //                    logger.debug("Debug level = " + debugLevel + ", " + debugLevelStr);
            }
                 */
            }

            //iterate cyclic evets
            /*			NodeList childNodes = root.getChildNodes();
        for (int index = 0; index < childNodes.getLength(); index++) {
        Node configElement = childNodes.item(index);
        if(configElement.getNodeType() == Node.ELEMENT_NODE) {
        String configNodeName = configElement.getNodeName();
        String configNodeValue = configElement.getFirstChild().getNodeValue();
        System.out.println("  "+configNodeName+" \t\t\t["+configNodeValue+"]");
        //					configProperties.addConfiguration(configNodeName, configNodeValue);
        }
        }
             */
        } catch (SAXException e) {
            logger.severe("ERROR: There is an XML problem with your configuration file ");
            e.printStackTrace();
            //throw new InvalidConfigException(e);
        } catch (IOException e) {
            logger.severe("ERROR: There is an IO problem with your configuration file ");
            //			throw new InvalidConfigException(e);
        }

    }
}
