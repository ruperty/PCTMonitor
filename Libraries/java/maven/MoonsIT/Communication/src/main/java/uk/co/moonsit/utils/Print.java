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

import java.util.logging.Logger;

/**
 *
 * @author ReStart
 */
public class Print {

    private static final Logger logger = Logger.getLogger(Print.class.getName());

    public static void logArray(String name, double[] data) {
        StringBuilder sb = new StringBuilder(name + ": ");
        for (int i = 0; i < data.length; i++) {
            sb.append(data[i]).append(" ");
        }
        logger.info(sb.toString());
    }

    public static void logArray(String name, float[] data) {
        StringBuilder sb = new StringBuilder(name + ": ");
        for (int i = 0; i < data.length; i++) {
            sb.append(data[i]).append(" ");
        }
        logger.info(sb.toString());
    }
}
