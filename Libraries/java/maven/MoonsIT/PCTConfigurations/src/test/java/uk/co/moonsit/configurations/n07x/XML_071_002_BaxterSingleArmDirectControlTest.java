/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.moonsit.configurations.n07x;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rupert
 */
public class XML_071_002_BaxterSingleArmDirectControlTest {

    public XML_071_002_BaxterSingleArmDirectControlTest() {
    }

    /**
     * Test of main method, of class XML_071_002_BaxterSingleArmDirectControl.
     *
     * @throws java.lang.Exception
     */
    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        try {
            XML_071_002_BaxterSingleArmDirectControl.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
