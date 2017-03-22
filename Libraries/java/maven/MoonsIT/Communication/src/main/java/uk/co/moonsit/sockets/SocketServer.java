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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moonsit.messaging.BeginEndEnvelope;

/**
 *
 * @author ReStart
 */
public class SocketServer {

    static final Logger LOG = Logger.getLogger(SocketServer.class.getName());

    private ServerSocket serverSocket;
    private Socket server;
    private DataOutputStream out;
    private DataInputStream in;
    private final BeginEndEnvelope envelope;
    private final int port, listenTimeout, readTimeout;

    public SocketServer(int port, int listenTimeout, int readTimeout) throws IOException {
        this.port = port;
        this.listenTimeout = listenTimeout;
        this.readTimeout = readTimeout;
        envelope = new BeginEndEnvelope("11", "28,13");
    }

    private void init() throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(listenTimeout);
    }

    @SuppressWarnings("LoggerStringConcat")
    public void connect() throws IOException {
        if (serverSocket == null || serverSocket.isClosed()) {
            init();
        }
        LOG.log(Level.INFO, "Wait on " + serverSocket.getLocalPort());//+" on "+InetAddress.getLocalHost()+"...");
        server = serverSocket.accept();
        server.setKeepAlive(true);
        server.setSoTimeout(readTimeout);
        serverSocket.close();

        LOG.log(Level.INFO, "Connected to {0}", server.getRemoteSocketAddress());
        out = new DataOutputStream(server.getOutputStream());
        in = new DataInputStream(server.getInputStream());
    }

    public void closeStreams() throws IOException {
        if (out != null) {
            out.close();
        }
        if (in != null) {
            in.close();
        }
        if (server != null) {
            server.close();
        }
    }

    public void close() throws IOException {
        if (out != null) {
            out.close();
        }
        if (in != null) {
            in.close();
        }
        if (server != null) {
            server.close();
        }
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    public int getPort() {
        int port = 0;
        if (serverSocket != null) {
            port = serverSocket.getLocalPort();
        }
        return port;
    }

    public boolean isConnected() {
        if (server == null) {
            return false;
        }
        return server.isConnected();
    }

    public boolean isClosed() {
        return server.isClosed();
    }

    public String socketStatus() {
        //if (server != null) {
        //  logger.log(Level.INFO, "bound {0} closed {1} connected {2}", new Object[]{server.isBound(), server.isClosed(), server.isConnected()});
        //}
        return server == null ? "null" : "not null";
    }

    /*
     public void connectionLost() throws IOException {
     close();
     server = null;
     }*/
    public int available() throws IOException {
        return in.available();
    }

    public int readInt() throws IOException {
        return in.readInt();
    }

    public String readUTF() throws IOException {
        return in.readUTF();
    }

    public String readLine() throws IOException {
        BufferedReader bin = new BufferedReader(new InputStreamReader(server.getInputStream()));
        return bin.readLine();
    }

    public int read() throws IOException {
        return in.read();
    }

    public float readFloat() throws IOException {
        return in.readFloat();
    }

    public double readDouble() throws IOException {
        return in.readDouble();
    }

    public void writeInt(int size) throws IOException {
        out.writeInt(size);
    }

    public void writeUTF(String s) throws IOException {
        out.writeUTF(s);
    }

    public void writeFloat(float f) throws IOException {
        out.writeFloat(f);
    }

    public void writeDouble(double d) throws IOException {
        out.writeDouble(d);
    }

    public void writeLine(String s) throws IOException {
        String s1 = s + "\n";

        out.write(s1.getBytes(), 0, s1.length());
    }

    public void sendEnvelopedMessage(String msg) throws IOException, Exception {
        envelope.sendMessage(msg, out);
    }

    public void sendString(String msg) throws IOException {
        out.write(msg.getBytes());
    }

    public String sendAndReceive(String message) throws IOException {

        out.writeUTF(message);
        String response = in.readUTF();
        //logger.info("+++ "+response);
        return response;
    }

    public String receiveEnvelopedMessage() throws IOException, Exception {
        return envelope.receiveMessage(in);
    }

    @SuppressWarnings("null")
    public static void main(String[] args) {
        //int port = Integer.parseInt(args[0]);
        int port = 30000;
        int timeout = 10000;
        int rtimeout = 10000;
        SocketServer ss = null;
        int mode = 2;

        try {
            ss = new SocketServer(port, timeout, rtimeout);
        } catch (IOException ex) {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            ss.connect();
        } catch (java.net.SocketTimeoutException ex) {
            Logger.getLogger(ex.toString());
        } catch (IOException ex) {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (ss.isConnected()) {

            switch (mode) {
                case 0:

                    try {
                        String response;
                        response = ss.sendAndReceive("1");
                        System.out.println("Response  " + response);
                        response = ss.sendAndReceive("2");
                        System.out.println("Response  " + response);
                        response = ss.sendAndReceive("3");
                        System.out.println("Response  " + response);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;

                case 1:
                    for (int i = 0; i <= 2; i++) {
                        try {
                            String message = ss.readLine();
                            LOG.info("Rcvd:" + message);
                            String response = "Ok";
                            ss.writeLine(response);
                            LOG.info("Sent:" + response);
                        } catch (IOException ex) {
                            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;

                case 2:
                    try {
                        String message = ss.readLine();
                        LOG.info("Rcvd:" + message);
                        String response = " {\"type\":\"ENTERED_LEVEL\",\"level\":1,\"expectedAtomEnergy\":1,\"targetAreaStart\":0.0,\"targetAreaStop\":0.75,\"x\":-0.25,\"xMin\":-0.75,\"xMax\":0.75,\"y\":-400.0,\"yMin\":-600.0,\"yMax\":0.0,\"simulatedTime\":0.0,\"expectedAtomPosition\":-0.24999999761338147,\"fidelity\":0.018812260960665109}";
                        ss.writeLine(response);
                        LOG.info("Sent:" + response);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    for (int i = 0; i <= 1000; i++) {
                        try {
                            String message = ss.readLine();
                            LOG.info("Rcvd:" + message);
                            String response = "{\"type\":\"MOVED_CURSOR_TO_POINT\",\"level\":0,\"expectedAtomEnergy\":1,\"targetAreaStart\":0.0,\"targetAreaStop\":0.0,\"x\":0.0,\"xMin\":0.0,\"xMax\":0.0,\"y\":0.0,\"yMin\":0.0,\"yMax\":0.0,\"simulatedTime\":0.020000000000000005,\"expectedAtomPosition\":-0.11582906481374585,\"fidelity\":0.1546016681591781} ";
                            ss.writeLine(response);
                            LOG.info("Sent:" + response);
                            Thread.sleep(5000);
                        } catch (IOException ex) {
                            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InterruptedException ex) {
                    Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                    }
                    break;

            }
            try {
                ss.close();
            } catch (IOException ex) {
                Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
