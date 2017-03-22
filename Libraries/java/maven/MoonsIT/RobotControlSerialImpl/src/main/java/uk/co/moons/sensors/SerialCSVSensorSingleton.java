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

import java.util.logging.Logger;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.logging.Level;

public class SerialCSVSensorSingleton extends SerialSensorSingleton {//implements SerialPortEventListener {

    private static final Logger logger = Logger.getLogger(SerialCSVSensorSingleton.class.getName());
    private static SerialCSVSensorSingleton instance = null;

    private List<Float> data;

    protected SerialCSVSensorSingleton() {
        // Exists only to defeat instantiation.
    }

    protected SerialCSVSensorSingleton(String port, int timeOut, int dataRate) throws PortInUseException, UnsupportedCommOperationException, IOException, TooManyListenersException, Exception {

        super(port, timeOut, dataRate);
        data = new ArrayList<>();
        /*logger.info("Waiting for data");
        //write("0");
        while (!available()) ;
        logger.info("Data available");*/
    }

    public static SerialCSVSensorSingleton getInstance(String port, int timeOut, int dataRate) throws PortInUseException, UnsupportedCommOperationException, IOException, TooManyListenersException, Exception {

        if (instance == null) {
            instance = new SerialCSVSensorSingleton(port, timeOut, dataRate);
        }

        return instance;
    }

    public static SerialCSVSensorSingleton getInstance() {

        if (instance == null) {
            instance = new SerialCSVSensorSingleton();
        }
        return instance;
    }

    public synchronized boolean available() {
        //return data.size() > 0;
        return data.size() > 0;
    }

   

   
    @Override
 public void update() {
        processLine(getData());
    }
 
    private void processLine(String inputLine) {
        if (inputLine.length() > 0) {
            //logger.log(Level.INFO, "{0}", inputLine);
            //if (!inputLine.startsWith(">")) {
            String[] arr = inputLine.split(",");
            for (int i = 0; i < arr.length; i++) {
                try {
                    float newf = Float.parseFloat(arr[i]);
                    try {
                        if (data.size() <= i) {
                            data.add(newf);
                        } else {
                            data.set(i, newf);
                        }
                    } catch (IndexOutOfBoundsException e) {
                        logger.warning(e.toString());
                    }
                } catch (NumberFormatException e) {
                    logger.log(Level.WARNING, "{0} <{1}> <{2}>", new Object[]{e.toString(), inputLine, arr[i]});
                }
            }
            // }
        }
    }

    @Override
    public synchronized float getData(int index) throws Exception {
        if (data == null || index > data.size() - 1) {
            throw new Exception("Wrong list size in SerialCSVInterfaceSingleton");
        }

        return data.get(index);
    }

    @Override
    public synchronized void close() {
        if (instance != null) {
            //serialPort.removeEventListener();
            serialPort.close();
            logger.info("Closed serial port");
        }
        instance = null;
    }
}
