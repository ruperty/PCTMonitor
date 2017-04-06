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
 * @author Rupert Young
 */
public class ODGConfig011_OuputLinkPositionTest {

    public ODGConfig011_OuputLinkPositionTest() {
    }

    /**
     * Test of main method, of class ODGConfig.
     */
    @Test
    public void testMain() {
        String[] args = {"..\\..\\..\\..\\..\\Controllers\\Models\\PRGUI\\", "GUITest011_OuputLinkPosition"};
        System.out.println(args[0] + args[1]);
        try {
            ODGConfig.main(args);
            fail("Config should fail");
        } catch (Exception ex) {
            System.out.println(ex.toString());

            if (ex.toString().equals("java.lang.Exception: Function IntegrateY does not appear in XML configuration, ensure it is less than 3cm from is link")) {
                System.out.println("Correct exception");
            } else {
                fail(ex.toString());
            }
        }
    }

}
