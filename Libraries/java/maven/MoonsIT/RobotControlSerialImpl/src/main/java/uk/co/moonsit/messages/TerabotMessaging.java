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
package uk.co.moonsit.messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moons.sensors.SerialTerabotSensorSingleton;

/**
 *
 * @author ReStart
 */
public class TerabotMessaging {

    private static final Logger LOG = Logger.getLogger(TerabotMessaging.class.getName());
    private byte[] message;
    private final byte header = 0x5A;//'a';

    public void printBytes(byte[] buf) {
        for (int i = 0; i < buf.length; i++) {
            int a = buf[i];
            System.out.print(a);
            System.out.print(" ");
        }
        System.out.println();

    }

    public void printBytes(char[] buf) {
        for (int i = 0; i < buf.length; i++) {
            int a = buf[i];
            System.out.print(a);
            System.out.print(" ");
        }
        System.out.println();

    }

    public void printBytes(CharBuffer buf) {
        for (int i = 0; i < buf.length(); i++) {
            int a = buf.get(i);
            System.out.print(a);
            System.out.print(" ");
        }
        System.out.println();

    }

    public void setQueryStatusMessage() {
        char[] checksumData = new char[2];
        checksumData[0] = 0;
        checksumData[1] = 0xA0;

        message = new byte[4];
        message[0] = header;
        message[1] = 0;
        message[2] = (byte) 0xA0;
        message[3] = (byte) 0xA0;//(byte) calculateChecksum(checksumData, checksumData.length);
        //message[4] = 'd';
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public void sendData(OutputStream output) throws IOException {
        output.write(message);
    }

    public String getData(BufferedReader input) throws Exception {
        StringBuilder sb = new StringBuilder();

        try {
            while(!input.ready());
            int hdr = input.read();
            while (hdr != 0x5B) {
                hdr = input.read();
            }
            LOG.log(Level.INFO, "header {0} {1} ", new Object[]{hdr, Integer.toBinaryString(hdr)});
            sb.append(hdr);

            int size = input.read();
            LOG.log(Level.INFO, "size {0}", size);
            char[] checksumData = new char[size + 2];
            checksumData[0] = ((char) size);
            sb.append(size);

            char[] id = new char[1];
            input.read(id);
            checksumData[1] = id[0];
            //int id = input.read();
            //checksum.Data[1] = ((char) id);
            LOG.log(Level.INFO, "id {0} {1}", new Object[]{id[0], Integer.toBinaryString(id[0])});
            sb.append(id);

            CharBuffer body = CharBuffer.allocate(size);
            int nchars = input.read(body.array());
            LOG.log(Level.INFO, "bytes read {0}", nchars);
            //checksumData.put(body.array(), 0,nchars);
            for (int i = 2; i < (2 + size); i++) {
                checksumData[i] = body.get(i - 2);
            }

            System.out.print("Body:");
            printBytes(body);
            //byte[] bytes = { }
            //float f = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();

            //System.out.println(Arrays.toString(body.array()));
            System.out.print("Checksum:");
            printBytes(checksumData);
            //System.out.println(Arrays.toString(checksumData.array()));

            char[] checksum = new char[1];
            input.read(checksum);
            char calcChecksum = calculateChecksum(checksumData, size);
            LOG.log(Level.INFO, "checksums {0} {1}", new Object[]{checksum[0], calcChecksum});
            LOG.log(Level.INFO, "checksums {0} {1}", new Object[]{(int) (checksum[0]), (int) calcChecksum});

            sb.append(body);
            sb.append(checksum);

            LOG.log(Level.INFO, "--> {0}", sb.toString());
        } catch (IOException ex) {
            Logger.getLogger(SerialTerabotSensorSingleton.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sb.toString();
    }

    private char calculateChecksum(char[] data, int iSize) {
        int i;
        int checksum = 0;

        for (i = 0; i < iSize; i++) {
            char c = data[i];
            checksum = checksum ^ (data[i]); // XOR

        }
        return (char) checksum;
    }

}
