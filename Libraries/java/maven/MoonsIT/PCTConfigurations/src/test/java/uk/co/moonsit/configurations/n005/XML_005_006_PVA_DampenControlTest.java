/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.moonsit.configurations.n005;

import org.junit.Test;
import static org.junit.Assert.*;
import pct.moons.co.uk.schema.layers.Layers;

/**
 *
 * @author ReStart
 */
public class XML_005_006_PVA_DampenControlTest {
    
    public XML_005_006_PVA_DampenControlTest() {
    }

    /**
     * Test of main method, of class XML_005_006_PVA_DampenControl.
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
try{        XML_005_006_PVA_DampenControl.main(args);
 } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }    }

    
}
