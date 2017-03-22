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
package uk.co.moons.control.links;

import java.util.List;
import uk.co.moons.control.functions.BaseControlFunction;
        
/**
 *
 * @author Rupert Young
 */

public interface SignalLinkInterface {

    public List<BaseControlFunction> getControlList();
    public void addControl(BaseControlFunction control) throws Exception;

}
