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
package uk.co.moonsit.sockets;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moonsit.messaging.BeginEndEnvelope;

/**
 *
 * @author Rupert Young
 */
public class SocketClient {

    static final Logger LOG = Logger.getLogger(SocketClient.class.getName());

    private final String server;
    private final int port;
    private final int timeout;
    private Socket client;
    protected DataInputStream in;
    protected DataOutputStream out;
    private final BeginEndEnvelope envelope;
    private boolean finished = false;

    public SocketClient(String server, int port, int timeout) {
        this.timeout = timeout;
        this.server = server;
        this.port = port;
        envelope = new BeginEndEnvelope("11", "28,13");
    }

    @SuppressWarnings("LoggerStringConcat")
    public void connect() throws IOException {
        LOG.log(Level.INFO, "Connecting to " + server + " on port " + port);
        //client = new Socket(server, port);
        client = new Socket();
        client.connect(new InetSocketAddress(server, port), timeout);
        client.setKeepAlive(true);
        //client.setSoTimeout(5000);
        LOG.log(Level.INFO, "Just connected to " + client.getRemoteSocketAddress());

        in = new DataInputStream(client.getInputStream());
        out = new DataOutputStream(client.getOutputStream());

        LOG.info(client.toString() );
    }

    @SuppressWarnings("SleepWhileInLoop")
    public boolean makeConnection() throws ConnectException {
        boolean rtn = true;

        if (finished) {
            LOG.log(Level.INFO, "Connection finished ");
            rtn = false;
        } else {
            while (!isConnected() || isClosed()) {
                try {
                    connect();
                    rtn = true;
                } catch (IOException e) {
                    LOG.log(Level.INFO, "Connection failed {0}", e.toString());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MessageClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    rtn = false;
                }
                LOG.log(Level.INFO, "Finished {0}", finished);
                if (finished) {
                    break;
                }
            }
        }
        return rtn;
    }

    public String receiveAndSend(String response) throws IOException, EOFException {
        String message = in.readUTF();
        out.writeUTF(response);
        return message;
    }

    public void writeInt(int i) throws IOException {
        out.writeInt(i);
    }

    public void writeUTF(String s) throws IOException {
        out.writeUTF(s);
    }

    public void writeLine(String s) throws IOException {
        String s1 = s + "\n";
        
        out.write(s1.getBytes(), 0, s1.length());
    }

    public void write(byte b) throws IOException {
        out.write(b);
    }

    public void writeFloat(float f) throws IOException {
        out.writeFloat(f);
    }

    public void writeDouble(double d) throws IOException {
        out.writeDouble(d);
    }

    public float readFloat() throws IOException {
        return in.readFloat();
    }

    public void flush() throws IOException {
        out.flush();
    }

    public double readDouble() throws IOException {
        return in.readDouble();
    }

    public int readInt() throws IOException {
        return in.readInt();
    }

    public String readUTF() throws IOException {
        return in.readUTF();
    }

    public String readLine() throws IOException {
        BufferedReader bin = new BufferedReader(new InputStreamReader(client.getInputStream()));
        return bin.readLine();
    }

    public void sendData(double[] data) throws IOException {
        for (double d : data) {
            writeDouble(d);
        }
    }

    public void sendData(String data) throws IOException {
        writeLine(data);
    }

    @SuppressWarnings("LoggerStringConcat")
    public String receiveData() throws IOException {
        return readLine();
    }

    public String receiveEnvelopedMessage() throws IOException, Exception {
        return envelope.receiveMessage(in);
    }

    public String receiveSizedMessage() throws IOException {
        int size = in.readInt();
        LOG.log(Level.INFO, "message size {0}", size);
        byte[] bs = new byte[size];
        in.read(bs);
        //logger.info("Msg:"+new String(bs));
        return new String(bs);
    }

    public void sendEnvelopedMessage(String msg) throws IOException, Exception {
        envelope.sendMessage(msg, out);
    }

    public boolean isConnected() {
        if (client == null) {
            return false;
        }
        return client.isConnected();
    }

    public boolean isClosed() {
        if (client == null) {
            return false;
        }
        return client.isClosed();
    }

    @SuppressWarnings("LoggerStringConcat")
    public void close() throws IOException {
        if (client == null) {
            LOG.info("client null");
        } else {
            LOG.info("client not null");
        }
        if (client != null) {
            LOG.log(Level.INFO, port + " isClosed " + client.isClosed());
            client.close();
            LOG.log(Level.INFO, "Closed " + port + " isConnected  " + client.isConnected() + " isClosed " + client.isClosed());
            client = null;
            if (in != null) {
                in.close();
                if (out != null) {
                    out.close();
                    LOG.info("Closed streams");
                }
            }
        }
    }

    public boolean isFinshed() {
        return finished;
    }

    public void setFinished(boolean finshed) {
        this.finished = finshed;
    }

    public static void main(String[] args) throws Exception {
        String serverName = args[0];
        int port = Integer.parseInt(args[1]);
        int mode = 1;
        SocketClient sc = new SocketClient(serverName, port, 5000);
        String message = "SpeedWeight_Constant:0";
        try {
            sc.makeConnection();

            switch (mode) {

                case 0: {
                    for (int i = 0; i < 2; i++) {
                        //message = sc.receiveAndSend("Ok");
                        sc.sendEnvelopedMessage(message);
                        System.out.println(message);
                    }
                    break;
                }
                case 1: {
                    for (int i = 0; i <= 2; i++) {
                        message = sc.receiveAndSend("Ok");
                        System.out.println(message);
                    }

                    break;
                }
            }
            Thread.sleep(1000);
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
