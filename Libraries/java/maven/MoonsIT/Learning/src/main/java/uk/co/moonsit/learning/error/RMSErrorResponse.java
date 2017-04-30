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
package uk.co.moonsit.learning.error;

/**
 *
 * @author Rupert
 */
public class RMSErrorResponse {

    public final static int PERIOD = 0;
    public final static int COUNTER = 1;
    private double errorSum;
    private Double limit = null;
    private Double scale = 1.0;

    public RMSErrorResponse(Double scale) {
        this.scale=scale;
        init();
    }

    public RMSErrorResponse(Double l, int period) {
        if (l != null) {
            setLimit(l, period);
        }
        init();
    }

    public final void setLimit(Double l, int period) {
        if (l == 0) {
            limit = null;
        } else {
            limit = l * period;
        }
    }

    private void init() {
        reset();
    }

    public void update(double error) {
        if (Math.abs(error) != Double.POSITIVE_INFINITY) {
            errorSum += error * error;
        }
        if (limit != null) {
            if (errorSum > limit) {
                errorSum = limit;
            }
        }
    }

    public double getErrorResponse(int period) {
        double response;

        response = scale* Math.sqrt(errorSum / period);

        reset();
        return response;
    }

    public void reset() {
        errorSum = 0;
    }

}
