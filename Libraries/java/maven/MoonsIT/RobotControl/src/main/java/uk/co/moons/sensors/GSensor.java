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
package uk.co.moons.sensors;

import java.util.logging.Logger;
import uk.co.moons.sensors.impl.HiTechnicGyroSensorImpl;
import uk.co.moons.sensors.states.BaseSensorState;

/**
 *
 * @author Rupert Young
 */
public class GSensor extends BaseSensor {

    static final Logger LOG = Logger.getLogger(GSensor.class.getName());

    private int offset = 0;

    //private final HiTechnicGyroSensorImpl sensorImpl;

    /**
     * Creates a new instance of USensor
     *
     * @param sp
     * @param st
     * @param o
     * @param calibrate
     * @param raw
     * @param sign
     *
     */
    public GSensor(String sp, BaseSensorState st, int o, boolean calibrate, boolean raw, int sign) {
        offset = o;
        state = st;
        sensorImpl = new HiTechnicGyroSensorImpl(sp, raw, sign);
        if (calibrate) {
            warningBeeps();
            ((HiTechnicGyroSensorImpl)sensorImpl).getGyroOffset();
            warningBeeps();
        } else {
            ((HiTechnicGyroSensorImpl)sensorImpl).setOffset(offset);
        }
    }

    private void warningBeeps() {
        ((HiTechnicGyroSensorImpl)sensorImpl).warningBeeps();
    }

    public void setOffset(int offset) {
        this.offset = offset;
        ((HiTechnicGyroSensorImpl)sensorImpl).setOffset(offset);
    }

    public boolean isRaw() {
        return ((HiTechnicGyroSensorImpl)sensorImpl).isRaw();
    }

    public void setRaw(boolean raw) {
        ((HiTechnicGyroSensorImpl)sensorImpl).setRaw(raw);
    }

    

    
}
