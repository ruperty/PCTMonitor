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
public class XML_003_005_AccelerometerControlTest {

    public XML_003_005_AccelerometerControlTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of main method, of class XML_003_005_AccelerometerControl.
     */
    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    public void testMain()  {
        System.out.println("main");
        String[] args = null;
        try {
            XML_003_005_AccelerometerControl.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
