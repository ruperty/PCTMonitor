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
public class XML_041_002_LocationControlTestTest {

    public XML_041_002_LocationControlTestTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of main method, of class XML_041_002_LocationControlTest.
     */
    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    public void testMain()  {
        System.out.println("main");
        String[] args = null;
        try {
            XML_041_002_LocationControlTest.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

   

}
