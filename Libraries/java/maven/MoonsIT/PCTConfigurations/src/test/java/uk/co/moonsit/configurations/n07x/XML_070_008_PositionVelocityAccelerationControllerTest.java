/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.moonsit.configurations.n07x;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import pct.moons.co.uk.schema.layers.Layers;

/**
 *
 * @author rupert
 */
public class XML_070_008_PositionVelocityAccelerationControllerTest {
    
    public XML_070_008_PositionVelocityAccelerationControllerTest() {
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
     * Test of main method, of class XML_070_008_PositionVelocityAccelerationController.
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
try{        XML_070_008_PositionVelocityAccelerationController.main(args);
} catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }    }

  
    
}
