/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.moonsit.configurations;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ReStart
 */
public class XML_031_002_ShakingControlIMUTest {

    public XML_031_002_ShakingControlIMUTest() {
    }

    /**
     * Test of main method, of class XML_031_001_ShakingControlIMU.
     * @throws java.lang.Exception
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        try {
            XML_031_002_ShakingControlIMU.main(args);
        } catch (Exception e) {
            fail(e.toString());
        }
    }

}
