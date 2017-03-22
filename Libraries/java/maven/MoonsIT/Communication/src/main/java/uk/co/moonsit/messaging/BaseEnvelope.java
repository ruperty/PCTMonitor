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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

public abstract class BaseEnvelope implements EnvelopeInterface {

    private static final Logger logger = Logger.getLogger("BaseEnvelope");

    //private static final String TAG = "BaseEnvelope";
    @Override
    @SuppressWarnings("LoggerStringConcat")
    public boolean sendMessage(String data, OutputStream oStream) throws Exception {
        //logger.info( "+++ Sending msg: "+ data);
        BufferedOutputStream bufferedoutputstream;
        try {
            bufferedoutputstream = new BufferedOutputStream(oStream);
            byte[] ed = envelope(data).getBytes();
            //System.out.println("+++ enveloped data*" + new String(ed)+"*");
            bufferedoutputstream.write(ed);

            bufferedoutputstream.flush();
        } catch (IOException e) {
            logger.severe("+++ "+e.toString()+" "+ data);
            throw e;
        } catch (Exception e) {
            logger.severe("+++ "+e.toString()+" "+ data);
            throw e;
        }

        return true;
    }
}
