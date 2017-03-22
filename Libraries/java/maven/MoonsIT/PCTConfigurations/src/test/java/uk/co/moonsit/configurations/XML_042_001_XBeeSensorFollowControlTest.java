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

/**
 *
 * @author ReStart
 */
public class XML_042_001_XBeeSensorFollowControlTest {

    public XML_042_001_XBeeSensorFollowControlTest() {
    }

 

    /**
     * Test of main method, of class XML_042_001_XBeeSensorFollowControl.
     *
     * @throws java.lang.Exception
     */
    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    public void testMain() throws Exception {
        System.out.println("main");
        try {
            String[] args = null;
            XML_042_001_XBeeSensorFollowControl.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
