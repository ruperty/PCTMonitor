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
package uk.co.moons.control;

import java.util.List;
import uk.co.moons.control.functions.BaseControlFunction;

/**
 *
 * @author Rupert Young
 * Moon's Information Technology
 *
 */
public class ControlFunctionCollection {
    private BaseControlFunction mainFunction;
    private List<BaseControlFunction> transferFunctions;

    public ControlFunctionCollection(){
    }
    
    public BaseControlFunction getMainFunction() {
        return mainFunction;
    }

    public void setMainFunction(BaseControlFunction mainFunction) {
        this.mainFunction = mainFunction;
    }

    public List<BaseControlFunction> getTransferFunctions() {
        return transferFunctions;
    }

    public void setTransferFunctions(List<BaseControlFunction> transferFunctions) {
        this.transferFunctions = transferFunctions;
    }
    
}
