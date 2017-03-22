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

import java.util.logging.Logger;

public class BeginEndEnvelope extends EnvelopeMessage {

    private static final Logger logger = Logger.getLogger("BeginEndEnvelope");

    //private static final String TAG = "BeginEndEnvelope";
    public BeginEndEnvelope(String stStart, String stEnd) {
        
        String[] sarr = stStart.split(",");
        start = new byte[sarr.length];
        for (int i = 0; i < sarr.length; i++) {
            start[i] = Byte.valueOf(sarr[i]);
        }
        
        String[] earr = stEnd.split(",");
        end = new byte[earr.length];
        for (int i = 0; i < earr.length; i++) {
            end[i] = Byte.valueOf(earr[i]);
        }
        
    }
}
