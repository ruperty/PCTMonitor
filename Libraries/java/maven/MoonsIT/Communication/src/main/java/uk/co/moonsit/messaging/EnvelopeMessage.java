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
package uk.co.moonsit.messaging;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnvelopeMessage extends BaseEnvelope {

    private static final Logger logger = Logger.getLogger("EnvelopeMessage");
    //private static final String TAG = "EnvelopeMessage";
    protected byte[] start;
    protected byte[] end;

    /*
     public static void main(String[] args) {    
     byte [] b=new byte[2];    
     b[0]=13;
     b[1]=28;        
     System.out.println(EnvelopeMessage.encodeBytes(b));
     }
     */
    public String encodeBytes(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(encode(bytes[i]));
            sb.append("|");
        }
        return sb.toString();
    }

    public String encode(byte b) {
        return String.format("%03d", (int) b);
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public String receiveMessage(InputStream iStream) throws IOException, Exception {
        String rtn = null;
        //boolean bCharsFound = true;
        int n;

        BufferedInputStream is = null;
        ByteArrayOutputStream bos = null;
        try {
            is = new BufferedInputStream(iStream);
            bos = new ByteArrayOutputStream();

            if (start != null) {
                n = start.length;
                byte bSChars[] = new byte[n];
                for (int i = 0; i < n; i++) {
                    readNextChar(is, bSChars);
                }
                if (isSame(bSChars, start))
					;// System.out.println("Start chars of envelope found");
                else {
                    //logger.log(Level.INFO, "+++ bSChars-->", encodeBytes(bSChars) + "<--");
                    throw new Exception("Socket communication lost");
                }
            }

            if (end != null) {
                n = end.length;
                byte bChars[] = new byte[n];
                boolean foundEnd = false;
                while (!foundEnd) {
                    readNextChar(is, bChars);
                    if (isSame(end, bChars)) {
                        foundEnd = true;
                    } else {
                        addChar(bos, bChars[n - 1]);
                    }
                }
                rtn = bos.toString().substring(0, bos.size() - (end.length - 1));
            } else {

                while (is.available() == 0) {
                    logger.info("+++ Waiting ...");
                    Thread.sleep(2000);
                }

                byte b[] = new byte[1];
                is.read(b);
                n = is.available();
                byte bChars[] = new byte[n];
                is.read(bChars);
                rtn = new String(b) + new String(bChars);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "+++ {0}", e.toString());
            throw e;
        } catch (Exception e) {
            logger.log(Level.WARNING, "+++ {0}", e.getMessage());
            throw e;
        } finally {
            if (bos != null) {
                bos.close();
            }
        }

        return rtn;
    }

    @Override
    public String envelope(String msg) throws Exception {
        return new String(start) + msg + new String(end);
    }

    private void readNextChar(BufferedInputStream is, byte[] bChars) throws Exception {
        for (int j = 0; j < bChars.length - 1; j++) {
            bChars[j] = bChars[j + 1];
        }
        try {
            bChars[bChars.length - 1] = (byte) is.read();
        } catch (IOException ex) {
            logger.severe("+++ data read:" + ex.toString());
            throw ex;
        } catch (Exception ex) {
            logger.severe("+++ connection:" + ex.toString());
            throw ex;
        }
    }

    private boolean isSame(byte[] one, byte[] two) {
        /*
         if (one.length > 0 && two.length > 0) {
         logger.log(Level.INFO, "+++ one:{0} two:{1}", new Object[]{new String(one), new String(two)});
         } else {
         logger.log(Level.INFO, "+++ isSame: sizes don't match");
         }*/

        for (int j = 0; j < one.length; j++) {
            if (one[j] != two[j]) {
                return false;
            }
        }
        return true;
    }

    private void addChar(ByteArrayOutputStream bos, byte c) throws IOException {
        byte[] b = new byte[1];

        b[0] = c;

        bos.write(c);
    }
}
