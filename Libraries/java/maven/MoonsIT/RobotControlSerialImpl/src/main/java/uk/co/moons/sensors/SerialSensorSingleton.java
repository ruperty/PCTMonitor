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
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.TooManyListenersException;
import java.util.logging.Level;

public class SerialSensorSingleton implements SerialInterface {

    private static final Logger LOG = Logger.getLogger(SerialSensorSingleton.class.getName());

    protected SerialPort serialPort;
    private BufferedReader input;
    protected InputStream inputStream;
    protected OutputStream output;
    private static SerialSensorSingleton instance = null;
    protected String portName;
    protected int timeOut;
    protected int dataRate;

    protected SerialSensorSingleton() {
        // Exists only to defeat instantiation.
    }

    protected SerialSensorSingleton(String port, int timeOut, int dataRate) throws PortInUseException, UnsupportedCommOperationException, IOException, TooManyListenersException, Exception {
        this.portName = port;
        this.timeOut = timeOut;
        this.dataRate = dataRate;
        long start = System.currentTimeMillis();
        initialize();
        input = new BufferedReader(new InputStreamReader(inputStream));
        LOG.log(Level.INFO, "Serial connected to {0} ms {1}", new Object[]{port, System.currentTimeMillis() - start});
        /*
         logger.info("Waiting for data");
         while (!available()) ;
         logger.info("Data available");
         */
    }

    public static SerialSensorSingleton getInstance(String port, int timeOut, int dataRate) throws PortInUseException, UnsupportedCommOperationException, IOException, TooManyListenersException, Exception {

        if (instance == null) {
            instance = new SerialSensorSingleton(port, timeOut, dataRate);
        }

        return instance;
    }

    public static SerialSensorSingleton getInstance() {

        if (instance == null) {
            instance = new SerialSensorSingleton();
        }
        return instance;
    }

    public final void initialize() throws PortInUseException, UnsupportedCommOperationException, IOException, TooManyListenersException, Exception {

        LOG.info("Start initialize");

        CommPortIdentifier portId = null;
        try {
            portId = CommPortIdentifier.getPortIdentifier(portName);
        } catch (NoSuchPortException e) {
            throw new Exception("Com port not found");
        }
        // open serial port, and use class name for the appName.
        serialPort = (SerialPort) portId.open(this.getClass().getName(), timeOut);

        // set port parameters
        serialPort.setSerialPortParams(dataRate,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);

        // open the streams
        inputStream = serialPort.getInputStream();

        output = serialPort.getOutputStream();

        /*
         // add event listeners
         serialPort.addEventListener(this);
         serialPort.notifyOnDataAvailable(true);
         */
    }

    public void write(float f) throws IOException {
        write((String.valueOf(f) + "\n"));
    }

    public void write(double d) throws IOException {

        write((String.valueOf(d) + "\n"));
    }

    public void write(String s) throws IOException {
        //logger.log(Level.INFO, ":{0}", s);
        String sout = s + "\n";

        output.write(sout.getBytes());

    }

    public synchronized void close() {
        if (instance != null) {
            //serialPort.removeEventListener();
            serialPort.close();
            LOG.info("Closed serial port");
        }
        instance = null;
    }

    @SuppressWarnings("empty-statement")
    protected synchronized String readLine() {
        String inputLine = null;
        boolean wait = true;
        try {
            while (!input.ready());
            while (wait) {
                try {
                    inputLine = input.readLine();
                    wait = false;
                } catch (IOException ex) {
                    LOG.log(Level.WARNING, "Waiting for data ... {0}", ex.toString());
                    //Thread.sleep(1000);
                    //Logger.getLogger(SerialInterfaceSingleton.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception e) {
            LOG.warning(e.toString() + " Data received " + inputLine == null ? "null" : "<" + inputLine + ">");
        }
        return inputLine;
    }

    protected String getData() {
        return readLine();
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float getData(int index) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
