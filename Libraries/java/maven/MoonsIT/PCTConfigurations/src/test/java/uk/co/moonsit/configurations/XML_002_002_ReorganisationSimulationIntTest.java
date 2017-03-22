/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.moonsit.configurations;

import uk.co.moonsit.configurations.n002.XML_002_002_ReorganisationSimulationInt;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rupert
 */
public class XML_002_002_ReorganisationSimulationIntTest {

    public XML_002_002_ReorganisationSimulationIntTest() {
    }

    /**
     * Test of main method, of class XML_002_002_ReorganisationSimulationInt.
     * @throws java.lang.Exception
     */
    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        try {
            XML_002_002_ReorganisationSimulationInt.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
