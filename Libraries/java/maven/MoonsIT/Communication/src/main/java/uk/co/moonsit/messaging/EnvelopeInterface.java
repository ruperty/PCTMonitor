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

import java.io.InputStream;
import java.io.OutputStream;

public interface EnvelopeInterface {

    public String envelope(String msg) throws Exception;

    public boolean sendMessage(String data, OutputStream oStream)
            throws Exception;

    public String receiveMessage(InputStream iStream) throws Exception;

}
