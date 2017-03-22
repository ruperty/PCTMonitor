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
package uk.co.moonsit.utils.timing;

import java.io.File;
import uk.co.moonsit.utils.timing.Time;

/**
*
* @author
* @version
*/

public interface DateAndTimeInterface  {
    
    public void Now();
    
    public File DateFileName(String name);
    
    public String YMD();
    
    public String HMS();
    
    public Time Time();
}
