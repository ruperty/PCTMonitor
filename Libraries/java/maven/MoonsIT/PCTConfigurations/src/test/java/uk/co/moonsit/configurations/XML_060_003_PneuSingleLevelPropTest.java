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
 * @author rupert
 */
public class XML_060_003_PneuSingleLevelPropTest {

    public XML_060_003_PneuSingleLevelPropTest() {
    }

    /**
     * Test of main method, of class XML_060_003_PneuSingleLevelProp.
     * @throws java.lang.Exception
     */
    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        try {
            XML_060_003_PneuSingleLevelProp.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }


}
