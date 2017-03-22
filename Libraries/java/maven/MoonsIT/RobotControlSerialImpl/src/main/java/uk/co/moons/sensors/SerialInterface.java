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

import java.io.IOException;

/**
 *
 * @author ReStart
 */
public interface SerialInterface {

    public void update();

    //public String getData() throws IOException;

    public float getData(int index) throws Exception;

    public void close();

}
