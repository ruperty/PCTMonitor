/*
 * This software is the property of Moon's Information Technology Ltd.
 * All rights reserved.
 * The software is only to be used for development and research purposes.
 * Commercial use is only permitted under license or agreement.
 */
package uk.co.moonsit.configurations;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ReStart
 */
public class XML_025_003_BeaconFollowTest {

    public XML_025_003_BeaconFollowTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of main method, of class XML_025_003_BeaconFollow.
     */
    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        try {
            XML_025_003_BeaconFollow.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test of run method, of class XML_025_003_BeaconFollow.
     */
    /*
     @Test
     public void testRun() {
     System.out.println("run");
     XML_025_003_BeaconFollow instance = new XML_025_003_BeaconFollow();
     Layers expResult = null;
     Layers result = instance.run();
     assertEquals(expResult, result);
     // TODO review the generated test code and remove the default call to fail.
     fail("The test case is a prototype.");
     }
     */
}
