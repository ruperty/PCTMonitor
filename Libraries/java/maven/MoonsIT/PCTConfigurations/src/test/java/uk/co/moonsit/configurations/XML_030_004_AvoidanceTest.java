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
public class XML_030_004_AvoidanceTest {

    public XML_030_004_AvoidanceTest() {
    }

    /**
     * Test of main method, of class XML_030_004_Avoidance.
     *
     * @throws java.lang.Exception
     */
    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        try {
            XML_030_004_Avoidance.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
