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
package uk.co.moons.control.neural;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moonsit.bluetooth.server.BluetoothExchangeServer;
import uk.co.moonsit.sensors.SensorData;
import uk.co.moonsit.sensors.Sensors;

/**
 *
 * @author ReStart
 */
public class BluetoothPhoneSensorNeuralFunction extends NeuralFunction {

    private static final Logger logger = Logger.getLogger(BluetoothPhoneSensorNeuralFunction.class.getName());
    private Sensors sensorState;
    private BlockingQueue<String> queue;
    public String service = null;
    public String uuid = null;
    public String axis = null;
    private BluetoothExchangeServer bluetoothServer = null;
    private String type=null;
    public Double factor=1.0;

    public BluetoothPhoneSensorNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        queue = new SynchronousQueue<String>();
        sensorState = new Sensors();
        //"sensorService",  "0000110100001000800000805F9B34FB" , "11", "28,13", null);

        for (Parameters param : ps) {
            if (param.getName().equals("Type")) {
                type = param.getValue();
            }
            if (param.getName().equals("Service")) {
                service = param.getValue();
            }
            if (param.getName().equals("Uuid")) {
                uuid = param.getValue();
            }
            if (param.getName().equals("Axis")) {
                axis = param.getValue();
            }
            if (param.getName().equals("Factor")) {
                factor = Double.parseDouble(param.getValue());
            }
        }
        if (uuid == null) {
            throw new Exception("Uuid null for BluetoothPhoneSensorNeuralFunction");
        }
        if (service == null) {
            throw new Exception("Service null for BluetoothPhoneSensorNeuralFunction");
        }
        if (type == null) {
            throw new Exception("Type null for BluetoothPhoneSensorNeuralFunction");
        }

        bluetoothServer = new BluetoothExchangeServer(service, uuid, "11", "28,13", queue, sensorState);

    }

    @Override
    public void init() {
        sensorState.setLinAccThreadFlag(true);
        Thread bts = new Thread(bluetoothServer);
        bts.start();
    }

    public void close() {
     if(sensorState!=null)    sensorState.setLinAccThreadFlag(false);
        //bluetoothServer.setFlag(Boolean.FALSE);
    }

    @Override
    public double compute() throws Exception {
        //logger.info("+++ compute");
        try {
            //logger.info("+++ about to dequeue");
            String data = queue.take();
            //logger.info("+++ data dequeued " + data);
            output = getAxis(data);

        } catch (InterruptedException ex) {
            Logger.getLogger(BluetoothPhoneSensorNeuralFunction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }

    private double getAxis(String data) throws Exception {
        double rtn = 0.0;
        String sarr[] = data.split("\\|");

        if (Integer.parseInt(sarr[0]) != SensorData.getType(type)) {
            throw new Exception("Data is not "+type);
        }

        if (axis.equalsIgnoreCase("X")) {
            rtn = Double.parseDouble(sarr[1]);
        }
        if (axis.equalsIgnoreCase("Y")) {
            rtn = Double.parseDouble(sarr[2]);
        }
        if (axis.equalsIgnoreCase("Z")) {
            rtn = Double.parseDouble(sarr[3]);
        }

        if (sarr[4] != null && sarr[4].length() > 0) {
            logger.info("+++ DELAY: " + (System.currentTimeMillis() - Long.parseLong(sarr[4])));
        }

        return factor*rtn;
    }
}
