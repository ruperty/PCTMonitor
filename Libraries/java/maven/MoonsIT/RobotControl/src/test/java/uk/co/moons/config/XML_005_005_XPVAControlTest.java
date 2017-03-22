/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.moons.config;

import org.junit.Test;
import static org.junit.Assert.*;
import uk.co.moons.config.ODGConfig;

/**
 *
 * @author ReStart
 */
public class XML_005_005_XPVAControlTest {
    
    public XML_005_005_XPVAControlTest() {
    }

    /**
     * Test of main method, of class TestMain.
     */
    @Test
    public void testMain() throws Exception {
        String[] args = {"C:\\Versioning\\PCTSoftware\\Controllers\\Models\\QuantumMoves\\", "005-005-XPVAControl"};
        System.out.println(args[0]+args[1]);


        try {
            ODGConfig.main(args);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.toString());
            System.out.println(ex.getCause());
            fail(ex.toString());
        }
    }
    
}
