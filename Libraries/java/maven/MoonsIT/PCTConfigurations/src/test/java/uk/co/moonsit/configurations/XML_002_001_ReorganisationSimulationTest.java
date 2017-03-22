/*
 * This software is the property of Moon's Information Technology Ltd.
 * All rights reserved.
 * The software is only to be used for development and research purposes.
 * Commercial use is only permitted under license or agreement.
 */
package uk.co.moonsit.configurations;

import uk.co.moonsit.configurations.n002.XML_002_001_ReorganisationSimulationProp;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ReStart
 */
public class XML_002_001_ReorganisationSimulationTest {

    public XML_002_001_ReorganisationSimulationTest() {
    }

    /**
     * Test of main method, of class XML_002_001_ReorganisationSimulation.
     */
    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        try {
            XML_002_001_ReorganisationSimulationProp.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
