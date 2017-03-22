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

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import java.util.logging.Logger;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import uk.co.moonsit.messages.TerabotMessaging;

public class SerialTerabotSensorSingleton extends SerialSensorSingleton {

    private static final Logger LOG = Logger.getLogger(SerialTerabotSensorSingleton.class.getName());
    private static SerialTerabotSensorSingleton instance = null;

    private List<Float> data;
    private TerabotMessaging terabot;
    private BufferedInputStream input;

    protected SerialTerabotSensorSingleton() {
        // Exists only to defeat instantiation.
    }

    protected SerialTerabotSensorSingleton(String port, int timeOut, int dataRate) throws PortInUseException, UnsupportedCommOperationException, IOException, TooManyListenersException, Exception {

        this.portName = port;
        this.timeOut = timeOut;
        this.dataRate = dataRate;
        long start = System.currentTimeMillis();
        initialize();
        input = new BufferedInputStream(serialPort.getInputStream());
        LOG.log(Level.INFO, "Serial connected to {0} ms {1}", new Object[]{port, System.currentTimeMillis() - start});

        data = new ArrayList<>();
        terabot = new TerabotMessaging();
        // open the streams

    }

    public static SerialTerabotSensorSingleton getInstance(String port, int timeOut, int dataRate) throws PortInUseException, UnsupportedCommOperationException, IOException, TooManyListenersException, Exception {

        if (instance == null) {
            instance = new SerialTerabotSensorSingleton(port, timeOut, dataRate);
        }

        return instance;
    }

    public static SerialTerabotSensorSingleton getInstance() {

        if (instance == null) {
            instance = new SerialTerabotSensorSingleton();
        }
        return instance;
    }

    public synchronized boolean available() {
        //return data.size() > 0;
        return data.size() > 0;
    }

    @Override
    public void update() {
        try {
            byte[] b = new byte[1];
            b[0] = 97;
            System.out.print("Write: ");
            System.out.println(b[0]);
            output.write(b);
            int i = input.read();
            System.out.print("Read: ");
            System.out.println(i);

            /*
            terabot.setQueryStatusMessage();
            terabot.printBytes(terabot.getMessage());
            for (int i = 0; i < 4; i++) {
                terabot.sendData(output);
                LOG.info("sent status " + new String(terabot.getMessage()));
            }
            terabot.getData(input);
             */
            // only for arduino
            /*
            String line = readLine();
            byte[] bs = line.getBytes();
            System.out.print("Line:");
            terabot.printBytes(bs);
            LOG.info("len " + line.length() + " " + line);
             */
        } catch (Exception ex) {
            Logger.getLogger(SerialTerabotSensorSingleton.class.getName()).log(Level.SEVERE, ex.toString(), ex);
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
            LOG.info("Closed serial port");
        }
        instance = null;
    }
}
