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
 * @author Rupert Young
 */
public class FrequencyMeasure {

    static final Logger logger = Logger.getLogger(FrequencyMeasure.class.getName());

    private int ctr = 1;
    private final int samples;
    private long previousTime;
    private float frequency;

    public FrequencyMeasure(int s) {
        samples = s;
    }

    public void init() {
        if (ctr == 1) {
            previousTime = System.currentTimeMillis();
        }
    }

    public void mark() {
        if (ctr % samples == 0) {
            long now = System.currentTimeMillis();
            long diff = now - previousTime;
            previousTime = now;
            frequency = diff /(float) samples;
            logger.info("Frequency = " + frequency);
        }
        ctr++;
    }

    public int getCtr() {
        return ctr;
    }

    public float getFrequency() {
        return frequency;
    }
    
    
}
