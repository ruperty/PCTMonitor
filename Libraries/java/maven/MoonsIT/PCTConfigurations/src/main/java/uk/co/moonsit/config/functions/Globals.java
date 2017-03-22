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
package uk.co.moonsit.config.functions;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rupert Young
 */
public class Globals extends HashMap<String, String> {
    
    private static final Logger LOG = Logger
            .getLogger(Globals.class.getName());
    
    private static Globals instance;
    
    private static HashMap<String, Boolean> check;    
    
    protected Globals() {
    }
    
    public static Globals getInstance() {
        
        if (instance == null) {
            instance = new Globals();
            check = new HashMap<String, Boolean>();
        }
        
        return instance;
    }
    
    public void verify() {
        for (Entry entry : check.entrySet()) {
            if (!(Boolean) entry.getValue()) {
                LOG.log(Level.WARNING, "Value {0} not used ", entry.getKey());
            }
        }
    }
    
    @Override
    public String put(String key, String value) {
        check.put(key, Boolean.FALSE);
        return super.put(key, value); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String get(String key) throws Exception {
        String str = super.get(key);        
        check.replace(key, Boolean.TRUE);        
        if (str == null) {
            //throw new Exception("No lookup found for key "+ key);
            LOG.info("No lookup found for key " + key);
        }
        
        return str;
    }
    
    public String getAssert(String key) throws Exception {
        String str = super.get(key);
        check.replace(key, Boolean.TRUE);
        if (str == null) {
            throw new Exception("No lookup found for key " + key);
            //LOG.info("No lookup found for key "+ key);
        }
        
        return str;
    }
    
}
