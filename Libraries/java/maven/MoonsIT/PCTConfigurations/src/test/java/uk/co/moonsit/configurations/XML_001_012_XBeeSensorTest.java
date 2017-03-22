/*
 * This software is the property of Moon's Information Technology Ltd.
 * All rights reserved.
 * The software is only to be used for development and research purposes.
 * Commercial use is only permitted under license or agreement.
 */
package uk.co.moonsit.configurations;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ReStart
 */
public class XML_001_012_XBeeSensorTest {

    public XML_001_012_XBeeSensorTest() {
    }

  

    /**
     * Test of main method, of class XML_001_012_XBeeSensor.
     */
    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    public void testMain()  {
        System.out.println("main");
        String[] args = null;
        try {
            XML_001_012_XBeeSensor.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
