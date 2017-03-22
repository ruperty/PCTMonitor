/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.moonsit.configurations.n072;

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
public class XML_072_001_ImageTargetReadTest {
    
    public XML_072_001_ImageTargetReadTest() {
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
     * Test of main method, of class XML_072_001_ImageTargetRead.
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
try{        XML_072_001_ImageTargetRead.main(args);
} catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }    }

  
    
}
