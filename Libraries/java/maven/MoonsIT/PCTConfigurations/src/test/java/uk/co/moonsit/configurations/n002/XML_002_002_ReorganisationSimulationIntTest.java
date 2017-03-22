/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.moonsit.configurations.n002;

import org.junit.Test;
import static org.junit.Assert.*;
import pct.moons.co.uk.schema.layers.Layers;

/**
 *
 * @author ReStart
 */
public class XML_002_002_ReorganisationSimulationIntTest {

    public XML_002_002_ReorganisationSimulationIntTest() {
    }

    /**
     * Test of main method, of class XML_002_002_ReorganisationSimulationInt.
     */
    @Test
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
