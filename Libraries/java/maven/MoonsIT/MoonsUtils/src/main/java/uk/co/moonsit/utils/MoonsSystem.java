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
package uk.co.moonsit.utils;

import java.io.File;

/**
 *
 * @author ReStart
 */
public class MoonsSystem {
    
    
    public static String getPrefix() {
        String prefix;
        String os = System.getProperty("os.name");
        String home = System.getProperty("user.home");

        /*Properties props = System.getProperties();
        for (Object key : props.keySet()) {
            String skey = (String) key;
            String value = props.getProperty(skey);
            LOG.log(Level.INFO, "P: {0} {1}", new Object[]{skey, value});
        }*/
        if (os.equalsIgnoreCase("linux")) {
            String s = File.separator;
            prefix = home + s;
        } else {
            prefix = "C:" + File.separator;
        }
        return prefix;
    }
}
