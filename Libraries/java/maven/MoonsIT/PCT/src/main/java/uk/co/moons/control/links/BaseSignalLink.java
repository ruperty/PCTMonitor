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

import java.util.ArrayList;
import java.util.List;
import uk.co.moons.control.functions.BaseControlFunction;

/**
 * BaseNeuralFunction
 *
 * @author Rupert Young
 * Copyright Moon's IT 2007-2008
 */
public abstract class BaseSignalLink implements SignalLinkInterface {

    protected List<BaseControlFunction> controls;
    protected double output = 0;
    protected List<String> types;

    public BaseSignalLink() {
        controls = new ArrayList<BaseControlFunction>();
        types = new ArrayList<String>();
    }

    public void addControl(BaseControlFunction control) throws Exception {
        controls.add(control);
        if (control == null) {
            throw new Exception("Link is null" );
        }
    }

    public String getType(int index){
        if(index>types.size()-1)
            return null;

        return types.get(index);
    }
    public void addType(String type) throws Exception {
        //if (type == null) {
          //  return;
        //}
        types.add(type);
    }

    public List<BaseControlFunction> getControlList() {
        return controls;
    }

}
