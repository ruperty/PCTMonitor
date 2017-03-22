/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.moons.config;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ReStart
 */
public class ODGConfig005Test {
    
    public ODGConfig005Test() {
    }

   

    /**
     * Test of main method, of class ODGConfig.
     */
    @Test
    public void testMain()  {
        String[] args = {"C:\\Versioning\\PCTSoftware\\Controllers\\Models\\PRGUI\\", "GUITest005"};
        System.out.println(args[0] + args[1]);
        try {
            ODGConfig.main(args);
        } catch (Exception ex) {
            System.out.println(ex.getCause());
            fail(ex.toString());
        }
    }
    
}
