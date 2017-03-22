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
package uk.co.moonsit.bluetooth.connection;

/**
 *
 * @author Rupert Young Moon's Information Technology
 *
 */
import com.intel.bluetooth.RemoteDeviceHelper;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

//import bt.BTListener.MyDeviceListenerFilter;
public class MyDiscoveryListener implements DiscoveryListener {

    private static final Object lock = new Object();
    private final ArrayList<RemoteDevice> devices;
    private DiscoveryAgent agent;

    public MyDiscoveryListener() {
        devices = new ArrayList<RemoteDevice>();
    }

    public void deviceInquiry() throws BluetoothStateException {
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        agent = localDevice.getDiscoveryAgent();
        agent.startInquiry(DiscoveryAgent.GIAC, this);

        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            System.out.println(e.toString());
            return;
        }

        System.out.println("Device Inquiry Completed. ");

    }

    public void listServices(String id) throws BluetoothStateException {
        UUID[] uuidSet = new UUID[1];
        //uuidSet[0] = new UUID(0x1105); //OBEX Object Push service
        uuidSet[0] = new UUID(id, false);

        int[] attrIDs = new int[]{
            0x0100 // Service name
        };

        for (RemoteDevice device : this.devices) {
            agent.searchServices(attrIDs, uuidSet, device, this);

            try {
                synchronized (lock) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                System.out.println(e.toString());
                return;
            }

            System.out.println("Service search finished.");
        }

    }

    public static void main(String[] args) {
        try {
            MyDiscoveryListener listener = new MyDiscoveryListener();

            listener.deviceInquiry();
            listener.listServices("0000110100001000800000805F9B34FB");

        } catch (BluetoothStateException ex) {
            Logger.getLogger(MyDiscoveryListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass arg1) {
        String name;
        String bt = "";
        try {
            name = btDevice.getFriendlyName(false);
            bt = btDevice.getBluetoothAddress();
        } catch (IOException e) {
            name = btDevice.getBluetoothAddress();
        }
        devices.add(btDevice);

//if(name.equals("Rupert Young S4")) 
        System.out.println("device found: " + name + " " + bt);

        //int rssi = pollRSSI(btDevice);
        //System.out.println("rssi: " + rssi);

    }

    @Override
    public void inquiryCompleted(int arg0) {
        synchronized (lock) {
            lock.notify();
        }
    }

    @Override
    public void serviceSearchCompleted(int arg0, int arg1) {
        synchronized (lock) {
            lock.notify();
        }
    }

    @Override
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        for (ServiceRecord servRecord1 : servRecord) {
            String url = servRecord1.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
            if (url == null) {
                continue;
            }
            DataElement serviceName = servRecord1.getAttributeValue(0x0100);
            if (serviceName != null) {
                String name = ((String) serviceName.getValue()).trim();

                System.out.println("service [" + name + "] found " + url);

                if (name.equals("MYYAPP")) {
                    sendMessageToDevice(url);
                }
            } else {
                System.out.println("service found " + url);
            }
        }
    }

    @SuppressWarnings("SleepWhileInLoop")
    public int pollRSSI(RemoteDevice device) {
        int rtn = 0;
        try {
            while (true) {

                System.out.println();
                if (device != null) {

                    System.out.println("Device " + device.getFriendlyName(false) + " " + device.getBluetoothAddress());

                    rtn = RemoteDeviceHelper.readRSSI(device);
                    System.out.println("RSSI = " + rtn);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("RSSI = Connection Error");
            //Thread.sleep(1000);
        }
        return rtn;
    }

    private void sendMessageToDevice(String serverURL) {
        try {
            System.out.println("Connecting to " + serverURL);

            //connect to the server and send a line of text
            StreamConnection streamConnection = (StreamConnection) Connector.open(serverURL);

            //send string
            OutputStream outStream = streamConnection.openOutputStream();
            PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outStream));

            int rssi = pollRSSI(devices.get(3));
            pWriter.write("RSSI=" + rssi);
            pWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
