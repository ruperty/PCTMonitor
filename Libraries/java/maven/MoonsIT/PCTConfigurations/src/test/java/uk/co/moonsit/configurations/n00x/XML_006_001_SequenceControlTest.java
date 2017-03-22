/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.moonsit.configurations.n00x;

import org.junit.Test;
import static org.junit.Assert.*;
import pct.moons.co.uk.schema.layers.Layers;

/**
 *
 * @author ReStart
 */
public class XML_006_001_SequenceControlTest {

    public XML_006_001_SequenceControlTest() {
    }

    /**
     * Test of main method, of class XML_006_001_SequenceControl.
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        try {
            String[] args = null;
            XML_006_001_SequenceControl.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
