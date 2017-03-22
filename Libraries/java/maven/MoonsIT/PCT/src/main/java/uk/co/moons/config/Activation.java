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
package uk.co.moons.config;

//import pct.moons.co.uk.schema.layers.Activation.Functions.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import pct.moons.co.uk.schema.layers.Link;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moons.control.links.BaseSignalLink;
import uk.co.moons.control.links.SignalLink;

/**
 *
 * @author Rupert Young
 */
public class Activation {

    private  BaseSignalLink links = null;
    private List<Integer> functions = null;
    public static final int INPUT = 0;
    public static final int REFERENCE = 1;
    public static final int ERROR = 2;
    public static final int OUTPUT = 3;

    
    
     
     
     
    public Activation(Link link, pct.moons.co.uk.schema.layers.Activation.Functions.List list, HashMap<String, BaseControlFunction> hmControls) throws Exception {
        BaseControlFunction control = hmControls.get(link.getName());
        if (control == null) {
            throw new Exception("ActivationLink " + link.getName() + " is null");
        }

        links = new SignalLink();
        links.addControl(control);
        links.addType(link.getType());

        functions = new ArrayList<> ();
        for(String s:list.getFunctionType()){
            if(s.equalsIgnoreCase("input")){
                functions.add(INPUT);
            }
            if(s.equalsIgnoreCase("reference")){
                functions.add(REFERENCE);
            }
            if(s.equalsIgnoreCase("error")){
                functions.add(ERROR);
            }
            if(s.equalsIgnoreCase("output")){
                functions.add(OUTPUT);
            }
        }
        
    }

    public BaseSignalLink getLinks() {
        return links;
    }

    public List<Integer> getFunctions() {
        return functions;
    }
    
    
    
}
