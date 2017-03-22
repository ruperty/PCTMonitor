/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.moonsit.configurations;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import pct.moons.co.uk.schema.layers.Layers;

/**
 *
 * @author ReStart
 */
public class XML_050_002_LocationControlFullIMUTest {

    public XML_050_002_LocationControlFullIMUTest() {
    }

    /**
     * Test of main method, of class XML_050_002_LocationControlFullIMU.
     * @throws java.lang.Exception
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        try {
            XML_050_002_LocationControlFullIMU.main(args);
        } catch (Exception e) {
            fail(e.toString());
        }
    }

}
