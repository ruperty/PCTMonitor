/*
 * 
 * This software is the property of Rupert Young, rupert@moonsit.co.uk.
 * 
 * All rights reserved.
 * 
 * 
 * Use of the software is only permitted under license or agreement.
 * 
 * 
 */
package uk.co.moonsit.configurations;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rupert
 */
public class XML_041_003_LocationControlTest {

    public XML_041_003_LocationControlTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class XML_041_003_LocationControl.
     * @throws java.lang.Exception
     */
    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        try {
            XML_041_003_LocationControl.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
